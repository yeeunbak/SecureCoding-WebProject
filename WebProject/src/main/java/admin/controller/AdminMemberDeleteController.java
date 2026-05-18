package admin.controller;

import java.io.IOException;

import board.service.BoardService;
import board.service.BoardServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.service.MemberService;
import member.service.MemberServiceImpl;

// 관리자 회원 삭제 처리
@WebServlet("/admin/member/delete")
public class AdminMemberDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 회원 관련 비즈니스 로직 처리용 Service
    private MemberService memberService;
    // 게시글 관련 비즈니스 로직 처리용 Service
    private BoardService boardService;

    // Service 객체 생성
    public AdminMemberDeleteController() {
        memberService = new MemberServiceImpl();
        boardService = new BoardServiceImpl();
    }

    // 회원 삭제 요청 처리
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        	// 현재 로그인 관리자 정보 가져오기
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        
        	// 삭제할 회원 아이디 가져오기
        String userId = request.getParameter("userId");

        if (userId == null || userId.trim().equals("")) {
            response.sendRedirect(request.getContextPath() + "/admin/member/list?msg=error");
            return;
        }

        	// 자기 자신은 삭제할 수 없도록 처리
        if (userId.equals(loginId)) {
            response.sendRedirect(request.getContextPath() + "/admin/member/list?msg=self");
            return;
        }

        try {
        		// 회원이 작성한 게시글 수 조회
            int boardCount = boardService.countBoardByWriterId(userId);

            	// 작성한 게시글이 있으면 회원 삭제 불가
            if (boardCount > 0) {
                response.sendRedirect(request.getContextPath() + "/admin/member/list?msg=hasBoard");
                return;
            }

            	 // 회원 삭제 처리
            memberService.deleteMember(userId);

        } catch (Exception e) {
            e.printStackTrace(); 
            	// 오류 발생 시 오류 메시지와 함께 이동
            response.sendRedirect(request.getContextPath() + "/admin/member/list?msg=error");
            return;
        }
        	
        	// 삭제 성공 시 성공 메시지와 함께 이동
        response.sendRedirect(request.getContextPath() + "/admin/member/list?msg=success");
    }
}