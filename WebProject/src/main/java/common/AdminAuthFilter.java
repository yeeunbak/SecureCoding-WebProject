package common;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/admin/*")
public class AdminAuthFilter extends HttpFilter implements Filter {

    private static final long serialVersionUID = 1L;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        String loginId = null;
        String loginRole = null;

        if (session != null) {
            loginId = (String) session.getAttribute("loginId");
            loginRole = (String) session.getAttribute("loginRole");
        }

        if (loginId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }

        if (!"ADMIN".equals(loginRole)) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }

        chain.doFilter(request, response);
    }
}