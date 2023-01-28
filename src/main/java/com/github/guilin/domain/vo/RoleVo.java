package com.github.guilin.domain.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于存放选中的角色列表
 *
 * @author CaoChenLei
 */
@Data
public class RoleVo {
    private List<List<Long>> checks = new ArrayList<>();
}
