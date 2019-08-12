package com.nyd.admin.model;

import com.nyd.admin.model.paging.Paging;
import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/12/6
 **/
@Data
public class ReconQueryVo extends Paging implements Serializable{
    private String startDate;
    private String endDate;
    private String reconDate;
    private String resultCode;
    private String orderNo;
    private String fundCode;
}
