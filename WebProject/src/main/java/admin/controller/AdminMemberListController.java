package admin.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.dto.MemberDTO;
import member.service.MemberService;
import member.service.MemberServiceImpl;

// 관리자 회원 목록 조회 처리
@WebServlet("/admin/member/list")
public class AdminMemberListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 회원 관련 비즈니스 로직 처리용 Service
    private MemberService memberService;

    // Service 객체 생성
    public AdminMemberListController() {
        memberService = new MemberServiceImpl();
    }

    // 회원 목록 조회 요청 처리
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        	// 전체 회원 목록 조회
        List<MemberDTO> memberList = memberService.selectMemberList();

        // JSP로 전달할 회원 목록 저장
        request.setAttribute("memberList", memberList);

        	// 관리자 회원 목록 화면으로 이동
        request.getRequestDispatcher("/admin/memberList.jsp").forward(request, response);
    }
}