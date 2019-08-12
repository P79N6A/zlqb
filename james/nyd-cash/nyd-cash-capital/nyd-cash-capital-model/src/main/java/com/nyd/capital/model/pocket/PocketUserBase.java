package com.nyd.capital.model.pocket;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 口袋理财用户信息
 * @author liuqiu
 */
@Data
@ToString
public class PocketUserBase implements Serializable {

    /**
     * 身份证号
     */
    private String id_number;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 文化程度(1:'博士',2:'硕士',3:'本科',
     * 4:'大专'5:'中专',6:'高中',7:'初中',
     * 8:'初中以下')(非必填)
     */
    private int education_level;

    /**
     * 性别（男或女）
     */
    private String property;

    /**
     * 借款人类型(1:企业，2:个人)
     */
    private String type = "2";

    /**
     * 生日时间戳（非必传）
     */
    private int birthday;

    /**
     * 紧急/其他联系人姓名（非必传）
     */
    private String contact_username;

    /**
     * 紧急/其他联系人手机号（非必传）
     */
    private int contact_phone;
}
