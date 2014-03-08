package edu.sjsu.cmpe.library.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public class Review {

	long id;
	@Min(1)
	@Max(5)
	@Range()
	@NotNull(message = "{rating cannot be empty}")
	int rating;

	@NotBlank(message = "{comment cannot be empty}")
	String comment;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
