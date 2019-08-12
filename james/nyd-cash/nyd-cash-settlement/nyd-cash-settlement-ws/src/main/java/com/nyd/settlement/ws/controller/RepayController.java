package com.nyd.settlement.ws.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.settlement.model.dto.repay.RepayAddDto;
import com.nyd.settlement.model.dto.repay.RepayAdviceDto;
import com.nyd.settlement.model.dto.repay.RepayAuditQueryDto;
import com.nyd.settlement.model.dto.repay.RepayQueryDto;
import com.nyd.settlement.model.vo.NydHlbVo;
import com.nyd.settlement.service.BuckleService;
import com.nyd.settlement.service.RepayAuditService;
import com.nyd.settlement.service.RepayOrderService;
import com.nyd.settlement.service.RepayService;
import com.nyd.settlement.service.utils.ValidateUtil;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2018/1/15
 **/
@RestController
@RequestMapping("/settlement/repaymgm")
public class RepayController {

    Logger logger = LoggerFactory.getLogger(RepayController.class);

    @Autowired
    private RepayAuditService repayAuditService;
    @Autowired
    private RepayService repayService;
    @Autowired
    private RepayOrderService repayOrderService;

    @Autowired
    private BuckleService buckleService;


    /**
     * 未还款查询
     * @param dto
     * @return
     */
    @RequestMapping("/norepay")
    public ResponseData nonrepay(@RequestBody RepayQueryDto dto){
        ResponseData responseData = ResponseData.success();
        dto.setBillStatus("1");
        responseData.setData(repayOrderService.findRepayPage(dto));
        return responseData;
    }

    /**
     * 已还款查询
     * @param dto
     * @return
     */
    @RequestMapping("/yesrepay")
    public ResponseData yesrepay(@RequestBody RepayQueryDto dto){
        ResponseData responseData = ResponseData.success();
        dto.setBillStatus("2");
        responseData.setData(repayOrderService.findRepayPage(dto));
        return responseData;
    }

    /**
     * 账单列表查询
     * @param dto
     * @return
     */
    @RequestMapping("/bill-list")
    public ResponseData billList(@RequestBody RepayQueryDto dto){
        ResponseData responseData = ResponseData.success();
        responseData.setData(repayOrderService.getBillList(dto.getOrderNo()));
        return responseData;
    }

    /**
     * 建议金额计算
     * @param dto
     * @return
     */
    @RequestMapping("/adviceAmount")
    public ResponseData adviceAmount(@RequestBody RepayAdviceDto dto) throws Exception {
        BigDecimal result = repayService.adviceAmount(dto);
        logger.info("建议金额 为"+result);
        JSONObject object = new JSONObject();
        object.put("adviceAmount",result.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        logger.info("");
        return ResponseData.success(object);
    }

    /**
     * 添加还款
     * @return
     */
    @RequestMapping("/addRepay")
    public ResponseData addRepay(@RequestBody RepayAddDto dto, HttpServletRequest request){
        String updateBy = request.getHeader("accountNo");
        dto.setUpdateBy(updateBy);
        try {
            repayAuditService.save(dto);
            return ResponseData.success();
        }catch (Exception e) {
            return ResponseData.error();
        }
    }

    /**
     * 审核查询
     * @return
     */
    @RequestMapping("/queryAudit")
    public ResponseData queryAudit(@RequestBody RepayAuditQueryDto dto){
        return ResponseData.success(repayAuditService.findAuditList(dto));
    }

    /**
     * 审核
     * 入参为审核的ids
     * @return
     */
    @RequestMapping(value = "/doAudit", method = RequestMethod.POST, produces = "application/json")
    public ResponseData audit(@RequestBody List<Long> ids,  HttpServletRequest request ){
        String updateBy = request.getHeader("accountNo");

        logger.info("audit param is"+JSONObject.toJSONString(ids)+updateBy);
        if(ids.size() == 0){
            return ResponseData.error("参数不能为空！");
        }
        ResponseData responseData = ResponseData.success();
        JSONObject result = repayAuditService.audit(ids,updateBy);
        responseData.setData(result);

        logger.info("审核结果"+JSON.toJSONString(result));
        return responseData;
    }

    /**
     * 查询还款详情
     * @return
     */
    @RequestMapping("/queryRepayDetail")
    public ResponseData queryRepayDetail(@RequestBody Map map){
        String billNo = (String) map.get("billNo");
        logger.info("查询还款详情传入的billNo为:"+billNo);
        if(billNo==null||billNo.trim().length()==0){
            throw new NullPointerException("queryRepayDetail~billNo为空");
        }
        return ResponseData.success(repayService.queryRepayDetailByBillNo(billNo));
    }

    @RequestMapping("/init")
    public ResponseData init(){
        List<String> list = new ArrayList<>();
        list.add("kzjr");
        return ResponseData.success(list);
    }

    /**
     * 还款流水查询
     * @param dto
     * @return
     */
    @RequestMapping("/queryRepay")
    public ResponseData queryRepay(@RequestBody RepayQueryDto dto){
        ResponseData responseData = ResponseData.success();
        responseData.setData(repayService.findPage(dto));
        return responseData;
    }
    @RequestMapping(value = "/queryBank",method = RequestMethod.POST, produces = "application/json")
    public  ResponseData queryBank(@RequestBody String userId){
        logger.info("查询银行");
        JSONObject object = JSONObject.parseObject(userId);
        return ResponseData.success(buckleService.queryBanks(object.getString("userId")));
    }

    @PostMapping("/withHold")
    @ResponseBody
    public ResponseData withHold(@RequestBody NydHlbVo vo) throws Exception {

        logger.info("代扣"+ JSON.toJSONString(vo));
         ValidateUtil.process(vo);

        return buckleService.withHold(vo);

    }

}
