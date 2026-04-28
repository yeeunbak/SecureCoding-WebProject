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

@WebServlet("/board/comment/update")
public class BoardCommentUpdateController extends HttpServlet {
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

        // boardId, commentId -> 어떤 글의 댓글 수정할지 식별
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        int commentId = Integer.parseInt(request.getParameter("commentId"));
        String content = request.getParameter("content");

        String sql =
            "UPDATE TB_COMMENT " +
            "SET CONTENT = ?, UPD_DATE = SYSDATE " +
            "WHERE COMMENT_ID = ? AND WRITER_ID = ?"; // 어떤 글의 댓글 수정할지 식별, 작성자 본인만 삭제 가능

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, content);
            pstmt.setInt(2, commentId);
            pstmt.setString(3, loginId);

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        	// 수정 후, 해당 게시글로 이동
        response.sendRedirect(request.getContextPath() + "/board/boardDetail.jsp?boardId=" + boardId);
    }
}