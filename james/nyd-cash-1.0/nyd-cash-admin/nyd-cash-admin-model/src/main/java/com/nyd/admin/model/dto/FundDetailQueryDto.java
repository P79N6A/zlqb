package com.nyd.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;

/**
 * @author Peng
 * @create 2018-01-02 11:02
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FundDetailQueryDto implements Serializable{
    //姓名
    private String name;
    //证件号码
    private String idNumber;
    //开始时间
    private String startDate;
    //结束时间
    private String endDate;


    //资产Id
    private String fundId;

}
