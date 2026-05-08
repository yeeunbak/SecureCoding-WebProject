package board.controller;

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

@WebServlet({"/board/detail", "/admin/board/detail"})
public class BoardDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BoardService boardService;

    public BoardDetailController() {
        boardService = new BoardServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        boolean isAdminPage = uri.equals(contextPath + "/admin/board/detail");

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        String loginRole = (String) session.getAttribute("loginRole");

        if (isAdminPage && !"ADMIN".equals(loginRole)) {
        	if ("ADMIN".equals(loginRole)) {
        	    response.sendRedirect(contextPath + "/admin/board/list");
        	} else {
        	    response.sendRedirect(contextPath + "/board/list");
        	}
            return;
        }

        String boardIdParam = request.getParameter("boardId");

        if (boardIdParam == null || boardIdParam.trim().equals("")) {
            if (isAdminPage) {
                response.sendRedirect(contextPath + "/admin/board/list");
            } else {
                response.sendRedirect(contextPath + "/board/list");
            }
            return;
        }

        int boardId = Integer.parseInt(boardIdParam);

        BoardDTO board = boardService.selectBoardDetail(boardId);

        if (board == null) {
            if (isAdminPage) {
                response.sendRedirect(contextPath + "/admin/board/list");
            } else {
                response.sendRedirect(contextPath + "/board/list");
            }
            return;
        }

        if (!isAdminPage
                && "Y".equals(board.getIsSecret())
                && !loginId.equals(board.getWriterId())
                && !"ADMIN".equals(loginRole)) {

            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>");
            response.getWriter().println("alert('비밀글은 작성자만 볼 수 있습니다.');");
            response.getWriter().println("location.href='" + contextPath + "/board/list';");
            response.getWriter().println("</script>");
            return;
        }

        boardService.updateViewCount(boardId);
        board.setViewCount(board.getViewCount() + 1);

        List<BoardFileDTO> fileList = boardService.selectFileList(boardId);
        List<CommentDTO> commentList = boardService.selectCommentList(boardId);

        boolean isWriter = loginId.equals(board.getWriterId());

        request.setAttribute("board", board);
        request.setAttribute("fileList", fileList);
        request.setAttribute("commentList", commentList);
        request.setAttribute("isWriter", isWriter);

        if (isAdminPage) {
            request.getRequestDispatcher("/admin/adminboardDetail.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/board/boardDetail.jsp").forward(request, response);
        }
    }
}