package com.zbw.big.study.es.dao;

public class BankAccount {

	private int account_number;
	private int balance;
	private String firstname;
	private String lastname;
	private int age_sortagg;
	private String gender;
	private String address;
	private String employer;
	private String email;
	private String city;
	private String state;
	private int age_nosortagg;
	private String city_fielddata;
	
	public int getAge_sortagg() {
		return age_sortagg;
	}
	public void setAge_sortagg(int age_sortagg) {
		this.age_sortagg = age_sortagg;
	}
	public int getAge_nosortagg() {
		return age_nosortagg;
	}
	public void setAge_nosortagg(int age_nosortagg) {
		this.age_nosortagg = age_nosortagg;
	}
	public String getCity_fielddata() {
		return city_fielddata;
	}
	public void setCity_fielddata(String city_fielddata) {
		this.city_fielddata = city_fielddata;
	}
	public int getAccount_number() {
		return account_number;
	}
	public void setAccount_number(int account_number) {
		this.account_number = account_number;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmployer() {
		return employer;
	}
	public void setEmployer(String employer) {
		this.employer = employer;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
