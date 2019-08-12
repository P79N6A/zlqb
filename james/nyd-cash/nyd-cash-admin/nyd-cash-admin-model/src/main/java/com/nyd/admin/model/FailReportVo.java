package com.nyd.admin.model;

import com.nyd.admin.model.paging.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 * @author
 * @create 2017-12-14 22:12
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FailReportVo extends Paging implements Serializable {
    //日期
    private String startDate;

    private String endDate;
}
