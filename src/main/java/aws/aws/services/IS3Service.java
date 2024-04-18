package aws.aws.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IS3Service {

    String uploadFile(MultipartFile file) throws IOException;
    String downLoadFile(String fileName) throws IOException;
    List<String> listFiles() throws IOException;
    String updateFile(MultipartFile file, String oldFileName) throws IOException;
    String deleteFile(String fileName) throws IOException;
}
