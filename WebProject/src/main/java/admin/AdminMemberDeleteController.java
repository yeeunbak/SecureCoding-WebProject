package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import common.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/member/delete")
public class AdminMemberDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginRole = (String) session.getAttribute("loginRole");
        String loginId = (String) session.getAttribute("loginId");

        // 1. 관리자 체크
        if (!"ADMIN".equals(loginRole)) {
            response.sendRedirect(request.getContextPath() + "/main.jsp");
            return;
        }

        String userId = request.getParameter("userId");

        // 2. 자기 자신 삭제 방지
        if (userId.equals(loginId)) {
            response.sendRedirect(request.getContextPath() + "/admin/memberList.jsp?msg=self");
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            // 3. 게시글 존재 여부 체크
            String checkSql = "SELECT COUNT(*) FROM TB_BOARD WHERE WRITER_ID = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            int boardCount = 0;

            if (rs.next()) {
                boardCount = rs.getInt(1);
            }

            rs.close();
            pstmt.close();

            // 게시글 있으면 삭제 금지
            if (boardCount > 0) {
                response.sendRedirect(request.getContextPath() + "/admin/memberList.jsp?msg=hasBoard");
                return;
            }

            // 4. 회원 삭제
            String deleteSql = "DELETE FROM TB_MEMBER WHERE USER_ID = ?";
            pstmt = conn.prepareStatement(deleteSql);
            pstmt.setString(1, userId);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/memberList.jsp?msg=error");
            return;
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception e) {}
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
            if (conn != null) try { conn.close(); } catch (Exception e) {}
        }

        // 성공 후 이동
        response.sendRedirect(request.getContextPath() + "/admin/memberList.jsp?msg=success");
    }
}