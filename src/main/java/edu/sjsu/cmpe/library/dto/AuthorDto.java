package edu.sjsu.cmpe.library.dto;

import edu.sjsu.cmpe.library.domain.Author;

public class AuthorDto extends LinksDto {
	private Author author;
	
	public AuthorDto(Author author){
		this.author=author;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}
	
	

}
