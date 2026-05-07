package board.controller;

import java.io.IOException;

import board.dto.CommentDTO;
import board.service.BoardService;
import board.service.BoardServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({
    "/board/comment/write",
    "/board/comment/update",
    "/board/comment/delete"
})
public class BoardCommentController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BoardService boardService;

    public BoardCommentController() {
        boardService = new BoardServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        
        int boardId = 0;

        try {
            boardId = Integer.parseInt(request.getParameter("boardId"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        CommentDTO comment = new CommentDTO();
        comment.setBoardId(boardId);
        comment.setWriterId(loginId);

        if (uri.equals(contextPath + "/board/comment/write")) {
            String content = request.getParameter("content");
            comment.setContent(content);

            boardService.insertComment(comment);

        } else if (uri.equals(contextPath + "/board/comment/update")) {
            int commentId = Integer.parseInt(request.getParameter("commentId"));
            String content = request.getParameter("content");

            comment.setCommentId(commentId);
            comment.setContent(content);

            boardService.updateComment(comment);

        } else if (uri.equals(contextPath + "/board/comment/delete")) {
            int commentId = Integer.parseInt(request.getParameter("commentId"));

            comment.setCommentId(commentId);

            boardService.deleteComment(comment);
        }

        response.sendRedirect(contextPath + "/board/detail?boardId=" + boardId);
    }
}