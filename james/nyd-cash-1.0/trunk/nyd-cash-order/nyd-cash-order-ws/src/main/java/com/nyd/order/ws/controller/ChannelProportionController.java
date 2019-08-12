package com.nyd.order.ws.controller;

import com.alibaba.fastjson.JSON;
import com.nyd.order.entity.ChannelProportionConfig;
import com.nyd.order.model.YmtKzjrBill.KzjrRepayDetail;
import com.nyd.order.model.YmtKzjrBill.KzjrRepayInfo;
import com.nyd.order.service.ChannelProportionService;
import com.nyd.order.ws.controller.dto.UpdateChannelConfigReqDTO;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 侬要贷资金渠道路由
 *
 * @author shaoqing.liu
 * @date 2018/10/12 10:38
 */
@RestController
@RequestMapping("/order")
public class ChannelProportionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelProportionController.class);
    @Autowired
    private ChannelProportionService channelProportionService;

    /**
     * 侬要贷资金渠道路由列表
     */
    @RequestMapping(value = "/zjly/getChannelConfig", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getChannelConfig() {
        ResponseData responseData = ResponseData.success();
        List<ChannelProportionConfig> proportionConfigs = channelProportionService.getChannelConfig();
        responseData.setData(proportionConfigs);
        return responseData;

    }

    /**
     * 修改资金路由值
     */
    @RequestMapping(value = "/zjly/updateChannelConfig", method = RequestMethod.POST, produces = "application/json")
    public ResponseData updateChannelConfig(@RequestBody UpdateChannelConfigReqDTO updateChannelConfigReqDTO) {
        try {
            if (updateChannelConfigReqDTO.getId() > 0) {

                if (updateChannelConfigReqDTO.getChannelLimit() >= 0 || updateChannelConfigReqDTO.getChannelRatio() >= 0) {
                    ChannelProportionConfig channelProportionConfig = new ChannelProportionConfig();

                    channelProportionConfig.setId(updateChannelConfigReqDTO.getId());
                    if (updateChannelConfigReqDTO.getChannelRatio() >= 0) {
                        channelProportionConfig.setChannelRatio(updateChannelConfigReqDTO.getChannelRatio());
                    }
                    if (updateChannelConfigReqDTO.getChannelLimit() >= 0) {
                        channelProportionConfig.setChannelLimit(updateChannelConfigReqDTO.getChannelLimit());
                    }
                    int update = channelProportionService.updateChannelConfig(channelProportionConfig);

                    ResponseData responseData = ResponseData.success();
                    responseData.setData(update);
                    return responseData;
                }
            }
        } catch (Exception ex) {
            LOGGER.error("updateChannelConfig error:{}", ex);
        }
        return ResponseData.error();

    }


}
