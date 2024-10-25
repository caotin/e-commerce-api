package com.challenge.ecommerce.files.controllers;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.utils.ApiResponse;
import com.challenge.ecommerce.utils.CloudUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FileController {
  @Autowired private CloudUtils cloudUtils;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> updateImage(@RequestParam MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new CustomRuntimeException(ErrorCode.IMAGE_NOT_FOUND);
    }
    String fileName = file.getOriginalFilename();
    if (!Objects.requireNonNull(fileName).endsWith(".jpg")
        && !fileName.endsWith(".png")
        && !fileName.endsWith(".tiff")
        && !fileName.endsWith(".webp")
        && !fileName.endsWith(".jfif")) {
      throw new CustomRuntimeException(ErrorCode.IMAGE_NOT_SUPPORT);
    }

    CompletableFuture<String> uploadFuture = cloudUtils.uploadFileAsync(file);

    try {
      String imageUrl = uploadFuture.get();
      var resp =
          ApiResponse.builder().result(imageUrl).message("Update Image successfully").build();
      return ResponseEntity.ok(resp);
    } catch (Exception e) {
      throw new CustomRuntimeException(ErrorCode.SET_IMAGE_NOT_SUCCESS);
    }
  }
}
