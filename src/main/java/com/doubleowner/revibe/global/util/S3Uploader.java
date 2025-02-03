package com.doubleowner.revibe.global.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.doubleowner.revibe.global.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;

import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.FAILED_UPLOAD_IMAGE;

@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final String[] PERMITTED_FILE_EXTENSIONS = {"jpg", "png", "jpeg", "gif", "pdf", "csv"};

    public String upload(MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            validateFile(originalFilename);
        }
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, originalFilename, file.getInputStream(), metadata);
        return amazonS3Client.getUrl(bucket, originalFilename).toString();

    }

    // S3 에서 이미지 삭제
    public void deleteImage(String image) throws IOException {
        String imageKey = image.replace("https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/", "");
        imageKey = URLDecoder.decode(imageKey, "UTF-8");
        amazonS3Client.deleteObject(bucket, imageKey);
    }

    private void validateFile(String fileName) {

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();


        boolean isAllowed = Arrays.asList(PERMITTED_FILE_EXTENSIONS).contains(extension);

        if (!isAllowed) {
            throw new CommonException(FAILED_UPLOAD_IMAGE, "올바른 확장자가 아닙니다");
        }
    }
}
