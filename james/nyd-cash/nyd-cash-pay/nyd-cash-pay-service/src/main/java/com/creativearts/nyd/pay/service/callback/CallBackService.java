package com.creativearts.nyd.pay.service.callback;

import org.springframework.stereotype.Service;

/**
 * Cong Yuxiang
 * 2017/11/17
 **/
@Service
public class CallBackService implements ICallBackService{
//    @Autowired
//    private RabbitmqProducerProxy rabbitmqProducerProxy;

    @Override
    public boolean saveWxCallBack(String callback) {
//        rabbitmqProducerProxy.convertAndSend();
        return false;
    }
}
