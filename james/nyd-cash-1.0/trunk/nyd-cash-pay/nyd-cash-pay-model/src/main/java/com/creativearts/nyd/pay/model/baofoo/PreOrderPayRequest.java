package com.creativearts.nyd.pay.model.baofoo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/12/12
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PreOrderPayRequest implements Serializable{
    private String version;
    private String input_charset;
    private String terminal_id;
    private String member_id;
    private String data_type;
    private String txt_sub_type;
//    private String
}
