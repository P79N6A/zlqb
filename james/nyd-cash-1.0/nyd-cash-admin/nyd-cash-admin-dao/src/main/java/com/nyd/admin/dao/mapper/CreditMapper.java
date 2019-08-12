package com.nyd.admin.dao.mapper;

import com.nyd.admin.model.Info.CreditInfo;
import com.nyd.admin.model.Info.UserInfo;
import com.nyd.admin.model.dto.CreditDto;
import com.nyd.admin.model.dto.CreditRemarkDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: wucx
 * @Date: 2018/10/16 21:13
 */
@Mapper
public interface CreditMapper {
    /**
     * 授信查询
     * @param creditDto
     * @return
     */
    List<CreditInfo> findCreditDetails(CreditDto creditDto);

    /**
     * 授信查询个数
     * @param creditDto
     * @return
     */
    Integer findCount(CreditDto creditDto);

    /**
     * 查询用户姓名和性别
     * @param userId
     * @return
     */
    List<UserInfo> findRealNameAndGender(@Param("userId") String userId);

    /**
     * 查询操作
     * @param accountNumber
     * @return
     */
    String findRemark(@Param("accountNumber") String accountNumber);

    /**
     * 更改remark
     * @param creditRemarkDto
     * @return
     */
    Integer updateCreditRemark(CreditRemarkDto creditRemarkDto);

    /**
     * 操作日志表中新增一条记录
     * @param creditRemarkDto
     * @return
     */
    Integer insertCreditLog(CreditRemarkDto creditRemarkDto);
}
