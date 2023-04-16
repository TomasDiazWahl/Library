/**
 * Name: Tomas Diaz-Wahl
 * Date: 3/26/2023
 * Description: this class represents a library with books, shelves, and readers
 */



import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;

public class Library {
    public static final int LENDING_LIMIT = 5;
    // this param controls book inventory on shelves vs in the library inventory
    public static final int MAX_BOOKS_ON_SHELF = 5;
    private String name;
    private static int libraryCard;
    private List<Reader> readers;
    private HashMap<String, Shelf> shelves;
    private HashMap<Book, Integer> books;

    // constructor
    public Library (String libraryName){
        this.name = libraryName;
        libraryCard = -1;
        shelves = new HashMap<>();
        books = new HashMap<>();
        readers = new ArrayList<>();
    }

    // method to parse csv file "filename" and initialize books, shelves, and readers
    public Code init(String filename){
        File currentFile = null; // create a file object to store our file
        Scanner readFile = null; // create our scanner object to read from file
        Code initCode = Code.UNKNOWN_ERROR;
        String countStr;
        int bookCount = -1;
        int readerCount = -1;
        int shelfCount = -1;

        try{
            currentFile = new File(filename); // attempt to instantiate file object
        }catch (NullPointerException e){
            System.out.println("Could not find file: " + filename);
        }
        try{
            readFile = new Scanner(currentFile); // attempt to instantiate scanner object
        } catch (FileNotFoundException e) {
            System.out.println("Could not open file "+ filename);
        }

        countStr = readFile.nextLine();
        bookCount  = convertInt(countStr, Code.BOOK_COUNT_ERROR);
        if (bookCount < 0){
            initCode = errorCode(bookCount);
            return initCode;
        }
        initCode = initBooks(bookCount, readFile);
        if(initCode != Code.SUCCESS){
            return initCode;
        }
        System.out.println("Listing books in Library:");
        listBooks();

        countStr = readFile.nextLine();
        shelfCount  = convertInt(countStr, Code.BOOK_COUNT_ERROR);
        if (shelfCount < 0){
            initCode = errorCode(shelfCount);
            return initCode;
        }
        initCode = initShelves(shelfCount, readFile);
        if(initCode != Code.SUCCESS){
            return initCode;
        }
        System.out.println("Listing shelves in Library:");
        listShelves(true);

//        for (Shelf shelf : shelves.values()){
//            System.out.println(shelf.listBooks());
//        }

        countStr = readFile.nextLine();
        readerCount  = convertInt(countStr, Code.BOOK_COUNT_ERROR);
        if (readerCount < 0){
            initCode = errorCode(readerCount);
            return initCode;
        }
        initCode = initReader(readerCount, readFile);
        if(initCode != Code.SUCCESS){
            return initCode;
        }
        System.out.println("Listing all readers:");
        listReaders();
        listBooks();
        System.out.println(""); // need a new line

        return Code.SUCCESS;
    }

