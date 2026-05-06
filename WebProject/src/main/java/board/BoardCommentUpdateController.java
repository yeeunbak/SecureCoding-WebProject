package board;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/board/comment/update")
public class BoardCommentUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BoardService boardService;

    public BoardCommentUpdateController() {
        boardService = new BoardServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");

        if (loginId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }

        int boardId = Integer.parseInt(request.getParameter("boardId"));
        int commentId = Integer.parseInt(request.getParameter("commentId"));
        String content = request.getParameter("content");

        CommentDTO comment = new CommentDTO();
        comment.setBoardId(boardId);
        comment.setCommentId(commentId);
        comment.setContent(content);
        comment.setWriterId(loginId);

        boardService.updateComment(comment);

        response.sendRedirect(request.getContextPath() + "/board/detail?boardId=" + boardId);
    }
}