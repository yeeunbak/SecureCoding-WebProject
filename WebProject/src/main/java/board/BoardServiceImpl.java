package board;

import java.util.List;

public class BoardServiceImpl implements BoardService {

    private BoardDAO boardDAO;

    public BoardServiceImpl() {
        boardDAO = new BoardDAO();
    }

    @Override
    public List<BoardDTO> selectBoardList(String searchType, String keyword) {
        return boardDAO.selectBoardList(searchType, keyword);
    }

    @Override
    public BoardDTO selectBoardDetail(int boardId) {
        return boardDAO.selectBoardDetail(boardId);
    }

    @Override
    public void updateViewCount(int boardId) {
        boardDAO.updateViewCount(boardId);
    }

    @Override
    public List<BoardFileDTO> selectFileList(int boardId) {
        return boardDAO.selectFileList(boardId);
    }

    @Override
    public List<CommentDTO> selectCommentList(int boardId) {
        return boardDAO.selectCommentList(boardId);
    }

    @Override
    public int insertBoard(BoardDTO board, List<BoardFileDTO> fileList) {
        return boardDAO.insertBoard(board, fileList);
    }

    @Override
    public int updateBoard(BoardDTO board, List<BoardFileDTO> fileList) {
        return boardDAO.updateBoard(board, fileList);
    }

    @Override
    public int deleteBoard(int boardId, String writerId) {
        return boardDAO.deleteBoard(boardId, writerId);
    }
}