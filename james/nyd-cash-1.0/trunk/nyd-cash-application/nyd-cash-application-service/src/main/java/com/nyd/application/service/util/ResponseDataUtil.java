package com.nyd.application.service.util;

import com.nyd.application.service.consts.ApplicationConsts;
import com.nyd.application.service.exception.RpcResponseException;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author yifeng.qiu
 * @date 2018/04/16
 * @Descreption:
 */
public class ResponseDataUtil {
    private final static Logger logger = LoggerFactory.getLogger(ResponseDataUtil.class);


    public static <T> T responseUnifyDeal(ResponseData<T> data, Class<T> clazz) {
        if (StringUtils.equals(ApplicationConsts.RET_STATUS.SUCCESS, data.getStatus())) {
            return data.getData();
        } else {
            throw new RpcResponseException
                    (data.getMsg());
        }

    }

    public static <T> List<T> responseUnifyDealList(ResponseData<List<T>> data, Class<T> clazz) {
        if (StringUtils.equals(ApplicationConsts.RET_STATUS.SUCCESS, data.getStatus())) {
            List<T> list = data.getData();
            return list;
        } else {
            throw new RpcResponseException(data.getMsg());
        }

    }

    public static <T, M> List<T> copyBeanList(List<M> list, Class<T> clazz) {
        List<T> beanList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (M info : list) {
                T bean = null;
                try {
                    bean = clazz.newInstance();
                } catch (Exception e) {
                    throw new RpcResponseException(e);
                }
                BeanUtils.copyProperties(info, bean);
                beanList.add(bean);
            }
        }

        return beanList;
    }

    public static String msgAddUUID(String msg) {
        return "错误号:" + UUID.randomUUID() + " " + msg;
    }

    public static String msgAddUUID(Exception e) {
        return "错误号:" + UUID.randomUUID() + " " + getTrace(e);
    }

    public static String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }


}
