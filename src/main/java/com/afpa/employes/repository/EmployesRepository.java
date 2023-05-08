package com.afpa.employes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.afpa.employes.bean.Employes;

public interface EmployesRepository extends JpaRepository<Employes, Integer>{

 Employes findByEmail(String email);

}
