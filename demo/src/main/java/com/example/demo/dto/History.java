package com.example.demo.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="history")  //table name
public class History {	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long seq;
	@Column(length=100, nullable=false) // varchar(100) not null
	private String id;
	@Column(length=100, nullable=false) // varchar(100) not null
	private String keyword;
	@Column(length=100, nullable=false) // varchar(100) not null
	private Date srchDt;
	
	public History(){}
	
	public History(String id, String keyword, Date srchDt){
		this.id = id;
		this.keyword = keyword;
		this.srchDt = srchDt;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Date getSrchDt() {
		return srchDt;
	}

	public void setSrchDt(Date srchDt) {
		this.srchDt = srchDt;
	}
	

}