    // method to initialize books. It takes an int bookCount that is read in init method and is the number of records to be read
    private Code initBooks (int bookCount, Scanner scan){
        Book currentBook = null; // this will represent the current book read from the CSV file
        String[] currentRecord; // this will represent the current line / record being read
        int currentPageCount = 0; // this will represent the value for page number, we will read as a string and attempt to convert to an int
        Code errorCode = Code.UNKNOWN_ERROR; // this represents our return code
        LocalDate currentDueDate; // this will represent the value for dueDate which will be read as a string and converted into a local date object

        System.out.println("Parsing " + bookCount + " books");
        if (bookCount < 1){ // we check to make sure that the number of records to be read is not less than 1
            errorCode = Code.LIBRARY_ERROR;
            return errorCode;
        }

        for (int i = 0; i < bookCount; i++){ // for loop to read each record
            if (!scan.hasNextLine()){ // check to make sure that there is a record to read
                errorCode = Code.UNKNOWN_ERROR;
                return errorCode;
            }

            String bookStr = scan.nextLine();
            System.out.println("Parsing book: " + bookStr);
            currentRecord = bookStr.split("[,]"); // we split the record along commas and store it into a string array

            if (currentRecord.length < (Book.DUE_DATE_ + 1)){ // check to make sure the record is of the length we expect
                errorCode = Code.UNKNOWN_ERROR;
                return errorCode;
            }

            for (int j = 0; j < currentRecord.length; j++){ // for loop to check our string array is filled with data
                if (currentRecord[j].equals("")){
                    errorCode = Code.UNKNOWN_ERROR;
                    return errorCode;
                }
            }

            currentPageCount = convertInt(currentRecord[Book.PAGE_COUNT_], Code.PAGE_COUNT_ERROR); // we attempt to convert string to int
            if (currentPageCount <= 0){
                errorCode = Code.PAGE_COUNT_ERROR;
                return errorCode;
            }

            currentDueDate = convertDate(currentRecord[Book.DUE_DATE_], Code.DATE_CONVERSION_ERROR); // attempt to convert christian to satanism
            if (currentDueDate == null){
                errorCode = Code.DATE_CONVERSION_ERROR;
                return errorCode;
            }

            currentBook = new Book (currentRecord[Book.ISBN_], currentRecord[Book.TITLE_], currentRecord[Book.SUBJECT_], // instantiate book from record
                                    currentPageCount, currentRecord[Book.AUTHOR_], currentDueDate);
            this.addBook(currentBook);

            errorCode = Code.SUCCESS;
        } // end read record for loop


        System.out.println(errorCode.getMessage());
        return errorCode;
    } // end initBooks

    // method to initialize shelves
    private Code initShelves(int shelfCount, Scanner scan){
        Code shelfCode = Code.UNKNOWN_ERROR;
        Shelf currentShelf = null; // this will represent the current book read from the CSV file
        String[] currentRecord; // this will represent the current line / record being read
        int shelfNumber; // this will represent the value for page number, we will read as a string and attempt to convert to an int

        System.out.println("\nParsing " + shelfCount + " shelves");
        if (shelfCount < 1){
            shelfCode = Code.SHELF_COUNT_ERROR;
        }
        else{
            for (int i = 0; i < shelfCount; i++){
                if (!scan.hasNextLine()){
                    shelfCode = Code.SHELF_NUMBER_PARSE_ERROR;
                }
                else {
                    String shelfStr = scan.nextLine();
                    System.out.println("Parsing shelf: " + shelfStr);
                    currentRecord = shelfStr.split("[,]"); // we split the record along commas and store it into a string array
                    if (currentRecord.length < (Shelf.SUBJECT_ + 1)) { // check to make sure the record is of the length we expect
                        shelfCode = Code.UNKNOWN_ERROR;
                    }
                    else {
                        String shelfSubject = currentRecord[Shelf.SUBJECT_]; // get shelf subject from parsed value
                        shelfNumber = convertInt(currentRecord[Shelf.SHELF_NUMBER_], Code.SHELF_NUMBER_PARSE_ERROR);
                        currentShelf = new Shelf(shelfNumber, shelfSubject);
                        System.out.println("Populating Shelves");
                        this.addShelf(currentShelf);
                    }
                }
            } // end read record for loop
            if (shelves.size() != shelfCount){
                System.out.println("Number of shelves doesn't match expected");
                shelfCode = Code.SHELF_NUMBER_PARSE_ERROR;
            }
            else{
                shelfCode = Code.SUCCESS;
                System.out.println("SUCCESS");
            }
        }

        return shelfCode;
    }

