package common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import board.dto.BoardFileDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

public class FileUploadUtil {

    private static final String UPLOAD_DIR = "/home/yeeun/upload/board";

    public static List<BoardFileDTO> uploadBoardFiles(HttpServletRequest request)
            throws IOException, ServletException {

        List<BoardFileDTO> fileList = new ArrayList<>();

        File uploadDir = new File(UPLOAD_DIR);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        Collection<Part> parts = request.getParts();

        for (Part part : parts) {

            if (!"uploadFile".equals(part.getName()) || part.getSize() <= 0) {
                continue;
            }

            String originName = Paths.get(part.getSubmittedFileName())
                    .getFileName()
                    .toString();

            String fileExt = "";

            int dotIndex = originName.lastIndexOf(".");

            if (dotIndex != -1) {
                fileExt = originName.substring(dotIndex + 1);
            }

            String saveName = UUID.randomUUID().toString() + "_" + originName;
            String savePath = UPLOAD_DIR + File.separator + saveName;

            part.write(savePath);

            BoardFileDTO file = new BoardFileDTO();

            file.setOriginName(originName);
            file.setSaveName(saveName);
            file.setSavePath(savePath);
            file.setFileSize(part.getSize());
            file.setFileExt(fileExt);

            fileList.add(file);
        }

        return fileList;
    }
}