package globant.team.seven.imageprocessingservice.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FileHelper {

    private FileHelper() {}

    public  static java.io.File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
        java.io.File convFile = new java.io.File(System.getProperty("java.io.tmpdir")+"/"+fileName);
        multipart.transferTo(convFile);
        return convFile;
    }
}
