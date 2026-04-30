package board;

import java.util.List;

public interface BoardService {

	// 게시글 목록 조회 + 검색
    List<BoardDTO> selectBoardList(String searchType, String keyword);
}