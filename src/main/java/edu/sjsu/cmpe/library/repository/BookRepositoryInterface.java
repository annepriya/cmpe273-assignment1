package edu.sjsu.cmpe.library.repository;

import javax.ws.rs.core.MultivaluedMap;

import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Review;

/**
 * Book repository interface.
 * 
 * What is repository pattern?
 * 
 * @see http://martinfowler.com/eaaCatalog/repository.html
 */
public interface BookRepositoryInterface {
    /**
     * Save a new book in the repository
     * 
     * @param newBook
     *            a book instance to be create in the repository
     * @return a newly created book instance with auto-generated ISBN
     */
    Book saveBook(Book newBook);

    /**
     * Retrieve an existing book by ISBN
     * 
     * @param isbn
     *            a valid ISBN
     * @return a book instance
     */
    Book getBookByISBN(Long isbn);
    
    void deleteBook(Book existingBook);
    
    Book updateBook(Book updateBook ,Long authorId ,MultivaluedMap<String, String> params);
    Book updateBook(Book updateBook , MultivaluedMap<String, String> params);
    void saveReviewToBook(Book book ,Review review);

    // TODO: add other operations here!
}
