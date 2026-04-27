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

@WebServlet("/board/write")
public class BoardWriteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8"); // 한글 깨짐 방지

        // 로그인한 사용자 ID 가져오기
        HttpSession session = request.getSession();
        String writerId = (String) session.getAttribute("loginId");

        // 로그인 안 했을 경우, 로그인으로 이동
        if (writerId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }

        // JSP에서 보낸 값 받기
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String isSecret = request.getParameter("isSecret");

        // 비밀글 체크 안하면 NULL
        if (isSecret == null) {
            isSecret = "N";
        }

        // DB에 글 저장
        String sql =
            "INSERT INTO TB_BOARD " +
            "(BOARD_ID, TITLE, CONTENT, WRITER_ID, IS_SECRET) " +
            "VALUES (SEQ_TB_BOARD.NEXTVAL, ?, ?, ?, ?)";

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, writerId);
            pstmt.setString(4, isSecret);

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 작성 후, 게시글 목록으로 이동
        response.sendRedirect(request.getContextPath() + "/board/boardList.jsp");
    }
}