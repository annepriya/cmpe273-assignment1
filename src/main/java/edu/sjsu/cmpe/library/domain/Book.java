package edu.sjsu.cmpe.library.domain;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.cmpe.library.dto.LinksDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import edu.sjsu.cmpe.library.dto.LinkDto;

@JsonPropertyOrder({ "isbn", "title", "publication-date", "language", "num-pages", "status", "reviews", "authors"})
public class Book  {
	
    private long isbn;
	
    private String title;
    @JsonProperty("publication-date")
    private String publicationDate;
  
    private String language;
    @JsonProperty("num-pages")
    private String numPages;
    @JsonProperty("status")
    private String status="available";
    @JsonProperty
    private ArrayList<Review> reviews = new ArrayList<Review>();
    
 
    
   
    
  
   
   
    
    
	
	
	

	
    
    

    // add more fields here

	
	

	public ArrayList<Review> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}

	
	/**
     * @return the isbn
     */
    public long getIsbn() {
	return isbn;
    }

    /**
     * @param isbn
     *            the isbn to set
     */
    public void setIsbn(long isbn) {
	this.isbn = isbn;
    }

    /**
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }

	public String getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getNumPages() {
		return numPages;
	}

	public void setNumPages(String numPages) {
		this.numPages = numPages;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
    
}
