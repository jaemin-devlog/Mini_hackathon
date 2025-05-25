package com.example.running.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir; // application.properties 등에서 설정

    public String uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String newFilename = UUID.randomUUID().toString() + extension;

        Path targetLocation = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(newFilename);
        Files.createDirectories(targetLocation.getParent());
        file.transferTo(targetLocation.toFile());

        return "/uploads/" + newFilename;  // 반환값: 접근 가능한 URL 경로
    }
}
