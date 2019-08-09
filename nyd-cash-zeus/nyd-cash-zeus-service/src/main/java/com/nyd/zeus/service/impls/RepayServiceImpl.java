package com.nyd.zeus.service.impls;

import com.nyd.zeus.api.RepayContract;
import com.nyd.zeus.dao.RepayDao;
import com.nyd.zeus.model.RepayInfo;
import com.nyd.zeus.service.RepayService;
import com.tasfe.framework.support.model.ResponseData;
import com.tasfe.framework.uid.service.BizCode;
import com.tasfe.framework.uid.service.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhujx on 2017/11/21.
 */
@Service(value = "repayContract")
public class RepayServiceImpl implements RepayContract, RepayService {

    private static Logger LOGGER = LoggerFactory.getLogger(RepayServiceImpl.class);

    @Autowired
    RepayDao repayDao;

    /**
     * 保存还款信息
     * @param repayInfo
     * @return
     * @throws Exception
     */
    @Override
    public ResponseData save(RepayInfo repayInfo) throws Exception {
        ResponseData responseData = ResponseData.success();
        try {
            repayDao.save(repayInfo);
        } catch (Exception e) {
            responseData = ResponseData.error("保存还款信息失败");
            LOGGER.error("save repayInfo error! billNo = "+ repayInfo.getBillNo(), e.getMessage());
        }
        return responseData;
    }

    /**
     * 获取还款信息
     * @param billNo
     * @return
     * @throws Exception
     */
    @Override
    public ResponseData getRepayInfoByBillNo(String billNo) throws Exception {
        ResponseData responseData = ResponseData.success();
        try {
            List<RepayInfo> repayInfoList = repayDao.getRepayInfoByBillNo(billNo);
            responseData.setData(repayInfoList);
        } catch (Exception e) {
            responseData = ResponseData.error("获取还款信息失败");
            LOGGER.error("getRepayInfoByBillNo error! billNo = " + billNo, e.getMessage());
        }
        return responseData;
    }
}
