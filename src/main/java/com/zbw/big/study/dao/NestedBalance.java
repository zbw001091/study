package com.zbw.big.study.dao;

import java.util.List;

public class NestedBalance {
	private int BalanceNo;
	private String Name;
	private String description;
	private List<NestedLabour> labours;
	
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
	public List<NestedLabour> getLabours() {
		return labours;
	}
	public void setLabours(List<NestedLabour> labours) {
		this.labours = labours;
	}
}
