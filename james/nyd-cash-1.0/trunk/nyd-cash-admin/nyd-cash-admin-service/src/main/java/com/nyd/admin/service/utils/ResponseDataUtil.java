package com.nyd.admin.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author jiaxy
 * @date 20180614
 */
public class ResponseDataUtil {
    private final static Logger logger = LoggerFactory.getLogger(ResponseDataUtil.class);

    public static <T, M> List<T> copyBeanList(List<M> list, Class<T> clazz) {
        List<T> beanList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (M info : list) {
                T bean = null;
                try {
                    bean = clazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                BeanUtils.copyProperties(info, bean);
                beanList.add(bean);
            }
        }

        return beanList;
    }


}
