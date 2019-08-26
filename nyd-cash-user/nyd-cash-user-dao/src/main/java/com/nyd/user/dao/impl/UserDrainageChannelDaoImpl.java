package com.nyd.user.dao.impl;

import com.nyd.user.dao.UserDrainageChannelDao;
import com.nyd.user.entity.Detail;
import com.nyd.user.entity.User;
import com.nyd.user.entity.UserDrainageChannel;
import com.nyd.user.model.vo.UserDrainageChannelVo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户引流渠道Dao
 * @author san
 */
@Repository
public class UserDrainageChannelDaoImpl implements UserDrainageChannelDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public UserDrainageChannel getUserDrainageChannelByDrainageChannelName(String drainageChannelName) throws Exception {
        UserDrainageChannel userDrainageChannel = new UserDrainageChannel();
        Criteria criteria = Criteria.from(UserDrainageChannel.class).fields("state", "drainage_channel_name")
                .where()
                .and("drainage_channel_name",Operator.EQ,drainageChannelName)
                .and("drainage_channel_name",Operator.EQ,drainageChannelName)
                .endWhere();
        List<UserDrainageChannel> userDrainageChannelList = crudTemplate.find(userDrainageChannel,criteria);
        if (userDrainageChannelList != null && userDrainageChannelList.size() > 0){
            return userDrainageChannelList.get(0);
        }
        return null;
    }

    @Override
    public void save(UserDrainageChannel userDrainageChannel) throws Exception {
        crudTemplate.save(userDrainageChannel);
    }
}
