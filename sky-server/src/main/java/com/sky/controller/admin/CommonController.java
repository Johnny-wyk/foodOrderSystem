package com.sky.controller.admin;

import com.sky.dto.DishPageQueryDTO;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public Result<String> upload(@RequestBody MultipartFile file){
        log.info("上传文件,{}",file);

        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //获取原始文件名的后缀
            String substring = originalFilename.substring(originalFilename.lastIndexOf(","));
            //构造新文件名称
            String objectName = UUID.randomUUID().toString() + substring;
            //文件请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);

            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败,{}",e);
        }
        return Result.error("文件上传失败");
    }


}