    // next init method bruv
    private Code initReader (int readerCount, Scanner scan){
        Code readerCode = Code.UNKNOWN_ERROR;
        Reader reader = null; // this will represent the current book reader from the CSV file
        String[] currentRecord; // this will represent the current line / record being read
        int shelfNumber; // this will represent the value for page number, we will read as a string and attempt to convert to an int

        System.out.println("Parsing " + readerCount + " readers");
        if (readerCount < 1){
            readerCode = Code.READER_COUNT_ERROR;
        }
        else{
            for (int i = 0; i < readerCount; i++){
                if (!scan.hasNextLine()){
                    readerCode = Code.UNKNOWN_ERROR;
                }
                else {
                    String readerStr = scan.nextLine();
                    System.out.println("Parsing reader: " + readerStr);
                    currentRecord = readerStr.split("[,]"); // we split the record along commas and store it into a string array
                    if (currentRecord.length < (Reader.BOOK_COUNT_ + 1)) { // check to make sure the record is of the length we expect
                        readerCode = Code.UNKNOWN_ERROR;
                    }
                    else {
                        int tempNumber = convertInt(currentRecord[Reader.CARD_NUMBER_], Code.READER_CARD_NUMBER_ERROR);
                        reader = new Reader(tempNumber,
                                currentRecord[Reader.NAME_],
                                currentRecord[Reader.PHONE_]);
                        addReader(reader);
                        int bookCount = convertInt(currentRecord[Reader.BOOK_COUNT_], Code.READER_COUNT_ERROR);
                        // this checks the number of different books this reader has (variable length record)
                        if (currentRecord.length <= (Reader.BOOK_COUNT_ + bookCount * 2)){
                            readerCode = Code.UNKNOWN_ERROR;
                        }
                        else{
                            for (int j = Reader.BOOK_START_; j < (Reader.BOOK_START_ + 2 * bookCount); j += 2){
                                String tempIsbn = currentRecord[j];
                                LocalDate tempDate = convertDate(currentRecord[j + 1], Code.DATE_CONVERSION_ERROR);
                                Book verifyBook = getBookByISBN(tempIsbn);
                                if (verifyBook != null){
                                    readerCode = checkOutBook(reader, verifyBook);
                                    System.out.println(readerCode.getMessage());
                                }
                                else{
                                    System.out.println("ERROR");
                                }
                            } // end for loop checkout book
                        }
                    } // end small else
                } // end medium else
            } // end read record for loop
        } // end big else

        return readerCode;
    }

    // adds a new book object to the HashMap in Library
    public Code addBook (Book newBook){
        Integer bookCount = 0; // this will hold the current number of books
        Code addBookCode = Code.UNKNOWN_ERROR; // return code
        String currentSubject = newBook.getSubject(); // String to hold current book subject
        Shelf matchingSubjectShelf; // shelf object that matches passed in book subject

        if (books.containsKey(newBook)){ // check if the book is in the books HashMap in Library
            bookCount = books.get(newBook); // get the current book count
            bookCount += 1; // update the book count
            books.put(newBook, bookCount); // update the HashMap
            // The line that follows is per the assignment instructions but uncommented line matches main.java
            // System.out.println(this.getBookCount(newBook) + " copies of " + newBook.getTitle() + " in the library.");
            System.out.println(this.getBookCount(newBook) + " copies of " + newBook + " in the library.");
        }
        else{
            bookCount = 1;
            books.put(newBook, bookCount);
            // The line that follows is per the assignment instructions but uncommented line matches main.java
            // System.out.println(newBook.getTitle() + " added to stacks.");
            System.out.println(newBook + " added to stacks.");
        }

        Shelf matchingShelf = shelves.get(newBook.getSubject());
        if (matchingShelf == null){
            System.out.println("No shelf for subject " + newBook.getSubject() + " books");
            addBookCode = Code.SUCCESS;
        }
        else{
            Code populateShelfCode = populateShelf(newBook, matchingShelf);
            switch (populateShelfCode) {
                case UNKNOWN_ERROR, BOOK_NOT_IN_INVENTORY_ERROR -> {
                    return populateShelfCode;
                }
                case SHELF_EXISTS_ERROR -> {
                    System.out.println("No shelf for " + newBook.getSubject() + " books");
                }
                case SUCCESS -> {
                    addBookCode = Code.SUCCESS;
                }
            }
        }

        return addBookCode;
    }

    // method not used as it causes unusual side effects
    private Code addBookToShelf(Book book, Shelf shelf){
        Code addBookToShelfCode = Code.UNKNOWN_ERROR;
        addBookToShelfCode = returnBook(book);

        if (addBookToShelfCode.equals(Code.SUCCESS)){
            return addBookToShelfCode;
        }

        if (shelf == null){
            return Code.SHELF_EXISTS_ERROR;
        }
        if (book == null){
            return Code.UNKNOWN_ERROR;
        }

        addBookToShelfCode = shelf.addBook(book);
        if (!addBookToShelfCode.equals(Code.SUCCESS)){
            System.out.println(addBookToShelfCode.getMessage());
        }
        return addBookToShelfCode;
    }

