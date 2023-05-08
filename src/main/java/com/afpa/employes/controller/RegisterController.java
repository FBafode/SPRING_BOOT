package com.afpa.employes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.afpa.employes.bean.User;
import com.afpa.employes.repository.RoleRepository;
import com.afpa.employes.service.UserService;

@Controller
public class RegisterController {
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	RoleRepository roleRepository;
	
	@GetMapping("/register")
	public String register(Model model ) {
		
		model.addAttribute("user", new User());
		model.addAttribute("roles", roleRepository.findAll());
		
		return ("user/register");
	}
	
	@PostMapping("/register")
	public String register(@Validated User user, BindingResult bindingResult, Model model) {
		
		if(bindingResult.hasErrors()) {
			
			return ("user/register");
			
		}else if((userService.getByEmail(user.getEmail()) != null)) {
			
		     bindingResult.addError(new FieldError("user","email","Le mail existe deja !"));
	            return ("/register");
		}
		userService.createUser(user);
		
		return "user/register";
	}
}
