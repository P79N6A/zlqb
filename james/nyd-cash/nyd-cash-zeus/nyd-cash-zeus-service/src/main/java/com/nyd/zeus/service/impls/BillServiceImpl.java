package com.nyd.zeus.service.impls;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.nyd.order.api.OrderDetailContract;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.zeus.api.BillContract;
import com.nyd.zeus.dao.BillDao;
import com.nyd.zeus.dao.OverdueBillDao;
import com.nyd.zeus.model.BillDetail;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.OverdueBillInfo;
import com.nyd.zeus.service.BillService;
import com.nyd.zeus.service.enums.BillStatusEnum;
import com.nyd.zeus.service.util.DateUtil;
import com.tasfe.framework.support.model.ResponseData;
import com.tasfe.framework.uid.service.BizCode;
import com.tasfe.framework.uid.service.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhujx on 2017/11/18.
 */
@Service(value = "billContract")
public class BillServiceImpl implements BillContract,BillService{

    private static Logger LOGGER = LoggerFactory.getLogger(BillServiceImpl.class);

    @Autowired
    private BillDao billDao;

    @Autowired
    private OverdueBillDao overdueBillDao;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired(required = false)
    private OrderDetailContract orderDetailContract;


    /**
     * 据userId获取账单信息  决策系统用
     * @param userId
     * @return
     */
    @Override
    public ResponseData<List<BillInfo>> queryBillInfoByUserId(String userId) {
        ResponseData responseData = ResponseData.success();
        try {
            List<BillInfo> billInfoList = billDao.getObjectsByUserId(userId);
            responseData.setData(billInfoList);
        } catch (Exception e) {
            responseData = ResponseData.error("获取账单信息失败");
            LOGGER.error("queryBillInfoByUserId error! userId = " + userId,e);
        }
        return responseData;
    }

    /**
     * 根据订单号获取账单信息  决策系统用
     * @param orderNo
     * @return
     */
    @Override
    public ResponseData<List<BillInfo>> queryBillInfoByOrderNO(String orderNo) {
        ResponseData responseData = ResponseData.success();
        try {
            List<BillInfo> billInfoList = billDao.getObjectsByOrderNo(orderNo);
            responseData.setData(billInfoList);
        } catch (Exception e) {
            responseData = ResponseData.error("获取账单信息失败");
            LOGGER.error("queryBillInfoByOrderNO error! orderNo = " + orderNo, e);
        }
        return responseData;
    }

    @Override
    public ResponseData<List<BillInfo>> queryBillInfoByYmtOrderNo(String ibankOrderNo) {
        ResponseData responseData = ResponseData.success();
        try {
            List<BillInfo> billInfoList = billDao.getObjectsByYmtOrderNo(ibankOrderNo);
            responseData.setData(billInfoList);
        } catch (Exception e) {
            responseData = ResponseData.error("获取账单信息失败");
            LOGGER.error("queryBillInfoByYmtOrderNo error! ibankOrderNo = " + ibankOrderNo, e);
        }
        return responseData;
    }

    /**
     * 据userId获取逾期账单信息  决策系统用
     * @param userId
     * @return
     */
    @Override
    public ResponseData<List<OverdueBillInfo>> queryOverdueBillInfoByUserId(String userId) {
        ResponseData responseData = ResponseData.success();
        try {
            List<OverdueBillInfo> overdueBillInfoList = overdueBillDao.getObjectsByUserId(userId);
            responseData.setData(overdueBillInfoList);
        } catch (Exception e) {
            responseData = ResponseData.error("获取逾期账单信息失败");
            LOGGER.error("queryOverdueBillInfoByUserId error! userId = " + userId,e);
        }
        return responseData;
    }

    /**
     * 根据订单号获取逾期账单信息  决策系统用
     * @param orderNo
     * @return
     */
    @Override
    public ResponseData<List<OverdueBillInfo>> queryOverdueBillInfoByOrderNO(String orderNo) {
        ResponseData responseData = ResponseData.success();
        try {
            List<OverdueBillInfo> overdueBillInfoList = overdueBillDao.getObjectsByOrderNo(orderNo);
            responseData.setData(overdueBillInfoList);
        } catch (Exception e) {
            responseData = ResponseData.error("获取逾期账单信息失败");
            LOGGER.error("queryOverdueBillInfoByOrderNO error! orderNo = " + orderNo, e);
        }
        return responseData;
    }

