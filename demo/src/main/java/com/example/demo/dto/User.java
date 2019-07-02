package com.example.demo.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")  //table name
public class User {
	@Id 
	private String id;
	@Column(length=100, nullable=false) // varchar(100) not null
	private String name;
	@Column(length=100, nullable=false) // varchar(100) not null
	private String pswd;
	public User(){}
	
	public User(String id, String name, String pswd){
		this.id = id;
		this.name = name;
		this.pswd = pswd;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPswd() {
		return pswd;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}
    
	
}
