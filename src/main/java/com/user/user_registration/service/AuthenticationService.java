package com.user.user_registration.service;

import com.user.user_registration.configs.TokenService;
import com.user.user_registration.models.User;
import com.user.user_registration.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import com.user.user_registration.models.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	
	public ResponseEntity login(AuthenticationDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
		var auth = this.authenticationManager.authenticate(usernamePassword);
		
		var token = tokenService.generateToken((User) auth.getPrincipal());
		
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}
	
	public void registerUser(RegisterDTO data) {
		if(this.userRepository.findByEmail(data.email()).isPresent()) {
			throw new RuntimeException("Email já registrado");
		}
		
		String encryptedPassword = passwordEncoder.encode(data.password());
		User newUser = new User(data.name(), data.age(), data.email(), encryptedPassword, data.role());
		userRepository.save(newUser);
	}
	
	public void updateUser(UpdateDTO data) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		String encryptedPassword = passwordEncoder.encode(data.password());
		
		if(data.name() != null) currentUser.setName(data.name());
		if(data.age() != null) currentUser.setAge(data.age());
		if(data.password() != null) currentUser.setPassword(encryptedPassword);
		if(data.login() != null && !data.login().equals(currentUser.getEmail())) currentUser.setEmail(data.login());
		
		userRepository.save(currentUser);
	}
	
	public void deleteUser(Long id) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		
		if (!id.equals(currentUser.getId())) {
			throw  new RuntimeException("Você não tem permissão para deletar este usuário");
		}
		
		userRepository.delete(currentUser);
	}
}
