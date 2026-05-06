package admin;

import java.io.IOException;
import java.util.List;

import board.dto.BoardDTO;
import board.dto.BoardFileDTO;
import board.dto.CommentDTO;
import board.service.BoardService;
import board.service.BoardServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/board/detail")
public class AdminBoardDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BoardService boardService;

    public AdminBoardDetailController() {
        boardService = new BoardServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginRole = (String) session.getAttribute("loginRole");
        String loginId = (String) session.getAttribute("loginId");

        if (!"ADMIN".equals(loginRole)) {
            response.sendRedirect(request.getContextPath() + "/main.jsp");
            return;
        }

        String boardIdParam = request.getParameter("boardId");

        if (boardIdParam == null || boardIdParam.trim().equals("")) {
            response.sendRedirect(request.getContextPath() + "/admin/board/list");
            return;
        }

        int boardId = Integer.parseInt(boardIdParam);

        BoardDTO board = boardService.selectBoardDetail(boardId);

        if (board == null) {
            response.sendRedirect(request.getContextPath() + "/admin/board/list");
            return;
        }

        boardService.updateViewCount(boardId);
        board.setViewCount(board.getViewCount() + 1);

        List<BoardFileDTO> fileList = boardService.selectFileList(boardId);
        List<CommentDTO> commentList = boardService.selectCommentList(boardId);

        boolean isWriter = loginId != null && loginId.equals(board.getWriterId());

        request.setAttribute("board", board);
        request.setAttribute("fileList", fileList);
        request.setAttribute("commentList", commentList);
        request.setAttribute("isWriter", isWriter);

        request.getRequestDispatcher("/admin/adminboardDetail.jsp").forward(request, response);
    }
}