package com.creativearts.nyd.pay.service.weixinpay.oauth;

import com.creativearts.nyd.pay.config.utils.LogKit;

import java.util.concurrent.Callable;

/**
 * Cong Yuxiang
 * 2017/11/15
 **/
public class RetryUtils {
    public RetryUtils() {
    }

    public static <V extends RetryUtils.ResultCheck> V retryOnException(int retryLimit, Callable<V> retryCallable) {
        V v = null;

        for(int i = 0; i < retryLimit; ++i) {
            try {
                v = (V) retryCallable.call();
            } catch (Exception var5) {
                LogKit.warn("retry on " + (i + 1) + " times v = " + (v == null ? null : v.getJson()), var5);
            }

            if (null != v && v.matching()) {
                break;
            }

            LogKit.error("retry on " + (i + 1) + " times but not matching v = " + (v == null ? null : v.getJson()));
        }

        return v;
    }

    public static <V extends RetryUtils.ResultCheck> V retryOnException(int retryLimit, long sleepMillis, Callable<V> retryCallable) throws InterruptedException {
        V v = null;

        for(int i = 0; i < retryLimit; ++i) {
            try {
                v = (V) retryCallable.call();
            } catch (Exception var7) {
                LogKit.warn("retry on " + (i + 1) + " times v = " + (v == null ? null : v.getJson()), var7);
            }

            if (null != v && v.matching()) {
                break;
            }

            LogKit.error("retry on " + (i + 1) + " times but not matching v = " + (v == null ? null : v.getJson()));
            Thread.sleep(sleepMillis);
        }

        return v;
    }

    public interface ResultCheck {
        boolean matching();

        String getJson();
    }
}
