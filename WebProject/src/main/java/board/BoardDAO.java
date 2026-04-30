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
}