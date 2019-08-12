package com.nyd.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;

/**
 * Created by hwei
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InnerTestInfo {
    //主键id
    @Id
    private Long id;
    //手机号
    private String mobile;
    //姓名
    private String realName;
    //是否在用0在用，1弃用
    private Integer isInUse;
    //是否已删除
    private Integer deleteFlag;

}
