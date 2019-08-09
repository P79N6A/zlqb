package com.nyd.order.service;

import com.nyd.order.model.dto.BorrowConfirmDto;
import com.nyd.order.model.dto.BorrowDto;
import com.nyd.order.model.vo.*;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Created by dengw on 2017/11/8.
 */
public interface OrderInfoService {
    ResponseData<BorrowVo> getBorrowInfo(BorrowDto borrowDto);

    /**
     * 借款确认
     * @param borrowConfirmDto BorrowConfirmDto
     * @param isFirst boolean
     * @return ResponseData
     */
    ResponseData borrowInfoConfirm(BorrowConfirmDto borrowConfirmDto,boolean isFirst) throws Exception;

    ResponseData newBorrowInfoConfirm(BorrowConfirmDto borrowConfirmDto,boolean isFirst) throws Exception;

    ResponseData<BorrowResultVo> getBorrowResult(String userId);

    ResponseData<BorrowDetailVo> getBorrowDetail(String orderNo);

    ResponseData<List<BorrowRecordVo>> getBorrowAll(String userId);

    /**
     * 查询开户情况
     * @param borrowConfirmDto
     * @param b
     * @return
     */
    ResponseData selectOpenPage(BorrowConfirmDto borrowConfirmDto, boolean b);
}
