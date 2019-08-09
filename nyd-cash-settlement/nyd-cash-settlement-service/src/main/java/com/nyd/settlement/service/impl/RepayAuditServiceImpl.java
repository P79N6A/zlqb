package com.nyd.settlement.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.nyd.settlement.dao.ds.DataSourceContextHolder;
import com.nyd.settlement.dao.mapper.RepayMapper;
import com.nyd.settlement.dao.mapper.RepayTmpMapper;
import com.nyd.settlement.entity.repay.RepaySettleTmp;
import com.nyd.settlement.model.dto.repay.RepayAddDto;
import com.nyd.settlement.model.dto.repay.RepayAuditQueryDto;
import com.nyd.settlement.model.vo.repay.RepayAuditVo;
import com.nyd.settlement.service.CrudService;
import com.nyd.settlement.service.RepayAuditService;
import com.nyd.settlement.service.RepayCalService;
import com.nyd.settlement.service.aspect.RoutingDataSource;
import com.nyd.settlement.service.struct.RepayAuditStruct;
import com.nyd.settlement.service.utils.ValidateUtil;
import com.nyd.zeus.api.BillContract;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2018/1/16
 **/
@Service
public class RepayAuditServiceImpl extends CrudService<RepayTmpMapper,RepaySettleTmp> implements RepayAuditService{
    Logger logger = LoggerFactory.getLogger(RepayServiceImpl.class);

    @Autowired
    BillContract billContract;

    @Autowired
    RepayMapper repayMapper;
    @Autowired
    RepayTmpMapper repayTmpMapper;



    @Autowired
    private RepayCalService repayCalService;



    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void save(RepayAddDto dto) {
        ValidateUtil.process(dto);
        RepaySettleTmp entity = RepayAuditStruct.INSTANCE.dto2Po(dto);
        entity.setAuditStatus(1);
        entity.setRepayType(4);
        this.getDao().insert(entity);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public PageInfo<RepayAuditVo> findAuditList(RepayAuditQueryDto repayAuditQueryDto) {
        Map<String,Object> map = new HashMap<>();

        if(StringUtils.isNotBlank(repayAuditQueryDto.getName())) {
            map.put("userName", repayAuditQueryDto.getName());
        }
        if(StringUtils.isNotBlank(repayAuditQueryDto.getMobile())) {
            map.put("mobile", repayAuditQueryDto.getMobile());
        }
        if("1".equals(repayAuditQueryDto.getType())) {
            map.put("type", 0);
        }else if("2".equals(repayAuditQueryDto.getType())){
            map.put("type",1);
        }
        PageInfo<RepaySettleTmp> result = this.queryPage(repayAuditQueryDto,map);
        return RepayAuditStruct.INSTANCE.poPage2VoPage(result);
    }

   @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public JSONObject audit(List<Long> ids,String updateBy) {


        List<RepaySettleTmp> auditList = this.getDao().queryListByIds(ids);
        int failCount = 0;
        List<String> list = new ArrayList<>();
        for(RepaySettleTmp tmp : auditList){

            logger.info("待审核:"+JSONObject.toJSONString(tmp));
            try {
                tmp.setUpdateBy(updateBy);
                repayCalService.doRepay(tmp);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("审核异常",e);
                failCount++;
                list.add(tmp.getUserName());
            }
        }
        JSONObject result = new JSONObject();
        result.put("failCount",failCount);
        result.put("names",list);
        return result;
    }






}
