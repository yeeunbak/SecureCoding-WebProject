package board;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import common.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/board/comment/delete")
public class BoardCommentDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

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

        // boardId, commentId -> 어떤 글의 댓글 삭제할지 식별
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        int commentId = Integer.parseInt(request.getParameter("commentId"));

        String sql =
            "DELETE FROM TB_COMMENT " +
            "WHERE COMMENT_ID = ? AND BOARD_ID = ? AND WRITER_ID = ?"; // 어떤 글의 댓글 삭제할지 식별, 작성자 본인만 삭제 가능

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, commentId);
            pstmt.setInt(2, boardId);
            pstmt.setString(3, loginId);

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        	// 삭제 후, 해당 게시글로 이동
        response.sendRedirect(request.getContextPath() + "/board/boardDetail.jsp?boardId=" + boardId);
    }
}