package com.nyd.batch.service.tmp;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Cong Yuxiang
 * 2017/12/11
 **/
@Component
public @Data
class KzjrConfig {

    private String channelCode="20171218103609227830087680";

    private String privateKey="769924a36f1748cd";


    private String baseUrl="https://xd.kzlicai.com/api";
}
