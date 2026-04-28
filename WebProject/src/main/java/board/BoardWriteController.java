package board;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

@WebServlet("/board/write")
@MultipartConfig // 파일 업로드를 처리하기 위한 설정
public class BoardWriteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 서버에 파일이 저장될 경로
    private static final String UPLOAD_DIR = "/home/yeeun/upload/board"; // 추후에 경로 어떻게 변경하는 게 좋을지 고민

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8"); // 한글 깨짐 방지

        // 로그인한 사용자 ID 가져오기
        HttpSession session = request.getSession();
        String writerId = (String) session.getAttribute("loginId");

        // 로그인 안 했을 경우, 로그인 페이지로 이동
        if (writerId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }

        // JSP에서 전달된 게시글 데이터
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String isSecret = request.getParameter("isSecret");

        // 체크박스 미선택 시 null → 기본값 N 처리
        if (isSecret == null) {
            isSecret = "N";
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        int boardId = 0; // 게시글 PK (파일 저장 시 필요)

        try {
            conn = DBUtil.getConnection();

            // 1. 게시글 번호 먼저 생성 (파일 저장 시 사용)
            String seqSql = "SELECT SEQ_TB_BOARD.NEXTVAL FROM DUAL";
            pstmt = conn.prepareStatement(seqSql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                boardId = rs.getInt(1);
            }

            rs.close();
            pstmt.close();

            // 2. 게시글 DB 저장
            String sql =
                "INSERT INTO TB_BOARD " +
                "(BOARD_ID, TITLE, CONTENT, WRITER_ID, IS_SECRET) " +
                "VALUES (?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, boardId);
            pstmt.setString(2, title);
            pstmt.setString(3, content);
            pstmt.setString(4, writerId);
            pstmt.setString(5, isSecret);

            pstmt.executeUpdate();

            pstmt.close();

            // 3. 첨부파일 처리 (여러 개 업로드 가능)
            for (Part filePart : request.getParts()) {

                // uploadFile 이름만 처리
                if (!"uploadFile".equals(filePart.getName())) {
                    continue;
                }

                	// 파일 선택 안된 경우 skip
                if (filePart.getSize() <= 0) {
                    continue;
                }

                	// 업로드 폴더 없으면 생성
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                	// 원본 파일명 추출
                String originName = getFileName(filePart);

                	// 확장자 추출
                String fileExt = "";
                int dotIndex = originName.lastIndexOf(".");
                if (dotIndex != -1) {
                    fileExt = originName.substring(dotIndex + 1);
                }

                	// 저장 파일명 (UUID로 파일 중복 방지)
                String saveName = UUID.randomUUID().toString() + "_" + originName;

                	// 실제 저장 경로
                String savePath = UPLOAD_DIR + File.separator + saveName;

                	// 파일을 서버에 저장
                filePart.write(savePath);

                // DB에 파일 정보 저장
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 자원 정리
            if (rs != null) try { rs.close(); } catch (Exception e) {}
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
            if (conn != null) try { conn.close(); } catch (Exception e) {}
        }

        // 작성 후 상세 페이지로 이동
        response.sendRedirect(request.getContextPath() + "/board/boardDetail.jsp?boardId=" + boardId);
    }

    // multipart에서 파일명 추출
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