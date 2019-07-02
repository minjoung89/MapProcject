package com.example.demo.dto;

public class Popular {
	private String keyword;
	private int count;
	
	public Popular(){}
	
	public Popular(String keyword, int count){
		this.keyword = keyword;
		this.count = count;
	}
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
