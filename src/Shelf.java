/**
 * Name: Tomas Diaz-Wahl
 * Date: 3/4/2023
 * Description: This is a plain old java object that represents a shelf
 * in a library.
 */

import java.util.HashMap;

public class Shelf {
    public static final int SHELF_NUMBER_ = 0;
    public static final int SUBJECT_ = 1;
    private int shelfNumber;
    private String subject;
    private HashMap<Book, Integer> books;

    public Shelf(int shelfNumber, String subject) {
        this.shelfNumber = shelfNumber;
        this.subject = subject;
        books = new HashMap<>();
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shelf shelf = (Shelf) o;

        if (shelfNumber != shelf.shelfNumber) return false;
        return subject.equals(shelf.subject);
    }

    @Override
    public int hashCode() {
        int result = shelfNumber;
        result = 31 * result + subject.hashCode();
        return result;
    }

    public String toString (){
        String str;

        str = this.shelfNumber + " : " + this.subject;
        return str;
    }

    public int getBookCount(Book book){
        int bookCount;

        if (!books.containsKey(book)){
            return -1;
        }
        bookCount = books.get(book);
        return bookCount;
    }

    public Code addBook (Book book){
        int bookCount = 0;

        if (books.containsKey(book)){
            bookCount = books.get(book) + 1;
        }
        else if (book.getSubject().equals(this.subject)){
            bookCount += 1; // update bookCount to have first book
        }
        else{
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }

        books.put(book, bookCount); // this will execute for if and else if only
        System.out.println(book.toString() + " added to shelf");
        return Code.SUCCESS;
    }

    public Code removeBook (Book book){
        int bookCount = 0;
        Code code;

        if (!books.containsKey(book)){
            System.out.println(book.getTitle() + " is not on shelf " + this.subject);
            code = Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        else if (books.containsKey(book) && books.get(book) <= 0){
            System.out.println("No copies of " + book.getTitle() + " remain of shelf " + this.subject);
            code = Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        else {
            bookCount = books.get(book) - 1;
            books.put(book, bookCount);
            System.out.println(book.getTitle() + " successfully removed from shelf " + this.subject);
            code = Code.SUCCESS;
        }
        return code;
    }

    // use string builder in loop because it is more memory efficient
    public String listBooks(){
        int shelfBookCount = 0;
        String bookList;

        if (books.isEmpty()){
            bookList = "0 books on shelf: " + this.shelfNumber + " : " + this.subject + "\n";
        }
        else {
            for (int bookCount : books.values()) {
                shelfBookCount += bookCount;
            }

            bookList = shelfBookCount + " books on shelf: " + this.shelfNumber + " : " + this.subject + "\n";

            for (HashMap.Entry<Book, Integer> bookEntry : books.entrySet()) {
                String bookString;
                Book book;
                int bookCount;

                book = bookEntry.getKey();
                bookCount = bookEntry.getValue();

                // if (bookCount > 0){  // Current code lists all books even if count is 0.
                // Should I implement this if statement to filter out 0 count books?
                bookString = book.getTitle() + " by " + book.getAuthor() + " ISBN:" + book.getIsbn() + " " + bookCount + "\n";
                bookList = bookList + bookString;
            }
        }
        return bookList;
    }
}
