package com.leyou.upload.controller;

import com.leyou.upload.service.UploadService;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file){
        log.info("测试lombok@Slf4j注释");
        String  url=this.uploadService.uploadImage(file);
        if(StringUtils.isBlank(url)){
            return ResponseEntity.badRequest().build();

        }return ResponseEntity.status(HttpStatus.CREATED).body(url);
    }
}
