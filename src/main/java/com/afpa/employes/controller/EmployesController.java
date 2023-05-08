package com.afpa.employes.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.afpa.employes.bean.Employes;
import com.afpa.employes.repository.EmployesRepository;
import com.afpa.employes.service.EmployesService;


@Controller
public class EmployesController {
	
	@Autowired
	private EmployesService employesService;
	
	@Autowired
	private EmployesRepository employesRepository;
	
	
@GetMapping("/add")
public String AffichageList(Model model, Employes employes) {
	
	getEmployesList(model);
	
	return "employes"; //(renvoi sur la page employes)
	
}


@GetMapping("/add/{id}") 
public String show(@PathVariable(value="id") int id, Model model , Employes employes) {

Optional<Employes> getEmployesFounded = employesService.getEmployeIfExist(id);

if(getEmployesFounded.isPresent()) {

model.addAttribute("employe", getEmployesFounded.get());

return "employes"; 
}

return "redirect:/add";
}

//Traitement du formulaire home employes
@PostMapping("/add")
public String employes(@Validated Employes employes, BindingResult bindingResult, Model model, @RequestParam("id") Optional<Long> id) {
	System.out.println("DEHORS ");
	
	if(bindingResult.hasErrors()) {
		System.out.println(bindingResult.getErrorCount());
		getEmployesList(model);
		
		System.out.println("bbbbbbbbbbbbbbbbbbbbb ");
		return "employes";
		
	}else if (employesService.addEmploye(employes, bindingResult, id) != null) {
		
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa ");
		getEmployesList(model);
		
    	return "employes";
		
	}else {
		System.out.println("aqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq ");
		
		employesService.addEmploye(employes, bindingResult, id);
		
		model.addAttribute("msg",employes);
		
		return "redirect:/add";
	}
	
}


//SUPRESSION EMPLOYE
@GetMapping("/delete/{id}") 
public String delete(@PathVariable(value="id") int employeID, Model model) {

	if (employesService.delete(employeID)) {
		model.addAttribute("msg","success");
		
	} else {
		model.addAttribute("msg","echec");
	}
	return "redirect:/";
}



//METHODE UTLISANT LE SERVICE POUR RECUPERER LA LISTE

public void getEmployesList (Model model) {
	
	if (employesService.listEmployes().size() > 0) {
		
		model.addAttribute("employesList", employesService.listEmployes()); //(employesList est le contenu))
	
		 
	}
	
}






}
