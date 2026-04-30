package board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import common.MyBatisUtil;

public class BoardDAO {

    private static final String NAMESPACE = "board.BoardMapper.";

    // 게시글 목록 조회 + 검색
    public List<BoardDTO> selectBoardList(String searchType, String keyword) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("searchType", searchType);
        paramMap.put("keyword", keyword);

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectList(NAMESPACE + "selectBoardList", paramMap);
        }
    }
    
    // 게시글 상세 조회
    public BoardDTO selectBoardDetail(int boardId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectOne(NAMESPACE + "selectBoardDetail", boardId);
        }
    }

    // 조회수 증가
    public void updateViewCount(int boardId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false)) {
            session.update(NAMESPACE + "updateViewCount", boardId);
            session.commit();
        }
    }

    // 첨부파일 목록 조회
    public List<BoardFileDTO> selectFileList(int boardId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectList(NAMESPACE + "selectFileList", boardId);
        }
    }
    
    // 댓글 목록
    public List<CommentDTO> selectCommentList(int boardId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectList(NAMESPACE + "selectCommentList", boardId);
        }
    }
    
 // 게시글 작성 + 첨부파일 저장
    public int insertBoard(BoardDTO board, List<BoardFileDTO> fileList) {
        SqlSession session = null;

        try {
            session = MyBatisUtil.getSqlSessionFactory().openSession(false);

            int boardId = session.selectOne(NAMESPACE + "selectNextBoardId");
            board.setBoardId(boardId);

            session.insert(NAMESPACE + "insertBoard", board);

            if (fileList != null && !fileList.isEmpty()) {
                for (BoardFileDTO file : fileList) {
                    file.setBoardId(boardId);
                    session.insert(NAMESPACE + "insertBoardFile", file);
                }
            }

            session.commit();
            return boardId;

        } catch (Exception e) {
            if (session != null) {
                session.rollback();
            }
            e.printStackTrace();
            return 0;

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // 게시글 수정 + 새 첨부파일 추가
    public int updateBoard(BoardDTO board, List<BoardFileDTO> fileList) {
        SqlSession session = null;

        try {
            session = MyBatisUtil.getSqlSessionFactory().openSession(false);

            int result = session.update(NAMESPACE + "updateBoard", board);

            if (result > 0 && fileList != null && !fileList.isEmpty()) {
                for (BoardFileDTO file : fileList) {
                    file.setBoardId(board.getBoardId());
                    session.insert(NAMESPACE + "insertBoardFile", file);
                }
            }

            session.commit();
            return result;

        } catch (Exception e) {
            if (session != null) {
                session.rollback();
            }
            e.printStackTrace();
            return 0;

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // 게시글 삭제
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
            if (session != null) {
                session.rollback();
            }
            e.printStackTrace();
            return 0;

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}