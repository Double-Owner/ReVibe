package com.doubleowner.revibe.global.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, originalFilename, file.getInputStream(), metadata);
        return amazonS3Client.getUrl(bucket, originalFilename).toString();

    }
    // S3 에서 이미지 삭제
    public void deleteImage(String image) throws IOException {
        amazonS3Client.deleteObject(bucket, image.substring(image.lastIndexOf("/") + 1));
    }
}
