package com.nyd.product.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/22.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_product_fund_rel")
public class ProductFundRel {

    @Id
    private Long id;
    //金融产品code
    private String productCode;
    //资金源编码
    private String fundCode;
    //优先级
    private Integer priority;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;

}
