package com.nyd.order.model.YmtKzjrBill.dto;

import com.nyd.order.model.YmtKzjrBill.BillYmtInfo;
import com.nyd.order.model.YmtKzjrBill.OverdueBillYmtInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 19:42 2018/7/6
 */
@Data
public class BillYmtDto implements Serializable{

    private Date startTime;

    private Date endTime;
    /**状态值*/
    private String status;
    /**ymt逾期信息配置表*/
    private ProductOverdueFeeItemYmtDto productOverdueFeeItemYmtDto;
    /**ymt账单信息表*/
    private BillYmtInfo billYmtInfo;
    /**逾期账单信息*/
    private OverdueBillYmtInfo overdueBillYmtInfo;
    /**资产编号*/
    private String  assetCode;

    private Integer currentPeriod;

}
