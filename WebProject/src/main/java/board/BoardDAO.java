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
}