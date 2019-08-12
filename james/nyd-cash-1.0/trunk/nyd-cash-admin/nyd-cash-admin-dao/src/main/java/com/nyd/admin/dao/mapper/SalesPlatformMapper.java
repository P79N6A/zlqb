package com.nyd.admin.dao.mapper;

import com.nyd.admin.model.SalesPlatformInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by hwei on 2018/12/3.
 */
@Mapper
public interface SalesPlatformMapper {
    /**
     * 注册未填写资料
     * @param startTime
     * @param endTime
     * @return
     */
    List<SalesPlatformInfo> findRegisterUnfilledData(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 注册未填写资料个数
     * @param startTime
     * @param endTime
     * @return
     */
    Integer findRegisterUnfilledDataCount(@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * 注册资料填写不完整
     * @param endTime
     * @return
     */
    List<SalesPlatformInfo> findDataIncomplete(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 注册资料填写不完整个数
     * @param endTime
     * @return
     */
    Integer findDataIncompleteCount(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 放款成功
     * @param endTime
     * @return
     */
    List<SalesPlatformInfo> findLoadSuccess(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 借款成功个数
     * @param endTime
     * @return
     */
    Integer findLoadSuccessCount(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<String> selectByMobile(Map<String, String> params);
}
