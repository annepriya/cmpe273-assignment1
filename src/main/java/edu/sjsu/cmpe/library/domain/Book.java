package edu.sjsu.cmpe.library.domain;

import java.util.ArrayList;

import edu.sjsu.cmpe.library.dto.LinksDto;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Book {
    private long isbn;
    private String title;
    @JsonProperty("publication-date")
    private String publicationDate;
    private String language;
    @JsonProperty("num-pages")
    private String numPages;
    private String status="available";
   

	
	private ArrayList<Author> authors;
	private ArrayList<Review> reviews;
	
	

	
    
    

    // add more fields here

    public ArrayList<Review> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}

	public ArrayList<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(ArrayList<Author> authors) {
		this.authors = authors;
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
