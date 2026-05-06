package member.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/* 로그아웃 버튼 클릭 시 */
@WebServlet("/logout")
public class LogoutController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    		/* 현재세션 가져오기 */
        HttpSession session = request.getSession(false);

        /* 세션 존재 -> loginId, loginName, loginRole 제거 */
        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/member/login.jsp?msg=logout");
    }
}