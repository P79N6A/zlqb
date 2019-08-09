package com.nyd.order.api;

import com.nyd.order.entity.PockerOrderEntity;
import com.nyd.order.entity.WithholdOrder;
import com.nyd.order.model.JudgeInfo;
import com.nyd.order.model.KzjrPageInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.WithholdOrderInfo;
import com.nyd.order.model.WithholdTaskConfig;
import com.nyd.order.model.dto.BorrowConfirmDto;
import com.nyd.order.model.vo.BorrowConfirmVo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.Date;
import java.util.List;

/**
 * Created by Dengw on 2017/11/13
 */
public interface OrderContract {
    ResponseData<OrderInfo> getOrderByOrderNo(String orderNo);

    ResponseData<WithholdOrderInfo> getWithholdOrderByMemberId(String memberId);

    ResponseData<JudgeInfo> judgeBorrow(String userId, boolean task);

    ResponseData<JudgeInfo> judgeBorrowNew(String userId, String appName);

    ResponseData<List<OrderInfo>> getOrdersByUserId(String userId);

    ResponseData<List<OrderInfo>> getLastOrderByUserId(String userId);


    /**
     * 借款确认
     *
     * @param borrowConfirmDto BorrowConfirmDto
     * @return ResponseData<BaseInfo>
     */
    ResponseData<BorrowConfirmVo> borrowInfoConfirm(BorrowConfirmDto borrowConfirmDto);

    //ResponseData borrowConfirmQuery(String userIdOuter,String accountNumber);

    ResponseData<KzjrPageInfo> kzjrPageErrorGenerateOrder(String userId) throws Exception;

    ResponseData updateOrderInfo(OrderInfo orderInfo);

    /**
     * 查询所有待放款的订单
     *
     * @return
     */
    ResponseData queryOrdersWhenOrderStatusIsWait(String fundCode);

    OrderInfo getOrderByIbankOrderNo(String orderNo);

    OrderInfo getOrderInfoByOrderNo(String orderNo);

    /**
     * @param @param  orderNo
     * @param @return
     * @return ResponseData<PockerOrderEntity>
     * @throws
     * @author chenjqt
     * @Description: 根据userId 获取 PockerOrderEntity
     */
    ResponseData<PockerOrderEntity> getByUserId(String orderNo);

    /**
     * @param @param  pockerOrderEntity
     * @param @return
     * @return ResponseData
     * @throws
     * @author chenjqt
     * @Description: 保存 PockerOrderEntity
     */
    ResponseData savePockerOrderEntity(PockerOrderEntity pockerOrderEntity);

    /**
     * @param @param  pockerOrderEntity
     * @param @return
     * @return ResponseData
     * @throws
     * @author chenjqt
     * @Description: TODO
     */
    ResponseData updatePockerOrderEntity(PockerOrderEntity pockerOrderEntity);

    ResponseData<PockerOrderEntity> getPocketOrderByOrderNo(String orderNo);

    ResponseData<WithholdTaskConfig> selectTaskTime();

    ResponseData<WithholdTaskConfig> selectTaskTimeByCode(String code);

    ResponseData updateTaskTime(Date startTime);

    ResponseData updateTaskTimeByCode(Date startTime, String code);

    /**
     * 根据口袋理财订单号查询订单信息
     *
     * @param pocketNo
     */
    ResponseData<PockerOrderEntity> getPockerOrderEntityByPocketNo(String pocketNo);

    /**
     * @param funCode
     * @return
     */
    ResponseData getKdlcWaitLoan(String funCode);

    /**
     * 查询所有状态为 创建订单处理中
     *
     * @return
     */
    List<PockerOrderEntity> taskCreateStatusAllData();

    ResponseData<WithholdOrderInfo> findWithholdOrderByMemberIdDesc(String memberId);


    ResponseData saveWithholdOrder(WithholdOrder order);

    ResponseData updateWithHoldOrder(WithholdOrder order);



    /**
     * 根据时间区间查询区间内拒绝订单
     *
     * @return
     */
    ResponseData selectWithholdOrder(String order);

    /**
     * 查询代扣中的所有订单
     * @return
     */
    ResponseData selectWithholdIng();

    ResponseData getRefusedOrders(Date startTime, Date endTime);

}
