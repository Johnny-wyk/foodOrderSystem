package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@Api(tags="通用接口")
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     * @param multipartFile
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile multipartFile){
        log.info("文件上传:{}",multipartFile);
        try {
            //原始文件名
            String originalFilename = multipartFile.getOriginalFilename();
            //获取原始文件名的后缀
            String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
           //构造新文件名称
            String s = UUID.randomUUID().toString() + substring;
            //文件的请求路径
            String filePath=aliOssUtil.upload(multipartFile.getBytes(),s);
            return Result.success(filePath);
        } catch (IOException e) {
            log.info("文件上传失败：{}",e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
