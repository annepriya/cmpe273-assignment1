package edu.sjsu.cmpe.library.api.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.dto.BookDto;
import edu.sjsu.cmpe.library.dto.LinkDto;
import edu.sjsu.cmpe.library.dto.LinksDto;
import edu.sjsu.cmpe.library.repository.AuthorRepository;
import edu.sjsu.cmpe.library.repository.BookRepositoryInterface;

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
    public BookDto getBookByIsbn(@PathParam("isbn") LongParam isbn) {
    
	Book book = bookRepository.getBookByISBN(isbn.get());
	checkNotNull(book, " Book does not exist");
	
	
	BookDto bookResponse = new BookDto(book);
	
	bookResponse.addLink(new LinkDto("view-book", "/books/" + book.getIsbn(),
		"GET"));
	bookResponse.addLink(new LinkDto("update-book",
		"/books/" + book.getIsbn(), "POST"));
	// add more links
	
	int counter=0;
	if(book.getAuthors()!=null){
	int size=book.getAuthors().size();
	ArrayList<LinkDto> authorLinks = new ArrayList<LinkDto>();
	while(size>0){
		
		//authorLinks.add(new LinkDto("view-author", "/books" +book.getIsbn()+"/authors" +book.getAuthors().get(counter), "POST"));
		bookResponse.addLink(new LinkDto("view-author", "/books/" +book.getIsbn()+"/authors/" +book.getAuthors().get(counter).getId(), "GET"));
           counter++;
           size--;
	}
	

	}

	return bookResponse;
	}
	
	
    

    @POST
    @Timed(name = "create-book")
    public Response createBook(Book request , Author author) {
	// Store the new book in the BookRepository so that we can retrieve it.
    	
	Book savedBook = bookRepository.saveBook(request);

	String location = "/books/" + savedBook.getIsbn();
	BookDto bookResponse = new BookDto(savedBook);
	bookResponse.addLink(new LinkDto("view-book", location, "GET"));
	bookResponse.addLink(new LinkDto("update-book", location, "POST"));
	// Add other links if needed

	return Response.status(201).entity(bookResponse).build();
    }
    
   @DELETE
   @Path("/{isbn}")
   @Timed(name = "delete-book")
   
   public Response deleteBook(@PathParam("isbn") LongParam isbn){
	   
	   Book book = bookRepository.getBookByISBN(isbn.get());
	   bookRepository.deleteBook(book);
	   LinksDto links = new LinksDto();
	   links.addLink(new LinkDto("create-book", "/books", "POST"));
	   return Response.status(200).entity(links).build();
   }
   
   
   @PUT
   @Path("/{isbn}")
   @Timed( name = "update-book")
   public Response updateBook(@PathParam("isbn") LongParam isbn ,@Context UriInfo uriInfo ) {
	   
	  
	   Book book = bookRepository.getBookByISBN(isbn.get());
		checkNotNull(book, " Book does not exist");
		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		
		
		//bookRepository.updateBook(book,title);
		bookRepository.updateBook(book, queryParams);
		LinksDto links = new LinksDto();
		links.addLink(new LinkDto("create-book", "/books", "POST"));
		links.addLink(new LinkDto("update-book", "/books", "PUT"));
		links.addLink(new LinkDto("delete-book", "/books", "DELETE"));
		return Response.status(200).entity(links).build();
	   
   
   }
   
}

