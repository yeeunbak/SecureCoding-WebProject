package board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import board.dto.BoardFileDTO;
import board.service.BoardService;
import board.service.BoardServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 첨부파일 다운로드 처리
@WebServlet("/board/file/download")
public class BoardFileDownloadController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 게시글 관련 비즈니스 로직 처리용 Service
    private BoardService boardService;

    // Service 객체 생성
    public BoardFileDownloadController() {
        boardService = new BoardServiceImpl();
    }

    // 첨부파일 다운로드 요청 처리
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    		// 다운로드할 파일 번호 가져오기
        String fileIdParam = request.getParameter("fileId");

        // fileId가 없으면 게시글 목록으로 이동
        if (fileIdParam == null || fileIdParam.trim().equals("")) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }

        // 문자열 → int 변환
        int fileId = Integer.parseInt(fileIdParam);

        // 파일 정보 조회
        BoardFileDTO fileInfo = boardService.selectFileDetail(fileId);
     
        // 파일 정보가 없으면 게시글 목록으로 이동
        if (fileInfo == null) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }

        // 실제 저장된 파일 객체 생성
        File file = new File(fileInfo.getSavePath());

        // 파일이 존재하지 않으면 게시글 목록으로 이동
        if (!file.exists()) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }
        
        // 한글 파일명 인코딩 처리
        String encodedFileName = URLEncoder.encode(fileInfo.getOriginName(), "UTF-8").replaceAll("\\+", "%20");
       
        // 파일 다운로드 응답 설정
        response.setContentType("application/octet-stream");						
        // 브라우저에서 다운로드 창이 뜨도록 설정
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
        // 파일 크기 설정
        response.setContentLengthLong(file.length());

        // 파일 읽기 및 다운로드 처리
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            
            // 파일 내용을 읽어서 브라우저로 전송
            while ((length = fis.read(buffer)) > 0) {
                response.getOutputStream().write(buffer, 0, length);
            }
        }
    }
}