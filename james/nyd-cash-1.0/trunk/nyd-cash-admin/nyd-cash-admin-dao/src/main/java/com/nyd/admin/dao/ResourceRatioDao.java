package com.nyd.admin.dao;

import com.nyd.admin.model.ResourceRatioVo;
import com.nyd.admin.model.TransformReportVo;
import java.util.List;

/**
 * @author Peng
 * @create 2017-12-27 11:17
 **/
public interface ResourceRatioDao {
    List<ResourceRatioVo> getResourceRatio() throws Exception;
}
