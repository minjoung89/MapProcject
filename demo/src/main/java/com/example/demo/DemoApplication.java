package com.example.demo;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.dto.User;
import com.example.demo.service.UserService;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner{

	@Autowired
	private UserService userService;
		
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception{
		System.out.println("dddddddddddddddddddd");
		// user ����
		User user = new User();
		user.setId("test");
		user.setName("�׽�Ʈ����");
		// ��ȣȭ�� �򹮰� Salt�� ����
		String password = "123456";
		String key = "this is password"; //16����Ʈ ���� ���� ����...
		
		try {
			// Cipher ��ü ����
			Cipher cipher = Cipher.getInstance("AES");
			
			// Cipher ��ü �ʱ�ȭ
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			
			// ��ȣȭ
			byte[] encryptPassword = cipher.doFinal(password.getBytes("UTF-8"));
			System.out.println(new String(encryptPassword));

			user.setPswd(new String(encryptPassword));
			
			userService.createUser(user);
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

}
