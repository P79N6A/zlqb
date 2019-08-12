package com.nyd.capital;

import com.nyd.capital.model.annotation.EncryptField;
import com.nyd.capital.model.annotation.RequireField;
import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/11/14
 **/
public @Data
class TestPo implements Serializable{
    @RequireField
    @EncryptField
    private String xm;
    private String dz;
    @EncryptField
    private String yhkh;
    @RequireField
    @EncryptField
    private String sjh;
    @RequireField
    private String sfz;
}
