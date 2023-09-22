package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.util.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AmazonS3Controller {

    private final AmazonS3Service amazonS3Service;

    @PostMapping("/uploads")
    public ResponseEntity<Object> uploadFiles(
            @RequestParam(value = "fileType") String fileType,
            @RequestPart(value = "files")List<MultipartFile> multipartFiles
            ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(amazonS3Service.uploadFiles(fileType, multipartFiles));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteFile(
            @RequestParam(value = "uploadFilePath") String uploadFilePath,
            @RequestPart(value = "uuidFileName") String uuidFileName
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(amazonS3Service.deleteFile(uploadFilePath, uuidFileName));
    }
}
