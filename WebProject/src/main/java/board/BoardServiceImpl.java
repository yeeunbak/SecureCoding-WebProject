package board;

import java.util.List;

public class BoardServiceImpl implements BoardService {

    private BoardDAO boardDAO;

    public BoardServiceImpl() {
        boardDAO = new BoardDAO();
    }

    // 게시글 목록 조회 + 검색
    @Override
    public List<BoardDTO> selectBoardList(String searchType, String keyword) {
        return boardDAO.selectBoardList(searchType, keyword);
    }
}