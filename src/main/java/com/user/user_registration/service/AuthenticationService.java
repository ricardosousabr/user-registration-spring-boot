package com.user.user_registration.service;

import com.auth0.jwt.JWT;
import com.user.user_registration.configs.TokenService;
import com.user.user_registration.models.User;
import com.user.user_registration.repositories.UserRepository;
import jakarta.websocket.Decoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import com.user.user_registration.models.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

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
	
	@Autowired
	UserAuthenticated userAuthenticated;
	
	
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
	
	public void updateUser(Long id, UpdateDTO data) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long currentId = (Long) authentication.getPrincipal();
		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
		User target = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		
		if (isAdmin || target.getId().equals(currentId)) {
			if(data.name() != null) target.setName(data.name());
			if(data.age() != null) target.setAge(data.age());
			if(data.password() != null) target.setPassword(passwordEncoder.encode(data.password()));
			if(data.login() != null && !data.login().equals(target.getEmail())) target.setEmail(data.login());
		} else {
			throw new RuntimeException("Você não tem permissão para atualizar esse este usuário");
		}
		
		userRepository.save(target);
	}
	
	public ResponseEntity deleteUser(Long id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long currentId = (Long) authentication.getPrincipal();
		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
		User target = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		
		if (isAdmin || target.getId().equals(currentId)) {
			userRepository.delete(target);
			return ResponseEntity.ok("Usuário deletado com sucesso");
		} else {
			throw  new RuntimeException("Você não tem permissão para deletar este usuário");
		}
	}
}
