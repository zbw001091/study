package com.zbw.big.study.dao;

import java.util.List;

public class NestedLabour {
	private int LabourNo;
	private String LabourName;
	private String LabourDesc;
	private List<NestedPart> parts;
	
	public int getLabourNo() {
		return LabourNo;
	}
	public void setLabourNo(int labourNo) {
		LabourNo = labourNo;
	}
	public String getLabourName() {
		return LabourName;
	}
	public void setLabourName(String labourName) {
		LabourName = labourName;
	}
	public String getLabourDesc() {
		return LabourDesc;
	}
	public void setLabourDesc(String labourDesc) {
		LabourDesc = labourDesc;
	}
	public List<NestedPart> getParts() {
		return parts;
	}
	public void setParts(List<NestedPart> parts) {
		this.parts = parts;
	}
}
