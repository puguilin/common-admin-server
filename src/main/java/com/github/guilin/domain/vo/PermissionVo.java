package com.github.guilin.domain.vo;

import com.github.guilin.domain.entity.SysMenu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于存放菜单列表以及选中的集合
 *
 * @author CaoChenLei
 */
@Data
public class PermissionVo {
    private List<SysMenu> menus = new ArrayList<>();
    private Long[] checks;
}
