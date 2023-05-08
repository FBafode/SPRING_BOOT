package com.afpa.employes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.afpa.employes.bean.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByEmail(String Email);

	

}
