package edu.sjsu.cmpe.library.api.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.sun.jersey.spi.inject.Errors.ErrorMessagesException;
import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.ErrorHandling;
import edu.sjsu.cmpe.library.domain.Review;
import edu.sjsu.cmpe.library.dto.AuthorArrayDto;
import edu.sjsu.cmpe.library.dto.AuthorDto;
import edu.sjsu.cmpe.library.dto.AuthorsDto;
import edu.sjsu.cmpe.library.domain.BookDetail;
import edu.sjsu.cmpe.library.dto.LinkDto;
import edu.sjsu.cmpe.library.dto.LinksDto;
import edu.sjsu.cmpe.library.dto.ReviewDto;
import edu.sjsu.cmpe.library.dto.ReviewsDto;
import edu.sjsu.cmpe.library.repository.BookRepositoryInterface;

import javax.ws.rs.core.Request;

@Path("/v1/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
	/** bookRepository instance */
	private final BookRepositoryInterface bookRepository;

	/**
	 * BookResource constructor
	 * 
	 * @param bookRepository
	 *            a BookRepository instance
	 */
	public BookResource(BookRepositoryInterface bookRepository) {
		this.bookRepository = bookRepository;

	}

	@GET
	@Path("/{isbn}")
	@Timed(name = "view-book")
	public AuthorsDto getBookByIsbn(@PathParam("isbn") LongParam isbn , @Context Request req, @Context UriInfo ui) {
		
		BookDetail book = bookRepository.getBookByISBN(isbn.get());
		Book originalBook = createBookToView(book);
		checkNotNull(book, " Book does not exist");

		Author[] authorsList = book.getAuthors();
		int counter = 0;
		int size = authorsList.length;
		List<LinkDto> authors = new ArrayList<LinkDto>();

		while (size > 0) {
			String location1 = "/books/" + book.getIsbn() + "/authors/"
					+ authorsList[counter].getId();

			authors.add(new LinkDto("view-author", location1, "GET"));
			counter++;
			size--;
		}
		
		int reviewCount=0;
		ArrayList<Review> reviewsList=book.getReviews();
		List<LinkDto> reviews = new ArrayList<LinkDto>();
		if(reviewsList!=null){
		int reviewSize=reviewsList.size();
		
		while (reviewSize > 0) {
			String reviewLocation = "/books/" + book.getIsbn() + "/reviews/"
					+ reviewsList.get(reviewCount).getId();

			reviews.add(new LinkDto("view-rview", reviewLocation, "GET"));
			reviewCount++;
			reviewSize--;
		}
		}
		
		AuthorsDto bookResponse = new AuthorsDto(authors,reviews);
		bookResponse.setBook(originalBook);
		String location = "/books/" + book.getIsbn();

		// add more links
		

		bookResponse.addLink(new LinkDto("view-book", location, "GET"));
		bookResponse.addLink(new LinkDto("update-book", location, "PUT"));
		bookResponse.addLink(new LinkDto("delete-book", location, "DELETE"));
		bookResponse.addLink(new LinkDto("create-review", location, "POST"));
		if (book.getReviews() != null)
			bookResponse.addLink(new LinkDto("view-all-reviews", location
					+ "/reviews", "GET"));
		
		return bookResponse;
		
	}
	
	

	/*
	 * method populates a book instance for viewing
	 */
	private Book createBookToView(BookDetail book) {
		// TODO Auto-generated method stub
		Book originalbook = new Book();
		originalbook.setIsbn(book.getIsbn());
		originalbook.setLanguage(book.getLanguage());
		originalbook.setNumPages(book.getNumPages());
		originalbook.setPublicationDate(book.getPublicationDate());
		originalbook.setTitle(book.getTitle());
		originalbook.setStatus(book.getStatus());
		return originalbook;

	}

	@POST
	@Timed(name = "create-book")
	public Response createBook(@Valid BookDetail request)
			throws ErrorMessagesException {
		ErrorHandling error = new ErrorHandling();

		// Store the new book in the BookRepository so that we can retrieve it.

		// validation for default values for status
		if (!request.getStatus().equals("available")
				&& !request.getStatus().equals("lost")
				&& !request.getStatus().equals("checked-out")
				&& !request.getStatus().equals("in-queue")) {
			error.setStatusCode("400");
			error.setErrorDesc("status should be {available,lost,checked-out,in-queue} in lower case letters");
			return Response.status(400).entity(error).build();

		}

		// validation for language
		String checkLanguage = request.getLanguage();
		if (!checkLanguage.equals(checkLanguage.toLowerCase())) {
			error.setStatusCode("400");
			error.setErrorDesc("language should be specified in lowercase letters");
			return Response.status(400).entity(error).build();
		}
		BookDetail savedBook = bookRepository.saveBook(request);

		String location = "/books/" + savedBook.getIsbn();
		// Add other links if needed
		LinksDto links = new LinksDto();
		links.addLink(new LinkDto("view-book", location, "GET"));
		links.addLink(new LinkDto("update-book", location, "PUT"));
		links.addLink(new LinkDto("delete-book", location, "DELETE"));
		links.addLink(new LinkDto("create-review", location + "/reviews",
				"POST"));

		return Response.status(200).entity(links).build();

	}

	@DELETE
	@Path("/{isbn}")
	@Timed(name = "delete-book")
	public Response deleteBook(@PathParam("isbn") LongParam isbn) {

		Book book = bookRepository.getBookByISBN(isbn.get());
		bookRepository.deleteBook(book);
		LinksDto links = new LinksDto();
		links.addLink(new LinkDto("create-book", "/books", "POST"));
		return Response.status(200).entity(links).build();
	}

	@PUT
	@Path("/{isbn}")
	@Timed(name = "update-book")
	public Response updateBook(@PathParam("isbn") LongParam isbn,
			@Context UriInfo uriInfo) {

		BookDetail book = (BookDetail) bookRepository.getBookByISBN(isbn.get());
		checkNotNull(book, " Book does not exist");

		MultivaluedMap<String, String> queryParams = uriInfo
				.getQueryParameters();
		String status = queryParams.getFirst("status");
		if (status != null) {
			if (!status.equalsIgnoreCase("available")
					&& !status.equalsIgnoreCase("lost")
					&& !status.equalsIgnoreCase("checked-out")
					&& !status.equalsIgnoreCase("in-queue")) {
				ErrorHandling error = new ErrorHandling();
				error.setStatusCode("400");
				error.setErrorDesc("status should be {available,lost,checked-out,in-queue} in lower case letters");
				return Response.status(400).entity(error).build();

			}

		}
		bookRepository.updateBook(book, queryParams);
		String location = "/books/" + book.getIsbn();
		LinksDto links = new LinksDto();
		links.addLink(new LinkDto("create-book", location, "POST"));
		links.addLink(new LinkDto("update-book", location, "PUT"));
		links.addLink(new LinkDto("delete-book", location, "DELETE"));
		links.addLink(new LinkDto("create-review", location, "POST"));
		if (book.getReviews() != null)
			links.addLink(new LinkDto("view-all-reviews",
					location + "/reviews", "GET"));
		return Response.status(200).entity(links).build();

	}

	@PUT
	@Path("/{isbn}/authors/{id}")
	@Timed(name = "update-author")
	public Response updateAuthor(@PathParam("isbn") LongParam isbn,
			@PathParam("id") LongParam id, @Context UriInfo uriInfo) {

		BookDetail book = (BookDetail) bookRepository.getBookByISBN(isbn.get());
		checkNotNull(book, " Book does not exist");
		MultivaluedMap<String, String> queryParams = uriInfo
				.getQueryParameters();
		long authorId = id.get();
		bookRepository.updateBook(book, authorId, queryParams);
		LinksDto links = new LinksDto();
		links.addLink(new LinkDto("create-book", "/books", "POST"));
		links.addLink(new LinkDto("update-book", "/books", "PUT"));
		links.addLink(new LinkDto("delete-book", "/books", "DELETE"));
		return Response.status(200).entity(links).build();

	}

	@GET
	@Path("/{isbn}/authors")
	@Timed(name = "view-all-authors")
	public AuthorArrayDto viewAllAuthors(@PathParam("isbn") LongParam isbn) {
		BookDetail book = (BookDetail) bookRepository.getBookByISBN(isbn.get());
		checkNotNull(book, " Book does not exist");
		Author[] authorArray = book.getAuthors();
		AuthorArrayDto authors = new AuthorArrayDto(authorArray);

		return authors;
	}

	@GET
	@Path("/{isbn}/authors/{id}")
	@Timed(name = "view-author")
	public AuthorDto viewAuthor(@PathParam("isbn") LongParam isbn,
			@PathParam("id") LongParam id) {
		BookDetail book = (BookDetail) bookRepository.getBookByISBN(isbn.get());
		checkNotNull(book, " Book does not exist");
		Author author = new Author();
		long authorId = id.get();

		Author[] authors = book.getAuthors();
		int authorCount = authors.length;
		int index = 0;

		while (authorCount > 0) {
			if (authors[index].getId() == authorId) {
				author.setId(authorId);
				author.setName(authors[index].getName());

			}
			index++;
			authorCount--;

		}

		AuthorDto authorResponse = new AuthorDto(author);
		authorResponse.addLink(new LinkDto("view-author", "/books/" + isbn
				+ "/authors/" + author.getId(), "GET"));
		return authorResponse;

	}

	@POST
	@Path("{isbn}/reviews")
	@Timed(name = "create-review")
	public Response createBookReview(@Valid Review review,
			@PathParam("isbn") LongParam isbn) {
		checkNotNull(review, "review can't be null");
		BookDetail book = (BookDetail) bookRepository.getBookByISBN(isbn.get());
		bookRepository.saveReviewToBook(book, review);
		LinksDto links = new LinksDto();
		links.addLink(new LinkDto("view-review", "/books/" + isbn + "/reviews/"
				+ review.getId(), "GET"));
		return Response.status(201).entity(links).build();

	}

	@GET
	@Path("{isbn}/reviews/{id}")
	@Timed(name = "view-review")
	public ReviewDto viewBookReview(@PathParam("isbn") LongParam isbn,
			@PathParam("id") LongParam reviewId) {

		BookDetail book = (BookDetail) bookRepository.getBookByISBN(isbn.get());
		checkNotNull(book, "book does not exist");
		long id = reviewId.get();
		Review review = new Review();
		ArrayList<Review> reviews = book.getReviews();
		int numOfReviews = reviews.size();
		int index = 0;
		while (numOfReviews > 0) {
			if (reviews.get(index).getId() == id) {
				review = reviews.get(index);
			}
			index++;
			numOfReviews--;
		}

		ReviewDto reviewDto = new ReviewDto(review);
		reviewDto.addLink(new LinkDto("view-review", "/books/" + isbn
				+ "/reviews/" + id, "GET"));
		return reviewDto;

	}

	@GET
	@Path("{isbn}/reviews")
	@Timed(name = "view-all-reviews")
	public ReviewsDto viewAllReviews(@PathParam("isbn") LongParam isbn) {
		BookDetail book = (BookDetail) bookRepository.getBookByISBN(isbn.get());
		checkNotNull(book, "book does not exist");
		ArrayList<Review> reviewList = book.getReviews();
		ReviewsDto reviews = new ReviewsDto(reviewList);
		return reviews;

	}

}
