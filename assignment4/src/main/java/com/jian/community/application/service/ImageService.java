package com.jian.community.application.service;

import com.jian.community.application.util.FileStorage;
import com.jian.community.domain.constant.ErrorCode;
import com.jian.community.domain.exception.BadRequestException;
import com.jian.community.presentation.dto.ImageUrlResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ImageService {

    private final int MAX_IMAGE_SIZE = 1024 * 1024 * 5;
    private final String rootDir = "images";
    private final String baseUrl = "http://localhost:8080/";

    private final FileStorage fileStorage;

    public ImageUrlResponse upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException(
                    ErrorCode.INVALID_IMAGE_FORMAT,
                    "비어 있는 파일은 저장할 수 없습니다."
            );
        }

        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new BadRequestException(
                    ErrorCode.IMAGE_TOO_LARGE,
                    "최대 5MB의 이미지 파일만 저장할 수 있습니다."
            );
        }

        String filePath = fileStorage.save(file, rootDir);
        String imageUrl = String.format("%s/%s", baseUrl, filePath);
        return new ImageUrlResponse(imageUrl);
    }
}
