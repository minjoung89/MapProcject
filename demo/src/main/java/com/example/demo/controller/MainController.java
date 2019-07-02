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
			// ��й�ȣ ����
			String inputPswd = user.getPswd();
			String currentPswd = currentUser.get().getPswd();
			
			// �Է� ��й�ȣ ��ȣȭ
			String key = "this is password"; //16����Ʈ ���� ���� ����...
			
			try {
				// Cipher ��ü ����
				Cipher cipher = Cipher.getInstance("AES");
				
				// Cipher ��ü �ʱ�ȭ
				SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
				cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
				
				// ��ȣȭ
				byte[] encryptPassword = cipher.doFinal(inputPswd.getBytes("UTF-8"));
				System.out.println(new String(encryptPassword));
				
				// �����й�ȣ�� ��ġ�ϴ��� ����
				if(new String(encryptPassword).equals(currentPswd)) { // ��ġ��
					loginResultDto.setErrYn(false);
					loginResultDto.setErrMsg("");
					return new ResponseEntity<LoginResultDto>(loginResultDto, HttpStatus.OK);
				}
				else { // ��ġ���� ����
					loginResultDto.setErrYn(true);
					loginResultDto.setErrMsg("�߸��� ��й�ȣ�Դϴ�.");
					return new ResponseEntity<LoginResultDto>(loginResultDto, HttpStatus.OK);
				}
			} catch (Exception e) {
				return new ResponseEntity<LoginResultDto>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else{ // not exist
			loginResultDto.setErrYn(true);
			loginResultDto.setErrMsg("���̵� �������� �ʽ��ϴ�.");
			return new ResponseEntity<LoginResultDto>(loginResultDto,HttpStatus.OK);
		}		
	}
	
}
