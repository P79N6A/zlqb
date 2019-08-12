package com.nyd.settlement.model.vo.repay;

import lombok.Data;

/**
 * Created by Dengw on 2018/1/18
 * 还款审核结果
 */
@Data
public class AuditResultVo {
    //审核是否成功
    private boolean AuditFlag;
    //审核话术提示
    private String AuditMsg;
}
