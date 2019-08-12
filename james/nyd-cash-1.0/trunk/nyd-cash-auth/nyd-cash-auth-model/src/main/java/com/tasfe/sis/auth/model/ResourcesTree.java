package com.tasfe.sis.auth.model;

import lombok.Data;

import java.util.List;

/**
 * 资源树
 * Created by Lait on 2017/8/24.
 */
@Data
public class ResourcesTree {

    private Long id;
    private Long pid;
    private String name;
    private int level;
    private List<ResourcesTree> childs;

}
