package edu.sjsu.cmpe.library.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.sjsu.cmpe.library.domain.Author;
@JsonPropertyOrder(alphabetic = true)
public class AuthorArrayDto extends LinksDto{
	private Author[] authors;

	public AuthorArrayDto(Author[] authorArray) {
		// TODO Auto-generated constructor stub
		this.authors=authorArray;
	}

	public Author[] getAuthors() {
		return authors;
	}

	public void setAuthors(Author[] authors) {
		this.authors = authors;
	}
	

}
