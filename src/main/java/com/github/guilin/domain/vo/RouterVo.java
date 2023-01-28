package com.github.guilin.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由表项视图，属性来源请参考：https://panjiachen.gitee.io/vue-element-admin-site/zh/guide/essentials/router-and-nav.html
 *
 * @author CaoChenLei
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo {
    private String name;
    private String path;
    private String component;
    private boolean hidden;
    private boolean alwaysShow;
    private String redirect;
    private Meta meta;
    private List<RouterVo> children = new ArrayList<>();

    @Data
    @AllArgsConstructor
    public class Meta {
        private String title;
        private String icon;
        private String[] roles;
        //private boolean noCache;
        //private boolean breadcrumb;
        //private boolean affix;
        //private String activeMenu;
    }
}
