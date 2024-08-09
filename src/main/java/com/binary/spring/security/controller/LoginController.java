package com.binary.spring.security.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.binary.spring.security.dto.SignInRequest;
import com.binary.spring.security.dto.SignUpRequest;
import com.binary.spring.security.entity.AppUser;
import com.binary.spring.security.repository.UserRepository;
import com.binary.spring.security.service.UserServiceImpl;
import com.binary.spring.security.utility.UtilityHelper;

import jakarta.validation.Valid;

@RestController
public class LoginController {

	@Autowired
	UserRepository repository;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserServiceImpl service;

	@Autowired
	UtilityHelper util;

	/*
	 * @Value("${security.jwt.issuer}") private String jwtIssuer;
	 * 
	 * 
	 * @Value("${security.jwt.secret-key}") private String secretKey;
	 */

	@GetMapping("/message")
	public String message() {
		return "Hello , This is from messsage method";
	}

	@PostMapping("/signup")
	public ResponseEntity<Object> signUp(@Valid @RequestBody SignUpRequest request, BindingResult result) {

		try {
			if (result.hasErrors()) {
				var errorList = result.getAllErrors();
				var errorMap = new HashMap<String, String>();

				for (int i = 0; i < errorList.size(); i++) {
					var error = errorList.get(i);
					errorMap.put(error.getCode(), error.getDefaultMessage());
				}
				return ResponseEntity.badRequest().body(errorMap);

			}

			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			AppUser user = new AppUser();
			user.setFirstName(request.getFirstName());
			user.setLastName(request.getLastName());
			user.setUserName(request.getUserName());
			user.setEmail(request.getEmail());
			user.setPhone(request.getPhone());
			user.setRole(request.getRole());
			user.setAddress(request.getAddress());
			user.setCreatedAt(new Date());
			user.setPassword(encoder.encode(request.getPassword()));

			AppUser otherUser = repository.findByUserName(request.getUserName());

			if (otherUser != null) {
				return ResponseEntity.badRequest().body("User name already exist !");
			}

			AppUser user2 = repository.findByUserName(request.getUserName());

			if (user2 != null) {
				return ResponseEntity.badRequest().body("User with this Email already exist !");
			}
			// Saving user into database
			repository.save(user);

			String jwtToken = util.createJwToken(user);
			// Creating response object to send back the response to client
			Map<String, Object> response = new HashMap<String, Object>();
			response.put("token", jwtToken);
			response.put("user", user);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			System.out.println("Exception Occured !!");
			e.printStackTrace();
		} finally {
			System.out.println("Inside finally block of signup method");
		}
		return ResponseEntity.badRequest().body("Soemthing went wrong !!");

	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@Valid @RequestBody SignInRequest signIn, BindingResult result) {
		
		try {
			if(result.hasErrors()) {
				var errorList= result.getAllErrors();
				Map<String, String> errorMap = new HashMap<String, String>();
	
				for(int i =0; i<errorList.size(); i++) {
					var error = errorList.get(i);
					errorMap.put(error.getCode(), error.getDefaultMessage());
				}
				return ResponseEntity.badRequest().body(errorMap);
			}
		}catch(Exception e) {
			System.out.println("Exception occured");
			e.printStackTrace();
		}
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(signIn.getUserName(), signIn.getPassword()));

			AppUser user=repository.findByUserName(signIn.getUserName());

			String jwtToken= util.createJwToken(user);

			Map<String, Object> response = new HashMap<String, Object>();
			response.put("token", jwtToken);
			response.put("user", user);

			return ResponseEntity.ok(response);	

		}catch(Exception e) {
			System.out.println("Exception occurred !!");
			e.printStackTrace();
		}
		return ResponseEntity.badRequest().body("Bad Username or password");

	}
	
	@GetMapping("/profile")
	public ResponseEntity<Object> profile(Authentication auth) {
		
		var response = new HashMap<String, Object>();
		response.put("UserName", auth.getName());
		response.put("Authorities", auth.getAuthorities());
		
		AppUser user = repository.findByUserName(auth.getName());
		response.put("User", user);
		
		return ResponseEntity.ok(response);

	}
	
}
