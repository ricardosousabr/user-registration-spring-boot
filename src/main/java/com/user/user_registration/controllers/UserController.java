package com.user.user_registration.controllers;

import com.user.user_registration.models.User;
import com.user.user_registration.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserRepository userRepository;
	
	@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> getAllUser() {
		return userRepository.findAll();
	}
	
	@PostMapping(value = "/createUser", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public User createNewUser(@RequestBody User user) {
		User createUser = new User();
		
		createUser.setName(user.getName());
		createUser.setAge(user.getAge());
		createUser.setEmail(user.getEmail());
		
		return userRepository.save(createUser);
	}
	
	@PutMapping(value = "/updateUser", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public User updateUser(@RequestBody User user) {
		User getUser = userRepository.findById(user.getId()).orElseThrow();
		
		User updatedUser = new User();
		
		updatedUser.setId(user.getId());
		updatedUser.setName(user.getName());
		updatedUser.setEmail(user.getEmail());
		updatedUser.setAge(user.getAge());
		
		return  userRepository.save(updatedUser);
	}
	
	@DeleteMapping(value = "/deleteUser/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public User deleteUser(@PathVariable Long id) {
		User getUser = userRepository.findById(id).orElseThrow();
		
		userRepository.delete(getUser);
		return getUser;
	}
}
