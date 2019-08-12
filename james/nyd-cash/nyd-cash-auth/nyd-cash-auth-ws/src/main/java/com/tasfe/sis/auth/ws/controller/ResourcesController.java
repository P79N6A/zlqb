package com.tasfe.sis.auth.ws.controller;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.params.Page;
import com.tasfe.sis.auth.entity.Resources;
import com.tasfe.sis.auth.model.ResourcesInfos;
import com.tasfe.sis.auth.service.ResourcesService;
import com.tasfe.zh.base.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Lait on 2017/8/17.
 */
@RestController
@RequestMapping("/resources")
public class ResourcesController {
    @Autowired
    private ResourcesService resourcesService;

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    public ResponseData addResources(@RequestBody Resources resources) throws Exception {
        ResponseData<Resources> responseData = new ResponseData();
        resourcesService.createResources(resources);
        responseData.setCode("").setMsg("");
        responseData.setData(resources);
        return responseData;
    }


    @RequestMapping("/upd")
    public void updResources(@RequestBody Resources resources) throws Exception {
        resourcesService.updResources(resources);
    }


    /**
     * 分页列出资源
     *
     * @param resourcesInfos
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public ResponseData listResources(@RequestBody ResourcesInfos resourcesInfos) throws Exception {
        ResponseData<Page> responseData = new ResponseData();
        Page<Resources> page = resourcesService.pagingRosources(resourcesInfos, Criteria.from(Resources.class).limit(Page.resolve(resourcesInfos.getPageNo(), resourcesInfos.getPageSize())));
        responseData.setData(page);
        responseData.setCode("100000");
        responseData.setMsg("success");
        return responseData;
    }


    /**
     * 分页列出角色
     *
     * @param resources
     * @return
     * @throws Exception
     */
    @RequestMapping("/listTree")
    public ResponseData listResourceTree(@RequestBody Resources resources) throws Exception {
        ResponseData<List> responseData = new ResponseData();
        responseData.setData(resourcesService.listResourcesTree(resources));
        responseData.setCode("100000");
        responseData.setMsg("success");
        return responseData;
    }


}
