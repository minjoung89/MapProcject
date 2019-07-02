package com.example.demo.controller;

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

import com.example.demo.dto.LoginResultDto;
import com.example.demo.dto.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/restful/api")
public class MainController {
	@Autowired
	private UserService userService;
		
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
	
	@GetMapping("/user/{id}")
	public ResponseEntity<User> userList(@PathVariable String id) {
		System.out.println("+++++++id: "+id);
		Optional<User> user = userService.getUser(id); 
		
		if(user.isPresent()){	//exist
			return new ResponseEntity<User>(user.get(), HttpStatus.OK);
		}
		else{
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}		
	}
	

	@PostMapping("/login")
	public ResponseEntity<LoginResultDto> login(@RequestBody User user,UriComponentsBuilder builder) {
		System.out.println("++++++++id: "+user.getId()+"/ pswd : "+user.getPswd());
		Optional<User> currentUser = userService.getUser(user.getId());
		LoginResultDto loginResultDto = new LoginResultDto();
		if(currentUser.isPresent()){	//exist
			// 비밀번호 검증
			String inputPswd = user.getPswd();
			String currentPswd = currentUser.get().getPswd();
			
			// 입력 비밀번호 암호화
			String key = "this is password"; //16바이트 길이 제한 있음...
			
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
	
}
