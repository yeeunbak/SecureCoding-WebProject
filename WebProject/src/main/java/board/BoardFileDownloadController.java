package board;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import common.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/board/file/download")
public class BoardFileDownloadController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        	// 파일 식별용 파라미터
        String fileIdParam = request.getParameter("fileId");

        // fileId 없으면 목록으로 이동
        if (fileIdParam == null || fileIdParam.trim().equals("")) {
            response.sendRedirect(request.getContextPath() + "/board/boardList.jsp");
            return;
        }

        int fileId = Integer.parseInt(fileIdParam);

        String originName = ""; // 사용자에게 보여줄 파일명
        String savePath = "";   // 서버에 저장된 실제 파일 경로

        // DB에서 파일 정보 조회
        String sql =
            "SELECT ORIGIN_NAME, SAVE_PATH " +
            "FROM TB_BOARD_FILE " +
            "WHERE FILE_ID = ?";

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, fileId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    originName = rs.getString("ORIGIN_NAME");
                    savePath = rs.getString("SAVE_PATH");
                } else {
                    // 파일 정보 없으면 목록으로
                    response.sendRedirect(request.getContextPath() + "/board/boardList.jsp");
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/board/boardList.jsp");
            return;
        }

        	// 실제 파일 객체 생성
        File file = new File(savePath);

        	// 파일 없으면 목록으로
        if (!file.exists()) {
            response.sendRedirect(request.getContextPath() + "/board/boardList.jsp");
            return;
        }

        String encodedFileName = URLEncoder.encode(originName, "UTF-8").replaceAll("\\+", "%20"); // 한글 파일명 깨짐 방지

        	// 다운로드 설정
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
        response.setContentLengthLong(file.length());

        	// 파일을 읽어서 응답으로 전송
        try (
            FileInputStream fis = new FileInputStream(file)
        ) {
            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) > 0) {
                response.getOutputStream().write(buffer, 0, length);
            }
        }
    }
}