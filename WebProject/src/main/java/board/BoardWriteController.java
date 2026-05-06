package board;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/board/write")
@MultipartConfig
public class BoardWriteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String UPLOAD_DIR = "/home/yeeun/upload/board";

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

        List<BoardFileDTO> fileList = new ArrayList<>();

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        for (Part filePart : request.getParts()) {
            if (!"uploadFile".equals(filePart.getName())) {
                continue;
            }

            if (filePart.getSize() <= 0) {
                continue;
            }

            String originName = getFileName(filePart);

            String fileExt = "";
            int dotIndex = originName.lastIndexOf(".");
            if (dotIndex != -1) {
                fileExt = originName.substring(dotIndex + 1);
            }

            String saveName = UUID.randomUUID().toString() + "_" + originName;
            String savePath = UPLOAD_DIR + File.separator + saveName;

            filePart.write(savePath);

            BoardFileDTO file = new BoardFileDTO();
            file.setOriginName(originName);
            file.setSaveName(saveName);
            file.setSavePath(savePath);
            file.setFileSize(filePart.getSize());
            file.setFileExt(fileExt);

            fileList.add(file);
        }

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