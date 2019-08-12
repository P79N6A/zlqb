package com.nyd.admin.dao.mapper;

import com.nyd.admin.model.Info.CustomerQueryInfo;
import com.nyd.admin.model.Info.OrderDetailsInfo;
import com.nyd.admin.model.Info.RechargePaymentRecordInfo;
import com.nyd.admin.model.Info.RepayInfo;
import com.nyd.admin.model.dto.CustomerQueryDto;
import com.nyd.admin.model.dto.RechargePaymentRecordDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author cm
 */
@Mapper
public interface CustomerHandleQueryMapper {


    List<CustomerQueryInfo> getAllCustomer(CustomerQueryDto customerQueryDto);

    /**
     * 充值付费记录
     * @param rechargePaymentRecordDto
     * @return
     */
    List<RechargePaymentRecordInfo> findRechargePaymentRecords(RechargePaymentRecordDto rechargePaymentRecordDto);

    /**
     * 支付评估费个数
     * @param rechargePaymentRecordDto
     * @return
     */
    Integer findPayAssessCount(RechargePaymentRecordDto rechargePaymentRecordDto);

    /**
     * 查询交易渠道
     * @param userId
     * @param createTime
     * @return
     */
    List<RepayInfo> findRepayChannelByUserIdAndCreateTime(@Param("userId") String userId, @Param("createTime") String createTime);

    /**
     * 订单详情
     * @param rechargePaymentRecordDto
     * @return
     */
    List<OrderDetailsInfo> findOrderDetails(RechargePaymentRecordDto rechargePaymentRecordDto);

    /**
     * 根据会员编号查询优惠券id
     * @param memberId
     * @return
     */
    String findCouponIdByMemberId(@Param("memberId") String memberId);

    /**
     * 根据优惠卷id获取优惠券金额
     * @param couponId
     * @return
     */
    BigDecimal findCouponFeeByCouponId(@Param("couponId") String couponId);

    /**
     * 查询代扣记录
     * @param rechargePaymentRecordDto
     * @return
     */
    List<RechargePaymentRecordInfo> findWithHoldOrder(RechargePaymentRecordDto rechargePaymentRecordDto);

    /**
     * 查询身份证号
     * @param rechargePaymentRecordDto
     * @return
     */
    String findIdNumberByUserId(RechargePaymentRecordDto rechargePaymentRecordDto);

    /**
     * 查询代扣记录总个数
     * @param rechargePaymentRecordDto
     * @return
     */
    Integer findWithHoldOrderCount(RechargePaymentRecordDto rechargePaymentRecordDto);

    /**
     * 查询充值和现金支付记录
     * @param rechargePaymentRecordDto
     * @return
     */
    List<RechargePaymentRecordInfo> findRechargeAndCashPayDetails(RechargePaymentRecordDto rechargePaymentRecordDto);

    /**
     * 查询充值和现金支付记录个数
     * @param rechargePaymentRecordDto
     * @return
     */
    Integer findRechargeAndCashPayCount(RechargePaymentRecordDto rechargePaymentRecordDto);

    /**
     * 查询赠送记录
     * @param rechargePaymentRecordDto
     * @return
     */
    List<RechargePaymentRecordInfo> findReturnTicketLog(RechargePaymentRecordDto rechargePaymentRecordDto);

    /**
     * 查询赠送记录个数
     * @param rechargePaymentRecordDto
     * @return
     */
    Integer findReturnTicketLogCount(RechargePaymentRecordDto rechargePaymentRecordDto);

}
