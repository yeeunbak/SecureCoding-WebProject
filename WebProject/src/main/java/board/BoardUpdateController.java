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

@WebServlet("/board/update")
public class BoardUpdateController extends HttpServlet {
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

        // boardId -> 어떤 글 수정할지 식별
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String isSecret = request.getParameter("isSecret");

        if (isSecret == null) {
            isSecret = "N";
        }

        String sql =
            "UPDATE TB_BOARD " +
            "SET TITLE = ?, CONTENT = ?, IS_SECRET = ?, UPD_DATE = SYSDATE " +
            "WHERE BOARD_ID = ? AND WRITER_ID = ?"; // 어떤 글 수정할지 식별, 작성자 본인만 수정 가능

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, isSecret);
            pstmt.setInt(4, boardId);
            pstmt.setString(5, loginId);

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 수정 후, 상세페이지로 이동
        response.sendRedirect(request.getContextPath() + "/board/boardDetail.jsp?boardId=" + boardId);
    }
}