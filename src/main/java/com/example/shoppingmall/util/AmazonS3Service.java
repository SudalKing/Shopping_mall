package com.example.shoppingmall.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.shoppingmall.domain.awsS3.dto.S3FileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AmazonS3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public List<S3FileDto> uploadFiles(String fileType, List<MultipartFile> multipartFiles){
        List<S3FileDto> s3FileDtoList = new ArrayList<>();
        String uploadFilePath = fileType + "/" + getFolderName();

        for(MultipartFile multipartFile: multipartFiles){
            String originalFileName = multipartFile.getOriginalFilename();
            String uploadFileName = getUUIDFileName(originalFileName);
            String uploadFileUrl = "";

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            try(InputStream inputStream = multipartFile.getInputStream()){
                String keyName = uploadFilePath + "/" + uploadFileName;
                amazonS3Client.putObject(new PutObjectRequest(
                        bucketName, keyName, inputStream, objectMetadata
                ));
                uploadFileUrl = amazonS3Client.getUrl(bucketName, keyName).toString();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("File upload failed", e);
            }

            s3FileDtoList.add(
                    S3FileDto.builder()
                            .originalFileName(originalFileName)
                            .uploadFileName(uploadFileName)
                            .uploadFilePath(uploadFilePath)
                            .uploadFileUrl(uploadFileUrl)
                            .build()
            );
        }
        return s3FileDtoList;
    }

    public String deleteFile(String uploadFilePath, String uuidFileName){
        String result = "success";

        try {
            String keyName = uploadFilePath + "/" + uuidFileName;
            boolean isObjectExist = amazonS3Client.doesObjectExist(bucketName, keyName);

            if (isObjectExist){
                amazonS3Client.deleteObject(bucketName, keyName);
            } else {
                result = "File Not Found!";
            }
        } catch(Exception e){
            log.debug("Delete File Failed", e);
        }

        return result;
    }

    private String getUUIDFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }

    private String getFolderName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        return str.replace("-", "/");
    }
}
