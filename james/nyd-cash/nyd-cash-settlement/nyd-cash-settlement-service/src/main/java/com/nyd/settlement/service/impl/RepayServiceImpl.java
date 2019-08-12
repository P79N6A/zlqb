package com.nyd.settlement.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderDetailContract;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.product.api.ProductContract;
import com.nyd.product.model.ProductInfo;
import com.nyd.product.model.ProductOverdueFeeItemInfo;
import com.nyd.settlement.dao.ds.DataSourceContextHolder;
import com.nyd.settlement.dao.mapper.RepayLogMapper;
import com.nyd.settlement.dao.mapper.RepayMapper;
import com.nyd.settlement.entity.Bill;
import com.nyd.settlement.entity.repay.TRepayLog;
import com.nyd.settlement.model.dto.repay.RepayAdviceDto;
import com.nyd.settlement.model.dto.repay.RepayQueryDto;
import com.nyd.settlement.model.po.repay.RepayAmountOfDay;
import com.nyd.settlement.model.po.repay.RepayPo;
import com.nyd.settlement.model.vo.repay.RepayDetailVo;
import com.nyd.settlement.model.vo.repay.RepayVo;
import com.nyd.settlement.service.RepayService;
import com.nyd.settlement.service.aspect.RoutingDataSource;
import com.nyd.settlement.service.struct.RepayLogStruct;
import com.nyd.settlement.service.struct.RepayStruct;
import com.nyd.settlement.service.utils.AmountUtils;
import com.nyd.settlement.service.utils.DateUtil;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Cong Yuxiang
 * 2018/1/16
 **/
@Service
public class RepayServiceImpl implements RepayService{
    Logger logger = LoggerFactory.getLogger(RepayServiceImpl.class);

    @Autowired
    private RepayLogMapper repayLogMapper;

    @Autowired
    private ProductContract productContract;

    @Autowired
    private OrderDetailContract orderDetailContract;

    @Autowired
    private OrderContract orderContract;

