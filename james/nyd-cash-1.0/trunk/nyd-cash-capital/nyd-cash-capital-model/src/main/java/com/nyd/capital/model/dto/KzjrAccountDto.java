package com.nyd.capital.model.dto;

import com.nyd.capital.model.enums.KzjrAccountStatus;
import lombok.Data;

/**
 * Cong Yuxiang
 * 2017/12/13
 **/
@Data
public class KzjrAccountDto {
    private String p2pId;
    private KzjrAccountStatus status;
    private Long id;
}
