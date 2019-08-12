package com.nyd.admin.service.utils;

import lombok.Data;

/**
 * @Author: wucx
 * @Date: 2018/10/18 18:05
 */
@Data
public class PagingUtils {

    //总条数
    private Integer totalNum;
    //总页数
    private Integer totalPage;
    //每页个数
    private Integer pageSize;
    //当前页码
    private Integer pageNum;
    //当前页从第几条开始查
    private Integer queryIndex;

    public static PagingUtils pagination(Integer totalNum,Integer pageSize,Integer pageNum){
        PagingUtils page = new PagingUtils();
        page.setTotalNum(totalNum);
        Integer totalPage = (totalNum + pageSize - 1)/pageSize;
        page.setTotalPage(totalPage);
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setQueryIndex((pageNum - 1) * pageSize);
        return page;
    }
}
