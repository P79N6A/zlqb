package com.nyd.application.service.util;

import com.fadada.sdk.client.FddClientBase;
import com.fadada.sdk.client.FddClientExtra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by hwei on 2017/11/17.
 */
@Component
public class FadadaApi {
    @Autowired
    AppProperties appProperties;

    public FddClientBase getClienBase(){
        FddClientBase clientBase = null;
        if (clientBase==null){
            clientBase = new FddClientBase(appProperties.getFadadaAppId(),appProperties.getFadadaAppSecret(),appProperties.getFadadaVersion(),appProperties.getFadadaUrl());
        }
        return clientBase;
    }

    public FddClientExtra getClientExtra(){
        FddClientExtra clientExtra = null;
        if (clientExtra==null){
            clientExtra = new FddClientExtra(appProperties.getFadadaAppId(),appProperties.getFadadaAppSecret(),appProperties.getFadadaVersion(),appProperties.getFadadaUrl());
        }
        return clientExtra;
    }
}