    // addShelf method adds a shelf to the library
    public Code addShelf(String shelfSubject){
        int shelfNumber = shelves.size() + 1;
        Shelf shelf = new Shelf(shelfNumber, shelfSubject);
        return addShelf(shelf);
    }

    public Code addShelf(@NotNull Shelf shelf){ // should be private? Could have conflicting shelf number issue
        Code addShelfCode;
        if (shelves.containsKey(shelf.getSubject())){
            System.out.println("ERROR: Shelf already exists " + shelf.getSubject());
            addShelfCode = Code.SHELF_EXISTS_ERROR;
        }
        else{
            int shelfNumberFromShelf = shelf.getShelfNumber();
            int shelfNumberFromShelves = 0;

            // finds the highest shelf number in the shelves hashmap
            for (Map.Entry<String,Shelf> entry : shelves.entrySet()){ // iterator to step through shelves hashmap
                Shelf tempShelf = entry.getValue();
                int tempShelfNumber = tempShelf.getShelfNumber();
                if (tempShelfNumber > shelfNumberFromShelves){
                    shelfNumberFromShelves = tempShelfNumber;
                }
            }

            // if no shelves are deleted this if statement will never execute
            // this if statement protects against invalid shelf numbers
            if (shelf.getShelfNumber() != (shelfNumberFromShelves + 1)){
                shelf.setShelfNumber(shelfNumberFromShelves + 1);
            }
            shelves.put(shelf.getSubject(),shelf);

            // add library books to new shelf
            populateShelves(true);

            addShelfCode = Code.SUCCESS;
        }

        return addShelfCode;
    }

    // add reader method
    public Code addReader (Reader reader){
        boolean readerExists = false;
        boolean sameCardNumber = false;
        Code addReaderCode = Code.UNKNOWN_ERROR;
        for (Reader r : readers) {
            if(r.equals(reader)){
                readerExists = true;
                System.out.println(reader.getName() + " already has an account!");
                addReaderCode = Code.READER_ALREADY_EXISTS_ERROR;
                break;
            }
            if (r.getCardNumber() == reader.getCardNumber()){
                sameCardNumber = true;
                System.out.println(r.getName() + " and " + reader.getName() + " have the same card number!");
                addReaderCode = Code.READER_CARD_NUMBER_ERROR;
                break;
            }
        }
        if (!readerExists && !sameCardNumber){
            readers.add(reader);
            if (reader.getCardNumber() > libraryCard){
                libraryCard = reader.getCardNumber();
            }
            System.out.println(reader.getName() + " added to the library!");
            addReaderCode = Code.SUCCESS;
        }

        return addReaderCode;
    }

