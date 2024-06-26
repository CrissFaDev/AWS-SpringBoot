package aws.aws.controllers;

import aws.aws.services.IS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class S3Controller {

    @Autowired
    private IS3Service is3Service;

    @GetMapping("download/{fileName}")
    public String downloadFile(@PathVariable("fileName") String fileName) throws IOException {
        return  is3Service.downLoadFile(fileName);
    }

    @GetMapping("/list")
    public List<String> geAllObject() throws IOException {
        return is3Service.listFiles();
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        return is3Service.uploadFile(file);
    }

    @PutMapping("/update/{oldFileName}")
    public String updateFile(@RequestParam("file") MultipartFile file, @PathVariable("oldFileName") String oldFileName) throws IOException {
        return is3Service.updateFile(file, oldFileName);
    }

    @DeleteMapping("/delete/{fileName}")
    public String deleteFile(@PathVariable("fileName")String fileName) throws IOException {
        return is3Service.deleteFile(fileName);
    }

}
