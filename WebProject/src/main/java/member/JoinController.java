/* 회원가입 요청 받기 -> 값 정리해 Service에 넘김 -> 화면 이동 */
package member;

import java.io.IOException;
import java.sql.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/* 회원가입 버튼 클릭 시 */
@WebServlet("/join")
public class JoinController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MemberService memberService;

    public JoinController() {
        memberService = new MemberServiceImpl();
    }

    /* /join 주소 요청 -> join.jsp 이동 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/join.jsp");
    }

    /* 회원가입 */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        /* join.jsp에서 입력한 값 꺼내기 */
        String userName = request.getParameter("userName");
        String userId = request.getParameter("userId");
        String userPw = request.getParameter("userPw");
        String birthDateStr = request.getParameter("birthDate");
        String userEmail = request.getParameter("userEmail");

        /* DTO에 담기 */
        MemberDTO member = new MemberDTO();
        member.setUserName(userName);
        member.setUserId(userId);
        member.setUserPw(userPw);
        member.setBirthDate(Date.valueOf(birthDateStr)); // 문자열 -> DATE형식
        member.setUserEmail(userEmail);

        /* Service 호출 */
        int result = memberService.joinMember(member);

        if (result == -1) {
            response.sendRedirect(request.getContextPath() + "/join.jsp?msg=duplicate");  // 중복 ID
        } else if (result > 0) {
            response.sendRedirect(request.getContextPath() + "/login.jsp"); 		 	  // 가입성공
        } else {
            response.sendRedirect(request.getContextPath() + "/join.jsp?msg=fail");  	  // 가입실패
        }
    }
}