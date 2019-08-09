package com.nyd.user.dao.impl;

import com.nyd.user.dao.ChannelDao;
import com.nyd.user.entity.Channel;
import com.nyd.user.model.ChannelInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhujx on 2017/12/5.
 */
@Repository
public class ChannelDaoImpl implements ChannelDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(ChannelInfo channelInfo) throws Exception {
        Channel  channel = new Channel();
        BeanUtils.copyProperties(channelInfo,channel);
        crudTemplate.save(channel);
    }

    /**
     * 根据url查询渠道信息
     * @param url
     * @return
     * @throws Exception
     */
    @Override
    public ChannelInfo getChannelInfoByUrl(String url) throws Exception{
        Criteria criteria = Criteria.from(Channel.class)
                .where()
                .and("url", Operator.EQ,url)
                .and("delete_flag", Operator.EQ,0)
                .endWhere();
        ChannelInfo channelInfo = new ChannelInfo();
        List<ChannelInfo> list = crudTemplate.find(channelInfo,criteria);
        if(list != null && list.size() > 0){
            channelInfo = list.get(0);
        }
        return channelInfo;
    }

}
