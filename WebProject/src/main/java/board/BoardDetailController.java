package board;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/board/detail")
public class BoardDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BoardService boardService;

    public BoardDetailController() {
        boardService = new BoardServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        String loginRole = (String) session.getAttribute("loginRole");

        if (loginId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }

        String boardIdParam = request.getParameter("boardId");

        if (boardIdParam == null || boardIdParam.trim().equals("")) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }

        int boardId = Integer.parseInt(boardIdParam);

        BoardDTO board = boardService.selectBoardDetail(boardId);

        if (board == null) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }

        // 비밀글 접근 제한
        if ("Y".equals(board.getIsSecret())
                && !loginId.equals(board.getWriterId())
                && !"ADMIN".equals(loginRole)) {

            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>");
            response.getWriter().println("alert('비밀글은 작성자만 볼 수 있습니다.');");
            response.getWriter().println("location.href='" + request.getContextPath() + "/board/list';");
            response.getWriter().println("</script>");
            return;
        }

        // 권한 확인 후 조회수 증가
        boardService.updateViewCount(boardId);

        // 화면에도 증가된 조회수 반영
        board.setViewCount(board.getViewCount() + 1);

        List<BoardFileDTO> fileList = boardService.selectFileList(boardId);
        List<CommentDTO> commentList = boardService.selectCommentList(boardId);

        request.setAttribute("board", board);
        request.setAttribute("fileList", fileList);
        request.setAttribute("commentList", commentList);

        request.getRequestDispatcher("/board/boardDetail.jsp").forward(request, response);
    }
}