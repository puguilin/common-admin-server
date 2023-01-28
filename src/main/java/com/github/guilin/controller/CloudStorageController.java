package com.github.guilin.controller;

import com.github.guilin.annotation.SysLogRecord;
import com.github.guilin.common.Constant;
import com.github.guilin.domain.vo.CloudStorageConfigVo;
import com.github.guilin.domain.vo.ResultVo;
import com.github.guilin.domain.vo.UploadVo;
import com.github.guilin.service.SysConfigService;
import com.github.guilin.service.impl.CloudStorageServiceImpl;
import com.github.guilin.utils.FileUtils;
import com.github.guilin.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "云端存储接口")
@RestController
public class CloudStorageController {
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private CloudStorageServiceImpl cloudStorageService;

    @SysLogRecord("上传指定文件")
    @ApiOperation("上传指定文件")
    @PostMapping("/api/cloudStorage/upload")
    public UploadVo upload(MultipartFile file) {
        if (file == null) {
            return UploadVo.fail("请传入要上传的文件！");
        }
        try {
            Map<String, String> sysConfigs = sysConfigService.selectMap();
            String cloudStorageConfigValue = sysConfigs.get(Constant.CLOUD_STORAGE_CONFIG);
            CloudStorageConfigVo cloudStorageConfig = JsonUtils.toBean(cloudStorageConfigValue, CloudStorageConfigVo.class);
            String domain = cloudStorageConfig.getDomain();
            String prefix = cloudStorageConfig.getPrefix();
            String randomFileName = FileUtils.getRandomFileName();
            String originalFileName = file.getOriginalFilename();
            String originalFileType = FileUtils.getFileExtension(originalFileName);
            long originalFileSize = file.getSize();
            String key = String.format("%s/%s%s", prefix, randomFileName, "".equals(originalFileType) ? "" : "." + originalFileType);
            String url = String.format("%s/%s", domain, key);
            boolean result = cloudStorageService.put(cloudStorageConfig, file.getBytes(), key);
            if (result) {
                return new UploadVo(Constant.RESPONSE_SUCCESS,
                        "上传成功！",
                        randomFileName,
                        originalFileType,
                        originalFileSize,
                        url,
                        key
                );
            } else {
                return UploadVo.fail("上传失败！");
            }
        } catch (Exception e) {
            return UploadVo.fail("请检查云存储参数！");
        }
    }

    @SysLogRecord("删除指定文件")
    @ApiOperation("删除指定文件")
    @GetMapping("/api/cloudStorage/delete")
    public ResultVo delete(String key) {
        try {
            Map<String, String> sysConfigs = sysConfigService.selectMap();
            String cloudStorageConfigValue = sysConfigs.get(Constant.CLOUD_STORAGE_CONFIG);
            CloudStorageConfigVo cloudStorageConfig = JsonUtils.toBean(cloudStorageConfigValue, CloudStorageConfigVo.class);
            boolean result = cloudStorageService.delete(cloudStorageConfig, key);
            if (result) {
                return ResultVo.success("删除成功！");
            } else {
                return ResultVo.fail("删除失败！");
            }
        } catch (Exception e) {
            return ResultVo.fail("请检查云存储参数！");
        }
    }

    @SysLogRecord("批量删除文件")
    @ApiOperation("批量删除文件")
    @GetMapping("/api/cloudStorage/batchDelete")
    public ResultVo batchDelete(@RequestParam(value = "keys[]", required = false) List<String> keys) {
        try {
            Map<String, String> sysConfigs = sysConfigService.selectMap();
            String cloudStorageConfigValue = sysConfigs.get(Constant.CLOUD_STORAGE_CONFIG);
            CloudStorageConfigVo cloudStorageConfig = JsonUtils.toBean(cloudStorageConfigValue, CloudStorageConfigVo.class);
            boolean result = cloudStorageService.batchDelete(cloudStorageConfig, keys);
            if (result) {
                return ResultVo.success("删除成功！");
            } else {
                return ResultVo.fail("删除失败！");
            }
        } catch (Exception e) {
            return ResultVo.fail("请检查云存储参数！");
        }
    }
}
