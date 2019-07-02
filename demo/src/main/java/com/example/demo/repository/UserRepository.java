package com.example.demo.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.dto.User;

@Repository
public interface UserRepository  extends JpaRepository<User, String> {
 
}
