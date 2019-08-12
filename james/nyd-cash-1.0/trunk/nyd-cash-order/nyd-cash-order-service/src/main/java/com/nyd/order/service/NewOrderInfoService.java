package com.nyd.order.service;

import com.nyd.order.model.BaseInfo;
import com.nyd.order.model.BorrowInfo;
import com.nyd.order.model.OrderUpdatInfo;
import com.nyd.order.model.WithholdCallBackInfo;
import com.nyd.order.model.dto.BorrowConfirmDto;
import com.nyd.order.model.dto.BorrowDto;
import com.nyd.order.model.dto.BorrowMqConfirmDto;
import com.nyd.order.model.msg.OrderMessage;
import com.nyd.order.model.vo.BorrowDetailVo;
import com.nyd.order.model.vo.BorrowRecordVo;
import com.nyd.order.model.vo.BorrowResultVo;
import com.nyd.order.model.vo.BorrowVo;
import com.nyd.user.entity.User;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * @author liuqiu
 * 订单改造
 */
public interface NewOrderInfoService {
    /**
     * 借款确认（生成订单）
     * @param borrowConfirmDto
     * @return
     * @throws Exception
     */
    ResponseData newBorrowInfoConfirm(BorrowConfirmDto borrowConfirmDto) throws Exception;

    /**
     * 借款结果
     * @param userId
     * @return
     */
    ResponseData<BorrowResultVo> getBorrowResult(String userId);

    /**
     * 借款详情
     * @param orderNo
     * @return
     */
    ResponseData<BorrowDetailVo> getBorrowDetail(String orderNo);
    /**
     * 订单详情
     * @param userId
     * @return
     */
    ResponseData<BorrowDetailVo> getBorrowDetailByUserId(String userId);

    /**
     * 所有借款记录
     * @param userId
     * @return
     */
    ResponseData<List<BorrowRecordVo>> getBorrowAll(String userId);
    /**
     * 获取新报告内容
     * @param base
     * @return
     */
    ResponseData getNewReport(BaseInfo base);
    /**
     * 所有借款记录区分appName
     * @param userId
     * @return
     */
    ResponseData<List<BorrowRecordVo>> getBorrowAllWithAppName(String userId,String appName);

    /**
     * 借款信息
     * @param borrowDto
     * @return
     */
    ResponseData<BorrowVo> getBorrowInfo(BorrowDto borrowDto);

    /**
     * 借款确认(代扣版)
     * @param borrowConfirmDto
     * @return
     */
    ResponseData borrowInfoConfirm(BorrowConfirmDto borrowConfirmDto) throws Exception;

    ResponseData mqRepost(BorrowMqConfirmDto borrowConfirmDto) throws Exception;


    /**
     * 借款信息(代扣版)
     * @param borrowDto
     * @return
     */
    ResponseData getBorrowInfoForPay(BorrowDto borrowDto);

    /**
     * 银行卡列表(代扣版)
     * @param borrowConfirmDto
     * @return
     */
    ResponseData borrowBanks(BorrowConfirmDto borrowConfirmDto);

    ResponseData queryResult(BaseInfo baseInfo);

    /**
     * 代扣回调
     * @param message
     * @return
     */
    ResponseData withholdCallBack(OrderMessage message);
    /**
     * 代扣回调
     * @param info
     * @return
     */
    ResponseData updateOrderNoticeStatus(OrderUpdatInfo info);

    /**
     * 代扣回调发短信
     * @param withholdCallBackInfo
     * @return
     */
    ResponseData withholdCallBackMsg(WithholdCallBackInfo withholdCallBackInfo);


    ResponseData  queryPayOrderByBusinessOrderNo(String businessOrderNo);


   /**
     * 借款详情
     * @param orderNo
     * @return
     */
    ResponseData<BorrowInfo> getBorrowInfoByOrderNo(String orderNo);
    /**
     * 订单详情
     * @param orderNo
     * @return
     */
    ResponseData<BorrowInfo> getBorrowInfoByO(String orderNo);

    /**
     * 所有借款记录
     * @param userId
     * @return
     */
    ResponseData<List<BorrowInfo>> getBorrowInfoAll(String userId);
    
    public ResponseData  checkOrderSuccess(User user);
}
