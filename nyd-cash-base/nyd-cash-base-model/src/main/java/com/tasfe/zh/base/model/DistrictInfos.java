package com.tasfe.zh.base.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Created by Lait on 2017/7/16.
 */
@Data
@NoArgsConstructor
@ToString
public class DistrictInfos {

    private Long id;

    private Long pid;

    // 增加用户id 可以不传,如果不传默认为管理员
    private Long uid = 1L;

    // 类型 用户/产品/其他...
    private String type;

    // key 标签/系列/其他...
    private String key;

    // 值 标签/系列的值/其他的值...
    private String val;

    // 代码
    private String code;

    // 描述 描述
    private String desc;

    // 字典树
    private List<DistrictInfos> districtTrees;
}
