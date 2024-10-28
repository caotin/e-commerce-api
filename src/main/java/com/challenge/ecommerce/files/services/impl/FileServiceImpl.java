package com.challenge.ecommerce.files.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.files.services.IFileService;
import com.challenge.ecommerce.utils.CloudUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FileServiceImpl implements IFileService {

  CloudUtils cloudUtils;

  @Override
  public String uploadFile(MultipartFile file) {
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
      return uploadFuture.get();
    } catch (Exception e) {
      throw new CustomRuntimeException(ErrorCode.SET_IMAGE_NOT_SUCCESS);
    }
  }
}
