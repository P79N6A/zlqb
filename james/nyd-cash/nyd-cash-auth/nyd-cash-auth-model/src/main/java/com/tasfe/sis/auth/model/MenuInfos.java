package com.tasfe.sis.auth.model;

import lombok.Data;

import java.util.List;

/**
 * Created by Lait on 2017/7/31.
 */
@Data
public class MenuInfos {
    private Long id;
    // 菜单名称
    private String name;
    // 菜单的uri
    private String val;
    // 菜单图标
    private String icon;
    // 打开方式（当前窗口0，新窗口1）
    private int target;
    // 子节点
    private List<MenuInfos> childs;
}
