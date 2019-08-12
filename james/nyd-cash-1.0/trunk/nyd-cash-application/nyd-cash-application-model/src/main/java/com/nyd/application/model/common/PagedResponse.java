package com.nyd.application.model.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 带分页的返回模型
 * @author fxw
 *
 * @param <T>
 */
public class PagedResponse<T> implements Serializable {

	private static final long serialVersionUID = -8835791622389513023L;

	// 总条数
	private long total;

	// 记录
	private T data;
	
	// 是否成功
	private boolean success = false;
	
	// 返回信息
	private String msg;
	
    // 返回编码
    private String code;
	
	// 页脚
	private List<Map<String,String>> footer;
	
	// 排序字段
    private String sort;
    // 顺序
    private String order;
    
    private int pageNo;
    
    private int pageSize;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<Map<String,String>> getFooter() {
		return footer;
	}

	public void setFooter(List<Map<String,String>> footer) {
		this.footer = footer;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	

	public static PagedResponse<?> error(String msg) {
		PagedResponse<?> result=new PagedResponse<>();
		result.setSuccess(false);
		result.setMsg(msg);
		return result;
	}

	public static PagedResponse<?> success(String msg) {
		PagedResponse<?> result=new PagedResponse<>();
		result.setSuccess(true);
		result.setMsg(msg);
		return result;
	}
}
