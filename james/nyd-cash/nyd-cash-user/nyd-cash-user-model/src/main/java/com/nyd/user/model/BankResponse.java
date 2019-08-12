package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 17/11/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankResponse implements Serializable {
    private String bankCode;
    private String bankPhone;
    private String bankName;
}
