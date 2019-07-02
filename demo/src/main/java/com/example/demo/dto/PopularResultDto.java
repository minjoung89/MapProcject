package com.example.demo.dto;

import java.util.List;

public class PopularResultDto {
	private List<Popular> popularList;
	private int totalcount;
	public List<Popular> getPopularList() {
		return popularList;
	}
	public void setPopularList(List<Popular> popularList) {
		this.popularList = popularList;
	}
	public int getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}
	
}
