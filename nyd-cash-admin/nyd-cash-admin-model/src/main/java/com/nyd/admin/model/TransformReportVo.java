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
public class TransformReportVo extends Paging implements Serializable {
    //日期（条件）
    private String startDate;
    //日期（条件）
    private String endDate;
    //渠道
    private String source;

}
