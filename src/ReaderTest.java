import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addBook() {
        Code code;
        Reader reader = new Reader(123,"555-555-5555","John Smith");
        Book book = new Book ("1234","Book Title", "Subject", 5,"Cool Dude", LocalDate.now());

        assertEquals(reader.addBook(book), Code.SUCCESS);
        code = reader.addBook(book);
        assertNotEquals(code, Code.SUCCESS);
        assertEquals(code, Code.BOOK_ALREADY_CHECKED_OUT_ERROR);
    }

    @Test
    void removeBook() {
        Code code;
        Reader reader = new Reader(123,"555-555-5555","John Smith");
        Book book = new Book ("1234","Book Title", "Subject", 5,"Cool Dude", LocalDate.now());

        assertEquals(reader.removeBook(book),Code.READER_DOESNT_HAVE_BOOK_ERROR);
        reader.addBook(book);
        assertEquals((reader.removeBook(book)),Code.SUCCESS);
    }

    @Test
    void hasBook() {
        Code code;
        Reader reader = new Reader(123,"555-555-5555","John Smith");
        Book book = new Book ("1234","Book Title", "Subject", 5,"Cool Dude", LocalDate.now());

        assertFalse(reader.hasBook(book));
        reader.addBook(book);
        assertTrue(reader.hasBook(book));
    }

    @Test
    void getBookCount() {
        Code code;
        int bookCount = 0;
        Reader reader = new Reader(123,"555-555-5555","John Smith");
        Book book = new Book ("1234","Book Title", "Subject", 5,"Cool Dude", LocalDate.now());
        Book book2 = new Book ("4567","Book Tile", "Subect", 5,"Coo Dude", LocalDate.now());
        assertEquals(reader.getBookCount(), bookCount);
        reader.addBook(book);
        bookCount = bookCount + 1;
        assertEquals(reader.getBookCount(), bookCount);
        reader.addBook(book2);
        bookCount = bookCount + 1;
        assertEquals(reader.getBookCount(), bookCount);
    }
}