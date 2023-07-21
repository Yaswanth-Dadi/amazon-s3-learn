package com.yash.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${bucketName}")
    private String s3BucketName;

    private final AmazonS3 s3;

    public String saveFile(MultipartFile multipartFile) {
        String filename = multipartFile.getOriginalFilename();
        try {
            PutObjectResult putObjectResult = s3.putObject(s3BucketName, filename, convertMultipartToFile(multipartFile));
            return putObjectResult.getContentMd5();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String deleteFile(String filename) {
        s3.deleteObject(s3BucketName,filename);
        return "File deleted Successfully!";
    }

    public byte[] downloadFile(String filename) {
        S3Object s3Object = s3.getObject(s3BucketName, filename);
        S3ObjectInputStream objectContent = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(objectContent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllFileNames() {
        ObjectListing objects = s3.listObjects(s3BucketName);
        return objects.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.getOriginalFilename() == null)
            return null;

        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(multipartFile.getBytes());
        outputStream.close();
        return file;
    }
}
