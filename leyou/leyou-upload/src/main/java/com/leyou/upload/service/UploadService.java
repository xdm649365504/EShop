package com.leyou.upload.service;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.SocketUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.xml.ws.soap.Addressing;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {

    @Autowired
    private FastFileStorageClient storageClient;

    private static final Logger logger = LoggerFactory.getLogger(UploadService.class);
    // private static String  str[]={"a","b"};
    private static final List<String> CONTENT_TYPES = Arrays.asList("image/gif", "image/jpeg");

    public String uploadImage(MultipartFile file) {
        //校验文件类型
        String originalFilename = file.getOriginalFilename();
//          originalFilename=System.currentTimeMillis()+originalFilename;
        // StringUtils.substringAfterLast(originalFilename,".");
        String contentType = file.getContentType();
        if (!CONTENT_TYPES.contains(contentType)) {
            logger.info("文件类型不合法: {}", originalFilename);
            return null;
        }
        try {
            //校验文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                logger.info("文件内容不合法：{}", originalFilename);
                return null;
            }
            //保存到文件服务器
            file.transferTo(new File("c:\\leyou\\image\\" + originalFilename));
            //保存到fastdfs虚拟机服务器
//            String ext = StringUtils.substringAfterLast(originalFilename, ".");
//            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);

            //返回url进行回显
            System.out.println(originalFilename);
            return "http://image.leyou.com/" + originalFilename;
            //保存到fastdfs
           // return "http://image.leyou.com/" + storePath.getFullPath();
        } catch (IOException e) {
            logger.info("服务器内部异常{}", originalFilename);
            e.printStackTrace();
        }
        return null;
    }
}
