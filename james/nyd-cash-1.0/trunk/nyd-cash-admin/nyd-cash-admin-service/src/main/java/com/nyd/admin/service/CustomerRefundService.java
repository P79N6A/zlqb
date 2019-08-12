package com.nyd.admin.service;

import com.nyd.admin.model.dto.CustomerRefundDto;
import com.nyd.admin.model.dto.RefundApplyDto;
import com.nyd.user.model.RefundApplyInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @Author: wucx
 * @Date: 2018/11/2 18:34
 */
public interface CustomerRefundService {

    /**
     * 审核接口
     * @param customerRefundDto
     * @return
     */
    ResponseData customerOperational(CustomerRefundDto customerRefundDto);
    /**
     * 查询
     * @param refundApplyInfo
     * @return
     */
    ResponseData getRefundList(RefundApplyDto refundApplyInfo);
    /**
     * 查询详情
     * @param refundApplyInfo
     * @return
     */
    ResponseData getRefundDetail(RefundApplyDto refundApplyInfo);

    /**
     * 财务查询
     * @param refundApplyDto
     * @return
     */
    ResponseData financeQueryRefundList(RefundApplyDto refundApplyDto);
}
