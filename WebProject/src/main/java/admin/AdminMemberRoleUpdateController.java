package admin;

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

@WebServlet("/admin/member/roleUpdate")
public class AdminMemberRoleUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 관리자 체크
        HttpSession session = request.getSession();
        String loginRole = (String) session.getAttribute("loginRole");

        if (!"ADMIN".equals(loginRole)) {
            response.sendRedirect(request.getContextPath() + "/main.jsp");
            return;
        }

        String userId = request.getParameter("userId");
        String role = request.getParameter("role");

        String sql =
            "UPDATE TB_MEMBER " +
            "SET USER_ROLE = ? " +
            "WHERE USER_ID = ?";

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, role);
            pstmt.setString(2, userId);

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/admin/memberList.jsp");
    }
}