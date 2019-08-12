package com.nyd.wsm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by zhujx on 2017/12/8.
 * 用户联系人信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Contact {
    //用户ID
    private String userId;
    //联系人类型
    private String type;
    //联系人姓名
    private String name;
    //联系人手机号
    private String mobile;
    //与联系人的关系
    private String relationship;
}
