package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.History;
import com.example.demo.dto.Popular;
import com.example.demo.repository.HisRepository;


@Service
public class HisService {
	@Autowired
	private HisRepository hisRepository;
	
	public boolean createHistory(History history){
		try{
			hisRepository.save(history);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	public List<History> getHistoryListById(String id){

	    return hisRepository.findListById(id);
	    
	}
	

	public List<Map<String, Object>> getPopularList(){

	    return hisRepository.findPopularList();
	    
	}
}
