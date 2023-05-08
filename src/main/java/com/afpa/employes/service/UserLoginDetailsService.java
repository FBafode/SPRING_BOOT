package com.afpa.employes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.afpa.employes.bean.User;
import com.afpa.employes.bean.UserLogin;
import com.afpa.employes.repository.UserRepository;

public class UserLoginDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository .findByEmail(username);
		if(user == null) {
			
			throw new UsernameNotFoundException("User not found");
			
		}
		
		return new UserLogin(user);
	}

}
