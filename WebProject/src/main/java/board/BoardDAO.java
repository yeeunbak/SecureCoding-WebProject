package board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import common.MyBatisUtil;

public class BoardDAO {

    private static final String NAMESPACE = "board.BoardMapper.";

    /* =========================
       게시글 조회
       ========================= */

    public List<BoardDTO> selectBoardList(String searchType, String keyword) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("searchType", searchType);
        paramMap.put("keyword", keyword);

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectList(NAMESPACE + "selectBoardList", paramMap);
        }
    }

    public BoardDTO selectBoardDetail(int boardId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectOne(NAMESPACE + "selectBoardDetail", boardId);
        }
    }

    public void updateViewCount(int boardId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false)) {
            session.update(NAMESPACE + "updateViewCount", boardId);
            session.commit();
        }
    }

    /* =========================
       게시글 작성 / 수정 / 삭제
       ========================= */

    public int insertBoard(BoardDTO board, List<BoardFileDTO> fileList) {
        SqlSession session = null;

        try {
            session = MyBatisUtil.getSqlSessionFactory().openSession(false);

            int boardId = session.selectOne(NAMESPACE + "selectNextBoardId");
            board.setBoardId(boardId);

            session.insert(NAMESPACE + "insertBoard", board);

            insertFileList(session, boardId, fileList);

            session.commit();
            return boardId;

        } catch (Exception e) {
            if (session != null) session.rollback();
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) session.close();
        }
    }

    public int updateBoard(BoardDTO board, List<BoardFileDTO> fileList) {
        SqlSession session = null;

        try {
            session = MyBatisUtil.getSqlSessionFactory().openSession(false);

            int result = session.update(NAMESPACE + "updateBoard", board);

            if (result > 0) {
                insertFileList(session, board.getBoardId(), fileList);
            }

            session.commit();
            return result;

        } catch (Exception e) {
            if (session != null) session.rollback();
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) session.close();
        }
    }

    public int deleteBoard(int boardId, String writerId) {
        SqlSession session = null;

        try {
            session = MyBatisUtil.getSqlSessionFactory().openSession(false);

            BoardDTO board = session.selectOne(NAMESPACE + "selectBoardDetail", boardId);

            if (board == null || !writerId.equals(board.getWriterId())) {
                session.rollback();
                return 0;
            }

            session.delete(NAMESPACE + "deleteCommentByBoardId", boardId);
            session.delete(NAMESPACE + "deleteFileByBoardId", boardId);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("boardId", boardId);
            paramMap.put("writerId", writerId);

            int result = session.delete(NAMESPACE + "deleteBoard", paramMap);

            session.commit();
            return result;

        } catch (Exception e) {
            if (session != null) session.rollback();
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) session.close();
        }
    }

    /* =========================
    관리자 게시글 작성 / 수정 / 삭제
    ========================= */
    // 관리자 게시글 삭제
    public int adminDeleteBoard(int boardId) {
        SqlSession session = null;

        try {
            session = MyBatisUtil.getSqlSessionFactory().openSession(false);

            // 자식 데이터 먼저 삭제
            session.delete(NAMESPACE + "deleteCommentByBoardId", boardId);
            session.delete(NAMESPACE + "deleteFileByBoardId", boardId);

            int result = session.delete(NAMESPACE + "adminDeleteBoard", boardId);

            session.commit();
            return result;

        } catch (Exception e) {
            if (session != null) session.rollback();
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /* =========================
       파일
       ========================= */

    public List<BoardFileDTO> selectFileList(int boardId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectList(NAMESPACE + "selectFileList", boardId);
        }
    }

    public BoardFileDTO selectFileDetail(int fileId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectOne(NAMESPACE + "selectFileDetail", fileId);
        }
    }

    public BoardFileDTO selectFileDetailForWriter(int fileId, int boardId, String writerId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("fileId", fileId);
        paramMap.put("boardId", boardId);
        paramMap.put("writerId", writerId);

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectOne(NAMESPACE + "selectFileDetailForWriter", paramMap);
        }
    }

    public int deleteFile(int fileId, int boardId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("fileId", fileId);
        paramMap.put("boardId", boardId);

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false)) {
            int result = session.delete(NAMESPACE + "deleteFile", paramMap);
            session.commit();
            return result;
        }
    }

    /* =========================
       댓글
       ========================= */

    public List<CommentDTO> selectCommentList(int boardId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectList(NAMESPACE + "selectCommentList", boardId);
        }
    }

    public int insertComment(CommentDTO comment) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false)) {
            int result = session.insert(NAMESPACE + "insertComment", comment);
            session.commit();
            return result;
        }
    }

    public int updateComment(CommentDTO comment) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false)) {
            int result = session.update(NAMESPACE + "updateComment", comment);
            session.commit();
            return result;
        }
    }

    public int deleteComment(CommentDTO comment) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false)) {
            int result = session.delete(NAMESPACE + "deleteComment", comment);
            session.commit();
            return result;
        }
    }

    /* =========================
       내부 공통 메서드
       ========================= */

    private void insertFileList(SqlSession session, int boardId, List<BoardFileDTO> fileList) {
        if (fileList == null || fileList.isEmpty()) return;

        for (BoardFileDTO file : fileList) {
            file.setBoardId(boardId);
            session.insert(NAMESPACE + "insertBoardFile", file);
        }
    }
}