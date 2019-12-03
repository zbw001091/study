package com.zbw.big.study.dao;

public class Balance {
	private int BalanceNo;
	private String Name;
	private String description;
	private String LabourName;
	private String PartName;
	
	public int getBalanceNo() {
		return BalanceNo;
	}
	public void setBalanceNo(int balanceNo) {
		BalanceNo = balanceNo;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLabourName() {
		return LabourName;
	}
	public void setLabourName(String labourName) {
		LabourName = labourName;
	}
	public String getPartName() {
		return PartName;
	}
	public void setPartName(String partName) {
		PartName = partName;
	}
}
