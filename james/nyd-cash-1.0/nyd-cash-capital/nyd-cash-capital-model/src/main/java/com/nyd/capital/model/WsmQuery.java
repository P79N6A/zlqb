package com.nyd.capital.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/11/14
 **/
public @Data class WsmQuery implements Serializable{
    private List<String> shddhList;
    private String mid;
}
