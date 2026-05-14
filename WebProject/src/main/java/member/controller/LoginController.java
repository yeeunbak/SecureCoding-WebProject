package member.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.dto.MemberDTO;
import member.service.MemberService;
import member.service.MemberServiceImpl;

/* 로그인 버튼 클릭 시 */
@WebServlet("/login")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MemberService memberService;

    public LoginController() {
        memberService = new MemberServiceImpl();
    }

    /* login.jsp 이동 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/member/login.jsp");
    }

    /* login */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        /* login.jsp에서 입력한 값 꺼내기 */
        String userId = request.getParameter("userId");
        String userPw = request.getParameter("userPw");

        /* DTO에 담기 */
        MemberDTO member = memberService.login(userId, userPw);

        /* 회원정보 있음 */
        if (member != null) {
            HttpSession session = request.getSession();

            session.setAttribute("loginId", member.getUserId());
            session.setAttribute("loginName", member.getUserName());
            session.setAttribute("loginRole", member.getUserRole());

            if ("ADMIN".equals(member.getUserRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/board/list");
            } else {
                response.sendRedirect(request.getContextPath() + "/board/list");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp?msg=fail");
        }
    }
}