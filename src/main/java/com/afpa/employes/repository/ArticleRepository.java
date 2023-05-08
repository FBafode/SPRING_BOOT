package com.afpa.employes.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.afpa.employes.bean.Article;


public interface ArticleRepository extends JpaRepository<Article, Long> {

	 Article findByTitre(String titre);







}
