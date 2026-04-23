package member;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MemberService memberService;

    public LoginController() {
        memberService = new MemberServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String userId = request.getParameter("userId");
        String userPw = request.getParameter("userPw");

        MemberDTO member = memberService.login(userId, userPw);

        if (member != null) {
            HttpSession session = request.getSession();
            session.setAttribute("loginId", member.getUserId());
            session.setAttribute("loginName", member.getUserName());
            session.setAttribute("loginRole", member.getUserRole());

            response.sendRedirect(request.getContextPath() + "/main.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp?msg=fail");
        }
    }
}