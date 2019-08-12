package com.nyd.batch.dao.mapper;


import com.nyd.batch.entity.MemberFeeReport;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberFeeReportMapper {
    /**
     *
     * @mbg.generated 2017-12-25
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbg.generated 2017-12-25
     */
    int insert(MemberFeeReport record);

    /**
     *
     * @mbg.generated 2017-12-25
     */
    int insertSelective(MemberFeeReport record);

    /**
     *
     * @mbg.generated 2017-12-25
     */
    MemberFeeReport selectByPrimaryKey(Integer id);

    /**
     *
     * @mbg.generated 2017-12-25
     */
    int updateByPrimaryKeySelective(MemberFeeReport record);

    /**
     *
     * @mbg.generated 2017-12-25
     */
    int updateByPrimaryKey(MemberFeeReport record);
}