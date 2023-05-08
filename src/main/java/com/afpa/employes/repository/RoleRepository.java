package com.afpa.employes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.afpa.employes.bean.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
