package board;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

import common.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/board/update")
@MultipartConfig
public class BoardUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String UPLOAD_DIR = "/home/yeeun/upload/board";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");

        // 로그인 안 했을 경우
        if (loginId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }

        // boardId -> 어떤 글 수정할지 식별
        int boardId = Integer.parseInt(request.getParameter("boardId"));

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String isSecret = request.getParameter("isSecret");

        if (isSecret == null) {
            isSecret = "N";
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();

            // 게시글 수정
            String sql =
                "UPDATE TB_BOARD " +
                "SET TITLE = ?, CONTENT = ?, IS_SECRET = ?, UPD_DATE = SYSDATE " +
                "WHERE BOARD_ID = ? AND WRITER_ID = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, isSecret);
            pstmt.setInt(4, boardId);
            pstmt.setString(5, loginId);

            pstmt.executeUpdate();

            pstmt.close();
            pstmt = null;

            	// 첨부파일 처리
            for (Part filePart : request.getParts()) {
                if (!"uploadFile".equals(filePart.getName())) {
                    continue;
                }

                if (filePart.getSize() <= 0) {
                    continue;
                }

                	// 업로드 폴더 없으면 생성
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String originName = getFileName(filePart);

                String fileExt = "";
                int dotIndex = originName.lastIndexOf(".");
                if (dotIndex != -1) {
                    fileExt = originName.substring(dotIndex + 1);
                }

                String saveName = UUID.randomUUID().toString() + "_" + originName;
                String savePath = UPLOAD_DIR + File.separator + saveName;

                	// 실제 파일 저장
                filePart.write(savePath);

                // DB 저장
                String fileSql =
                    "INSERT INTO TB_BOARD_FILE " +
                    "(FILE_ID, BOARD_ID, ORIGIN_NAME, SAVE_NAME, SAVE_PATH, FILE_SIZE, FILE_EXT) " +
                    "VALUES (SEQ_TB_BOARD_FILE.NEXTVAL, ?, ?, ?, ?, ?, ?)";

                pstmt = conn.prepareStatement(fileSql);
                pstmt.setInt(1, boardId);
                pstmt.setString(2, originName);
                pstmt.setString(3, saveName);
                pstmt.setString(4, savePath);
                pstmt.setLong(5, filePart.getSize());
                pstmt.setString(6, fileExt);

                pstmt.executeUpdate();

                pstmt.close();
                pstmt = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
            if (conn != null) try { conn.close(); } catch (Exception e) {}
        }

        	// 수정 후, 상세페이지로 이동
        response.sendRedirect(request.getContextPath() + "/board/boardDetail.jsp?boardId=" + boardId);
    }

    // 파일명 추출
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");

        for (String token : contentDisposition.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }

        return "";
    }
}