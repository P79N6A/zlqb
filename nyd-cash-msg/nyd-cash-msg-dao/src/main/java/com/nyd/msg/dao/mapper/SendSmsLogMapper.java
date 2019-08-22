package com.nyd.msg.dao.mapper;


import com.nyd.msg.entity.SendSmsLog;
import com.tasfe.framework.crud.api.Crudable;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author san
 * @since 2019-08-22
 */
@Mapper
public interface SendSmsLogMapper  {

    int deleteByPrimaryKey(Long id);

    int insert(SendSmsLog record);

    int insertSelective(SendSmsLog record);

    SendSmsLog selectByPrimaryKey(Long id);

    List<SendSmsLog> selectByCondistion(Map map);

    int updateByPrimaryKeySelective(SendSmsLog record);

    int updateByPrimaryKeyWithBLOBs(SendSmsLog record);

    int updateByPrimaryKey(SendSmsLog record);

}
