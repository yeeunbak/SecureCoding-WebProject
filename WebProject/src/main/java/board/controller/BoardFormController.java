package board.controller;

import java.io.IOException;
import java.util.List;

import board.dto.BoardDTO;
import board.dto.BoardFileDTO;
import board.service.BoardService;
import board.service.BoardServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/board/form")
public class BoardFormController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BoardService boardService;

    public BoardFormController() {
        boardService = new BoardServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");

        String boardIdParam = request.getParameter("boardId");

        if (boardIdParam != null && !boardIdParam.trim().equals("")) {
            int boardId = Integer.parseInt(boardIdParam);

            BoardDTO board = boardService.selectBoardDetail(boardId);

            if (board == null) {
                response.sendRedirect(request.getContextPath() + "/board/list");
                return;
            }

            if (!loginId.equals(board.getWriterId())) {
                response.sendRedirect(request.getContextPath() + "/board/detail?boardId=" + boardId);
                return;
            }

            List<BoardFileDTO> fileList = boardService.selectFileList(boardId);

            request.setAttribute("board", board);
            request.setAttribute("fileList", fileList);
        }

        request.getRequestDispatcher("/board/boardForm.jsp").forward(request, response);
    }
}