package admin;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.MemberDTO;
import member.MemberService;
import member.MemberServiceImpl;

@WebServlet("/admin/member/list")
public class AdminMemberListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MemberService memberService;

    public AdminMemberListController() {
        memberService = new MemberServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginRole = (String) session.getAttribute("loginRole");

        if (!"ADMIN".equals(loginRole)) {
            response.sendRedirect(request.getContextPath() + "/main.jsp");
            return;
        }

        List<MemberDTO> memberList = memberService.selectMemberList();

        request.setAttribute("memberList", memberList);

        request.getRequestDispatcher("/admin/memberList.jsp").forward(request, response);
    }
}