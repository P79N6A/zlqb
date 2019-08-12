package com.nyd.admin.model.power;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujx on 2018/1/5.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TreeNode {

    private Long powerId;

    private Long pid;

    private String powerName;
    //权限KEY
    private String powerKey;
    //权限url
    private String powerUrl;
    //跳转
    private Integer jmp;

    public List<TreeNode> childs = new ArrayList<>();
}
