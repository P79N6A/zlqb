package com.nyd.settlement.dao.mapper;

import com.nyd.settlement.entity.refund.QueryRefundEntity;
import com.nyd.settlement.model.dto.QueryDto;
import com.nyd.settlement.model.po.refund.AlreadyRefundOrderPo;
import com.nyd.settlement.model.po.refund.RefundOrderPo;
import com.nyd.settlement.model.vo.refund.RefundOrderDetailVo;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by hwei on 2018/1/15.
 */
@Mapper
public interface BillOrderMapper {
    /**
     * 获取需要退款的订单信息
     * @return
     */
    List<RefundOrderPo> getReFundOrderNo(QueryRefundEntity queryRefundEntity);

    /**
     * 查询未退款详情
     * @param queryDto
     * @return
     */
    List<RefundOrderDetailVo> getRefundDetail(QueryDto queryDto);

    /**
     * 根据订单号手机号查询订单
     * @param map
     * @return
     */
    String queryOrderDetailByOrderNoAndMobile(Map map);

    /**
     * 根据订单号 账单号查询已退款金额
     * @param map
     * @return
     */
    BigDecimal queryAlreadRefundAmount(Map map);

    /**
     * 更新bill中的已退款金额
     * @param map
     */
    void updateRefundAmount(Map map);

    /**
     * 查询已退款记录
     * @param queryRefundEntity
     * @return
     */
    List<AlreadyRefundOrderPo> queryAlreadyReFund(QueryRefundEntity queryRefundEntity);
}
