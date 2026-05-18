package board.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import board.dto.BoardDTO;
import board.dto.BoardFileDTO;
import board.dto.CommentDTO;
import common.MyBatisUtil;

public class BoardDAO {

    private static final String NAMESPACE = "board.BoardMapper.";

    // 게시글 목록 + 검색 조회
    public List<BoardDTO> selectBoardList(String searchType, String keyword) {
        Map<String, Object> paramMap = new HashMap<>(); // 검색 조건 저장용
        paramMap.put("searchType", searchType);
        paramMap.put("keyword", keyword);

        // MyBatis SqlSession 생성 후 게시글 목록 조회
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

    // 게시글 조회 수 증가
    public void updateViewCount(int boardId) {
    	// auto commit 비활성화
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false)) {
            session.update(NAMESPACE + "updateViewCount", boardId);
            session.commit(); // 직접 commit
        }
    }
    
    // 회원이 작성한 게시글 수 조회
    public int countBoardByWriterId(String userId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectOne(NAMESPACE + "countBoardByWriterId", userId);
        }
    }

    // 게시글 등록
    public int insertBoard(BoardDTO board, List<BoardFileDTO> fileList) {
        SqlSession session = null;

        try {
            session = MyBatisUtil.getSqlSessionFactory().openSession(false); // auto commit 비활성화

            	// 다음 게시글 번호 조회
            int boardId = session.selectOne(NAMESPACE + "selectNextBoardId");
            
            // DTO에 게시글 번호 설정
            board.setBoardId(boardId);

            	// 게시글 등록
            session.insert(NAMESPACE + "insertBoard", board);

            	// 첨부파일 등록
            insertFileList(session, boardId, fileList);
            
            // commit
            session.commit();
            return boardId;

        } catch (Exception e) {
            if (session != null) session.rollback(); // 오류 발생 시 rollback
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) session.close(); // session 종료
        }
    }

    	// 게시글 수정
    public int updateBoard(BoardDTO board, List<BoardFileDTO> fileList) {
        SqlSession session = null;

        try {
            session = MyBatisUtil.getSqlSessionFactory().openSession(false); // auto commit 비활성화
            
            	// 게시글 수정
            int result = session.update(NAMESPACE + "updateBoard", board);

            	// 수정 성공 시, 새 첨부파일 등록
            if (result > 0) {
                insertFileList(session, board.getBoardId(), fileList);
            }
            
            // commit
            session.commit();
            return result;

        } catch (Exception e) {
            if (session != null) session.rollback(); // 오류 발생 시 rollback
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) session.close(); // session 종료
        }
    }

    	// 게시글 삭제
    public int deleteBoard(int boardId, String writerId) {
        SqlSession session = null;

        try {
            session = MyBatisUtil.getSqlSessionFactory().openSession(false); // auto commit 비활성화

            	// 게시글 정보 조회
            BoardDTO board = session.selectOne(NAMESPACE + "selectBoardDetail", boardId);

            	// 게시글이 없거나 작성자가 다르면 삭제 불가
            if (board == null || !writerId.equals(board.getWriterId())) {
                session.rollback();
                return 0;
            }

            session.delete(NAMESPACE + "deleteCommentByBoardId", boardId); // 댓글 삭제
            session.delete(NAMESPACE + "deleteFileByBoardId", boardId); // 첨부파일 삭제

            	// 게시글 삭제용 parameter Map
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("boardId", boardId);
            paramMap.put("writerId", writerId);

            	// 게시글 삭제
            int result = session.delete(NAMESPACE + "deleteBoard", paramMap);

            // commit
            session.commit();
            return result;

        } catch (Exception e) {
            if (session != null) session.rollback(); // 오류 발생 시 rollback
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) session.close(); // session 종료
        }
    }
    
    // 관리자 게시글 삭제
    public int adminDeleteBoard(int boardId) {
        SqlSession session = null;

        try {
            session = MyBatisUtil.getSqlSessionFactory().openSession(false); // auto commit 비활성화

            	// 자식 데이터 먼저 삭제
            session.delete(NAMESPACE + "deleteCommentByBoardId", boardId);
            session.delete(NAMESPACE + "deleteFileByBoardId", boardId);

            	// 게시글 삭제
            int result = session.delete(NAMESPACE + "adminDeleteBoard", boardId);

            // commit
            session.commit(); 
            return result;

        } catch (Exception e) {
            if (session != null) session.rollback();  // 오류 발생 시 rollback
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) session.close();  // session 종료
        }
    }

    // 첨부파일 목록 조회
    public List<BoardFileDTO> selectFileList(int boardId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectList(NAMESPACE + "selectFileList", boardId);
        }
    }

    // 첨부파일 상세 조회
    public BoardFileDTO selectFileDetail(int fileId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectOne(NAMESPACE + "selectFileDetail", fileId);
        }
    }

    	// 작성자 본인 게시글의 첨부파일인지 확인
    public BoardFileDTO selectFileDetailForWriter(int fileId, int boardId, String writerId) {
    	// parameter 저장용 Map
        Map<String, Object> paramMap = new HashMap<>(); 
        paramMap.put("fileId", fileId);
        paramMap.put("boardId", boardId);
        paramMap.put("writerId", writerId);

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectOne(NAMESPACE + "selectFileDetailForWriter", paramMap);
        }
    }
    
    // 첨부파일 삭제
    public int deleteFile(int fileId, int boardId) {
    	// parameter 저장용 Map
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("fileId", fileId);
        paramMap.put("boardId", boardId);

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false)) {
            int result = session.delete(NAMESPACE + "deleteFile", paramMap);
            session.commit();  // commit
            return result;
        }
    }
    
    // 댓글 목록 조회
    public List<CommentDTO> selectCommentList(int boardId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectList(NAMESPACE + "selectCommentList", boardId);
        }
    }

    // 댓글 등록
    public int insertComment(CommentDTO comment) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false)) {
            int result = session.insert(NAMESPACE + "insertComment", comment);
            session.commit();
            return result;
        }
    }
    
    // 댓글 수정
    public int updateComment(CommentDTO comment) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false)) {
            int result = session.update(NAMESPACE + "updateComment", comment);
            session.commit();
            return result;
        }
    }
    
    // 댓글 삭제
    public int deleteComment(CommentDTO comment) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false)) {
            int result = session.delete(NAMESPACE + "deleteComment", comment);
            session.commit();
            return result;
        }
    }

 
    // 첨부파일 등록 공통 메서드
    private void insertFileList(SqlSession session, int boardId, List<BoardFileDTO> fileList) {
    		
    		// 파일이 없으면 종료
    	if (fileList == null || fileList.isEmpty()) return;

    		// 첨부파일 등록
        for (BoardFileDTO file : fileList) {
            file.setBoardId(boardId);
            session.insert(NAMESPACE + "insertBoardFile", file);
        }
    }
}