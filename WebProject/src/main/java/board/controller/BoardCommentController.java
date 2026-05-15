package board.controller;

import java.io.IOException;

import board.dto.CommentDTO;
import board.service.BoardService;
import board.service.BoardServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// 댓글 작성, 수정, 삭제 처리
@WebServlet({
    "/board/comment/write",
    "/board/comment/update",
    "/board/comment/delete"
})
public class BoardCommentController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    	// 게시글 관련 비즈니스 로직 처리용 Service
    private BoardService boardService;

    // Service 객체 생성
    public BoardCommentController() {
        boardService = new BoardServiceImpl();
    }
    
    // 댓글 관련 요청 처리
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        
        int boardId = 0;

        	// 댓글이 작성된 게시글 번호 가져오기
        	// 숫자가 아니거나, 없으면 목록으로 이동
        try {
            boardId = Integer.parseInt(request.getParameter("boardId"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }
       
        String uri = request.getRequestURI();			// 현재 요청 URI 가져오기
        String contextPath = request.getContextPath();	// 프로젝트 ContextPath 가져오기

        	// 댓글 DTO 생성
        CommentDTO comment = new CommentDTO();
        comment.setBoardId(boardId);
        comment.setWriterId(loginId);

        if (uri.equals(contextPath + "/board/comment/write")) {						// 댓글 작성 처리
            String content = request.getParameter("content");						// 댓글 내용 가져오기
            comment.setContent(content);

            boardService.insertComment(comment);	// 등록

        } else if (uri.equals(contextPath + "/board/comment/update")) {				// 댓글 수정 처리
        	int commentId = Integer.parseInt(request.getParameter("commentId"));	// 수정할 댓글 번호
            String content = request.getParameter("content");						// 수정할 댓글 내용

            comment.setCommentId(commentId);
            comment.setContent(content);
            
            boardService.updateComment(comment);	// 수정
            
        } else if (uri.equals(contextPath + "/board/comment/delete")) {				// 댓글 삭제 처리
        	int commentId = Integer.parseInt(request.getParameter("commentId"));	// 삭제할 댓글 번호

            comment.setCommentId(commentId);
            boardService.deleteComment(comment);	// 삭제
        }
        
        	// 처리 후, 게시글 상세로 이동
        response.sendRedirect(contextPath + "/board/detail?boardId=" + boardId);
    }
}