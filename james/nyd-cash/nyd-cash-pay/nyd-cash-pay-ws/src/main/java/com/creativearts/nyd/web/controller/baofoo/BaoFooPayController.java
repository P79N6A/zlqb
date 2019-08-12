//package com.creativearts.nyd.web.controller.baofoo;
//
//import com.alibaba.fastjson.JSON;
//import com.creativearts.nyd.pay.model.baofoo.*;
//import com.creativearts.nyd.pay.service.newbaofoo.baofoo.service.BaoFooPayService;
//import com.tasfe.framework.support.model.ResponseData;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
///**
// * @author liuqiu
// */
//@RestController
//@RequestMapping("/pay/baofoo")
//public class BaoFooPayController {
//    private Logger log = LoggerFactory.getLogger(BaoFooPayController.class);
//
//    @Autowired
//    private BaoFooPayService baoFooPayService;
//
//    /**
//     * @param dto
//     * @return
//     */
//    @RequestMapping(value = "/prepareBind")
//    public ResponseData prepareBind(@RequestBody BaoFooPreparebindDto dto) {
//        log.info("宝付预绑卡请求参数:{}", dto.toString());
//        ResponseData data = baoFooPayService.prepareBind(dto);
//        log.info("宝付预绑卡响应参数:{}", JSON.toJSONString(data));
//        return data;
//    }
//
////    /**
////     * configBingCard:(绑卡确认接口). <br/>
////     *
////     * @param configBingCardBaoFooReqDTO
////     * @return
////     * @author wangzhch
////     * @since JDK 1.8
////     */
////    @RequestMapping(value = "/configBingCard")
////    public ResponseData configBingCard(@RequestBody ConfigBingCardBaoFooReqDTO configBingCardBaoFooReqDTO) {
////        log.info("宝付预绑卡请求参数:{}", JSON.toJSONString(configBingCardBaoFooReqDTO));
////        ResponseData data = baoFooPayService.configBingCard(configBingCardBaoFooReqDTO);
////        log.info("宝付预绑卡响应结果：{}", JSON.toJSONString(data));
////        return data;
////    }
//
//
////    /**
////     * untieCard:(解除银行卡绑定). <br/>
////     *
////     * @param untieCardBaoFooReqDTO
////     * @return
////     * @author wangzhch
////     * @since JDK 1.8
////     */
////    @RequestMapping(value = "/untieCard")
////    public ResponseData untieCard(@RequestBody UntieCardBaoFooReqDTO untieCardBaoFooReqDTO) {
////        log.info("宝付解除银行卡绑定接口请求参数:{}", JSON.toJSONString(untieCardBaoFooReqDTO));
////        ResponseData data = baoFooPayService.untieCard(untieCardBaoFooReqDTO);
////        log.info("宝付解除银行卡绑定接口响应参数:{}", JSON.toJSONString(data));
////        return data;
////    }
////
////    /**
////     * queryCardStatus:(银行卡绑定结果查询). <br/>
////     *
////     * @return
////     * @author wangzhch
////     * @since JDK 1.8
////     */
////    @RequestMapping(value = "/queryCardStatus")
////    public ResponseData queryCardStatus(@RequestBody QueryCardStatusBaoFooReqDTO queryCardStatusBaoFooReqDTO) {
////        log.info("宝付银行卡绑定结果查询接口请求参数:{}", JSON.toJSONString(queryCardStatusBaoFooReqDTO));
////        ResponseData data = baoFooPayService.queryCardStatus(queryCardStatusBaoFooReqDTO);
////        log.info("宝付银行卡绑定结果查询接口响应参数:{}", JSON.toJSONString(data));
////        return data;
////    }
////
////    /**
////     * queryOrderStatus:(支付结果查询). <br/>
////     *
////     * @param queryOrderStatusBaoFooReqDTO
////     * @return
////     * @author wangzhch
////     * @since JDK 1.8
////     */
////    @RequestMapping(value = "/queryOrderStatus")
////    public ResponseData queryOrderStatus(@RequestBody QueryOrderStatusBaoFooReqDTO queryOrderStatusBaoFooReqDTO) {
////        log.info("宝付支付结果查询接口请求参数:{}", JSON.toJSONString(queryOrderStatusBaoFooReqDTO));
////        ResponseData data = baoFooPayService.queryOrderStatus(queryOrderStatusBaoFooReqDTO);
////        log.info("宝付支付结果查询接口响应参数:{}", JSON.toJSONString(data));
////        return data;
////    }
//}