    /**
     * 查询未还清账单信息
     * @param userId
     * @return
     */
    @Override
    public ResponseData<Integer> getBillInfos(String userId) {
        LOGGER.info("获取的userId"+userId);
        ResponseData responseData = ResponseData.success();
        int count = 0;
        try {
            List<BillInfo> billInfoList = billDao.getObjectsByUserId(userId);
            if(billInfoList != null && billInfoList.size()>0){
                for(BillInfo billInfo : billInfoList){
                    if(!BillStatusEnum.REPAY_SUCCESS.getCode().equals(billInfo.getBillStatus())){
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            responseData = ResponseData.error("获取账单信息失败");
            LOGGER.error("get billInfo error! userId = "+userId,e);
        }
        responseData.setData(count);
        return responseData;
    }

    @Override
    public ResponseData<BillInfo> getBillInfo(String billNo) {
        ResponseData responseData = ResponseData.success();
        try {
            List<BillInfo> billInfoList = billDao.getObjectsByBillNo(billNo);
            if(billInfoList !=null && billInfoList.size() > 0){
                responseData.setData(billInfoList.get(0));
            }
        } catch (Exception e) {
            responseData = ResponseData.error("获取账单信息失败");
            LOGGER.error("get billInfo error! billNo = " + billNo, e);
        }
        return responseData;
    }

    @Override
    public ResponseData<OverdueBillInfo> getOverdueBillInfo(String billNo) {
        ResponseData responseData = ResponseData.success();
        try {
            OverdueBillInfo overdueBillInfo = overdueBillDao.getObjectByBillNo(billNo);
            responseData.setData(overdueBillInfo);
        } catch (Exception e) {
            responseData = ResponseData.error("获取账单信息失败");
            LOGGER.error("get billInfo error! billNo = " + billNo, e);
        }
        return responseData;
    }

    public static void setLOGGER(Logger LOGGER) {
        BillServiceImpl.LOGGER = LOGGER;
    }

    /**
     * 保存账单信息
     * @param billInfo
     * @return
     */
    @Override
    public ResponseData saveBillInfo(BillInfo billInfo) {
        ResponseData responseData = ResponseData.success();
        try {
            billInfo.setBillNo(idGenerator.generatorId(BizCode.BILL_NYD).toString());
            billDao.save(billInfo);
        } catch (Exception e) {
            responseData = ResponseData.error("保存账单信息失败");
            LOGGER.error("save billInfo error! userId = "+billInfo.getUserId(),e);
        }
        return responseData;
    }

    @Override
    public ResponseData updateBillInfoByBillNo(BillInfo billInfo) {
        ResponseData responseData = ResponseData.success();
        try {
            billDao.update(billInfo);
        } catch (Exception e) {
            responseData = ResponseData.error("修改账单信息失败");
            LOGGER.error("update billInfo error! userId = "+billInfo.getUserId(),e);
        }
        return responseData;
    }

    @Override
    public ResponseData saveOverdueBillInfo(OverdueBillInfo overdueBillInfo) {
        ResponseData responseData = ResponseData.success();
        try {
            overdueBillDao.save(overdueBillInfo);
        } catch (Exception e) {
            responseData = ResponseData.error("保存账单信息失败");
            LOGGER.error("save overdueBillInfo error! userId = "+overdueBillInfo.getUserId(),e);
        }
        return responseData;
    }

    @Override
    public ResponseData updateOverdueBillInfoByBillNo(OverdueBillInfo overdueBillInfo) {
        ResponseData responseData = ResponseData.success();
        try {
            overdueBillDao.update(overdueBillInfo);
        } catch (Exception e) {
            responseData = ResponseData.error("修改逾期账单信息失败");
            LOGGER.error("update overdueBillInfo error! userId = "+overdueBillInfo.getUserId(),e);
        }
        return responseData;
    }

     /**OpenAPI start*/
    /**
     * 还款计划
     * @param userId
     * @return
     */
    @Override
    public ResponseData<List<BillDetail>> getBillInfoLs(String userId) {
        ResponseData responseData = ResponseData.success();
        List<BillDetail> billDetailList = new ArrayList<BillDetail>();
        try {
            List<BillInfo> billInfoList = billDao.getObjectsByUserId(userId);
            if(billInfoList != null && billInfoList.size() > 0){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                for(BillInfo billInfo : billInfoList){
                        BillDetail billDetail = new BillDetail();
                        BeanUtils.copyProperties(billInfo,billDetail);
                        billDetail.setPromiseRepaymentDate(sdf.format(billInfo.getPromiseRepaymentDate()));
                        if(BillStatusEnum.REPAY_ING.getCode().equals(billDetail.getBillStatus())){
                            billDetail.setBillStatusShow(BillStatusEnum.REPAY_ING.getValue());
                        }else if(BillStatusEnum.REPAY_OVEDUE.getCode().equals(billDetail.getBillStatus())){
                            billDetail.setBillStatusShow(BillStatusEnum.REPAY_OVEDUE.getValue());
                        } else if(BillStatusEnum.REPAY_SUCCESS.getCode().equals(billDetail.getBillStatus())){
                            billDetail.setBillStatusShow(BillStatusEnum.REPAY_SUCCESS.getValue());
                        }
                        billDetailList.add(billDetail);
                }
                responseData.setData(billDetailList);
            }
        } catch (Exception e) {
            responseData = ResponseData.error("服务器开小差了,请稍后再试");
            LOGGER.error("get getBillInfoLs error! userId = " + userId, e);
        }
        return responseData;
    }

    /**
     * 立即还款页面
     * @param billNo
     * @return
     */
    @Override
    public ResponseData<BillDetail> getBillDetailInfo(String billNo) {
        ResponseData responseData = ResponseData.success();
        BillDetail billDetail = new BillDetail();
        try {
            List<BillInfo> billInfoList = billDao.getObjectsByBillNo(billNo);
            if(billInfoList !=null && billInfoList.size() > 0){
                BillInfo billInfo = billInfoList.get(0);
                BeanUtils.copyProperties(billInfo,billDetail);
                int remainingDays = 0;
                remainingDays = DateUtil.getDay(new Date(), billInfo.getPromiseRepaymentDate());
                billDetail.setRemainingDays(remainingDays);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                billDetail.setPromiseRepaymentDate(sdf.format(billInfo.getPromiseRepaymentDate()));
            }
            responseData.setData(billDetail);
        } catch (Exception e) {
            responseData = ResponseData.error("服务器开小差了,请稍后再试");
            LOGGER.error("getBillDetailInfo error! billNo = " + billNo, e);
        }
        return responseData;
    }

    @Override
    public ResponseData queryHlb(BillDetail billDetail) {
        ResponseData responseData = ResponseData.success();
        Map<String,Object> map = new HashMap<>();
        if (billDetail!=null&&billDetail.getBillNo()!=null) {
            try {
                List<BillInfo> billInfoList = billDao.getObjectsByBillNo(billDetail.getBillNo());
                if(billInfoList !=null && billInfoList.size() > 0){
                    BillInfo billInfo = billInfoList.get(0);
                    map.put("curRepayAmount",billInfo.getCurRepayAmount());
                    map.put("billNo",billInfo.getBillNo());
                    if (billInfo.getOrderNo()!=null) {
                        ResponseData<OrderDetailInfo> responseData1 = orderDetailContract.getOrderDetailByOrderNo(billInfo.getOrderNo());
                        if (responseData1!=null&&"0".equals(responseData1.getStatus())) {
                            OrderDetailInfo orderDetailInfo = responseData1.getData();
                            map.put("name",orderDetailInfo.getRealName());
                            map.put("idNumber",orderDetailInfo.getIdNumber());
                        }
                    }
                   responseData.setData(map);
                }
            } catch (Exception e) {
                responseData = ResponseData.error("服务器开小差了,请稍后再试");
                LOGGER.error("queryHlb error! billNo = " + billDetail.getBillNo(), e);
            }
        } else {
            responseData = ResponseData.error("服务器开小差了,请稍后再试");
            LOGGER.error("queryHlb error! query params = " + JSON.toJSONString(billDetail));
        }
        return responseData;
    }

    /**
     * 查找最新的还款计划记录
     * @param userId
     * @return
     */
    @Override
    public ResponseData<BillInfo> getBillInfoByUid(String userId) {
        ResponseData responseData = ResponseData.success();
        if(!StringUtils.isEmpty(userId)){
            try {
                BillInfo billInfo = billDao.getObjectByUserId(userId);
                responseData.setData(billInfo);
            } catch (Exception e) {
                responseData = ResponseData.error("服务器开小差了,请稍后再试");
                LOGGER.error("getBillInfoByUid error! userId = " + userId, e);
            }
        }else {
            responseData = ResponseData.error("服务器开小差了,请稍后再试");
        }
        return responseData;
    }
}
