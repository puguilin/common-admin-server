package com.github.guilin.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SysDepartment {
    private Long id;//部门id
    private String name;//部门名称
    private String manager;//部门经理
    private String phone;//部门电话
    private String email;//部门邮箱
    private Long parentId;//上级部门
    private Integer orderNum;//显示顺序
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;//创建时间
    private List<SysDepartment> children = new ArrayList<>();
}
