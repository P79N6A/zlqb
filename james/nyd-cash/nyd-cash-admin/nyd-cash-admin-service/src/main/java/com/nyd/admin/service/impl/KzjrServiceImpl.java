package com.nyd.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.KzjrProductConfigMapper;
import com.nyd.admin.entity.KzjrProductConfig;
import com.nyd.admin.model.KzjrProductConfigVo;
import com.nyd.admin.model.KzjrQueryVo;
import com.nyd.admin.service.KzjrService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.KzjrConfigStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Dengw on 2017/12/15
 */
@Service
public class KzjrServiceImpl implements KzjrService {
    Logger logger = LoggerFactory.getLogger(KzjrServiceImpl.class);

    @Autowired
    KzjrProductConfigMapper kzjrProductConfigMapper;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
    @Override
    public boolean saveKzjr(KzjrProductConfigVo vo) {
        boolean saveFlag = false;
        try {
            vo.setRemainAmount(vo.getTotalAmount());
            KzjrProductConfig kzjrProductConfig = KzjrConfigStruct.INSTANCE.vo2Po(vo);
            kzjrProductConfigMapper.insert(kzjrProductConfig);
            saveFlag = true;
        } catch (Exception e) {
            logger.error("保存空中金融失败",e);
        }
        return saveFlag;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
    @Override
    public boolean updateKzjr(KzjrProductConfigVo vo) {
        boolean updateFlag = false;
        try {
            KzjrProductConfig kzjrProductConfig = KzjrConfigStruct.INSTANCE.vo2Po(vo);
            kzjrProductConfigMapper.update(kzjrProductConfig);
            updateFlag = true;
        } catch (Exception e) {
            logger.error("更新空中金融失败",e);
        }
        return updateFlag;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
    @Override
    public PageInfo<KzjrProductConfigVo> findPage(KzjrQueryVo vo) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        KzjrProductConfig kzjrProductConfig = new KzjrProductConfig();
        kzjrProductConfig.setProductCode(vo.getProductCode());
        if(vo.getStartDate() != null){
            kzjrProductConfig.setStartDate(sdf.parse(vo.getStartDate()));
        }
        if(vo.getEndDate() != null){
            kzjrProductConfig.setEndDate(sdf.parse(vo.getEndDate()));
        }
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize(), vo.getOrderBy());
        List<KzjrProductConfig> list = kzjrProductConfigMapper.findList(kzjrProductConfig);
        PageInfo<KzjrProductConfig> result = new PageInfo<>(list);
        return KzjrConfigStruct.INSTANCE.poPage2VoPage(result);
    }
}
