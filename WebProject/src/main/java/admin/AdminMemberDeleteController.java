package admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.MemberService;
import member.MemberServiceImpl;

@WebServlet("/admin/member/delete")
public class AdminMemberDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MemberService memberService;

    public AdminMemberDeleteController() {
        memberService = new MemberServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginRole = (String) session.getAttribute("loginRole");
        String loginId = (String) session.getAttribute("loginId");

        if (!"ADMIN".equals(loginRole)) {
            response.sendRedirect(request.getContextPath() + "/main.jsp");
            return;
        }

        String userId = request.getParameter("userId");

        if (userId == null || userId.trim().equals("")) {
            response.sendRedirect(request.getContextPath() + "/admin/memberList.jsp?msg=error");
            return;
        }

        if (userId.equals(loginId)) {
            response.sendRedirect(request.getContextPath() + "/admin/memberList.jsp?msg=self");
            return;
        }

        try {
            int boardCount = memberService.countBoardByWriterId(userId);

            if (boardCount > 0) {
                response.sendRedirect(request.getContextPath() + "/admin/memberList.jsp?msg=hasBoard");
                return;
            }

            memberService.deleteMember(userId);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/memberList.jsp?msg=error");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/admin/memberList.jsp?msg=success");
    }
}