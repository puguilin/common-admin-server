package com.github.guilin.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SysMenu {
    private Long id;//菜单id
    private String name;//菜单名称
    private String code;//菜单标识
    private Integer type;//菜单类型
    private String icon;//菜单图标
    private String path;//路由地址
    private String component;//路由组件
    private Boolean hidden;//侧边栏显示（0：显示、1：隐藏）
    private Long parentId;//上级菜单
    private Integer orderNum;//显示顺序
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;//创建时间
    private List<SysMenu> children = new ArrayList<>();
}
