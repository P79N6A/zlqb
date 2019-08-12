package com.nyd.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/11/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VersionModel implements Serializable {
    //更新地址
    private String downloadUrl;
    //更新状态：1更新0不更新
    private Integer updated = 0;
    //是否强制更新：1强制更新0 不强制
    private Integer force;
    //是否强制更新：1强制更新0 不强制 临时使用
    private Integer forc;
    //备注
    private String remark;
}
