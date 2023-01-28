package com.github.guilin.domain.vo;

import lombok.Data;

/**
 * 云存储配置
 *
 * @author CaoChenLei
 */
@Data
public class CloudStorageConfigVo {
    private Integer type;//存储类型（0：七牛云、1：阿里云、2：腾讯云）
    private String bucket;//空间名称
    private String region;//所属区域
    private String domain;//访问域名
    private String prefix;//路径前缀
    /**
     * 全部统一为accessKey
     * 七牛云：AccessKey
     * 阿里云：AccessKey ID
     * 腾讯云：SecretId
     */
    private String accessKey;
    /**
     * 全部统一为secretKey
     * 七牛云：SecretKey
     * 阿里云：AccessKey Secret
     * 腾讯云：SecretKey
     */
    private String secretKey;
}
