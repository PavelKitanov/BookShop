//package com.ebookstore.microservices.bookservice.junitTests;
//
//import com.ebookstore.microservices.bookservice.exceptions.BookNotFoundException;
//import com.ebookstore.microservices.bookservice.models.Author;
//import com.ebookstore.microservices.bookservice.models.Book;
//import com.ebookstore.microservices.bookservice.models.Genre;
//import com.ebookstore.microservices.bookservice.repositories.BookRepository;
//import com.ebookstore.microservices.bookservice.services.AuthorService;
//import com.ebookstore.microservices.bookservice.services.GenreService;
//import com.ebookstore.microservices.bookservice.services.impl.BookServiceImpl;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//@SpringBootTest
//public class BookUnitTests {
//
//    @Mock
//    private BookRepository bookRepository;
//
//    @Mock
//    private AuthorService authorService ;
//
//    @Mock
//    private GenreService genreService ;
//
//    @InjectMocks
//    private BookServiceImpl bookService;
//
//    @BeforeEach
//    public void init(){
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testSaveBookWithExistingAuthor(){
//        Long authorId = 1L;
//        Long genreId = 1L;
//        String title = "Sample book";
//        String firstName = null;
//        String lastName = null;
//        Author existingAuthor = new Author("John", "Doe");
//        Genre genre = new Genre("Fiction");
//        String description = "description";
//        double price = 9.99;
//
//        when(authorService.findById(authorId)).thenReturn(existingAuthor);
//        when(genreService.findById(genreId)).thenReturn(genre);
//
//        Book savedBook = bookService.save(title, authorId, firstName, lastName, genreId, description, price);
//
//        assertEquals(title, savedBook.getTitle());
//        assertEquals(existingAuthor, savedBook.getAuthor());
//        assertEquals(genre, savedBook.getGenre());
//        assertEquals(description, savedBook.getDescription());
//        assertEquals(price, savedBook.getPrice());
//    }
//
//    @Test
//    public void testSaveBookWithNewAuthor(){
//        Long authorId = null;
//        Long genreId= 1L;
//        String title = "Sample book";
//        String firstName = "John";
//        String lastName = "Doe";
//        Genre genre = new Genre("Fiction");
//        String description = "description";
//        double price = 9.99;
//
//        when(authorService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(null);
//        when(genreService.findById(genreId)).thenReturn(genre);
//
//        Book savedBook = bookService.save(title, authorId, firstName, lastName, genreId, description, price);
//
//        assertEquals(title, savedBook.getTitle());
//        assertEquals(firstName, savedBook.getAuthor().getFirstName());
//        assertEquals(lastName, savedBook.getAuthor().getLastName());
//        assertEquals(genre, savedBook.getGenre());
//        assertEquals(description, savedBook.getDescription());
//        assertEquals(price, savedBook.getPrice());
//    }
//
//    @Test
//    public void testFindBooksByAuthor() {
//        Long authorId = 1L;
//        Author author = new Author("John", "Doe");
//        author.setAuthorId(authorId);
//        Genre genre = new Genre("Drama");
//
//        when(authorService.findById(authorId)).thenReturn(author);
//
//        List<Book> allBooks = new ArrayList<>();
//        allBooks.add(new Book("Book 1", author, genre, "description", 8.99));
//        allBooks.add(new Book("Book 2", author, genre, "desc", 5.99));
//        allBooks.add(new Book("Book 3", new Author("Jane", "Smith"), genre, "another description", 15.99));
//
//        when(bookRepository.findAll()).thenReturn(allBooks);
//
//        List<Book> booksByAuthor = bookService.findByAuthor(authorId);
//
//        assertEquals(3, allBooks.size());
//        assertEquals(2, booksByAuthor.size());
//    }
//
//    @Test
//    public void testUpdateBookWhenAuthorExists(){
//        Long bookId = 1L;
//        Long authorId = 1L;
//        Long genreId = 1L;
//        String title = "Updated book title";
//        String description = "Updated description";
//        double price = 19.99;
//
//        Author existingAuthor = new Author("John", "Doe");
//        existingAuthor.setAuthorId(authorId);
//        Genre genre = new Genre("Fiction");
//        genre.setGenreId(genreId);
//        Book existingBook = new Book("Original title", new Author("Jane", "Smith"), new Genre("drama"), "Original description", 7.99);
//        existingBook.setBookId(bookId);
//
//        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
//        when(authorService.findById(authorId)).thenReturn(existingAuthor);
//        when(genreService.findById(genreId)).thenReturn(genre);
//        when(bookRepository.save(existingBook)).thenAnswer(invocation -> invocation.getArgument(0));
//
//        Book updatedBook = bookService.update(bookId, title, authorId, null, null, genreId, description, price);
//
//        assertEquals(title, updatedBook.getTitle());
//        assertEquals(existingAuthor, updatedBook.getAuthor());
//        assertEquals(genre, updatedBook.getGenre());
//        assertEquals(description, updatedBook.getDescription());
//        assertEquals(price, updatedBook.getPrice());
//    }
//
//    @Test
//    public void testUpdateBookWhenAuthorNotExists(){
//        Long bookId = 1L;
//        Long authorId = 1L;
//        Long genreId = 1L;
//        String title = "Updated book title";
//        String firstName = "Jane";
//        String lastName = "Smith";
//        String description = "Updated description";
//        double price = 19.99;
//
//
//        Author author = new Author("John", "Doe");
//        author.setAuthorId(authorId);
//        Genre genre = new Genre("Fiction");
//        genre.setGenreId(genreId);
//        Book existingBook = new Book("Original title", author, new Genre("drama"), "Original description", 7.99);
//        existingBook.setBookId(bookId);
//
//        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
//        when(authorService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(null);
//        when(genreService.findById(genreId)).thenReturn(genre);
//        when(bookRepository.save(existingBook)).thenAnswer(invocation -> invocation.getArgument(0));
//
//        Book updatedBook = bookService.update(bookId, title, null, firstName, lastName, genreId, description, price);
//
//        assertEquals(title, updatedBook.getTitle());
//        assertEquals(firstName, updatedBook.getAuthor().getFirstName());
//        assertEquals(lastName, updatedBook.getAuthor().getLastName());
//        assertEquals(genre, updatedBook.getGenre());
//        assertEquals(description, updatedBook.getDescription());
//        assertEquals(price, updatedBook.getPrice());
//    }
//
//    @Test
//    public void testFindBookByIdFound(){
//        Long bookId = 1L;
//        Book expectedBook = new Book("Sample book", new Author("John", "Doe"),new Genre("Fiction"), "Description", 19.99);
//        expectedBook.setBookId(bookId);
//
//        when(bookRepository.findById(bookId)).thenReturn(Optional.of(expectedBook));
//
//        Book bookFound = bookService.findById(bookId);
//
//        assertEquals(expectedBook, bookFound);
//    }
//
//    @Test
//    public void testFindBookByIdNotFound(){
//        Long bookId = 1L;
//
//        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
//
//        assertThrows(BookNotFoundException.class, () -> {
//            bookService.findById(bookId);
//        });
//    }
//
//    @Test
//    public void testGetAllBooks(){
//        Book book1 = new Book("Book 1", new Author("John", "Doe"), new Genre("Fiction"), "description", 10.99);
//        Book book2 = new Book("Book 2", new Author("John", "Doe"), new Genre("Non-Fiction"), "desc", 5.99);
//        Book book3 = new Book("Book 3", new Author("John", "Doe"), new Genre("Drama"), "another description", 8.99);
//        List<Book> expectedBooks = Arrays.asList(book1, book2, book3);
//
//        when(bookRepository.findAll()).thenReturn(expectedBooks);
//
//        List<Book> actualBooks = bookService.findAll();
//
//        assertEquals(expectedBooks.size(), actualBooks.size());
//        assertEquals(expectedBooks, actualBooks);
//    }
//
//    @Test
//    public void testDeleteBookById(){
//        Long bookId = 1L;
//
//        bookService.deleteById(bookId);
//
//        verify(bookRepository, times(1)).deleteById(bookId);
//    }
//
//}
