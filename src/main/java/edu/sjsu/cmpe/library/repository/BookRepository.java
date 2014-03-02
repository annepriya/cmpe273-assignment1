package edu.sjsu.cmpe.library.repository;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.core.MultivaluedMap;

import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Book;

public class BookRepository implements BookRepositoryInterface {
    /** In-memory map to store books. (Key, Value) -> (ISBN, Book) */
    private final ConcurrentHashMap<Long, Book> bookInMemoryMap;

    /** Never access this key directly; instead use generateISBNKey() */
    private long isbnKey;
    private long authorId;

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
	
	public Book updateBook(Book updateBook ,String title){
		
		
			updateBook.setTitle(title);
			bookInMemoryMap.put(isbnKey, updateBook);
			return updateBook;
		
		
	}
	
	public Book updateBook(Book updateBook , MultivaluedMap<String, String> params){
		
			
			String title =params.getFirst("title");
			if(title!=null)
				updateBook.setTitle(title);
		
		return updateBook;
	}
	
	
    
    
	public long generateAuthorId(){
		return Long.valueOf(++authorId);
		
		
	}
	

}
