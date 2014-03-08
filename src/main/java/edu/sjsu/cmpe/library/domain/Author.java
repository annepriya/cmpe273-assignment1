package edu.sjsu.cmpe.library.domain;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class Author {

	private long id = 0;
	@NotBlank(message = "{author name  cannot be empty}")
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long generateAuthorId() {
		return Long.valueOf(++id);

	}

}
