package member;

import java.io.IOException;
import java.sql.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/* 회원정보 수정 */
@WebServlet("/editMember")
public class UpdateMemberController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MemberService memberService;

    public UpdateMemberController() {
        memberService = new MemberServiceImpl();
    }

    /* 수정 화면 이동 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    		/* 기존 세션만 가져옴 */
        HttpSession session = request.getSession(false);
        
        /* 로그아웃 상태 -> login.jsp */
        if (session == null || session.getAttribute("loginId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String userId = (String) session.getAttribute("loginId"); // 로그인 사용자 ID 가져오기
        MemberDTO member = memberService.getMemberById(userId); // DB에서 회원정보 조회
        
        request.setAttribute("member", member); // JSP에서 쓸 수 있게 전달
        request.getRequestDispatcher("/editMember.jsp").forward(request, response); // editMember.jsp로 데이터 전달
    }

    /* 수정 처리 */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String userId = request.getParameter("userId");
        String userPw = request.getParameter("userPw");
        String userName = request.getParameter("userName");
        String birthDateStr = request.getParameter("birthDate");
        String userEmail = request.getParameter("userEmail");

        MemberDTO member = new MemberDTO();
        member.setUserId(userId);
        member.setUserPw(userPw);
        member.setUserName(userName);
        member.setBirthDate(Date.valueOf(birthDateStr));
        member.setUserEmail(userEmail);

        /* update Service 호출 */
        int result = memberService.updateMember(member);

        if (result > 0) {
            response.sendRedirect(request.getContextPath() + "/main.jsp");					// 수정 성공 
        } else {
            response.sendRedirect(request.getContextPath() + "/editMember.jsp?msg=fail");   // 수정 실패
        }
    }
}