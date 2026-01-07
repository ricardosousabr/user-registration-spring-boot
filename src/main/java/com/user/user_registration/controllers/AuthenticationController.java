package com.user.user_registration.controllers;

import com.user.user_registration.models.*;
import com.user.user_registration.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	@Autowired
	AuthenticationService authenticationService;
	
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
		return authenticationService.login(data);
	}
	
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
		authenticationService.registerUser(data);
		
		return ResponseEntity.status(201).body("Usuário registrado com sucesso");
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity update(@PathVariable(required = false) Long id , @RequestBody UpdateDTO data) {
		authenticationService.updateUser(id, data);
		
		return ResponseEntity.ok("Usuário atualizado com sucesso");
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity delete(@PathVariable(required = false) Long id) {
		authenticationService.deleteUser(id);
		
		return ResponseEntity.ok("usuário deletado com sucesso");
	}
}
