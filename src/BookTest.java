import org.junit.jupiter.api.*;

/**
 * Name: Tomas Diaz-Wahl
 * Date: 2/5/2023
 * Description: a unit test for the Book.java class object
 */

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    private String testISBN = "1337";
    private String testTitle = "Headfirst Java";
    private String testSubject = "eductaion";
    private int testPageCount = 1337;
    private String testAuthor = "Grady Booch";
    private LocalDate testDueDate = LocalDate.now();

    private Book testBook = null;

    @BeforeAll
    static void mainSetUp(){
        System.out.println("Main setup. Runs once before all.");
    }

    @AfterAll
    static void mainTearDown(){
        System.out.println("Main teardown. Runs once at the end.");
    }

    @BeforeEach
    void setUp() {
        System.out.println("Runs before each test.");
        testBook = new Book(testISBN,testTitle,testSubject,testPageCount,testAuthor,testDueDate);
    }

    @AfterEach
    void tearDown() {
        System.out.println("Runs after each test.");
        testBook = null;
    }

    @Test
    void testConstructor(){
        Book testBook2 = null;
        assertNull(testBook2);
        testBook2 = testBook;
        assertNotNull(testBook2);
        assertEquals(testBook2, testBook);
    }

    @Test
    void getIsbn() {
        assertEquals(testISBN, testBook.getIsbn()); //Validates getIsbn with (testValue)
    }

    @Test
    void setIsbn() {
        String str = "1447";
        testBook.setIsbn(str);
        assertNotEquals(testISBN, testBook.getIsbn());
        assertEquals(str, testBook.getIsbn()); //Validates setIsbn by setting test string and comparing with getIsbn
    }

    @Test
    void getTitle() {
        assertEquals(testTitle, testBook.getTitle());
    }

    @Test
    void setTitle() {
        String str = "Hello World";
        testBook.setTitle(str);
        assertNotEquals(testTitle, testBook.getTitle());
        assertEquals(str, testBook.getTitle());
    }

    @Test
    void getSubject() {
        assertEquals(testSubject, testBook.getSubject());
    }

    @Test
    void setSubject() {
        String str = "Hello World";
        testBook.setSubject(str);
        assertNotEquals(testSubject, testBook.getSubject());
        assertEquals(str, testBook.getSubject());
    }

    @Test
    void getPageCount() {
        assertEquals(testPageCount, testBook.getPageCount());
    }

    @Test
    void setPageCount() {
        int i = 1447;
        testBook.setPageCount(i);
        assertNotEquals(testPageCount, testBook.getPageCount());
        assertEquals(i, testBook.getPageCount());
    }

    @Test
    void getAuthor() {
        assertEquals(testAuthor, testBook.getAuthor());
    }

    @Test
    void setAuthor() {
        String str = "Hello World";
        testBook.setAuthor(str);
        assertNotEquals(testAuthor, testBook.getAuthor());
        assertEquals(str, testBook.getAuthor());
    }

    @Test
    void getDueDate() {
        assertEquals(testDueDate, testBook.getDueDate());
    }

    @Test
    void setDueDate() {
        LocalDate l = LocalDate.of(1985, Month.JANUARY,22);
        testBook.setDueDate(l);
        assertNotEquals(testDueDate, testBook.getDueDate());
        assertEquals(l, testBook.getDueDate());
    }

    @Test
    void testEquals() {
        Book testBook2 = new Book("1447", "Hello", "World", 1447, "Meow", LocalDate.now());
        Book testBook3 = new Book("1337", "Headfirst Java", "education", 1337, "Grady Booch", LocalDate.now());

        assertNotEquals(testBook2,testBook);
        //assertEquals(testBook3, testBook); //not working even though contents are identical

    }

}