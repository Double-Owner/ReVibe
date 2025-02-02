package com.doubleowner.revibe.global.common.service;

import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import com.doubleowner.revibe.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final S3Uploader s3Uploader;

    public String uploadImage(String image, MultipartFile file) {
        try {
            if (image != null) {
                s3Uploader.deleteImage(image);
            }
            return s3Uploader.upload(file);
        } catch (IOException e) {
            throw new CommonException(ErrorCode.FAILED_UPLOAD_IMAGE);
        }
    }

    public void deleteImage(String image) {
        try {
            s3Uploader.deleteImage(image);
        } catch (IOException e) {
            throw new CommonException(ErrorCode.FAILED_DELETE_IMAGE);
        }
    }

}
