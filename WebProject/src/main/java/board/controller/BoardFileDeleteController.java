package board.controller;

import java.io.File;
import java.io.IOException;

import board.dto.BoardFileDTO;
import board.service.BoardService;
import board.service.BoardServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// 첨부파일 삭제 처리
@WebServlet("/board/file/delete")
public class BoardFileDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 게시글 관련 비즈니스 로직 처리용 Service
    private BoardService boardService;

    // Service 객체 생성
    public BoardFileDeleteController() {
        boardService = new BoardServiceImpl();
    }

    // 첨부파일 삭제 요청 처리
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");

        int boardId = 0;

        	// 게시글 번호 가져오기
        	// 숫자가 아니거나, 없으면 게시글 목록으로 이동
        try {
            boardId = Integer.parseInt(request.getParameter("boardId"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }
        
        	// 삭제할 파일 번호 가져오기
        int fileId = Integer.parseInt(request.getParameter("fileId"));

        	// 파일이 로그인 사용자의 게시글에 속한 파일인지 확인
        BoardFileDTO fileInfo = boardService.selectFileDetailForWriter(fileId, boardId, loginId);

        	// 파일 정보가 없으면 게시글 상세로 이동
        if (fileInfo == null) {
            response.sendRedirect(request.getContextPath() + "/board/detail?boardId=" + boardId);
            return;
        }

        	// 실제 저장된 객체 생성
        File file = new File(fileInfo.getSavePath());

        	// 실제 파일 삭제
        if (file.exists()) {
            file.delete();
        }

        // DB 파일 정보 삭제
        boardService.deleteFile(fileId, boardId);

        	// 수정 화면으로 이동
        response.sendRedirect(request.getContextPath() + "/board/form?boardId=" + boardId);
    }
}