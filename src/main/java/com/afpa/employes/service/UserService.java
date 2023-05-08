package com.afpa.employes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.afpa.employes.bean.User;
import com.afpa.employes.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public boolean createUser (User user) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		user.setPassword(encoder.encode(user.getPassword()));
		
		userRepository.save(user);
		
		return true;
		
	}

	public User getByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}

}
