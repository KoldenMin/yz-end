package com.example.yz1.global;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Repository
public class GlobalUpload {
    @Value("${file.upload.path:uploads/avatars}")
    private String uploadPath;
    @Value("${file.upload.resume.path:uploads/resumes}")
    private String resumeUploadPath;

    private static final String[] ALLOWED_EXTENSIONS = {".pdf", ".doc", ".docx"};

    public String uploadAvatar(Long id, MultipartFile file, String prefix) throws IOException {
        // 确保上传目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
        String newFilename = prefix + "_" + id + "_" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;

        // 保存文件
        File destFile = new File(uploadDir.getAbsolutePath() + File.separator + newFilename);
        file.transferTo(destFile);

        // 返回访问URL
        return "/avatars/" + newFilename;
    }

    public String uploadResume(Long userId, MultipartFile file, String prefix, String[] allowedExtensions) throws IOException {
        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 确保上传目录存在
        File uploadDir = new File(resumeUploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 获取原始文件名和扩展名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("文件名无效");
        }

        // 验证文件类型
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (Arrays.stream(allowedExtensions).noneMatch(fileExtension::equals)) {
            throw new IllegalArgumentException("不支持的文件格式");
        }

        // 生成唯一文件名
        String newFilename = prefix + "_" + userId + "_" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;

        // 保存文件
        File destFile = new File(uploadDir.getAbsolutePath() + File.separator + newFilename);
        file.transferTo(destFile);

        // 返回访问 URL
        return "/resumes/" + newFilename;
    }

}
