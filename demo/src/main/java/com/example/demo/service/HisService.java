package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.History;
import com.example.demo.repository.HistoryRepository;


@Service
public class HisService {
	@Autowired
	private HistoryRepository historyRepository;
	
	public boolean createHistory(History history){
		try{
			historyRepository.save(history);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	public List<History> getHistoryListById(String id){

	    return historyRepository.findListById(id);
	    
	}
	

	public List<Map<String, Object>> getPopularList(){

	    return historyRepository.findPopularList();
	    
	}
}
