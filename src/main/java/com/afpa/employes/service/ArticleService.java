
package com.afpa.employes.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.afpa.employes.bean.Article;
import com.afpa.employes.repository.ArticleRepository;


@Service
public class ArticleService {
	
	@Autowired
	private ArticleRepository articleRepository;
	
	public Article ajoutArticle(Article article, BindingResult bindingresult, @RequestParam("id") Optional<Long> id){
		
		//verification si le mail exist déjà
		if(id.isPresent()) {
			System.out.println("POST -- -- "+ id);
			
			articleRepository.save(article);
			
		}else if (articleRepository.findByTitre(article.getTitre()) != null) {
			
			article  = articleRepository.findByTitre(article.getTitre());
			
	    	bindingresult.addError(new FieldError("artilce","nom","Le nom ("+article.getTitre()+") existe deja ..."));
			
		}else {
			
			articleRepository.save(article);
		}
		return article;	
	}
	
	
	public void addArticle (Article article) {
		
		articleRepository.save(article);
	}
	
	//Listing des employes
	public List<Article> listArticle(){
		
		return articleRepository.findAll();
	}
		
		
	//show 
	public Optional<Article> getArticleIfExist(Long id){
		
		return articleRepository.findById(id);
	}
	
		
	//delete
	public boolean supprimer(Long id) {
		
		Optional<Article> article= articleRepository.findById(id);
		
		if(article.isPresent()) {
			
			articleRepository.delete(article.get());
			System.out.println("deleted " );
			return true;
		}
		return false;
	}

	//UPLOADS IMAGE

			public void uploadsImage(String uploadDir, String fileName,
		            MultipartFile multipartFile) throws IOException {
				
		        Path uploadPath = Paths.get(uploadDir);
		         
		        //Check if exist
		        if (!Files.exists(uploadPath)) {
		        	System.err.println("Not EXISTS");
		        	
		            Files.createDirectories(uploadPath);
		        }
		         
		        try  {
		            Path filePath = uploadPath.resolve(fileName);
		            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		            
		        } catch (IOException ioe) {        
		            throw new IOException("Oups : " + fileName, ioe);
		        }
		}
	

}
