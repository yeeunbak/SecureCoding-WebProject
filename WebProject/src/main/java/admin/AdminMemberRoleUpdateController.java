package admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.service.MemberService;
import member.service.MemberServiceImpl;

@WebServlet("/admin/member/roleUpdate")
public class AdminMemberRoleUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MemberService memberService;

    public AdminMemberRoleUpdateController() {
        memberService = new MemberServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginRole = (String) session.getAttribute("loginRole");

        if (!"ADMIN".equals(loginRole)) {
            response.sendRedirect(request.getContextPath() + "/main.jsp");
            return;
        }

        String userId = request.getParameter("userId");
        String role = request.getParameter("role");

        if (userId == null || userId.trim().equals("")
                || role == null || role.trim().equals("")) {
            response.sendRedirect(request.getContextPath() + "/admin/member/list?msg=error");
            return;
        }

        memberService.updateMemberRole(userId, role);

        response.sendRedirect(request.getContextPath() + "/admin/member/list");
    }
}