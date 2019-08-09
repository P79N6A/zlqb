package com.nyd.settlement.service;

import com.nyd.settlement.entity.repay.RepaySettleTmp;

/**
 * 计算金额
 * Cong Yuxiang
 * 2018/1/23
 **/
public interface RepayCalService {
   void doRepay(RepaySettleTmp tmp) throws Exception;
}
