package com.binary.spring.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.binary.spring.security.entity.AppUser;
import com.binary.spring.security.repository.UserRepository;


@Service
public class UserServiceImpl implements UserDetailsService{

	@Autowired
	UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		AppUser user = repository.findByUserName(username);
		
		if(user!=null) {
			UserDetails myUser = User.withUsername(username).password(user.getPassword()).roles(user.getRole()).build();
			System.out.println("Inside loadByUsername method");
			return myUser;
		}
		throw new UsernameNotFoundException("User name not found");
			
	}
	
}
