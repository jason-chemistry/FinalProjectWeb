package com.djn.contact.model;

public class ContactsModel {
	private String name;
	private String phoneNumber;
	String company;
	String location;
	String email;

	@Override
	public String toString() {
		return "ContactsModel{" +
				"name='" + name + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", company='" + company + '\'' +
				", location='" + location + '\'' +
				", email='" + email + '\'' +
				'}';
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ContactsModel() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
