package com.challenge.ecommerce.files.controllers;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.files.services.IFileService;
import com.challenge.ecommerce.utils.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FileController {
  IFileService fileService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      content =
          @Content(
              schema = @Schema(implementation = ApiResponse.class),
              examples =
                  @ExampleObject(
                      value =
                          "{\n"
                              + "   \"message\": \"String\",\n"
                              + "   \"result\": \"String\"\n"
                              + "}")))
  public ResponseEntity<?> updateImage(@RequestParam MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new CustomRuntimeException(ErrorCode.IMAGE_NOT_FOUND);
    }
    var imageUrl = fileService.uploadFile(file);
    var resp = ApiResponse.builder().result(imageUrl).message("Update Image successfully").build();
    return ResponseEntity.ok(resp);
  }
}
