package edu.sjsu.cmpe.library.dto;

import edu.sjsu.cmpe.library.domain.Review;

public class ReviewDto extends LinksDto{
	
	private Review review;
	
	public ReviewDto(Review review){
		this.review=review;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}
	
	

}
