package com.nyd.admin.service.impl;

import com.nyd.admin.dao.UserDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.FundDetailMapper;
import com.nyd.admin.dao.mapper.FundInfoMapper;
import com.nyd.admin.entity.FundDetail;
import com.nyd.admin.entity.FundInfo;
import com.nyd.admin.model.dto.FundDetailQueryDto;
import com.nyd.admin.model.enums.UserType;
import com.nyd.admin.model.fundManageModel.FundDetailModel;
import com.nyd.admin.model.fundManageModel.FundInfoModel;
import com.nyd.admin.model.power.vo.UserVo;
import com.nyd.admin.service.CrudService;
import com.nyd.admin.service.FundInfoService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author Peng
 * @create 2018-01-02 14:47
 **/
@Service
public class FundInfoServiceImpl extends CrudService<FundInfoMapper,FundInfo> implements FundInfoService{

    @Autowired
    FundInfoMapper fundInfoMapper;
    @Autowired
    FundDetailMapper fundDetailMapper;
    @Autowired
    UserDao userDao;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData getFundInfo(FundDetailQueryDto dto,String accountNo) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //根据accountNo查询是内部还是外部用户
        UserVo vo = new UserVo();
        vo.setAccountNo(accountNo);
        List<UserVo> userVoList = userDao.getUserInfo(vo);
        if (userVoList!=null&&userVoList.size()>0) {
            vo = userVoList.get(0);
        } else {
            return ResponseData.error("不存在此用户");
        }
        FundInfo fundInfo = new FundInfo();
        if (UserType.OUTSIDE.getCode().equals(vo.getUserType())) { //外部用户只能查询自己本人
            fundInfo.setName(vo.getUserName());
            fundInfo.setIdNumber(vo.getIdNumber());
        }
        if (UserType.INSIDE.getCode().equals(vo.getUserType())) { //内部用户
            fundInfo.setName(dto.getName());
            fundInfo.setIdNumber(dto.getIdNumber());
        }
        if (dto.getEndDate() == null) {
            fundInfo.setEndDate(new Date());
        } else {
            fundInfo.setEndDate(sdf.parse(dto.getEndDate()));
        }
        if (dto.getStartDate() != null) {
            fundInfo.setStartDate(sdf.parse(dto.getStartDate()));
        }
        List<FundInfoModel> fundInfoModelList = fundInfoMapper.queryFundInfo(fundInfo);
        return ResponseData.success(fundInfoModelList);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData getFundDetail(FundDetailQueryDto dto){
        FundDetail fundDetail = new FundDetail();
        fundDetail.setFundId(dto.getFundId());
        List<FundDetailModel> list = fundDetailMapper.queryFundDetail(fundDetail);
        return ResponseData.success(list);
    }
}
