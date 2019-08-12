package com.nyd.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by hwei on 2017/12/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DspReportVo implements Serializable{
    //开始日期
    private String startDate;
    //结束日期
    private String endDate;
    //数据源接口
    private String dspInterface;

}
