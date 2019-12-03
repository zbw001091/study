package com.zbw.big.study.dao;

import java.util.List;

public class NestedObject {
	private String group;
	private List<User> userlist;
	
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public List<User> getUserlist() {
		return userlist;
	}
	public void setUserlist(List<User> userlist) {
		this.userlist = userlist;
	}
}
