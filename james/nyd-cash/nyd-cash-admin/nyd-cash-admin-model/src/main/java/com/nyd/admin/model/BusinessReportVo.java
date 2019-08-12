package com.nyd.admin.model;

import com.nyd.admin.model.paging.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BusinessReportVo extends Paging implements Serializable {
    //日期
    private String startDate;

    private String endDate;
}
