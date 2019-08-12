package com.nyd.capital.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Cong Yuxiang
 * 2017/12/15
 **/
@Data
public class TimeRangeVo implements Serializable{
    private Date startDate;
    private Date endDate;
    private Integer duration;
}
