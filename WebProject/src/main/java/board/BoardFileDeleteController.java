package board;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import common.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/board/file/delete")
public class BoardFileDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 로그인 사용자 확인
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");

        // 로그인 안 되어있으면 로그인 페이지로 이동
        if (loginId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }

        // 삭제할 파일 정보
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        int fileId = Integer.parseInt(request.getParameter("fileId"));

        String savePath = ""; // 실제 파일 경로

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            // 파일이 본인 게시글에 속한 것인지 확인
            String selectSql =
                "SELECT BF.SAVE_PATH " +
                "FROM TB_BOARD_FILE BF " +
                "JOIN TB_BOARD B ON BF.BOARD_ID = B.BOARD_ID " +
                "WHERE BF.FILE_ID = ? " +
                "AND BF.BOARD_ID = ? " +
                "AND B.WRITER_ID = ?";

            pstmt = conn.prepareStatement(selectSql);
            pstmt.setInt(1, fileId);
            pstmt.setInt(2, boardId);
            pstmt.setString(3, loginId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                savePath = rs.getString("SAVE_PATH");
            } else {
                // 권한 없으면 상세 페이지로
                response.sendRedirect(request.getContextPath() + "/board/boardDetail.jsp?boardId=" + boardId);
                return;
            }

            	// 리소스 정리
            rs.close();
            pstmt.close();

            	// 실제 파일 삭제
            File file = new File(savePath);

            if (file.exists()) {
                file.delete();
            }

            // DB에서도 삭제
            String deleteSql =
                "DELETE FROM TB_BOARD_FILE " +
                "WHERE FILE_ID = ? AND BOARD_ID = ?";

            pstmt = conn.prepareStatement(deleteSql);
            pstmt.setInt(1, fileId);
            pstmt.setInt(2, boardId);

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception e) {}
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
            if (conn != null) try { conn.close(); } catch (Exception e) {}
        }

        	// 삭제 후 수정 화면으로 이동
        response.sendRedirect(request.getContextPath() + "/board/boardForm.jsp?boardId=" + boardId);
    }
}