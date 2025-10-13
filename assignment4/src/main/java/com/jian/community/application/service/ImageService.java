package com.jian.community.application.service;

import com.jian.community.application.exception.ErrorMessage;
import com.jian.community.application.util.FileStorage;
import com.jian.community.application.exception.ErrorCode;
import com.jian.community.application.exception.BadRequestException;
import com.jian.community.presentation.dto.ImageUrlResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private static final  int MAX_IMAGE_SIZE = 1024 * 1024 * 5;
    private static final String ROOT_DIR_NAME = "images";

    @Value("${app.image.base-url}")
    private String baseUrl;
    private final FileStorage fileStorage;

    public ImageService(
            @Value("${app.image.base-url}") String baseUrl,
            FileStorage fileStorage
    ) {
        this.baseUrl = baseUrl;
        this.fileStorage = fileStorage;
    }

    public ImageUrlResponse upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException(
                    ErrorCode.INVALID_IMAGE_FORMAT,
                    ErrorMessage.IMAGE_MUST_NOT_EMPTY
            );
        }

        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new BadRequestException(
                    ErrorCode.IMAGE_TOO_LARGE,
                    ErrorMessage.IMAGE_TOO_LARGE
            );
        }

        String filePath = fileStorage.save(file, ROOT_DIR_NAME);
        String imageUrl = String.format("%s/%s", baseUrl, filePath);
        return new ImageUrlResponse(imageUrl);
    }
}
