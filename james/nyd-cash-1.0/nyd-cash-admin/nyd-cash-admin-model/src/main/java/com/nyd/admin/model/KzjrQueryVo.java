package com.nyd.admin.model;

import com.nyd.admin.model.paging.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/12/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KzjrQueryVo extends Paging implements Serializable {
    private String productCode;
    private String startDate;
    private String endDate;
}
