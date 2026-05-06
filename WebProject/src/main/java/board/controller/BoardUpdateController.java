package board.controller;

import java.io.IOException;
import java.util.List;

import board.dto.BoardDTO;
import board.dto.BoardFileDTO;
import board.service.BoardService;
import board.service.BoardServiceImpl;
import common.FileUploadUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/board/update")
@MultipartConfig
public class BoardUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BoardService boardService;

    public BoardUpdateController() {
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

        int boardId = 0;

        try {
            boardId = Integer.parseInt(request.getParameter("boardId"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String isSecret = request.getParameter("isSecret");

        if (isSecret == null) {
            isSecret = "N";
        }

        BoardDTO board = new BoardDTO();
        board.setBoardId(boardId);
        board.setTitle(title);
        board.setContent(content);
        board.setIsSecret(isSecret);
        board.setWriterId(loginId);

        List<BoardFileDTO> fileList = FileUploadUtil.uploadBoardFiles(request);

        for (BoardFileDTO file : fileList) {
            file.setBoardId(boardId);
        }

        int result = boardService.updateBoard(board, fileList);

        if (result > 0) {
            response.sendRedirect(request.getContextPath() + "/board/detail?boardId=" + boardId);
        } else {
            response.sendRedirect(request.getContextPath() + "/board/list");
        }
    }
}