package com.nyd.admin.dao.mapper;

import com.nyd.admin.entity.ReturnPremium;
import com.nyd.admin.model.Info.IntegratedVolumeInfo;
import com.nyd.admin.model.dto.BatchUserDto;
import com.nyd.admin.model.dto.IntegratedVolumeDto;
import com.nyd.admin.model.dto.RemarkDto;
import com.nyd.admin.model.dto.ReturnTicketLogDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReturnPremiumMapper {
    /**
     * 查询当天是否标记过
     * @param mobile
     * @return
     */
    List<ReturnPremium> selectIfLabelToday(String mobile);

    /**
     * 综合券管理（侬要贷）查询
     * @param integratedVolumeDto
     * @return
     */
    List<IntegratedVolumeInfo> findIntegratedVolumeDetails(IntegratedVolumeDto integratedVolumeDto);

    /**
     * 综合券管理（侬要贷）查询 总个数
     * @param integratedVolumeDto
     * @return
     */
    Integer getIntegratedVolumeDetailsCount(IntegratedVolumeDto integratedVolumeDto);

    /**
     * 批量发券
     * @param batchUserDto
     */
    Integer updatePremiumBypremiumId(BatchUserDto batchUserDto);

    /**
     * 保存券发放记录
     * @param list
     * @return
     */
    Integer insertRetuenTicketLog(@Param("list") List<ReturnTicketLogDto> list);

    /**
     * 修改备注
     * @param remarkDto
     * @return
     */
    Integer updateRemark(RemarkDto remarkDto);





}
