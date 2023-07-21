package com.yash.s3.controller;

import com.yash.s3.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/s3-learn")
@RequiredArgsConstructor
public class S3FileController {

    private final FileService fileService;


    @PostMapping("/upload")
    private ResponseEntity<String> upload(@RequestBody MultipartFile multipartFile) {
        String responseText = fileService.saveFile(multipartFile);
        return ResponseEntity.status(HttpStatus.OK).body(responseText);
    }

    @DeleteMapping("/{file-name}")
    public ResponseEntity<String> delete(@PathVariable("file-name") String filename) {
        String responseText = fileService.deleteFile(filename);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseText);
    }

    @GetMapping("/download/{file-name}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("file-name") String filename) {
        return ResponseEntity.status(HttpStatus.OK).body(fileService.downloadFile(filename));
    }

    @GetMapping("/files")
    public ResponseEntity<List<String>> getAllFileNames(){
        return ResponseEntity.status(HttpStatus.OK).body(fileService.getAllFileNames());
    }
}
