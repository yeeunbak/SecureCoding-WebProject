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

@WebServlet({"/board/list", "/admin/board/list"})
public class BoardListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BoardService boardService;

    public BoardListController() {
        boardService = new BoardServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        boolean isAdminPage = uri.equals(contextPath + "/admin/board/list");

        String searchType = request.getParameter("searchType");
        String keyword = request.getParameter("keyword");

        if (searchType == null || searchType.trim().equals("")) {
            searchType = "title";
        }

        if (keyword == null) {
            keyword = "";
        }

        List<BoardDTO> boardList = boardService.selectBoardList(searchType, keyword);

        request.setAttribute("boardList", boardList);
        request.setAttribute("searchType", searchType);
        request.setAttribute("keyword", keyword);

        if (isAdminPage) {
            request.getRequestDispatcher("/admin/adminboardList.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/board/boardList.jsp").forward(request, response);
        }
    }
}