package com.jian.community.presentation.controller;

import com.jian.community.application.service.ImageService;
import com.jian.community.presentation.dto.ImageUrlResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class FileController {

    private final ImageService imageService;

    @PostMapping(value = "/profile-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ImageUrlResponse uploadProfileImages(@RequestParam("profileImage") MultipartFile profileImage) {
        return imageService.upload(profileImage);
    }
}
