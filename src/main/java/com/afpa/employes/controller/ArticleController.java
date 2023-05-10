package com.afpa.employes.controller;

import java.io.IOException;
import java.util.Optional;

import javax.management.AttributeNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.afpa.employes.bean.Article;
import com.afpa.employes.bean.User;
import com.afpa.employes.bean.UserLogin;
import com.afpa.employes.service.ArticleService;
import com.afpa.employes.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ArticleController {
	
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private UserService userService;
	
		/*****************************************************************************************************
		 * 							TRAITEMENT DE LA PAGE LISTING DES ARTICLES
		 * *****************************************************************************************************/
	@GetMapping("/artic")
	public String showListArticles(Model model, Article article, @RequestParam(value= "id", required=false) Long id) {
		
		if(id != null) {
			
			Optional<Article> arti = articleService.getArticleIfExist(id);
			if(arti.isPresent()) {
				
				model.addAttribute("articles", arti.get());
				
			}
		}
		getArticleList(model);
		
		
		return "/article/article"; //(renvoi sur la page article)
		
	}
	

	//Traitement du formulaire home article
	/*
	 * @PostMapping("/artic") public String article(@Validated Article article,
	 * BindingResult bindingResult, Model model, @RequestParam("id") Optional<Long>
	 * id) {
	 * 
	 * 
	 * if(bindingResult.hasErrors()) {
	 * 
	 * getArticleList(model);
	 * 
	 * 
	 * return "/article/article";
	 * 
	 * }else if (articleService.ajoutArticle(article, bindingResult, id) != null) {
	 * 
	 * 
	 * getArticleList(model);
	 * 
	 * return "/article/article";
	 * 
	 * }else {
	 * 
	 * articleService.ajoutArticle(article, bindingResult, id);
	 * 
	 * model.addAttribute("msg",article);
	 * 
	 * return "redirect:/artic"; }
	 * 
	 * }
	 */
	
	@PostMapping("/artic")
	public String article(@Validated Article article, BindingResult bindingResult, Model model, HttpSession session ,@RequestParam("id") Optional<Long> id, @RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		
		if(bindingResult.hasErrors()) {
			
			getArticleList(model);
			
			
			return "/article/article";
		
		}else {
			
			String username = ((UserLogin)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
			
			if (username != null && !multipartFile.isEmpty()) {
				//Instancie un nouvel object user avec le mail de user connecté
				
				User user =new User();
				
				
				
				user = userService.getByEmail(username);
				
				System.out.println(user);
				
				
				article.setUser(user);
				
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		        article.setPhotos(fileName);
		        
		        try {
		        	//DESTINATION
					//String uploadDir = "static/images/" +article.getTitre();
					String destination = new ClassPathResource("/src/main/resources/static/upload").getPath();
					
					articleService.uploadsImage(destination, fileName, multipartFile);
		        	
		        }catch(IOException e) {
		        	
		        	e.printStackTrace();
		        }
				
				session.setAttribute("add", "L''article  " + article.getTitre() + " a bien été ajouté");
				
				articleService.addArticle(article);	
				
			}
			
			return "redirect:/artic";
		}
	}
	
	
	
	
	
		/********************************************************************************
		 * 			FIN	TRAITEMENT DE LA PAGE LISTING DES ARTICLES
		 * **********************************************************************************/
	

		/******************************************************************************************
		 * 			DEBUT	TRAITEMENT DE LA PAGE DÉDIE DE L'ARTICLE
		 * *****************************************************************************************/
	
		@GetMapping("artic/{id}")
		public  String view(@PathVariable (value="id") Long id, Model model, Article article) {
		
		Optional<Article> getArticleFounded = articleService.getArticleIfExist(id);
		
		if(getArticleFounded.isPresent()) {
			
			model.addAttribute("articles",getArticleFounded.get());
			
			return "article/viewArticle";
		}
		return "article/viewArticle";
	}

		//TRAITEMENT DE L'UPDATE
	@PostMapping("artic/{id}")
	public String update(@PathVariable(value = "id") Long id, @Validated Article articleDetails, BindingResult bindingResult, HttpSession session, @RequestParam("image") MultipartFile multipartFile) throws AttributeNotFoundException{
		
		System.err.println(articleDetails.getTitre());
		System.err.println(articleDetails.getResume());
		System.err.println(articleDetails.getContenu());
		System.err.println(articleDetails.getDateCreation());
		/*
        * La ligne ci-dessous joue un role essentiel. 
        * Il check si l'id, il le reoturne dans la variable sinon il léve une exception
        */
		

		Article article = articleService.getArticleIfExist(id)
	
	.orElseThrow(() -> new AttributeNotFoundException("Aucun article trouvé avec l'id :: " + id));
		
		article.setTitre(articleDetails.getTitre());
		article.setResume(articleDetails.getResume());
		article.setContenu(articleDetails.getContenu());
		article.setDateCreation(articleDetails.getDateCreation());
		
		//article.setPhotos(articleDetails.getPhotos());
		
		
	//Rajouter le user
 	// Recuperation du nom du User connecté
		
		String username = ((UserLogin)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		
		if (username != null) {
			//Instancie un nouvel object user avec le mail de user connecté
			
			User user =new User();
			
			user = userService.getByEmail(username);
			
			System.out.println(user);
			
			
			article.setUser(user);
			
			
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	        article.setPhotos(fileName);
	        
	        try {
	        	//DESTINATION
				//String uploadDir = "static/images/" +article.getTitre();
				String destination = new ClassPathResource("/src/main/resources/static/upload").getPath();
				
				articleService.uploadsImage(destination, fileName, multipartFile);
	        	
	        }catch(IOException e) {
	        	
	        	e.printStackTrace();
	        }
			
			
			session.setAttribute("update", "L''article  " + article.getTitre() + " a bien été modifié");
			
			articleService.addArticle(article);
			
		}
		
		return "redirect:/artic/"+id;
		
		
	}
	
	/******************************************************************************************
	 * 			FIN	TRAITEMENT DE LA PAGE DÉDIE DE L'ARTICLE
	 * *****************************************************************************************/
	
	

	/*****************************************************************************
	 * 						AJOUT ARTICLE VIA PAGE DEDIE
	 * *****************************************************************************/
	
	@GetMapping("/addArticle")
	public String addArt(Article article) {
		
		
		return "article/ajoutArticle";
		
		
	}
	
@PostMapping("/addArticle")
public String addArt (@Validated Article article, BindingResult bindingResult, HttpSession session) {
	
	if(bindingResult.hasErrors()) {
		System.err.println(bindingResult.getFieldErrorCount());
		System.err.println(bindingResult.getFieldError());
		return "article/ajoutArticle";
	
	}else {
	 // Recuperation du nom du User connecté
		
		String username = ((UserLogin)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		
		if (username != null) {
			//Instancie un nouvel object user avec le mail de user connecté
			
			User user =new User();
			
			user = userService.getByEmail(username);
			
			System.out.println(user);
			
			
			article.setUser(user);
			
			session.setAttribute("add", "L''article  " + article.getTitre() + " a bien été ajouté");
			
			articleService.addArticle(article);
			
		}
	
	
	
	return "/article/article";
	}
}
	
		/*****************************************************************************
		 * 				FIN TRAITEMENT 	AJOUT ARTICLE VIA PAGE DEDIE
		 * ***************************************************************************/
	
	
	
	
	
	

	//SUPRESSION EMPLOYE
	@GetMapping("/supprimer/{id}") 
	public String supprimer(@PathVariable(value="id") long articleID, Model model) {

		if (articleService.supprimer(articleID)) {
			
			model.addAttribute("msg","success");
			
		} else {
			model.addAttribute("msg","echec");
		}
		return "redirect:/artic";
	}



	//METHODE UTLISANT LE SERVICE POUR RECUPERER LA LISTE

	public void getArticleList (Model model) {
		
		if (articleService.listArticle().size() > 0) {
			
			System.out.println(articleService.listArticle());
			
			model.addAttribute("articleList", articleService.listArticle()); //(articleList est le contenu))
			 
		}
	
	}

}
