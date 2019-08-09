package com.nyd.settlement.ws.controller;


import com.nyd.settlement.model.dto.QueryDto;
import com.nyd.settlement.model.dto.refund.AddRefundDto;
import com.nyd.settlement.model.dto.refund.DoRefundDto;
import com.nyd.settlement.service.ReFundService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hwei on 2018/1/15.
 */
@RestController
@RequestMapping("/settlement")
public class ReFundController {
    private static Logger LOGGER = LoggerFactory.getLogger(ReFundController.class);

    @Autowired
    ReFundService reFundService;

    /**
     * 未退款查询
     * @param queryDto
     * @return
     */
    @RequestMapping(value = "/queryNoReFund", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryNoReFund(@RequestBody QueryDto queryDto) {
        return reFundService.queryNoReFund(queryDto);
    }

    /**
     * 未退款查询详情
     * @param queryDto
     * @return
     */
    @RequestMapping(value = "/queryNoReFundDetail", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryNoReFundDetail(@RequestBody QueryDto queryDto) {
        return reFundService.queryNoReFundDetail(queryDto);
    }

    /**
     * 添加退款
     * @param addRefundDto
     * @return
     */
    @RequestMapping(value = "/addReFund", method = RequestMethod.POST, produces = "application/json")
    public ResponseData addReFund(@RequestBody AddRefundDto addRefundDto) {
        return reFundService.addReFund(addRefundDto);
    }

    /**
     * 退款
     * @param doRefundDto
     * @return
     */
    @RequestMapping(value = "/doReFund", method = RequestMethod.POST, produces = "application/json")
    public ResponseData doReFund(@RequestBody DoRefundDto doRefundDto) {
        return reFundService.doReFund(doRefundDto);
    }

    /**
     * 已退款查询
     * @param queryDto
     * @return
     */
    @RequestMapping(value = "/queryAlreadyReFund", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryAlreadyReFund(@RequestBody QueryDto queryDto) {
        return reFundService.queryAlreadyReFund(queryDto);
    }

}
