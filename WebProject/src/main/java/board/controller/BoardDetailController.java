package board.controller;

import java.io.IOException;
import java.util.List;

import board.dto.BoardDTO;
import board.dto.BoardFileDTO;
import board.dto.CommentDTO;
import board.service.BoardService;
import board.service.BoardServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// 일반 사용자, 관리자 게시판 상세 둘 다 처리
@WebServlet({"/board/detail", "/admin/board/detail"})
public class BoardDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 게시글 관련 비즈니스 로직 처리용 Service
    private BoardService boardService;

    // Service 객체 생성
    public BoardDetailController() {
        boardService = new BoardServiceImpl();
    }

    // 게시글 상세 조회 요청 처리
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();

        // 관리자 상세 페이지 여부 확인
        boolean isAdminPage = uri.equals(contextPath + "/admin/board/detail");

        // 현재 요청에 맞는 목록 URL
        String listUrl = isAdminPage ? "/admin/board/list" : "/board/list";

        // 로그인 사용자 정보 가져오기
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        String loginRole = (String) session.getAttribute("loginRole");

        // 관리자 페이지 접근 시, 관리자가 아닐 경우 일반 게시판 목록으로 이동
        if (isAdminPage && !"ADMIN".equals(loginRole)) {
            response.sendRedirect(contextPath + "/board/list");
            return;
        }

        // 상세 조회할 게시글 번호 가져오기
        String boardIdParam = request.getParameter("boardId");

        // 게시글 번호가 없으면 목록으로 이동
        if (boardIdParam == null || boardIdParam.trim().equals("")) {
            response.sendRedirect(contextPath + listUrl);
            return;
        }

        // 문자열 → int 변환
        // 게시글 번호로 DB 조회
        int boardId = Integer.parseInt(boardIdParam);

        // 게시글 상세 조회
        BoardDTO board = boardService.selectBoardDetail(boardId);

        // 게시글이 존재하지 않으면 목록으로 이동
        if (board == null) {
            response.sendRedirect(contextPath + listUrl);
            return;
        }

        // 비밀글 접근 제한
        if ("Y".equals(board.getIsSecret())        		// 비밀글 O
                && !loginId.equals(board.getWriterId())	// 작성자 X
                && !"ADMIN".equals(loginRole)) {		// 관리자 X

            // 경고창 출력 후 게시글 목록으로 이동
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>");
            response.getWriter().println("alert('비밀글은 작성자만 볼 수 있습니다.');");
            response.getWriter().println("location.href='" + contextPath + "/board/list';");
            response.getWriter().println("</script>");
            return;
        }

        boardService.updateViewCount(boardId);									// 조회수 증가
        board.setViewCount(board.getViewCount() + 1);							// 화면 표시용 조회수
        List<BoardFileDTO> fileList = boardService.selectFileList(boardId);		// 첨부파일 목록 조회
        List<CommentDTO> commentList = boardService.selectCommentList(boardId);	// 댓글 목록 조회

        boolean isWriter = loginId.equals(board.getWriterId());         		// 현재 로그인 사용자가 작성자인지 확인

        // JSP로 전달할 데이터 저장
        request.setAttribute("board", board);
        request.setAttribute("fileList", fileList);
        request.setAttribute("commentList", commentList);
        request.setAttribute("isWriter", isWriter);

        if (isAdminPage) {
            request.getRequestDispatcher("/admin/adminboardDetail.jsp").forward(request, response); // 관리자 게시글 상세
        } else {
            request.getRequestDispatcher("/board/boardDetail.jsp").forward(request, response);		// 사용자 게시글 상세
        }
    }
}