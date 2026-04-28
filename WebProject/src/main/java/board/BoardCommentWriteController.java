package board;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import common.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/board/comment/write")
public class BoardCommentWriteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8"); // 한글 깨짐 방지

        	// 로그인한 사용자 ID 가져오기
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");

        	// 로그인 안 했을 경우, 로그인으로 이동
        if (loginId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }
        
        // JSP에서 보낸 값 받기
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        String content = request.getParameter("content");

        // DB에 댓글 저장
        String sql =
            "INSERT INTO TB_COMMENT " +
            "(COMMENT_ID, BOARD_ID, CONTENT, WRITER_ID) " +
            "VALUES (SEQ_TB_COMMENT.NEXTVAL, ?, ?, ?)";

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, boardId);
            pstmt.setString(2, content);
            pstmt.setString(3, loginId);

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        	// 작성 후, 해당 게시글로 이동
        response.sendRedirect(request.getContextPath() + "/board/boardDetail.jsp?boardId=" + boardId);
    }
}