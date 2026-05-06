package admin;

import java.io.IOException;
import java.util.List;

import board.BoardDTO;
import board.BoardService;
import board.BoardServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/board/list")
public class AdminBoardListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BoardService boardService;

    public AdminBoardListController() {
        boardService = new BoardServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginRole = (String) session.getAttribute("loginRole");

        if (!"ADMIN".equals(loginRole)) {
            response.sendRedirect(request.getContextPath() + "/main.jsp");
            return;
        }

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

        request.getRequestDispatcher("/admin/adminboardList.jsp").forward(request, response);
    }
}