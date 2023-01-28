package com.github.guilin.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysLog {
    private Long id;//日志id
    private Integer type;//日志类型
    private String title;//日志标题
    private String mapping;//请求映射
    private String mode;//请求方式
    private String method;//请求方法
    private String params;//请求参数
    private String result;//请求结果
    private String exception;//异常信息
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startTime;//开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endTime;//结束时间
    private String remoteIp;//请求地址
    private String remoteRegion;//请求区域
    private Long createBy;//操作用户
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;//创建时间
}
