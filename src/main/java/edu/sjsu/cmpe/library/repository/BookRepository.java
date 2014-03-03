package edu.sjsu.cmpe.library.repository;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.core.MultivaluedMap;

import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Review;

public class BookRepository implements BookRepositoryInterface {
    /** In-memory map to store books. (Key, Value) -> (ISBN, Book) */
    private final ConcurrentHashMap<Long, Book> bookInMemoryMap;

    /** Never access this key directly; instead use generateISBNKey() */
    private long isbnKey;
    private long authorId;
    private long reviewId;

    public BookRepository(ConcurrentHashMap<Long, Book> bookMap) {
	checkNotNull(bookMap, "bookMap must not be null for BookRepository");
	bookInMemoryMap = bookMap;
	isbnKey = 0;
    }

    /**
     * This should be called if and only if you are adding new books to the
     * repository.
     * 
     * @return a new incremental ISBN number
     */
    private final Long generateISBNKey() {
	// increment existing isbnKey and return the new value
	return Long.valueOf(++isbnKey);
    }

    /**
     * This will auto-generate unique ISBN for new books.
     */
    @Override
    public Book saveBook(Book newBook) {
	checkNotNull(newBook, "newBook instance must not be null");
	// Generate new ISBN
	Long isbn = generateISBNKey();
	newBook.setIsbn(isbn);
	// TODO: create and associate other fields such as author
	
	ArrayList<Author> bookAuthor=newBook.getAuthors();
	int numberOfAuthors=bookAuthor.size();
	ArrayList<Author> updatedAuthor = new ArrayList<Author>();
	for (int i=0;i<numberOfAuthors;i++){
		Author author=bookAuthor.get(i);
		long id=generateAuthorId();
		author.setId(id);
		author.setName(bookAuthor.get(i).getName());
		updatedAuthor.add(author);
		
	}
	newBook.setAuthors(updatedAuthor);


	// Finally, save the new book into the map
	bookInMemoryMap.putIfAbsent(isbn, newBook);

	return newBook;
    }
    
    public void saveReviewToBook(Book book ,Review review){
    	ArrayList<Review> reviews=new ArrayList<Review>();
    	reviewId=generateReviewId();
    
    	review.setId(reviewId);
    	if(book.getReviews()!=null){
    	reviews=book.getReviews();
    	}
    
    	reviews.add(review);
    	book.setReviews(reviews);
    	bookInMemoryMap.put(book.getIsbn(), book);
    	
   }

    /**
     * @see edu.sjsu.cmpe.library.repository.BookRepositoryInterface#getBookByISBN(java.lang.Long)
     */
    @Override
    public Book getBookByISBN(Long isbn) {
	checkArgument(isbn > 0,
		"ISBN was %s but expected greater than zero value", isbn);
	return bookInMemoryMap.get(isbn);
    }

	@Override
	public void deleteBook(Book existingBook) {
		// TODO Auto-generated method stub
		
		
	    	checkNotNull(existingBook, "newBook instance must not be null");
	    	bookInMemoryMap.remove(existingBook.getIsbn(), existingBook);
	    	if(existingBook.getIsbn()=='1')
	    	isbnKey--;
	    	
	    	
	}
	
	public Book updateBook(Book updateBook ,Long authorId ,MultivaluedMap<String, String> params){
		
		int id=0;
		ArrayList<Author>authors=updateBook.getAuthors();
		int numberOfAuthors=authors.size();
		for(int i=0;i<numberOfAuthors;i++){
			Author author =authors.get(i);
			if(author.getId()==authorId){
				String name=params.getFirst("name");
				author.setName(name);
			}
		}
		
		updateBook.setAuthors(authors);
		bookInMemoryMap.put(updateBook.getIsbn(), updateBook);
		return updateBook;
		
		
	}
	
	public Book updateBook(Book updateBook , MultivaluedMap<String, String> params){
		
			 /* String isbn =params.getFirst("isbn");
			checkNotNull(isbn, "newBook instance must not be null");
			updateBook.setIsbn(new Long(isbn));*/
		
		
			String title =params.getFirst("title");
			if(title!=null)
				updateBook.setTitle(title);
			
			String  publicationDate =params.getFirst("publication-date");
			if(publicationDate!=null)
			updateBook.setPublicationDate(publicationDate);
			
			
			String  language =params.getFirst("language");
			if(language!=null)
			updateBook.setLanguage(language);
			
			String  numPages =params.getFirst("num-pages");
			if(numPages!=null)
			updateBook.setNumPages(numPages);
			
			String  status =params.getFirst("status");
			if(status!=null)
			updateBook.setStatus(status);
			
		
			
			bookInMemoryMap.put(updateBook.getIsbn(), updateBook);
		   return updateBook;
	}
	
	
    
    
	public long generateAuthorId(){
		return Long.valueOf(++authorId);
		
		
	}
	
	public long generateReviewId(){
		return Long.valueOf(++reviewId);
	}
	

}
