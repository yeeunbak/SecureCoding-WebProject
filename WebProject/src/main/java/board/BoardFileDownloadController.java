package board;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/board/file/download")
public class BoardFileDownloadController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BoardService boardService;

    public BoardFileDownloadController() {
        boardService = new BoardServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fileIdParam = request.getParameter("fileId");

        if (fileIdParam == null || fileIdParam.trim().equals("")) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }

        int fileId = Integer.parseInt(fileIdParam);

        BoardFileDTO fileInfo = boardService.selectFileDetail(fileId);

        if (fileInfo == null) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }

        File file = new File(fileInfo.getSavePath());

        if (!file.exists()) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }

        String encodedFileName = URLEncoder.encode(fileInfo.getOriginName(), "UTF-8")
                                           .replaceAll("\\+", "%20");

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
        response.setContentLengthLong(file.length());

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) > 0) {
                response.getOutputStream().write(buffer, 0, length);
            }
        }
    }
}