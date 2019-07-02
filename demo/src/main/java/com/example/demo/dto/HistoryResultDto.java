package com.example.demo.dto;

import java.util.List;

public class HistoryResultDto {
	private List<History> historyList;
	private int totalcount;
	public List<History> getHistoryList() {
		return historyList;
	}
	public void setHistoryList(List<History> historyList) {
		this.historyList = historyList;
	}
	public int getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}
}
