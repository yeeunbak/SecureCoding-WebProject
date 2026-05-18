package admin.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.service.MemberService;
import member.service.MemberServiceImpl;

// 관리자 회원 권한 수정 처리
@WebServlet("/admin/member/roleUpdate")
public class AdminMemberRoleUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 회원 관련 비즈니스 로직 처리용 Service
    private MemberService memberService;

    // Service 객체 생성
    public AdminMemberRoleUpdateController() {
        memberService = new MemberServiceImpl();
    }

    // 회원 권한 수정 요청 처리
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 수정할 회원 아이디, 변경할 권한 가져오기
        String userId = request.getParameter("userId");
        String role = request.getParameter("role");

        // userId 또는 role 값이 없으면 오류 처리
        if (userId == null || userId.trim().equals("") || role == null || role.trim().equals("")) {
            response.sendRedirect(request.getContextPath() + "/admin/member/list?msg=error");
            return;
        }

        	// 회원 권한 수정 처리
        memberService.updateMemberRole(userId, role);

        // 수정 완료 후 회원 목록 페이지 이동
        response.sendRedirect(request.getContextPath() + "/admin/member/list");
    }
}