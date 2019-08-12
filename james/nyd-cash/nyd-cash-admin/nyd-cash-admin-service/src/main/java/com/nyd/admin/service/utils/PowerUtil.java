package com.nyd.admin.service.utils;

import com.nyd.admin.model.power.TreeNode;
import com.nyd.admin.model.power.UserPowerInfo;
import com.nyd.admin.model.power.vo.UserPowerVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujx on 2018/1/5.
 */
public class PowerUtil {

    public static UserPowerVo getUserPowerVo(List<UserPowerInfo> userPowerInfos){
        //组装TreeNode集合
        List<TreeNode> treeNodeList = new ArrayList<>();
        userPowerInfos.forEach(userPowerInfo -> {
            TreeNode treeNode = new TreeNode();
            BeanUtils.copyProperties(userPowerInfo,treeNode);
            treeNodeList.add(treeNode);
        });

        // 返回菜单
        List<TreeNode> treeMenuList = new ArrayList<>();
        // 先找到所有的一级菜单
        treeNodeList.stream().forEach(treeNode -> {
            // 一级菜单parentId = null ，或者为0
            if (treeNode.getPid() == 0 || treeNode.getPid() == null) {
                treeMenuList.add(treeNode);
            }
        });
        // 为一级菜单设置子菜单，getChild是递归调用的
        for (TreeNode menu : treeMenuList) {
            menu.setChilds(getChild(menu.getPowerId(), treeNodeList));
        }
        UserPowerVo userPowerVo = new UserPowerVo();
        userPowerVo.setTreeNodeList(treeMenuList);

        return  userPowerVo;
    }

    private static List<TreeNode> getChild(Long powerId, List<TreeNode> treeNodeList) {

        // 子菜单
        List<TreeNode> childList = new ArrayList<>();
        for (TreeNode menu : treeNodeList) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (menu.getPid() != 0) {
                if (menu.getPid().equals(powerId)) {
                    childList.add(menu);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (TreeNode menu : childList) {
            if (menu.getPid() != 0) {
                // 递归
                menu.setChilds(getChild(menu.getPowerId(), treeNodeList));
            }
        }
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }


    private void addChild(TreeNode treeNode,List<UserPowerInfo> userPowerInfoList){
        List<TreeNode> treeNodeChilds = new ArrayList<>();
        userPowerInfoList.stream().forEach(u->{
            if(u.getPowerId() == treeNode.getPowerId()){
                TreeNode treeNodeChild = new TreeNode();
                BeanUtils.copyProperties(u,treeNodeChild);
                treeNodeChilds.add(treeNodeChild);
            }
        });
        treeNode.setChilds(treeNodeChilds);
    }
}
