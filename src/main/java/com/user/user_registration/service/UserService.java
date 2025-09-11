package com.user.user_registration.service;

import com.user.user_registration.models.User;
import com.user.user_registration.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	
	public List<User> getAllUser() {
		return userRepository.findAll();
	}
	
	public User getUser(@PathVariable Long id) {
		return userRepository.findById(id).orElseThrow();
	}
	
	public User createNewUser(@RequestBody User user) {
		 User createUser = new User();
		 
		 createUser.setName(user.getName());
		 createUser.setEmail(user.getEmail());
		 createUser.setAge(user.getAge());
		 
		 return userRepository.save(createUser);
	}
	
	public User updateUser(@RequestBody User user) {
		User getUser = userRepository.findById(user.getId()).orElseThrow();
		User updateddUser = new User();
		
		updateddUser.setId(user.getId());
		updateddUser.setName(user.getName());
		updateddUser.setEmail(user.getEmail());
		updateddUser.setAge(user.getAge());
		
		return userRepository.save(updateddUser);
	}
	
	public User deleteUser(@PathVariable Long id) {
		User getUser = userRepository.findById(id).orElseThrow();
		
		userRepository.delete(getUser);
		
		return getUser;
	}
	
	public UserService() {};
}