    private Code removeBook(Book book){
        Code removeBookCode = Code.UNKNOWN_ERROR; // return code
        Shelf matchingSubjectShelf; // shelf object that matches passed in book subject

        // check to see if the book is on a matching shelf
        if (!shelves.containsKey(book.getSubject())){
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        matchingSubjectShelf = shelves.get(book.getSubject());
        populateShelves(false);
        int shelfCount = matchingSubjectShelf.getBookCount(book);
        // if yes check the book count
        if (shelfCount == 0){
            System.out.println("No copies of book: " + book + " remain in the stacks.");
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        removeBookCode = matchingSubjectShelf.removeBook(book);
        if (removeBookCode.equals(Code.SUCCESS)){
            System.out.println(book + " successfully removed from shelf " + matchingSubjectShelf.getSubject());
        }
        else{
            System.out.println(book + " failed to remove from shelf " + matchingSubjectShelf.getSubject());
        }
        return removeBookCode;
    }

    // removeReader method
    public Code removeReader(Reader reader){
        int readerIndex = readers.indexOf(reader);
        Code removeReaderCode = Code.UNKNOWN_ERROR;

        if (readerIndex < 0){
            System.out.println(reader.getName() + " is not part of this Library");
            removeReaderCode = Code.READER_NOT_IN_LIBRARY_ERROR;
        }
        else if (reader.getBookCount() > 0){
            System.out.println(reader.getName() + " must return all books!");
            removeReaderCode = Code.READER_STILL_HAS_BOOKS_ERROR;
        }
        else{
            readers.remove(readerIndex);
            removeReaderCode = Code.SUCCESS;
        }

        return removeReaderCode;
    }

    // getByISBN
    public Book getBookByISBN (String isbn){
        for (Book book : books.keySet()){ // iterator to step through books hashmap
            String tempISBN = book.getIsbn();
            if (tempISBN.equals(isbn)){
                return book;
            }
        }
        System.out.println("ERROR: Could not find a book with isbn: " + isbn);
        return null;
    }

    // getShelf method
    public Shelf getShelf(Integer shelfNumber){
        Integer num = 0;

        for (Map.Entry<String, Shelf> entry : shelves.entrySet()){
            num = entry.getValue().getShelfNumber();
            if (num.equals(shelfNumber)){
                return entry.getValue();
            }
        }
        System.out.println("No shelf number " + shelfNumber + " found");
        return null;
    }

    public Shelf getShelf(String subject){
        if (shelves.containsKey(subject)){
            return shelves.get(subject);
        }
        System.out.println("No shelf for " + subject + " books");
        return null;
    }

    // getReader method
    public Reader getReaderByCard(int cardNumber){
        for (Reader reader : readers){
            if (cardNumber == reader.getCardNumber()){
                return reader;
            }
        }

        System.out.println("Could not find reader with card #" + cardNumber);
        return null;
    }

    // get library card number method
    public static int getLibraryCardNumber(){
        return libraryCard + 1;
    }

    // Check out book method
    public Code checkOutBook(Reader reader, Book book){
        int readerIndex = readers.indexOf(reader);
        if (readerIndex < 0){
            System.out.println(reader.getName() + " doesn't have an account here");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }
        if (reader.getBookCount() >= LENDING_LIMIT){
            System.out.println(reader.getName() + " has reached the lending limit, (" + LENDING_LIMIT + ")");
            return Code.BOOK_LIMIT_REACHED_ERROR;
        }
        if (!books.containsKey(book)){
            System.out.println("ERROR: could not find " + book);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        if (!shelves.containsKey(book.getSubject())){
            System.out.println("no shelf for " + book.getSubject() + " books!");
            return Code.SHELF_EXISTS_ERROR;
        }

        // book added to reader inventory
        Reader mumenReader = readers.get(readerIndex);

        // book removed from library inventory and shelf inventory
        Code removeBookCode = removeBook(book);
        if (!removeBookCode.equals(Code.SUCCESS)){
            System.out.println("Couldn't checkout " + book);

            return removeBookCode;
        }

        Code tempCode = mumenReader.addBook(book);

        if (!tempCode.equals(Code.SUCCESS)){
            // add book back to library
            this.addBook(book);
            System.out.println("Couldn't checkout " + book);
            return tempCode;
        }

        return Code.SUCCESS;
    }

    // method for checking if reader exists in library
//    private boolean readerInLibrary(Reader reader){
//        for (Reader r : readers) {
//            if (r.equals(reader)) {
//                return true;
//            }
//        }
//        return false;
//    }

    // return book method returns a book to the library
    public Code returnBook(Reader reader, Book book){
        ArrayList<Book> booksFound = reader.getBooks();
        Code returnBookCode = Code.UNKNOWN_ERROR;

        if (!booksFound.contains(book)){
            System.out.println(reader.getName() + " doesn't have " + book.getTitle() + " checked out");
            returnBookCode = Code.READER_DOESNT_HAVE_BOOK_ERROR;
        }
        else if (!books.containsKey(book)){
            returnBookCode = Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        else{
            System.out.println(reader.getName() + " is returning " + book);
            returnBookCode = returnBook(book);

            if (!returnBookCode.equals(Code.SUCCESS)){
                System.out.println("Could not return " + book);
            }
            reader.removeBook(book);
        }

        return returnBookCode;
    }

    // returnBook

    public Code returnBook(Book book){
        Code returnBookCode = Code.UNKNOWN_ERROR;

        if (!shelves.containsKey(book.getSubject())){
            System.out.println("No shelf for " + book);
            returnBookCode = Code.SHELF_EXISTS_ERROR;
        }
        else{
            Shelf shelf = shelves.get(book.getSubject());
            // assignment instructions said to call returnBook in addBookToShelf, but I think this causes a logical error
            // instead returnBook() calls Library.addBook which is why line below is commented out
            // returning the book to the shelf alone does not return the book to the library books
            //returnBookCode = shelf.addBook(book);
            returnBookCode = addBook(book);
        }

        return returnBookCode;
    }

    private Code populateShelf(Book book, Shelf shelf){
        Code populateShelfCode = Code.UNKNOWN_ERROR;
        if (shelf == null || book == null){
            System.out.println("Shelf or book parameter is null");
            return Code.UNKNOWN_ERROR;
        }

        // check if the shelf exists in the library
        if (shelves.containsKey(shelf.getSubject())){
            if (!books.containsKey(book)){
                return Code.BOOK_NOT_IN_INVENTORY_ERROR;
            }

            int libraryCount = books.get(book);

            //check if there exists in the library to add to the shelf
            if (libraryCount == 0){
                populateShelfCode = Code.LIBRARY_OUT_OF_BOOKS_ERROR;
                return populateShelfCode;
            }

            populateShelfCode = shelf.addBook(book);
            // Transfer a book from library books to shelf
            if (populateShelfCode.equals(Code.SUCCESS)){
                libraryCount--;
                books.put(book,libraryCount);
            }
        }
        else{
            populateShelfCode = Code.SHELF_EXISTS_ERROR;
        }

        return populateShelfCode;
    }

    private Code populateShelves(Boolean printTotalBooks){
        Code populateShelvesCode = Code.UNKNOWN_ERROR;
        int booksAddedCount = 0;

        for (Map.Entry<Book, Integer> entry : books.entrySet()){
            // check to see if the current book subject matches a subject of an existing shelf
            if(shelves.containsKey(entry.getKey().getSubject())){
                int addCount = entry.getValue();
                if(addCount > Library.MAX_BOOKS_ON_SHELF){
                    addCount = Library.MAX_BOOKS_ON_SHELF;
                }

                // get reference to book from HashMap and reference to matching shelf from shelves HashMap
                Book bookToAdd = entry.getKey();
                Shelf matchingShelf = shelves.get(bookToAdd.getSubject());

                for (int i = 0; i < addCount; i++){
                    populateShelvesCode =  populateShelf(bookToAdd, matchingShelf);
                    if (populateShelvesCode.equals(Code.SUCCESS)){
                        booksAddedCount++;
                    }
                    else if (populateShelvesCode.equals(Code.SHELF_EXISTS_ERROR)){
                        System.out.println("No shelf exists for this subject: " + bookToAdd.getSubject());
                        System.out.println("Book not added");
                    }
                    else if (populateShelvesCode.equals(Code.SHELF_SUBJECT_MISMATCH_ERROR)){
                        System.out.println("Book " + bookToAdd + " subject mismatch with shelf " + matchingShelf);
                    }
                    else{
                        System.out.println("Unexpected Null Book Error. Null book not added to shelf.");
                    }
                }
            }
        }

        if (printTotalBooks){
            System.out.println(booksAddedCount + " book titles added to shelves");
        }
        return populateShelvesCode;
    }

    private int getBookCount(Book book){
        int numberOfBooks = 0;

        HashMap<Book, Integer> allBooks = new HashMap<>();
        numberOfBooks = books.get(book);

        Shelf shelf = shelves.get(book.getSubject());
        if (shelf != null){
            numberOfBooks += shelf.getBookCount(book);
        }

        return numberOfBooks;
    }

    // listBooks method lists all the books in the library
    public int listBooks(){
        int numberOfBooks = 0;

        HashMap<Book, Integer> allBooks = new HashMap<>();

        for (Map.Entry<Book, Integer> entry : books.entrySet()){
            allBooks.put(entry.getKey(), entry.getValue());
            numberOfBooks += entry.getValue();
        }

        for (Shelf shelf : shelves.values()){
            for (Map.Entry<Book, Integer> entry : shelf.getBooks().entrySet()){
                int numOnShelf = entry.getValue();
                int numInLib = allBooks.get(entry.getKey());
                allBooks.put(entry.getKey(), numInLib + numOnShelf);
                numberOfBooks += numOnShelf + numInLib;
            }
        }

        for (Map.Entry<Book, Integer> entry : allBooks.entrySet()){
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        System.out.println("Listing books out to readers: ");
        for (Reader reader : readers){
            System.out.println(reader);
            for (Book book : reader.getBooks()){
                numberOfBooks++;
            }
        }

        // System.out.println(entry.getValue() + " copies of " + entry.getKey() + " remain in the stacks.");
        return numberOfBooks;
    }

    // list shelves method
    public Code listShelves (boolean showBooks){


        for (Map.Entry<String, Shelf> entry : shelves.entrySet()){
            Shelf currentShelf = entry.getValue();
            if (showBooks){
                System.out.println(currentShelf.listBooks());
            }
            else{
                System.out.println(currentShelf);
            }

//            int bookCount = currentShelf.getTotalBookCount();
//            String str = "";
//            if (bookCount == 1){
//                str = "1 book ";
//            }
//            else {
//                str = bookCount + " books ";
//            }
//            System.out.println(str + "on shelf: " + currentShelf);

        }

        return Code.SUCCESS;
    }

    // listReaders method
    public int listReaders(){
        for (Reader aReader : readers){
            System.out.println(aReader);
        }
        return readers.size();
    }

    public int listReaders(boolean showBooks){
        if (showBooks){
            for (Reader reader : readers){
                String str;
                str = reader.getName() + " (#" + reader.getCardNumber() + ") has the following books: ";
                System.out.println(str);
                ArrayList<Book> readerBooks = reader.getBooks();
                System.out.print("[");
                int counter = 0;
                for (Book book : readerBooks){
                    if (counter != 0){
                        System.out.print(", ");
                    }
                    System.out.print(book);
                    counter++;
                }
                System.out.println("]");
            }
        }
        else{
            for (Reader reader : readers){
                System.out.println(reader);
            }
        }

        return readers.size();
    }

    // convert Int method
    public static int convertInt(String recordCountString, Code code){
        int num = -1;
        try{
            num = Integer.parseInt(recordCountString);
        } catch(NumberFormatException e) {
            System.out.println("Value which caused the error: [recordCountString]");
            System.out.println("Error message: " + code.getMessage());
            num = code.getCode();
        }
        return num;
    }

    // convert date method
    public static LocalDate convertDate(String date, Code errorCode){
        String[] checkDateFormat;
        int[] dateNotZero = new int[3];
        checkDateFormat= date.split("-");
        LocalDate localDate = LocalDate.of(1970, 01,01); // initialize local date to default date
        final int YEARIDX = 0;
        final int MONTHIDX = 1;
        final int DAYIDX = 2;

        if (date.equals("0000")){
            System.out.println("Using default date (01-jan-1970)");
        }
        else if (checkDateFormat.length != 3){
            System.out.println("ERROR: date conversion error, could not parse " + date);
            System.out.println("Using default date (01-jan-1970)");
        }
        else{
            for (int i = 0; i < checkDateFormat.length; i++){
                dateNotZero[i] = convertInt(checkDateFormat[i], errorCode);
            }
            if ((dateNotZero[YEARIDX] < 0) || (dateNotZero[MONTHIDX] < 0) || (dateNotZero[DAYIDX] < 0)){
                System.out.println("Year " + dateNotZero[YEARIDX]);
                System.out.println("Month " + dateNotZero[MONTHIDX]);
                System.out.println("Day " + dateNotZero[DAYIDX]);
                System.out.println("Using default date (01-jan-1970)");
            }
            else{
                localDate = LocalDate.parse(date);
            }
        }
        return localDate;
    }

    private Code errorCode(int codeNumber) {
        for (Code code : Code.values()) {
            if (code.getCode() == codeNumber) {
                return code;
            }
        }
        return Code.UNKNOWN_ERROR;
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int getLibraryCard() {
        return libraryCard;
    }

    public static void setLibraryCard(int libraryCard) {
        Library.libraryCard = libraryCard;
    }
}
