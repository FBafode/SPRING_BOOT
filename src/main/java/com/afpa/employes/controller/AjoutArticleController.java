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

import com.afpa.employes.bean.Article;
import com.afpa.employes.service.ArticleService;

@Controller
public class AjoutArticleController {
	
	
	
	@Autowired
	private ArticleService articleService;
	
	
	@GetMapping("/viewp")
	public  String viewp(Model model, Article article, @RequestParam(value= "id", required=false) Long id) {
		
		Optional<Article> getArticleFounded = articleService.getArticleIfExist(id);
		
		if(getArticleFounded.isPresent()) {
			model.addAttribute("article",getArticleFounded.get());
			
			return "article/viewArticle";
		}
		return "article/ajoutArticle";
	}
	
	//Traitement du formulaire home article
		@PostMapping("/viewp")
		public String viewp(@Validated Article article, BindingResult bindingResult, Model model, @RequestParam("id") Optional<Long> id) {
			System.out.println("DEHORS ");
			
			if(bindingResult.hasErrors()) {
				System.out.println(bindingResult.getErrorCount());
				
				
				System.out.println("bbbbbbbbbbbbbbbbbbbbb ");
				return "/article/ajoutArticle";
				
			}else if (articleService.ajoutArticle(article, bindingResult, id) != null) {
				
				System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa ");
				
				
		    	return "/article/article";
				
			}else {
				System.out.println("aqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq ");
				
				articleService.ajoutArticle(article, bindingResult, id);
				
				model.addAttribute("msg",article);
				
				return "redirect:/artic";
			}
		}

}
