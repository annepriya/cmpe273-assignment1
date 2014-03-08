package edu.sjsu.cmpe.library.domain;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Review;


public class BookDetail extends Book {
	@NotNull(message = "{author cannot be empty}")
	private Author[] authors;
	 private ArrayList<Review> reviews;
	 

	public ArrayList<Review> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}

	public Author[] getAuthors() {
		return authors;
	}

	public void setAuthors(Author[] authors) {
		this.authors = authors;
	}

}


