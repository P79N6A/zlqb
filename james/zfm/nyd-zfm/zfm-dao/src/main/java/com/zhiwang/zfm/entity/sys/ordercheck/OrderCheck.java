package com.zhiwang.zfm.entity.sys.ordercheck;



public class OrderCheck {
	
	public static final String defaultPW="888888";
		private String orderNo;
    //信审人员
    private String checkPersonnel;
    //借款期次
    private String  loanNumber;
    //客户姓名
    private String userName;
    //手机号码
    private String accountNumber;
    //注册渠道
    private String appName;
    //申请时间
    private String loanTime;
    //放款产品
    private String fundProduct;
    //分配时间
    private String distributionTime;
    //贷款编号
    private String loanNo;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getCheckPersonnel() {
		return checkPersonnel;
	}
	public void setCheckPersonnel(String checkPersonnel) {
		this.checkPersonnel = checkPersonnel;
	}
	public String getLoanNumber() {
		return loanNumber;
	}
	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getLoanTime() {
		return loanTime;
	}
	public void setLoanTime(String loanTime) {
		this.loanTime = loanTime;
	}
	public String getFundProduct() {
		return fundProduct;
	}
	public void setFundProduct(String fundProduct) {
		this.fundProduct = fundProduct;
	}
	public String getDistributionTime() {
		return distributionTime;
	}
	public void setDistributionTime(String distributionTime) {
		this.distributionTime = distributionTime;
	}
	public String getLoanNo() {
		return loanNo;
	}
	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}
    
    
}
