package com.nyd.settlement.model.dto.repay;

import com.nyd.settlement.model.paging.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 还款审核查询
 * Cong Yuxiang
 * 2018/1/16
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RepayAuditQueryDto extends Paging implements Serializable{
    private String name;
    private String mobile;
    private String type;//0 全部 1为已审核  2未审核
}
