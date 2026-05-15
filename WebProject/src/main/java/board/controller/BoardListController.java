package board.controller;

import java.io.IOException;
import java.util.List;

import board.dto.BoardDTO;
import board.service.BoardService;
import board.service.BoardServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//일반 사용자, 관리자 게시판 목록 둘 다 처리
@WebServlet({"/board/list", "/admin/board/list"})
public class BoardListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 게시글 관련 비즈니스 로직 처리용 Service
    private BoardService boardService;

    // Service 객체 생성
    public BoardListController() {
        boardService = new BoardServiceImpl();
    }

    // 게시글 목록 조회 요청 처리
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8"); // 한글 깨짐 방지

        String uri = request.getRequestURI(); 									// 현재 요청 URI 가져오기				예: /WebProject/board/list
        String contextPath = request.getContextPath(); 							// 프로젝트 ContextPath 가져오기 		예: /WebProject
        boolean isAdminPage = uri.equals(contextPath + "/admin/board/list");	// 현재 요청이 관리자 게시판 목록인지 확인

        String searchType = request.getParameter("searchType");					// 검색 조건(title/content/writer)
        String keyword = request.getParameter("keyword");		 				// 검색 키워드

        	// 검색 조건이 없으면 기본값 title 사용
        if (searchType == null || searchType.trim().equals("")) {
            searchType = "title";
        }

        // 검색어 없으면, 빈 문자열 처리
        if (keyword == null) {
            keyword = "";
        }

        	// 게시글 목록 + 검색 결과 조회
        List<BoardDTO> boardList = boardService.selectBoardList(searchType, keyword);

        // JSP로 전달할 데이터 저장
        request.setAttribute("boardList", boardList);
        request.setAttribute("searchType", searchType);
        request.setAttribute("keyword", keyword);

        if (isAdminPage) {
            request.getRequestDispatcher("/admin/adminboardList.jsp").forward(request, response);	// 관리자 게시글 목록
        } else {
            request.getRequestDispatcher("/board/boardList.jsp").forward(request, response);		// 사용자 게시글 목록
        }
    }
}