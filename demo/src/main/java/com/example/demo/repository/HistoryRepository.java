package com.example.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.History;

@Repository
public interface HistoryRepository  extends JpaRepository<History, Long>{
	@Query(value = "SELECT b.seq, b.id, b.keyword, b.srch_dt FROM History b where b.id = ?1 ORDER BY b.srch_dt DESC limit 10"
			,nativeQuery =true)
	List<History> findListById(String id); 
	
	@Query(value = "SELECT b.keyword as keyword, count(1) as count, MAX(b.srch_dt) as maxDt FROM History b GROUP BY b.keyword ORDER BY COUNT(1) DESC, MAX(b.srch_dt) DESC limit 10"
			,nativeQuery =true)
	List<Map<String, Object>> findPopularList(); 
	
}
