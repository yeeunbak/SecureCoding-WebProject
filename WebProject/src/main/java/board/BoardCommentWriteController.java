package board;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/board/comment/write")
public class BoardCommentWriteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BoardService boardService;

    public BoardCommentWriteController() {
        boardService = new BoardServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");

        // 로그인 체크
        if (loginId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }

        int boardId = Integer.parseInt(request.getParameter("boardId"));
        String content = request.getParameter("content");

        CommentDTO comment = new CommentDTO();
        comment.setBoardId(boardId);
        comment.setContent(content);
        comment.setWriterId(loginId);

        boardService.insertComment(comment);

        response.sendRedirect(request.getContextPath() + "/board/detail?boardId=" + boardId);
    }
}