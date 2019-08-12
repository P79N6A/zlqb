package com.nyd.admin.model;

import com.nyd.admin.model.paging.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/12/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductQueryVo extends Paging implements Serializable {
    private String productCode;
    private String productName;
    private String business;
    private String startDate;
    private String endDate;
}
