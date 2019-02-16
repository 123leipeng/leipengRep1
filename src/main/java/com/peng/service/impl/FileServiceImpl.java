package com.peng.service.impl;

import com.google.common.collect.Lists;
import com.peng.service.IFileService;
import com.peng.util.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);

        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;

        logger.info("开始文件上传：开始上传的文件文件名：{}，上传路径：{}，新文件名：{}", fileName, path, uploadFileName);
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile);
            FtpUtil.uploadFile(Lists.<File>newArrayList(targetFile));
            targetFile.delete();

        } catch (IOException e) {
            logger.info("文件上传失败", e);
            return null;
        }
        return targetFile.getName();
    }
}
