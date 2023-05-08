package com.afpa.employes.bean;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Article {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Le titre est vide")
	private String titre;
	
	
	@NotBlank(message = "Le résumé est vide")
	@Size(min=5, max=30, message="Le résumé est trop court")
	private String resume;
	
	@NotBlank
	private String contenu;
	
	@ManyToOne()
	private User user;
	
	private Date dateCreation;
	
	@Column(nullable = true, length = 128)
	private String photos;
	

	
	   public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}


	public String getResume() {
		return resume;
	}


	public void setResume(String resume) {
		this.resume = resume;
	}



	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}

	public Date getDateCreation() {
		return dateCreation;
	}


	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	@Transient
	    public String getPhotosImagePath() {
	        if (photos == null || id == null) return null;
	         
	        return "/uploads/img" + id + "/" + photos;
	    }

}