    @Autowired
    RepayMapper repayMapper;

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public List<RepayDetailVo> queryRepayDetailByBillNo(String billNo) {
        Map map = new HashMap<>();
        map.put("billNo",billNo);
        List<TRepayLog> result = repayLogMapper.selectRepayDetail(map);
        return RepayLogStruct.INSTANCE.polist2Volist(result);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public BigDecimal adviceAmount(RepayAdviceDto dto) throws Exception {
        logger.info("建议金额传入的参数为"+ JSON.toJSONString(dto));
        String calcuDate = dto.getCalcuteDate().split(" ")[0];
        String billNo = dto.getBillNo();

        Bill queryBill = new Bill();
        queryBill.setBillNo(billNo);
        queryBill.setDeleteFlag(0);
        Criteria criteria = Criteria.from(Bill.class);
        List<Bill> billList = null;
        try {
            billList = crudTemplate.find(queryBill,criteria);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("adviceAmount查询bill异常",e);
        }
        if(billList==null||billList.size()==0){
            return new BigDecimal("0");
        }
        Bill bill = billList.get(0);

        logger.info("bill为"+JSON.toJSONString(bill));

        String promiseDate = DateFormatUtils.format(bill.getPromiseRepaymentDate(),"yyyy-MM-dd");

        logger.info(billNo+"promiseDate为"+promiseDate);
        OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(bill.getOrderNo()).getData();

        ProductInfo productConfigInfo = productContract.getProductInfo(orderDetailInfo.getProductCode()).getData();
//        OrderInfo orderInfo = orderContract.getOrderByOrderNo(bill.getOrderNo()).getData();
        if(productConfigInfo.getProductType() == 0){
            //计算该账单 在输入的还款日期 之后 的金额.
//
//            BigDecimal buckleAmount = null;
//            try {
//                buckleAmount = AmountUtils.calcuteAmount(calcuDate,repayAmountOfDayList);
//            } catch (ParseException e) {
//                logger.error("建议金额计算往后金额异常",e);
//                e.printStackTrace();
//                throw new Exception("建议金额计算往后金额异常");
//            }

            List<RepayAmountOfDay> repayAmountOfDayList = repayMapper.queryRepayAmountOfDayDistinct(billNo);

            logger.info(billNo+"repayAmountOfDayList为:"+(repayAmountOfDayList==null?"null":JSON.toJSONString(repayAmountOfDayList)));
//            BigDecimal derate = new BigDecimal(0);
//            for(RepayAmountOfDay repayAmountOfDay:repayAmountOfDayList){
//                derate = derate.add(repayAmountOfDay.getDerateAmountOfDay());
//            }
            BigDecimal derate = AmountUtils.calcuteDerateBefore(dto.getCalcuteDate(),repayAmountOfDayList);

            logger.info(billNo+"derate"+derate);
            if(DateUtil.calcuteDateBetween(calcuDate,promiseDate)==0){
                //如果还款时间在约定还款时间内 建议金额为 正常金额减去 之前已还金额
                BigDecimal bigDecimal = AmountUtils.calcuteAmountBefore(dto.getCalcuteDate(),repayAmountOfDayList);
                return bill.getRepayInterest().add(bill.getRepayPrinciple()).subtract(bigDecimal).subtract(derate);
            }else {
                ProductOverdueFeeItemInfo productOverdueFeeItemInfo = productContract.getProductOverdueFeeItemInfo(orderDetailInfo.getProductCode()).getData();
//                bill.getAlreadyRepayAmount().subtract(buckleAmount);

//                if(bill.getWaitRepayPrinciple()..compareTo(new BigDecimal("0"))==0){
//                    return bill.getWaitRepayAmount();
//                }else{

                TreeMap<Integer,BigDecimal> map = AmountUtils.calAmountAndOverdueBefore(dto.getCalcuteDate(),repayAmountOfDayList,promiseDate);
                logger.info(billNo+"建议金额历史金额"+JSON.toJSONString(map));
//                int days = DateUtil.calcuteDateBetween(calcuDate,promiseDate);
//                //获取逾期单
//                OverdueBill queryOverdueBill = new OverdueBill();
//                queryOverdueBill.setBillNo(billNo);
//                Criteria criteriaOverDue = Criteria.from(OverdueBill.class);
//
//                List<OverdueBill> overdueBills = null;
//                try {
//                    overdueBills = crudTemplate.find(queryOverdueBill,criteriaOverDue);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if(overdueBills==null||overdueBills.size()==0){
//                    return new BigDecimal("0");
//                }
//                OverdueBill overdueBill = overdueBills.get(0);

                BigDecimal result = new BigDecimal("0"); //建议金额
                BigDecimal remainPrinciple = bill.getRepayPrinciple();
                BigDecimal remainInterest = bill.getRepayInterest();
                BigDecimal remainFine = productOverdueFeeItemInfo.getOverdueFine();
                BigDecimal remainFaxi = new BigDecimal("0");

                int tmpDays=1;  //根据新的本金 计算罚息的起始天  用于费率档次

                for(Map.Entry<Integer,BigDecimal> entry:map.entrySet()){
                    //在约定期内的 已有还款
                    if(entry.getKey()==0){
                        //本金 未冲平  计算剩余本金 及 建议金额。
                        if(entry.getValue().compareTo(bill.getRepayPrinciple())<0){
                            remainPrinciple = remainPrinciple.subtract(entry.getValue());
                            result = remainPrinciple.add(remainInterest).add(remainFine);
                        }else if(entry.getValue().compareTo(bill.getRepayPrinciple())>=0){  //本金已冲平 剩余本金为0  计算建议金额
                            remainPrinciple = new BigDecimal("0");
                            result = remainInterest.add(remainFine).subtract(entry.getValue().subtract(bill.getRepayPrinciple()));
                        }
                    }else {
                        //如果剩余本金 已冲平 则无须计算罚息
                        if(remainPrinciple.compareTo(new BigDecimal("0"))==0){
                            result = result.subtract(entry.getValue());
                        }else {
                            //有剩余本金 需要计算罚息
                            if(remainInterest.add(remainFine).add(remainFaxi).divide(bill.getRepayPrinciple(),2,BigDecimal.ROUND_HALF_UP).compareTo(productOverdueFeeItemInfo.getMaxOverdueFeeRate().divide(new BigDecimal(100))) == -1) {
                                remainFaxi = remainFaxi.add(AmountUtils.calFaxi(remainPrinciple, productOverdueFeeItemInfo, tmpDays, entry.getKey()));
                            }
//                            remainFaxi = remainFaxi.add(AmountUtils.calFaxi(remainPrinciple,productOverdueFeeItemInfo,tmpDays,entry.getKey()));
                            if(entry.getValue().compareTo(remainPrinciple)<0){
                                remainPrinciple = remainPrinciple.subtract(entry.getValue());
                                result = remainPrinciple.add(remainInterest).add(remainFine).add(remainFaxi);
                            }else if(entry.getValue().compareTo(remainPrinciple)>=0){
                                BigDecimal tm = entry.getValue().subtract(remainPrinciple);
                                remainPrinciple = new BigDecimal("0");
                                result = remainInterest.add(remainFine).add(remainFaxi).subtract(tm);
                            }

                        }

                        tmpDays = entry.getKey()+1;
                    }




                }
                return result.subtract(derate);


//                    BigDecimal result = bill.getRepayInterest().add(bill.getWaitRepayPrinciple()).add(overdueBill.getOverdueFine());
//                    BigDecimal interest = bill.getRepayInterest().add(overdueBill.getOverdueFine());
//                    int i=1;
//                    while (i<=days&&interest.divide(bill.getRepayPrinciple()).compareTo(productOverdueFeeItemInfo.getMaxOverdueFeeRate().divide(new BigDecimal(100))) == -1
//                            && bill.getWaitRepayPrinciple().compareTo(new BigDecimal(0)) == 1){
//                        BigDecimal dayOverdueAmount = OverdueAmountUtil.dayOverdueAmount(i,productOverdueFeeItemInfo,bill);
//                        interest.add(dayOverdueAmount);
//                        result.add(dayOverdueAmount);
//                    }
//
//                   return result;

//                }

            }

        }else {
            logger.info("productType不为0");
            return new BigDecimal("0");
        }

    }


   //还款记录查询
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_MYSQL)
    @Override
    public PageInfo<RepayVo> findPage(RepayQueryDto dto) {
        if (StringUtils.isNotBlank(dto.getEndDate())) {
           dto.setEndDate(dto.getEndDate());
        }else {
            dto.setEndDate(null);
        }
        if (StringUtils.isNotBlank(dto.getStartDate())) {
            dto.setStartDate(dto.getStartDate());
        } else{
            dto.setStartDate(null);
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderBy());
        List<RepayPo> repayPoList = repayMapper.repayRecordList(dto);
        return RepayStruct.INSTANCE.poPage2VoPage(new PageInfo(repayPoList));
    }

    @Override
    public void test(RepayPo po) {
//        repayMapper.insert(po);
    }

}
