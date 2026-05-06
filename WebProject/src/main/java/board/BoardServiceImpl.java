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
    public BoardFileDTO selectFileDetail(int fileId) {
        return boardDAO.selectFileDetail(fileId);
    }

    @Override
    public BoardFileDTO selectFileDetailForWriter(int fileId, int boardId, String writerId) {
        return boardDAO.selectFileDetailForWriter(fileId, boardId, writerId);
    }

    @Override
    public int deleteFile(int fileId, int boardId) {
        return boardDAO.deleteFile(fileId, boardId);
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
    
    @Override
    public int adminDeleteBoard(int boardId) {
        return boardDAO.adminDeleteBoard(boardId);
    }
    
    @Override
    public int insertComment(CommentDTO comment) {
        return boardDAO.insertComment(comment);
    }

    @Override
    public int updateComment(CommentDTO comment) {
        return boardDAO.updateComment(comment);
    }

    @Override
    public int deleteComment(CommentDTO comment) {
        return boardDAO.deleteComment(comment);
    }
}