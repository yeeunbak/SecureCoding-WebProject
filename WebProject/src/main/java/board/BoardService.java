package board;

import java.util.List;

public interface BoardService {

    // 게시글 목록 조회 + 검색
    List<BoardDTO> selectBoardList(String searchType, String keyword);

    // 게시글 상세 조회
    BoardDTO selectBoardDetail(int boardId);

    // 조회수 증가
    void updateViewCount(int boardId);

    // 첨부파일 목록 조회
    List<BoardFileDTO> selectFileList(int boardId);

    // 댓글 목록 조회
    List<CommentDTO> selectCommentList(int boardId);
    
    // 게시글 작성
    int insertBoard(BoardDTO board, List<BoardFileDTO> fileList);
    
    // 게시글 수정
    int updateBoard(BoardDTO board, List<BoardFileDTO> fileList);

    // 게시글 삭제
    int deleteBoard(int boardId, String writerId);
}