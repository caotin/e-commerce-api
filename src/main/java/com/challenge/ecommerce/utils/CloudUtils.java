package com.challenge.ecommerce.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Slf4j
public class CloudUtils {
    Cloudinary cloudinary ;

    public CompletableFuture<String> uploadFileAsync(MultipartFile image) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                //log.info(uploadResult.toString());
                return uploadResult.get("secure_url").toString();
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file", e);
            }
        });
    }

    public void deleteFile(String imgUrl) {
        int lastSlashIndex = imgUrl.lastIndexOf("/");
        int lastDocIndex = imgUrl.lastIndexOf(".");
        String publicID = imgUrl.substring(lastSlashIndex+1,lastDocIndex);
        try {
            var result = cloudinary.uploader().destroy(publicID, ObjectUtils.emptyMap());
            result.get("result");
        } catch (IOException e) {
            throw  new RuntimeException("Failed to delete file", e);
        }
    }
    public void deleteFileAsync(String imgUrl) {
        int lastSlashIndex = imgUrl.lastIndexOf("/");
        int lastDocIndex = imgUrl.lastIndexOf(".");
        String publicID = imgUrl.substring(lastSlashIndex+1,lastDocIndex);
        CompletableFuture.supplyAsync( () -> {
            try {
                var result = cloudinary.uploader().destroy(publicID, ObjectUtils.emptyMap());
                result.get("result");
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete file", e);
            }
            return null;
        } );

    }
}
