package edu.sjsu.cmpe.library.repository;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ConcurrentHashMap;

import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Book;

public class AuthorRepository {
	
	private final ConcurrentHashMap<Long , Author> authorInMemoryMap;
	
	private static long authorId=0;
	
	private Author[] authors;
	
	
	
	

	public AuthorRepository(
			ConcurrentHashMap<Long,Author> concurrentHashMap) {
		// TODO Auto-generated constructor stub
		this.authorInMemoryMap=concurrentHashMap;
		
	}



public Author saveAuthor(Author newAuthor){
	checkNotNull(newAuthor, "newBook instance must not be null");
	 
	// Generate new ISBN
	Long authorId = generateAuthorId();
	newAuthor.setId(authorId);
	authorInMemoryMap.putIfAbsent(authorId, newAuthor);

	return newAuthor;
	
}

	public long generateAuthorId(){
		return Long.valueOf(++authorId);
		
		
	}
	
	 

}
