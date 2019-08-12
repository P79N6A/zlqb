package com.nyd.admin.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/12/1
 **/
@Data
public class ReconciliationImportVo implements Serializable{
    private String fundCode;
    private String reconDate;
}
