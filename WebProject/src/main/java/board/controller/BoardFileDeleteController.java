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

@WebServlet("/board/file/delete")
public class BoardFileDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BoardService boardService;

    public BoardFileDeleteController() {
        boardService = new BoardServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");

        if (loginId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }

        int boardId = Integer.parseInt(request.getParameter("boardId"));
        int fileId = Integer.parseInt(request.getParameter("fileId"));

        // 파일이 로그인 사용자의 게시글에 속한 파일인지 확인
        BoardFileDTO fileInfo = boardService.selectFileDetailForWriter(fileId, boardId, loginId);

        if (fileInfo == null) {
            response.sendRedirect(request.getContextPath() + "/board/detail?boardId=" + boardId);
            return;
        }

        // 실제 파일 삭제
        File file = new File(fileInfo.getSavePath());

        if (file.exists()) {
            file.delete();
        }

        // DB 파일 정보 삭제
        boardService.deleteFile(fileId, boardId);

        response.sendRedirect(request.getContextPath() + "/board/form?boardId=" + boardId);
    }
}