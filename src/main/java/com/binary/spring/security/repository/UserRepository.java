package com.binary.spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.binary.spring.security.entity.AppUser;

public interface UserRepository  extends JpaRepository <AppUser, Integer> {
	
	public AppUser findByUserName(String userName);
	
	public AppUser findByEmail(String email);
	
}
