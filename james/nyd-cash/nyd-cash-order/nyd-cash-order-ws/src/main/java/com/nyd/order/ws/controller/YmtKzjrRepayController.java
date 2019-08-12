package com.nyd.order.ws.controller;

import com.alibaba.fastjson.JSON;
import com.ibank.order.model.bill.BillInfo;
import com.nyd.order.model.YmtKzjrBill.BillYmtInfo;
import com.nyd.order.model.YmtKzjrBill.KzjrRepayDetail;
import com.nyd.order.model.YmtKzjrBill.KzjrRepayInfo;
import com.nyd.order.service.YmtKzjr.BillYmtService;
import com.nyd.order.service.YmtKzjr.YmtKzjrRepayService;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 银码头空中金融还款改造
 * @author cm
 */
@RestController
@RequestMapping("/order")
public class YmtKzjrRepayController {
    private static Logger logger = LoggerFactory.getLogger(YmtKzjrRepayController.class);

    @Autowired
    private YmtKzjrRepayService ymtKzjrRepayService;

    @Autowired
    private BillYmtService billYmtService;


    /**
     * 空中金融还款详情页面
     */
    @RequestMapping(value = "/ymtkzjr/repayDetailByOrderSno", method = RequestMethod.POST, produces = "application/json")
    public ResponseData findKzjrRepayDetailByOrderSno(@RequestBody KzjrRepayInfo kzjrRepayInfo){
        logger.info("还款详情页面,请求参数："+ JSON.toJSON(kzjrRepayInfo));
        ResponseData<KzjrRepayDetail> responseData = ymtKzjrRepayService.findKzjrRepayDetail(kzjrRepayInfo.getOrderSno());
        return responseData;
    }


    /**
     * 根据资产编号，找到还款信息
     */
    @RequestMapping(value = "/ymtkzjr/repayDetailByAssetCode", method = RequestMethod.POST, produces = "application/json")
    public ResponseData findKzjrRepayDetailByAssetCode(@RequestBody KzjrRepayInfo kzjrRepayInfo){
        logger.info("根据资产编号跳转到还款详情页面,请求参数："+JSON.toJSON(kzjrRepayInfo));
        try {
            ResponseData<BillYmtInfo> responseData = billYmtService.findByAssetCode(kzjrRepayInfo.getAssetCode());
            if ("0".equals(responseData.getStatus())){
                BillYmtInfo billYmtInfo = responseData.getData();
                logger.info("根据资产编号找到的账单信息:"+JSON.toJSON(billYmtInfo));
                String orderSno = "";
                if (StringUtils.isNotBlank(billYmtInfo.getOrderSno())){
                    orderSno = billYmtInfo.getOrderSno();
                }
                ResponseData<KzjrRepayDetail> data = ymtKzjrRepayService.findKzjrRepayDetail(orderSno);
                return data;
            }else {
                logger.info("根据资产编号查找还款信息失败");
                return ResponseData.error(responseData.getMsg());
            }
        }catch (Exception e){
            logger.error("根据资产编号查找还款信息出错",e);
            e.printStackTrace();
            return ResponseData.error("服务器开小差,请稍后再试!");
        }
    }
}
