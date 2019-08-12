package com.nyd.order.model;

import java.math.BigDecimal;

public class OrderCashOut {
	private String id;
	private BigDecimal realLoanAmount;
	private Integer borrowTime;
	private BigDecimal managerFee;
	private BigDecimal interest;
	private BigDecimal sum;
	
	
	public BigDecimal getSum() {
		return sum;
	}
	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getRealLoanAmount() {
		return realLoanAmount;
	}
	public void setRealLoanAmount(BigDecimal realLoanAmount) {
		this.realLoanAmount = realLoanAmount;
	}
	public Integer getBorrowTime() {
		return borrowTime;
	}
	public void setBorrowTime(Integer borrowTime) {
		this.borrowTime = borrowTime;
	}
	public BigDecimal getManagerFee() {
		return managerFee;
	}
	public void setManagerFee(BigDecimal managerFee) {
		this.managerFee = managerFee;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	
}
