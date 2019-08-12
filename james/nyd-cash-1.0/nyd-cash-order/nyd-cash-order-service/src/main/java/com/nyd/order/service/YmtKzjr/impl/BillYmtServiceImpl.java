package com.nyd.order.service.YmtKzjr.impl;

import com.ibank.order.model.bill.BillInfo;
import com.nyd.order.dao.YmtKzjrBill.BillYmtDao;
import com.nyd.order.model.YmtKzjrBill.BillYmtInfo;
import com.nyd.order.service.YmtKzjr.BillYmtService;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillYmtServiceImpl implements BillYmtService {
    private static Logger logger = LoggerFactory.getLogger(BillYmtServiceImpl.class);

    @Autowired
    private BillYmtDao billYmtDao;

    /**
     *
     * 根据资产找到账单信息
     * @param assetCode
     * @return
     */
    @Override
    public ResponseData<BillYmtInfo> findByAssetCode(String assetCode) {
        logger.info("空中金融产品还款资产编号："+assetCode);
        ResponseData responseData = ResponseData.success();
        if (!StringUtils.isNotBlank(assetCode)){
            logger.error("资产编号为空");
            responseData = responseData.error("资产编号为空");
            return responseData;
        }

        try {
            List<BillYmtInfo> list = billYmtDao.selectByAssetCode(assetCode);
            if (list != null && list.size() > 0 ){
                BillYmtInfo billYmtInfo = list.get(0);
                responseData.setData(billYmtInfo);
            }else {
                logger.info("资产编号:"+assetCode+"不存在");
                responseData = responseData.error("该订单不存在");
                return responseData;
            }
        }catch (Exception e){
            logger.error("find repay detail by assetCode has error ",e);
            e.printStackTrace();
        }

        return responseData;
    }


}
