package com.afpa.employes.controller;

import java.util.Optional;

import javax.management.AttributeNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.afpa.employes.bean.Employes;
import com.afpa.employes.service.EmployesService;

@Controller
public class ShowEmployesController {
	
	@Autowired
	private EmployesService emplService;
	
	
	
	  @GetMapping("/show/{id}") 
	  public String show(@PathVariable(value="id") int id, Model model , Employes employes) {
	  
	  Optional<Employes> getEmployesFounded = emplService.getEmployeIfExist(id);
	  
	  if(getEmployesFounded.isPresent()) {
	  
	  model.addAttribute("employes", getEmployesFounded.get());
	  return "/showEmploye";
	  }
	  
	  return "showEmploye"; 
	  }
	 
	@PostMapping("/show/{id}")
    public String update(@PathVariable(value = "id") int id, @Validated Employes employeDetails) throws AttributeNotFoundException {
		/*
        * La ligne ci-dessous joue un role essentiel. 
        * Il check si l'id, il le reoturne dans la variable sinon il léve une exception
        */
		Employes employe = emplService.getEmployeIfExist(id)
       .orElseThrow(() -> new AttributeNotFoundException("Aucun employé avec l'id :: " + id));

		employe.setNom(employeDetails.getNom());
		employe.setPrenom(employeDetails.getPrenom());
		employe.setEmail(employeDetails.getEmail());
        
		/* emplService.addEmploye(employe, bindingResult); */
        
        return "showEmploye";
    }



}
