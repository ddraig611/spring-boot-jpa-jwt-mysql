package com.ddraig.jwtMysql.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddraig.jwtMysql.entity.User;
import com.ddraig.jwtMysql.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public Optional<User> findByUsernameOrEmail(String username, String email) {
		return userRepository.findByUsernameOrEmail(username, email);
	}
	
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public List<User> findByIdIn(List<Long> userIds) {
		return userRepository.findByIdIn(userIds);
	}
	
	public Boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	
	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	public User CreateUser(User user) {
		return userRepository.save(user);
	}
}
