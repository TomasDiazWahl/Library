/**
 * Name: Tomas Diaz-Wahl
 * Date: 3/4/2023
 * Description: This tests the Shelf class
 */

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;

import static java.lang.System.exit;
import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {
    private int testShelfNumber = 10; // arbitrary shelf number for testing
    private String testShelfSubject = "education";
    private Shelf testShelf = null;
    private Book testBook = null;
    private String testISBN = "1337";
    private String testTitle = "Headfirst Java";
    private String testBookSubject = "education";
    private String wrongBookSubject = "sci-fi";
    private int testPageCount = 1337;
    private String testAuthor = "Grady Booch";
    private LocalDate testDueDate = LocalDate.now();
    private boolean booksHaveBeenAdded = false;

    @BeforeEach
    void setUp() {
        testShelf = new Shelf(testShelfNumber, testShelfSubject);
        testBook = new Book(testISBN,testTitle,testBookSubject,testPageCount,testAuthor,testDueDate);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testConstructor(){
        Shelf testShelf2 = null;
        assertNull(testShelf2);
        testShelf2 = testShelf;
        assertNotNull(testShelf2);
        assertEquals(testShelf, testShelf2);
    }

    @Test
    void getShelfNumber() {
        int testNumber = 10;
        assertEquals(testNumber, testShelf.getShelfNumber());
    }

    @Test
    void setShelfNumber() {
        int testNumber = 15; // arbitrary number that is different from the shelf number
        assertNotEquals(testNumber, testShelf.getShelfNumber()); // make sure shelf number got initialized correctly
        testShelf.setShelfNumber(testNumber);
        assertEquals(testNumber, testShelf.getShelfNumber());
    }

    @Test
    void getSubject() {
        String rightSubject = "sci-fi"; // arbitrary subject to test shelf is initialized with subject
        assertEquals(testShelf.getSubject(),rightSubject);
    }

    @Test
    void setSubject() {
        String arbSubject = "romance"; // arbitrary subject to test shelf is initialized with subject
        assertNotEquals(arbSubject, testShelf.getSubject());
        testShelf.setSubject(arbSubject);
        assertEquals(arbSubject, testShelf.getSubject());
    }

    @Test
    void getBooks() {
        Shelf emptyShelf = null;
        HashMap <Book, Integer> testEmptyHashMap = new HashMap<>();
        HashMap <Book, Integer> testHashMap = new HashMap<>();
        HashMap <Book, Integer> bookHashMap;
        int manualBookCount = 1;

        assertNull(emptyShelf);
        emptyShelf = new Shelf(this.testShelfNumber, this.testShelfSubject);
        bookHashMap = emptyShelf.getBooks();
        assertEquals(testEmptyHashMap, bookHashMap);

        this.testShelf.addBook(this.testBook);
        testHashMap.put(this.testBook, manualBookCount);
        bookHashMap = this.testShelf.getBooks();
        assertEquals(testHashMap, bookHashMap);
    }

    @Test
    void setBooks() {
        HashMap <Book, Integer> bookHashMap  = new HashMap<>();
        HashMap <Book, Integer> testHashMap;
        Book wrongSubject = new Book (testISBN,testTitle,wrongBookSubject,testPageCount,testAuthor,testDueDate);
        int manualBookCount = 1;
        Integer iBookCount = manualBookCount;

        bookHashMap.put(testBook, manualBookCount);
        this.testShelf.setBooks(bookHashMap);
        testHashMap = this.testShelf.getBooks();
        assertEquals(bookHashMap, testHashMap);
    }

    @Test
    void testEquals() {
        Shelf shelfFish = new Shelf (this.testShelfNumber, this.testShelfSubject);
        boolean expectedVeracity;
        boolean actualVeracity;

        actualVeracity = this.testShelf.equals(this.testShelf);
        assertTrue(actualVeracity);

        actualVeracity= this.testShelf.equals(null);
        assertFalse(actualVeracity);

        actualVeracity = this.testShelf.equals(this.testBook);
        assertFalse(actualVeracity);

        shelfFish = new Shelf(this.testShelfNumber + 1, this.testShelfSubject);
        actualVeracity = this.testShelf.equals(shelfFish);
        assertFalse(actualVeracity);

        shelfFish = new Shelf(this.testShelfNumber, this.wrongBookSubject);
        actualVeracity = this.testShelf.equals(shelfFish);
        assertFalse(actualVeracity);

        shelfFish = new Shelf(this.testShelfNumber, this.testShelfSubject);
        actualVeracity = this.testShelf.equals(shelfFish);
        assertTrue(actualVeracity);
    }

    @Test
    void testHashCode() {
        Shelf shelfFish = new Shelf(this.testShelfNumber,this.testShelfSubject);
        int actualHashCode;
        int shelfFishCode;

        actualHashCode = this.testShelf.hashCode();
        shelfFishCode = shelfFish.hashCode();
        assertEquals(actualHashCode, shelfFishCode);

        shelfFish = new Shelf(this.testShelfNumber + 1, this.wrongBookSubject);
        actualHashCode = this.testShelf.hashCode();
        shelfFishCode = shelfFish.hashCode();
        assertNotEquals(actualHashCode, shelfFishCode);
    }

    @Test
    void testToString() {
        String expectedString = this.testShelfNumber + " : " + this.testShelfSubject;
        String actualString;

        actualString = this.testShelf.toString();
        assertEquals(expectedString,actualString);
    }

    @Test
    void getBookCount() {
        Random randomNumber = new Random();
        int numberOfBooks = randomNumber.nextInt(10) + 1;
        Book wrongBook = new Book(testISBN + 1,testTitle + 1,testBookSubject + 1,testPageCount + 1,testAuthor +1,testDueDate);

        for (int i = 0; i < numberOfBooks; i++){
            this.testShelf.addBook(this.testBook);
        }

        assertEquals(numberOfBooks, this.testShelf.getBookCount(this.testBook));

        this.testShelf.removeBook(this.testBook);

        assertNotEquals(numberOfBooks, this.testShelf.getBookCount(this.testBook));

        for (int i = 0; i < (numberOfBooks - 1); i++){
            this.testShelf.removeBook(this.testBook);
        }

        assertEquals(0, this.testShelf.getBookCount(this.testBook));

        assertEquals(-1, this.testShelf.getBookCount(wrongBook));
    }

    @Test
    void addBook() {
        Shelf emptyShelf = null;
        Book wrongSubject = new Book (testISBN,testTitle,wrongBookSubject,testPageCount,testAuthor,testDueDate);
        int expectedBookCount = 1;
        int actualBookCount;
        Code errorCode;

        errorCode = testShelf.addBook(this.testBook);
        assertEquals(Code.SUCCESS, errorCode);
        actualBookCount = this.testShelf.getBookCount(this.testBook);
        assertEquals(expectedBookCount, actualBookCount);

        errorCode = testShelf.addBook(this.testBook);
        assertEquals(Code.SUCCESS, errorCode);
        actualBookCount = this.testShelf.getBookCount(this.testBook);
        assertEquals(expectedBookCount + 1, actualBookCount);

        errorCode = testShelf.addBook(wrongSubject);
        assertEquals(Code.SHELF_SUBJECT_MISMATCH_ERROR, errorCode);
    }

    @Test
    void removeBook() {
        Book wrongBook = new Book (testISBN,testTitle,wrongBookSubject,testPageCount,testAuthor,testDueDate);
        Code errorCode;
        int bookCount;

        errorCode = this.testShelf.removeBook(wrongBook);
        assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, errorCode);

        errorCode = this.testShelf.addBook(this.testBook);
        assertEquals(Code.SUCCESS, errorCode);
        bookCount = this.testShelf.getBookCount(this.testBook);
        assertEquals(1, bookCount);

        errorCode = this.testShelf.removeBook(this.testBook);
        assertEquals(Code.SUCCESS, errorCode);
        bookCount = this.testShelf.getBookCount(this.testBook);
        assertEquals(0, bookCount);
    }

    @Test
    void listBooks() {
        String expectedString = "0 books on shelf: " + this.testShelfNumber + " : " + this.testShelfSubject + "\n";
        String actualString;

        actualString = this.testShelf.listBooks();
        assertEquals(expectedString, actualString);

        expectedString = "1" + " books on shelf: " + this.testShelfNumber + " : " + this.testShelfSubject + "\n";;

        this.testShelf.addBook(this.testBook);
        expectedString = expectedString + this.testTitle + " by " + this.testAuthor + " ISBN:" + this.testISBN + " 1\n";
        actualString = this.testShelf.listBooks();
        assertEquals(expectedString, actualString);
    }
}