package com.user.user_registration.controllers;

import com.user.user_registration.configs.TokenService;
import com.user.user_registration.models.*;
import com.user.user_registration.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
		if(this.userRepository.findByEmail(data.login()).isPresent()) return ResponseEntity.badRequest().build();
		
		String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
		User newUser = new User(data.name(), data.age(), data.login(), encryptedPassword, data.role());
		
		this.userRepository.save(newUser);
		
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/update")
	public ResponseEntity update(@RequestBody UpdateDTO data) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
		
		if(data.name() != null) user.setName(data.name());
		if(data.age() != null) user.setAge(data.age());
		if(data.password() != null) user.setPassword(encryptedPassword);
		if(data.login() != null && !data.login().equals(currentUser.getEmail())) user.setEmail(data.login());
		
		userRepository.save(user);
		
		return ResponseEntity.ok(currentUser);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity delete(@RequestBody DeleteDTO data) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new RuntimeException("Usaário não encontrado"));
		String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
		
		if (!Objects.equals(encryptedPassword, currentUser.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
		}
		
		userRepository.delete(user);
		return ResponseEntity.ok("Usuario deletado com sucesso");
	}
}
