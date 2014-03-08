package edu.sjsu.cmpe.library.repository;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.Valid;
import javax.ws.rs.core.MultivaluedMap;

import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Review;
import edu.sjsu.cmpe.library.domain.BookDetail;



public class BookRepository implements BookRepositoryInterface {
	/** In-memory map to store books. (Key, Value) -> (ISBN, Book) */
	private final ConcurrentHashMap<Long, BookDetail> bookInMemoryMap;

	/** Never access this key directly; instead use generateISBNKey() */
	private long isbnKey;
	private long authorId;
	private long reviewId;

	public BookRepository(ConcurrentHashMap<Long, BookDetail> bookMap) {
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
	public BookDetail saveBook(@Valid BookDetail newBook) {
		checkNotNull(newBook, "newBook instance must not be null");
		// Generate new ISBN
		Long isbn = generateISBNKey();
		newBook.setIsbn(isbn);
		// TODO: create and associate other fields such as author

		Author[] bookAuthor = newBook.getAuthors();
		int numberOfAuthors = bookAuthor.length;
		for (int i = 0; i < numberOfAuthors; i++) {
			Author author = bookAuthor[i];
			long id = generateAuthorId();
			author.setId(id);
			author.setName(bookAuthor[i].getName());
			bookAuthor[i] = author;

		}
		newBook.setAuthors(bookAuthor);

		// Finally, save the new book into the map
		bookInMemoryMap.putIfAbsent(isbn, newBook);
        authorId=0;
        return newBook;
	}

	public void saveReviewToBook(BookDetail book, @Valid Review review) {
		ArrayList<Review> reviews = new ArrayList<Review>();
		reviewId = generateReviewId();

		review.setId(reviewId);
		if (book.getReviews() != null) {
			reviews = book.getReviews();
		}

		reviews.add(review);
		book.setReviews(reviews);
		bookInMemoryMap.put(book.getIsbn(), book);
		 reviewId=0;

	}

	/**
	 * @see edu.sjsu.cmpe.library.repository.BookRepositoryInterface#getBookByISBN(java.lang.Long)
	 */
	@Override
	public BookDetail getBookByISBN(Long isbn) {
		checkArgument(isbn > 0,
				"ISBN was %s but expected greater than zero value", isbn);
		return bookInMemoryMap.get(isbn);
	}

	@Override
	public void deleteBook(Book existingBook) {
		// TODO Auto-generated method stub

		checkNotNull(existingBook, "newBook instance must not be null");
		bookInMemoryMap.remove(existingBook.getIsbn(), existingBook);
		if (existingBook.getIsbn() == 1)
			isbnKey = 0;
		authorId = 0;

	}

	public Book updateBook(@Valid BookDetail updateBook, Long authorId,
			MultivaluedMap<String, String> params) {

		int id = 0;
		Author[] authors = updateBook.getAuthors();
		int numberOfAuthors = authors.length;
		for (int i = 0; i < numberOfAuthors; i++) {
			Author author = authors[i];
			if (author.getId() == authorId) {
				String name = params.getFirst("name");
				author.setName(name);
			}
		}

		updateBook.setAuthors(authors);
		bookInMemoryMap.put(updateBook.getIsbn(), updateBook);
		return updateBook;

	}

	public Book updateBook(BookDetail updateBook,
			MultivaluedMap<String, String> params) {

		String title = params.getFirst("title");
		if (title != null)
			updateBook.setTitle(title);

		String publicationDate = params.getFirst("publication-date");
		if (publicationDate != null)
			updateBook.setPublicationDate(publicationDate);

		String language = params.getFirst("language");
		if (language != null)
			updateBook.setLanguage(language);

		String numPages = params.getFirst("num-pages");
		if (numPages != null)
			updateBook.setNumPages(numPages);

		String status = params.getFirst("status");
		if (status != null) {
			updateBook.setStatus(status);

		}

		bookInMemoryMap.put(updateBook.getIsbn(), updateBook);
		return updateBook;
	}

	public long generateAuthorId() {
		return Long.valueOf(++authorId);

	}

	public long generateReviewId() {
		return Long.valueOf(++reviewId);
	}

}
