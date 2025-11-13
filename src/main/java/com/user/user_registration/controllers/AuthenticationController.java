package com.user.user_registration.controllers;

import com.user.user_registration.configs.TokenService;
import com.user.user_registration.models.AuthenticationDTO;
import com.user.user_registration.models.LoginResponseDTO;
import com.user.user_registration.models.RegisterDTO;
import com.user.user_registration.models.User;
import com.user.user_registration.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
	 var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
	 var auth = this.authenticationManager.authenticate(usernamePassword);
	 
	 var token = tokenService.generateToken((User) auth.getPrincipal());
	 
	 
	 return ResponseEntity.ok(new LoginResponseDTO(token));
	}
	
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
		if(this.userRepository.findByEmail(data.login()) != null) return ResponseEntity.badRequest().build();
		
		String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
		User newUser = new User(data.name(), data.age(), data.login(), encryptedPassword, data.role());
		
		this.userRepository.save(newUser);
		
		return ResponseEntity.ok().build();
	}
}
