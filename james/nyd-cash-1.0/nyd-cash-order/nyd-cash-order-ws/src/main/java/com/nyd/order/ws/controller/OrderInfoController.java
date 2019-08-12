//package com.nyd.order.ws.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.nyd.order.model.BaseInfo;
//import com.nyd.order.model.dto.BorrowConfirmDto;
//import com.nyd.order.model.dto.BorrowDto;
//import com.nyd.order.service.OrderInfoService;
//import com.nyd.order.service.consts.OrderConsts;
//import com.nyd.order.service.util.OrderProperties;
//import com.tasfe.framework.support.model.ResponseData;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletResponse;
//
///**
// * Created by Dengw on 2017/11/11
// */
//@RestController
//@RequestMapping("/order")
//public class OrderInfoController {
//    private static Logger LOGGER = LoggerFactory.getLogger(OrderInfoController.class);
//
//    @Autowired
//    private OrderInfoService orderInfoService;
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//    @Autowired
//    private OrderProperties orderProperties;
//
//    /**
//     * 借款信息
//     *
//     * @param borrowDto
//     * @return ResponseData
//     */
//    @RequestMapping(value = "/borrow/auth", method = RequestMethod.POST, produces = "application/json")
//    public ResponseData borrowInfo(@RequestBody BorrowDto borrowDto) throws Throwable {
//        ResponseData responseData = orderInfoService.getBorrowInfo(borrowDto);
//        return responseData;
//    }
//
//    /**
//     * 借款确认
//     *
//     * @param borrowConfirmDto
//     * @return ResponseData
//     */
//    @RequestMapping(value = "/borrow/confirm/auth", method = RequestMethod.POST, produces = "application/json")
//    public ResponseData borrowConfirm(@RequestBody BorrowConfirmDto borrowConfirmDto) throws Throwable {
//        ResponseData responseData = ResponseData.success();
//        try {
//            responseData = orderInfoService.borrowInfoConfirm(borrowConfirmDto, true);
//        } catch (Exception e) {
//            return ResponseData.error(OrderConsts.DB_ERROR_MSG);
//        }
//        LOGGER.info(borrowConfirmDto.getUserId() + "confirm结果" + JSON.toJSONString(responseData));
//        return responseData;
//    }
//
//    /**
//     * 借款确认
//     *
//     * @param borrowConfirmDto
//     * @return ResponseData
//     */
//    @RequestMapping(value = "/borrow/newConfirm/auth", method = RequestMethod.POST, produces = "application/json")
//    public ResponseData borrowNewConfirm(@RequestBody BorrowConfirmDto borrowConfirmDto) throws Throwable {
//        ResponseData responseData = ResponseData.success();
//        try {
//            responseData = orderInfoService.newBorrowInfoConfirm(borrowConfirmDto, true);
//        } catch (Exception e) {
//            return ResponseData.error(OrderConsts.DB_ERROR_MSG);
//        }
//        LOGGER.info(borrowConfirmDto.getUserId() + "confirm结果" + JSON.toJSONString(responseData));
//        return responseData;
//    }
//
//    /**
//     * 查询开户结果
//     *
//     * @param baseInfo
//     * @return ResponseData
//     */
//    @RequestMapping(value = "/borrow/newConfirm/process", method = RequestMethod.POST, produces = "application/json")
//    public ResponseData selectOpenPage(@RequestBody BaseInfo baseInfo) throws Throwable {
//        ResponseData responseData = ResponseData.success();
//        LOGGER.info("process******************************************" + baseInfo.getUserId());
//        boolean flag1 = redisTemplate.delete(OrderConsts.REDIS_LOAN_ACCOUNT_STATUS + baseInfo.getUserId());
//        LOGGER.info("删除状态key的结果" + flag1);
//
//        BorrowConfirmDto dto = JSONObject.parseObject((String) redisTemplate.opsForValue().get(OrderConsts.REDIS_LOAN_KEY + baseInfo.getUserId()), BorrowConfirmDto.class);
//        if (StringUtils.isBlank(baseInfo.getFundCode())) {
//            baseInfo.setFundCode("kzjr");
//        }
//        dto.setFundCode(baseInfo.getFundCode());
//        LOGGER.info("redis" + JSON.toJSONString(dto));
//        boolean flag = redisTemplate.delete(OrderConsts.REDIS_LOAN_KEY + baseInfo.getUserId());
//        LOGGER.info("删除key的结果" + flag);
//        try {
//            responseData = orderInfoService.newBorrowInfoConfirm(dto, false);
//            LOGGER.info("redirect返回的结果" + JSON.toJSONString(responseData));
//
//        } catch (Exception e) {
//            return ResponseData.error();
//        }
//        return responseData;
//    }
//
//
//    @RequestMapping(value = "/borrow/confirm/redirect")
//    public void borrowConfirmRedirect(HttpServletResponse response) throws Throwable {
//
//        response.sendRedirect(orderProperties.getRedirectUrl());
//
//    }
//
//    /**
//     * 开户后跳转借款
//     *
//     * @return ResponseData
//     * congyuxiang
//     */
//    @RequestMapping(value = "/borrow/confirm/process")
//    public ResponseData borrowConfirmProcess(@RequestBody BaseInfo baseInfo) throws Throwable {
//
////        response.sendRedirect("https://www.baidu.com/");
//        ResponseData responseData = ResponseData.success();
//        LOGGER.info("process******************************************" + baseInfo.getUserId());
//        boolean flag1 = redisTemplate.delete(OrderConsts.REDIS_LOAN_ACCOUNT_STATUS + baseInfo.getUserId());
//        LOGGER.info("删除状态key的结果" + flag1);
//
//        BorrowConfirmDto dto = JSONObject.parseObject((String) redisTemplate.opsForValue().get(OrderConsts.REDIS_LOAN_KEY + baseInfo.getUserId()), BorrowConfirmDto.class);
//
//        LOGGER.info("redis" + JSON.toJSONString(dto));
//        boolean flag = redisTemplate.delete(OrderConsts.REDIS_LOAN_KEY + baseInfo.getUserId());
//        LOGGER.info("删除key的结果" + flag);
//        try {
//            responseData = orderInfoService.borrowInfoConfirm(dto, false);
//            LOGGER.info("redirect返回的结果" + JSON.toJSONString(responseData));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseData.error();
//        }
//        return responseData;
//
//    }
//
//    /**
//     * 查询开户状态页
//     *
//     * @return ResponseData
//     * congyuxiang
//     */
//    @RequestMapping(value = "/borrow/confirm/query")
//    public ResponseData borrowConfirmQuery(@RequestBody BaseInfo baseInfo) throws Throwable {
//        LOGGER.info("查询的userId*****" + baseInfo.getUserId());
////        response.sendRedirect("https://www.baidu.com/");
//        ResponseData responseData = ResponseData.success();
//        JSONObject obj = new JSONObject();
//        if (StringUtils.isNotBlank(baseInfo.getUserId())) {
//            String status = (String) redisTemplate.opsForValue().get(OrderConsts.REDIS_LOAN_ACCOUNT_STATUS + baseInfo.getUserId());
//            LOGGER.info("前端查询status" + status);
//            if (status == null) {
//                obj.put("result", "3");
//            } else {
//                if (status.contains("_")) {
//                    String[] statusSplist = status.split("_");
//                    obj.put("result", statusSplist[0]);
//                    obj.put("orderNo", statusSplist[1]);
//                } else {
//                    obj.put("result", status);
//                }
//
//            }
//            responseData.setData(obj);
//
//            LOGGER.info("页面查询开户状态返回的结果" + JSON.toJSONString(responseData));
//            return responseData;
//
//        } else {
//            obj.put("result", 4);
//            responseData.setData(obj);
//            LOGGER.info("页面查询开户状态返回的结果" + JSON.toJSONString(responseData));
//            return responseData;
//        }
//
//
//    }
//
//    /**
//     * 借款结果
//     *
//     * @param baseInfo
//     * @return ResponseData
//     */
//    @RequestMapping(value = "/borrow/result/auth", method = RequestMethod.POST, produces = "application/json")
//    public ResponseData borrowResult(@RequestBody BaseInfo baseInfo) throws Throwable {
//        ResponseData responseData = orderInfoService.getBorrowResult(baseInfo.getUserId());
//        LOGGER.info("流程result" + JSON.toJSONString(responseData));
//        return responseData;
//    }
//
//    /**
//     * 借款详情
//     *
//     * @param baseInfo
//     * @return ResponseData
//     */
//    @RequestMapping(value = "/borrow/detail/auth", method = RequestMethod.POST, produces = "application/json")
//    public ResponseData borrowDetail(@RequestBody BaseInfo baseInfo) throws Throwable {
//        ResponseData responseData = orderInfoService.getBorrowDetail(baseInfo.getOrderNo());
//        return responseData;
//    }
//
//    /**
//     * 所有借款记录
//     *
//     * @param baseInfo
//     * @return ResponseData
//     */
//    @RequestMapping(value = "/borrow/record/auth", method = RequestMethod.POST, produces = "application/json")
//    public ResponseData borrowAll(@RequestBody BaseInfo baseInfo) throws Throwable {
//        ResponseData responseData = orderInfoService.getBorrowAll(baseInfo.getUserId());
//        return responseData;
//    }
//
//}
