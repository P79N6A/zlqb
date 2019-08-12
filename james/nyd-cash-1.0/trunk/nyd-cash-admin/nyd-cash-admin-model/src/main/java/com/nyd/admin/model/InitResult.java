package com.nyd.admin.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/11/30
 **/
@Data
public class InitResult implements Serializable{
    private List<EnumModel> fundSourceType;
    private List<EnumModel> inUseType;
    private List<EnumModel> reconciliationType;
    private List<EnumModel> wsmReconciliationType;
}
