package io.bootify.ecommerce_app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileService {
    public String uploadFile(String path, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = path + fileName;
        File f = new File(path);
        if(!f.exists()) {
            f.mkdir();
        }
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }

    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
        String filePath = path + fileName;
        return new FileInputStream(filePath);
    }
}
