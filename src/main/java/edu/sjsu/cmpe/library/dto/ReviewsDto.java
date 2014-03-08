package edu.sjsu.cmpe.library.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.sjsu.cmpe.library.domain.Review;
@JsonPropertyOrder({"reviews" ,"links"})
public class ReviewsDto extends LinksDto {
	private ArrayList<Review> reviews;

	public ReviewsDto(ArrayList<Review> reviewList) {
		// TODO Auto-generated constructor stub
		this.reviews=reviewList;
	}

	public ArrayList<Review> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}
	

}
