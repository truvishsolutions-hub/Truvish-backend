package com.truvish.truvishbackend.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadDir;

    // ✅ Constructor — ensure folder exists
    public FileStorageService(@Value("${app.upload.dir:uploads}") String dir) throws Exception {
        this.uploadDir = Paths.get(dir).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadDir);
    }

    // =====================================================
    // ✅ STORE FILE (returns only filename for DB logo_img)
    // =====================================================
    public String storeFile(MultipartFile file) throws Exception {

        if (file == null || file.isEmpty()) {
            return null;
        }

        // 🔎 Original filename
        String original = file.getOriginalFilename();
        if (original == null || original.isBlank()) {
            original = "file";
        }

        // 🔎 Extract extension
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) {
            ext = original.substring(dot);
        }

        // 🔐 Unique filename
        String filename = UUID.randomUUID() + ext;

        // 📂 Target path
        Path target = uploadDir.resolve(filename);

        // 📥 Copy file
        Files.copy(
                file.getInputStream(),
                target,
                StandardCopyOption.REPLACE_EXISTING
        );

        // ✅ Save ONLY filename in DB
        return filename;
    }

    // =====================================================
    // ✅ LOAD FILE (for logo preview API)
    // =====================================================
    public Resource load(String filename) {
        try {
            if (filename == null || filename.isBlank()) {
                return null;
            }

            Path path = uploadDir.resolve(filename).normalize();
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }

    // =====================================================
    // 🧹 OPTIONAL — DELETE FILE
    // =====================================================
    public void delete(String filename) {
        try {
            if (filename == null || filename.isBlank()) {
                return;
            }

            Path path = uploadDir.resolve(filename).normalize();
            Files.deleteIfExists(path);

        } catch (Exception ignored) {
        }
    }
}