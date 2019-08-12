package com.nyd.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dengw on 2017/11/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IndexModel implements Serializable {
    //最高额度
    private String maxCredit;
    //是否展示活动0展示，1不展示
    private String showActivity;
    //是否展示版权0展示，1不展示
    private String showCopyRight = "1";
    //活动个数
    private String activityAmount;
    //横幅图片列表
    private List<BannerModel> bannerList;
    //广告列表
    private List<String> adList;
    //金融产品信息
    private List<ProductModel> productList;
    //ios版本号
    private String masterVersion;
    //是否展示消息红点
    private String ifRed;
    //活动信息
    private ActivityModel activity;
}
