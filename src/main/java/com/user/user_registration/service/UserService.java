package com.user.user_registration.service;

import com.user.user_registration.models.User;
import com.user.user_registration.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	
	public List<User> getAllUser() {
		return userRepository.findAll();
	}
	
	public User getUser(Long id) {
		return userRepository.findById(id).orElseThrow();
	}
	
	public User createNewUser(User user) {
		 
		 return userRepository.save(user);
	}
	
	public User updateUser(Long id, User user) {
		User getUser = userRepository.findById(id).orElseThrow();
		
		getUser
				.setName(user.getName())
				.setAge(user.getAge())
				.setEmail(user.getEmail());

		return userRepository.save(getUser);
	}
	
	public User deleteUser(Long id) {
		User getUser = userRepository.findById(id).orElseThrow();
		
		userRepository.delete(getUser);
		
		return getUser;
	}
	
	public User patchUser(Long id, User user) {
		User existingUser = userRepository.findById(id).orElseThrow();
		
		if (user.getName() != null) {
			existingUser.setName(user.getName());
		}
		if (user.getAge() != null) {
			existingUser.setAge(user.getAge());
		}
		if (user.getEmail() != null) {
			existingUser.setEmail(user.getEmail());
		}
		
		return userRepository.save(existingUser);
		
	}
	
	public UserService() {};
}
