package com.nyd.admin.model.paging;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 分页对象
 *
 */
public class Paging implements Serializable {

    /**
     * 页码
     */
    @Getter
    @Setter
    private int pageNum;
    /**
     * 每页条数
     */
    @Getter
    @Setter
    private int pageSize;
    /**
     * 排序字段
     */
    @Getter
    @Setter
    private String orderBy;

    public Paging() {
        this.pageNum = 1;
        this.pageSize = 20;
    }

    public Paging(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Paging(int pageNum, int pageSize, String orderBy) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
    }

}
