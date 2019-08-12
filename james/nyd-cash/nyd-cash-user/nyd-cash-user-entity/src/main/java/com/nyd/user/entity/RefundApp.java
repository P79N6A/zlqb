package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hwei on 2017/11/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_refund_app")
public class RefundApp implements Serializable{
    @Id
    private Long id;
    //app code
    private String appCode;
    //app名称
    private String refundAppName;
    //app url
    private String appUrl;
    //logo
    private String appLogo;
    //排序
    private Integer appSort;
    //推荐次数
    private Integer recomNum;
    //状态0启用，1停用
    private Integer appStatus;
    //最后修改人
    private String updateBy;

}
