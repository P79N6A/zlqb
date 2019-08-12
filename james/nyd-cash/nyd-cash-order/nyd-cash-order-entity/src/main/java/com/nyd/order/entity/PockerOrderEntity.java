package com.nyd.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * 
* @ClassName: PockerOrderEntity
* @Description: 口袋理财订单实体类
* @author chenjqt
* @date 2018年9月29日
*
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_pocket_order")
public class PockerOrderEntity implements Serializable{
	//为口袋理财生成的user_id,也是主键id
    @Id
    private Long id;
    //关联userId
    private Integer pocketUserId;
    //订单编号
    private String orderNo;
    //用户ID
    private String userId;
    //口袋理财放款订单编号(我们自己生成的30位Id)
    private String pocketNo;
    //交易状态
    private Integer orderStatus;
    //第三方通道编号
    private String thirdPlatform;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //
    private Integer deleteFlag;
    //
    private String updateBy;
    //口袋理财放款订单号（放款接口成功后，）
    private String payOrderId;
    //口袋理财创建订单号
    private String pocketCreateOrderId; 
     
}
