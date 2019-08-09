package com.nyd.admin.service;

import com.nyd.admin.model.dto.BatchCouponDto;
import com.nyd.admin.model.dto.IntegratedVolumeDto;
import com.nyd.admin.model.dto.RemarkDto;
import com.nyd.admin.model.dto.ReturnPremiumLabelDto;
import com.tasfe.framework.support.model.ResponseData;

public interface ReturnPremiumService {

    /**
     * 退费标记
     * @param returnPremiumLabelDto
     * @return
     */
    ResponseData saveReturnPremiumLabel(ReturnPremiumLabelDto returnPremiumLabelDto);

    /**
     * 综合券管理（侬要贷）查询
     * @param integratedVolumeDto
     * @return
     */
    ResponseData findIntegratedVolumeDetails(IntegratedVolumeDto integratedVolumeDto);

    /**
     * 批量发券
     * @param batchCouponDto
     * @return
     */
    ResponseData batchCoupons(BatchCouponDto batchCouponDto);

    /**
     * 修改备注
     * @param remarkDto
     * @return
     */
    ResponseData updateRemark(RemarkDto remarkDto);
}
