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

@WebServlet("/board/write")
@MultipartConfig
public class BoardWriteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BoardService boardService;

    public BoardWriteController() {
        boardService = new BoardServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String writerId = (String) session.getAttribute("loginId");

        if (writerId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }

        String returnUrl = request.getParameter("returnUrl");

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String isSecret = request.getParameter("isSecret");

        if (isSecret == null) {
            isSecret = "N";
        }

        BoardDTO board = new BoardDTO();
        board.setTitle(title);
        board.setContent(content);
        board.setWriterId(writerId);
        board.setIsSecret(isSecret);

        List<BoardFileDTO> fileList = FileUploadUtil.uploadBoardFiles(request);

        int boardId = boardService.insertBoard(board, fileList);

        if (boardId > 0) {
            if ("admin".equals(returnUrl)) {
                response.sendRedirect(request.getContextPath() + "/admin/board/detail?boardId=" + boardId);
            } else {
                response.sendRedirect(request.getContextPath() + "/board/detail?boardId=" + boardId);
            }
        } else {
            if ("admin".equals(returnUrl)) {
                response.sendRedirect(request.getContextPath() + "/admin/board/list");
            } else {
                response.sendRedirect(request.getContextPath() + "/board/list");
            }
        }
    }
}