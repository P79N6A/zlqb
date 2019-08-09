package com.nyd.user.ws.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.nyd.user.api.RefundUserContract;
import com.nyd.user.model.RefundAppCountInfo;
import com.nyd.user.model.RefundAppInfo;
import com.nyd.user.model.RefundInfo;
import com.nyd.user.model.RefundUserInfo;
import com.nyd.user.model.vo.RefundAppVo;
import com.nyd.user.service.RefundAppCountService;
import com.nyd.user.service.RefundAppService;
import com.nyd.user.service.RefundService;
import com.tasfe.framework.support.model.ResponseData;


/**
 * 
 * @author zhangdk
 *
 */
@RestController
@RequestMapping("/user")
public class RefundController {
    private static Logger LOGGER = LoggerFactory.getLogger(RefundController.class);

    @Autowired
    private RefundUserContract refundUserContract;
    @Autowired
    private RefundAppService refundAppService;
    @Autowired
    private RefundService refundService;
    @Autowired
    private RefundAppCountService refundAppCountService;

    /**
     * 查询是否退款白名单
     * @param req
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryRefundFlag/auth", method = RequestMethod.POST)
    public ResponseData queryRefundFlag(@RequestBody RefundUserInfo req) throws Throwable{
    	LOGGER.info("查询是否白名单请求信息：" + JSON.toJSONString(req));
        return refundUserContract.haveInWhiteList(req);
    }
    /**
     * 查询万花筒是否展示退款入口
     * @param req
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryRefundFlag/kalei/auth", method = RequestMethod.POST)
    public ResponseData queryRefundFlagKalei(@RequestBody RefundUserInfo req) throws Throwable{
    	LOGGER.info("查询万花筒是否展示退款入口请求信息：" + JSON.toJSONString(req));
    	return refundUserContract.ifShowRefund(req);
    }
    /**
     * 更新是否点击vip新口子标识
     * @param req
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/update/clickVip/auth", method = RequestMethod.POST)
    public ResponseData updateClickVip(@RequestBody RefundUserInfo req) throws Throwable{
    	LOGGER.info("更新是否点击VIP新口子：" + JSON.toJSONString(req));
    	return refundUserContract.updateClickVip(req);
    }
    /**
     * 获取任务列表
     * @param req
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/getTaskListByUserId/auth", method = RequestMethod.POST)
    public ResponseData getTaskListByUserId(@RequestBody RefundAppInfo req) throws Throwable{
    	LOGGER.info("获取任务列表请求参数：" + JSON.toJSONString(req));
    	try {
    		return refundAppService.getTaskListByUserId(req);
    	}catch(Exception e) {
    		LOGGER.info("获取任务列表请求异常：" + e.getMessage());
    		return ResponseData.error();
    	}
    }
    /**
     * 图片上传
     * @param req
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/refundApplyImge/auth", method = RequestMethod.POST)
    public ResponseData uploadRefundImge(@RequestBody RefundAppVo req) throws Throwable{
    	return refundService.uploadRefundImge(req);
    }
    
	@RequestMapping(value = "/refund/detail", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData refundDetail(@RequestBody RefundInfo req) {
		LOGGER.info("查询详情请求参数：" + req.getRefundNo());
		try {
			return refundService.getRefundAppListByRefundNo(req.getRefundNo());
		} catch (Exception e) {
			LOGGER.info("查询详情异常" + e.getMessage());
			return ResponseData.error("查询详情信息异常");
		}
	}

    /**
     * 退款重新上传图片前获取上一次图片原因key等
     * @param req
     * @return
     */
    @RequestMapping(value = "/refund/detail/auth", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData refundDetailAuth(@RequestBody RefundInfo req) {
        LOGGER.info("重新申请退款查询详情请求参数：" + req.getRefundNo());
        try {
            return refundService.getRefundAppListByRefundNoAuth(req.getRefundNo());
        } catch (Exception e) {
            LOGGER.info("重新申请退款查询详情请求参数" , e);
            return ResponseData.error("重新申请退款查询详情请求参数");
        }
    }

	
	/**
     * 更新点击数
     * @param req
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/updateCount/auth")
    public ResponseData updateCount(@RequestBody RefundAppCountInfo req) throws Throwable{
    	LOGGER.info("更新点击数请求参数：" + JSON.toJSONString(req));
    	req.setUpdateClikCount(1);
    	req.setCountDate(new Date());
    	return refundAppCountService.updateCount(req);
    }

}
