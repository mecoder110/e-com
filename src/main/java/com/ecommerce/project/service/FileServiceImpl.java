package com.ecommerce.project.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@Log
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImageToServer(String path, MultipartFile file) throws IOException {
        log.info("Uploading image");
        // Create random Filename to be uniquely identify
        String fileName = UUID.randomUUID().toString();

        // Append file extention to unique Filename
        fileName = fileName.concat(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
        log.info("Image Name: " + fileName);

        // create the file path uri
        String pathUrl = path + File.separator + fileName;
        log.info("Server Path: " + path);
        // check if path exist or create it
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        // copy file to loaction
        long copy = Files.copy(file.getInputStream(), Path.of(pathUrl));
        log.info("Saved: " + copy);
        return fileName;
    }
}
