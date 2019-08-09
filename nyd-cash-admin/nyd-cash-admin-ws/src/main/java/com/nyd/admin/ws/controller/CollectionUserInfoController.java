package com.nyd.admin.ws.controller;

import com.nyd.admin.model.dto.CollectionUserInfoDto;
import com.nyd.admin.service.CollectionUserService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiaxy
 * @date 20180613
 */
@RestController
@RequestMapping("/admin/collectionUser")
public class CollectionUserInfoController {
    @Autowired
    CollectionUserService collectionUserService;

    /**
     * 查询所有催收人员列表
     * @param dto
     * @return
     */
    @RequestMapping("/queryAllCollectionUser")
    public ResponseData queryAllCollectionUser(@RequestBody CollectionUserInfoDto dto){
       return collectionUserService.queryAllCollectionUser(dto);
    }

    /**
     * 根据分组id查询所有催收人员
     * @param dto
     * @return
     */
    @RequestMapping("/queryCollectionUserByGroupId")
    public ResponseData queryCollectionUserByGroupId(@RequestBody CollectionUserInfoDto dto){
        return collectionUserService.queryCollectionUserByGroupId(dto);
    }


    /**
     * 更新催收人员信息
     * @param dto
     * @return
     */
    @RequestMapping("/updateCollectionUser")
    public ResponseData updateCollectionUser(@RequestBody CollectionUserInfoDto dto){
        return collectionUserService.updateCollectionUser(dto);
    }

    /**
     * 删除催收人员（逻辑删除）
     * @param dto
     * @return
     */
    @RequestMapping("/deleteCollectionUser")
    public ResponseData deleteCollectionUser(@RequestBody CollectionUserInfoDto dto){
        return collectionUserService.deleteCollectionUser(dto);
    }

}
