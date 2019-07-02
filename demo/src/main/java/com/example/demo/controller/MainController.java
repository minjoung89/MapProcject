package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.dto.History;
import com.example.demo.dto.HistoryResultDto;
import com.example.demo.dto.LoginResultDto;
import com.example.demo.dto.Popular;
import com.example.demo.dto.PopularResultDto;
import com.example.demo.dto.User;
import com.example.demo.service.HisService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/restful/api")
public class MainController {
	@Autowired
	private UserService userService;
	@Autowired
	private HisService hisService;
		
	@PostMapping("/user")
	public ResponseEntity<Void> createUser(@RequestBody User user,UriComponentsBuilder builder) {
		boolean flag = userService.createUser(user);
        if (flag == false) {
   	    return new ResponseEntity<Void>(HttpStatus.CONFLICT);
           }
        //HttpHeaders headers = new HttpHeaders();
		//headers.setLocation(builder.path("index").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResultDto> login(@RequestBody User user,UriComponentsBuilder builder) {
		Optional<User> currentUser = userService.getUser(user.getId());
		LoginResultDto loginResultDto = new LoginResultDto();
		if(currentUser.isPresent()){	//exist
			// 비밀번호 검증
			String inputPswd = user.getPswd();
			String currentPswd = currentUser.get().getPswd();
			
			// 입력 비밀번호 암호화
			String key = "this is password"; //16바이트 길이 제한 
			
			try {
				// Cipher 객체 생성
				Cipher cipher = Cipher.getInstance("AES");
				
				// Cipher 객체 초기화
				SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
				cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
				
				// 암호화
				byte[] encryptPassword = cipher.doFinal(inputPswd.getBytes("UTF-8"));
				System.out.println(new String(encryptPassword));
				
				// 현재비밀번호와 일치하는지 검증
				if(new String(encryptPassword).equals(currentPswd)) { // 일치함
					loginResultDto.setErrYn(false);
					loginResultDto.setErrMsg("");
					return new ResponseEntity<LoginResultDto>(loginResultDto, HttpStatus.OK);
				}
				else { // 일치하지 않음
					loginResultDto.setErrYn(true);
					loginResultDto.setErrMsg("잘못된 비밀번호입니다.");
					return new ResponseEntity<LoginResultDto>(loginResultDto, HttpStatus.OK);
				}
			} catch (Exception e) {
				return new ResponseEntity<LoginResultDto>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else{ // not exist
			loginResultDto.setErrYn(true);
			loginResultDto.setErrMsg("아이디가 존재하지 않습니다.");
			return new ResponseEntity<LoginResultDto>(loginResultDto,HttpStatus.OK);
		}		
	}
	

	@PostMapping("/regHistory")
	public ResponseEntity<Void> createHistory(@RequestBody History history,UriComponentsBuilder builder) {
		System.out.println("+++++++id: "+history.getId()+"/keyword: "+history.getKeyword()+"/srchDt: "+history.getSrchDt());
		boolean flag = hisService.createHistory(history);
        if (flag == false) {
        	return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        //HttpHeaders headers = new HttpHeaders();
		//headers.setLocation(builder.path("index").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	@GetMapping("/history/{id}")
	public ResponseEntity<HistoryResultDto> getHistoryListById(@PathVariable String id) {
		System.out.println("+++++++id: "+id);
		List<History> historyList = hisService.getHistoryListById(id);
		int totalcount = 0;
		HistoryResultDto historyResultDto = new HistoryResultDto();
		if(historyList != null && historyList.size() > 0){	// exist
			totalcount=historyList.size();
			historyResultDto.setHistoryList(historyList);			
		}	
		historyResultDto.setTotalcount(totalcount);
		
		return new ResponseEntity<HistoryResultDto>(historyResultDto, HttpStatus.OK);
	}
	
	@GetMapping("/popular")
	public ResponseEntity<PopularResultDto> getPopularList() {
		List<Map<String, Object>> popularMapList = hisService.getPopularList();
		PopularResultDto popularResultDto = new PopularResultDto();
		int totalcount = 0;
		if(popularMapList != null && popularMapList.size() > 0){	// exist
			totalcount=popularMapList.size();
			List<Popular> popularList = new ArrayList<Popular>();
			for(Map<String, Object> popularMap : popularMapList) {
				Popular popular = new Popular();
				popular.setKeyword(popularMap.get("KEYWORD").toString());
				popular.setCount(Integer.parseInt(popularMap.get("COUNT").toString()));
				popularList.add(popular);
			}
			popularResultDto.setPopularList(popularList);
		}	
		popularResultDto.setTotalcount(totalcount);
		
		return new ResponseEntity<PopularResultDto>(popularResultDto, HttpStatus.OK);
	}
	
}
