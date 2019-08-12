package com.nyd.zeus.service.impls.zzl;

import com.nyd.zeus.api.zzl.ZeusForLXYService;
import com.nyd.zeus.api.zzl.ZeusSqlService;
import com.nyd.zeus.dao.BillDao;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.common.CommonResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "zeusForLXYService")
public class ZeusForLXYServiceImpl implements ZeusForLXYService {

    private static Logger logger = LoggerFactory.getLogger(ZeusForLXYServiceImpl.class);

    @Autowired
    private BillDao billDao;
    
    @Autowired
    private ZeusSqlService zeusSqlService;

    @Override
    public CommonResponse<BillInfo> queryBillInfoByOrderNO(String orderNo) {
        CommonResponse<BillInfo> common = new CommonResponse<BillInfo>();
        try {
        	String sql = "select * from t_bill where order_no = '%s'";
        	List<BillInfo> billInfoList = zeusSqlService.queryT(String.format(sql, orderNo), BillInfo.class);
          //  List<BillInfo> billInfoList = billDao.getObjectsByOrderNo(orderNo);
            if(null != billInfoList && billInfoList.size()>0){
                common.setData(billInfoList.get(0));
            }else {
                common.setCode("0");
                common.setMsg("操作失败");
                common.setSuccess(false);
                return common;
            }
            common.setCode("1");
            common.setMsg("操作成功");
            common.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("根据订单编号查询还款计划出现异常,请求参数:orderNo:" + orderNo);
            common.setCode("0");
            common.setMsg("操作失败");
            common.setSuccess(false);
        }
        return common;
    }
}
