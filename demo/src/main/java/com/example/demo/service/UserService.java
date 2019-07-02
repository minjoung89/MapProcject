package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public boolean createUser(User user){
		try{
			userRepository.save(user);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	public Optional<User> getUser(String id){
		return userRepository.findById(id);
	}
	
	public User updateUser(String id, User user){
		final Optional<User> fetchedUser = userRepository.findById(id);
		if(fetchedUser.isPresent()){
			user.setId(id);
			return userRepository.save(user);
		}
		else{
			return null;
		}
	}
	
}
