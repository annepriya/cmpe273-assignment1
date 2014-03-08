package edu.sjsu.cmpe.library.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Book;

@JsonPropertyOrder({ "book","reviews", "authors" })
public class AuthorsDto extends LinksDto {
	@JsonInclude(Include.NON_NULL)
	private Book book = new Book();
	private List<LinkDto> authors = new ArrayList<LinkDto>();
	private List<LinkDto> reviews=new ArrayList<LinkDto>();

	
	public List<LinkDto> getReviews() {
		return reviews;
	}

	public void setReviews(List<LinkDto> reviews) {
		this.reviews = reviews;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public List<LinkDto> getAuthors() {
		return authors;
	}

	public void setAuthors(List<LinkDto> authors) {
		this.authors = authors;
	}

	public AuthorsDto(List<LinkDto> authors ,List<LinkDto> reviews) {
		this.authors = authors;
		this.reviews=reviews;
	}

}
