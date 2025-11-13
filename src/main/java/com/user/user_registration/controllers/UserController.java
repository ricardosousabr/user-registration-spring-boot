package com.user.user_registration.controllers;

import com.user.user_registration.models.User;
import com.user.user_registration.repositories.UserRepository;
import com.user.user_registration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> getAllUser() {
		return userService.getAllUser();
	} // ADMIN
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public User getUser(@PathVariable Long id){
		return userService.getUser(id);
	}
	
//	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public User createNewUser(@RequestBody User user) {
//		return userService.createNewUser(user);
//	}
	
	@PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public User updateUser(@PathVariable Long id, @RequestBody User user) { return userService.updateUser(id, user); }
	
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public User deleteUser(@PathVariable Long id) {
		return userService.deleteUser(id);
	}
	
	@PatchMapping(value = "/{id}")
	public User patchUser(@PathVariable Long id, @RequestBody User user) {
		return userService.patchUser(id, user);
	}
}
