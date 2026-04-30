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
}