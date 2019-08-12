package com.nyd.admin.ws.controller;


import com.nyd.admin.model.dto.FundCreateDto;
import com.nyd.admin.model.dto.FundDetailQueryDto;
import com.nyd.admin.model.fundManageModel.CalculateModle;
import com.nyd.admin.service.FundInfoService;
import com.nyd.admin.service.FundManageService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * hwei
 * 2017/12/11
 **/
@RestController
@RequestMapping("/admin/fundManage")
public class FundManageController {

    @Autowired
    FundManageService fundManageService;

    @Autowired
    FundInfoService fundInfoService;

    /**
     * 资产信息查询
     * @return
     */
    @RequestMapping("/queryFundInfo")
    public ResponseData queryFundInfo(@RequestBody FundDetailQueryDto dto, HttpServletRequest request, HttpServletResponse response){
        String accountNo = request.getHeader("accountNo");
        try {
            return fundInfoService.getFundInfo(dto,accountNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.error();
    }

    /**
     * 资产信息详情查询
     * @return
     */
    @RequestMapping("/queryFundDetail")
    public ResponseData queryFundDetail(@RequestBody FundDetailQueryDto dto){
        try {
            return fundInfoService.getFundDetail(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.error();
    }

    /**
     * 获取资产管理 相关配置
     *
     * @return
     */
    @RequestMapping(value = "/config", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryFundConfig() {
        try {
            return fundManageService.queryFundConfig();
        } catch (Exception e) {
            return ResponseData.error("服务器开小差了");
        }
    }

    /**
     * 收益计算
     */
    @RequestMapping(value = "/calculation", method = RequestMethod.POST, produces = "application/json")
    public ResponseData calculation(@RequestBody CalculateModle calculateModle) {
        ResponseData responseData = ResponseData.success();
        try {
            //参数非空校验
            if (calculateModle == null || calculateModle.getInvestmentAmount() == null || calculateModle.getReturnRate() == null || calculateModle.getInvestmentTerm() == null) {
                return ResponseData.error("参数不能为空");
            }
            //利息= amt（1+n%）^m  -amt
            //月利率
            BigDecimal a = (calculateModle.getReturnRate().divide(new BigDecimal(12),6,BigDecimal.ROUND_HALF_UP)).add(new BigDecimal(1));
            BigDecimal b = a.pow(calculateModle.getInvestmentTerm());
            BigDecimal interest = calculateModle.getInvestmentAmount().multiply(b).subtract(calculateModle.getInvestmentAmount()).setScale(2, BigDecimal.ROUND_DOWN);
            return responseData.setData(interest);
        } catch (Exception e) {
            return ResponseData.error("服务器开小差了");
        }
    }

    /**
     * 资金创建
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public ResponseData createFundInfo(@RequestBody FundCreateDto fundCreateDto) {
        try {
            return fundManageService.createFundInfo(fundCreateDto);
        } catch (Exception e) {
           return ResponseData.error("服务器开小差了");
        }
    }

    /**
     * 资金创建日期接口
     */
    @RequestMapping(value = "/getFundDate/{months}", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getDate(@PathVariable Integer months) {
        Map<String,Object> map = new HashMap<>();
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.MONTH,months);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        map.put("accountDate",format.format(today));
        map.put("backDate",format.format(calendar.getTime()));
        return ResponseData.success(map);
    }

    /**
     * 资金创建日期接口
     */
    @RequestMapping(value = "/getFundDate", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getBackDate(@RequestBody FundCreateDto fundCreateDto) {
        Map<String,Object> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fundCreateDto.getAccountDate());
        calendar.add(Calendar.MONTH,fundCreateDto.getMonth());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        map.put("backDate",format.format(calendar.getTime()));
        return ResponseData.success(map);
    }
}
