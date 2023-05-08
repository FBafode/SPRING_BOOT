package com.afpa.employes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestParam;

import com.afpa.employes.bean.Employes;
import com.afpa.employes.repository.EmployesRepository;

@Service
public class EmployesService {
	
@Autowired
private EmployesRepository employesRepository;
	
	
public Employes addEmploye(Employes employes, BindingResult bindingresult, @RequestParam("id") Optional<Long> id){
	
	//verification si le mail exist déjà
	if(id.isPresent()) {
		System.out.println("POST -- -- "+ id);
		
		employesRepository.save(employes);
		
	}else if (employesRepository.findByEmail(employes.getEmail()) != null) {
		
		employes  = employesRepository.findByEmail(employes.getEmail());
		
    	bindingresult.addError(new FieldError("employes","email","L''email ("+employes.getEmail()+") existe deja ..."));
		
	}else {
		
		employesRepository.save(employes);
	}
		
	return employes;
		
}
	
	
//Listing des employes
public List<Employes> listEmployes(){
	
	return employesRepository.findAll();
}
	
	
//show 
public Optional<Employes> getEmployeIfExist(int id){
	
	return employesRepository.findById(id);
}

//delete
public boolean delete(int id) {
	
	Optional<Employes> employe =employesRepository.findById(id);
	
	if(employe.isPresent()) {
		
		employesRepository.delete(employe.get());
		System.out.println("deleted " );
		return true;
	}
	return false;
	
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
