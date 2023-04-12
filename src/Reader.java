/**
 * Name: Tomas Diaz-Wahl
 * Date: 3/4/2023
 * Description: This is a plain old java object that represents a reader
 * in a library.
 */

import java.util.ArrayList;

public class Reader {
    private int cardNumber;
    private String name;
    private String phone;
    // reader can not have more than one copy of any book checked out
    // because no information exists to say how many books they have
    private ArrayList<Book> books;

    public static final int CARD_NUMBER_ = 0;
    public static final int NAME_ = 1;
    public static final int PHONE_ = 2;
    public static final int BOOK_COUNT_ = 3;
    public static final int BOOK_START_ = 4;

    //constructor
    public Reader (int cardNumber, String name, String phone){
        books = new ArrayList<>();
        this.cardNumber = cardNumber;
        this.phone = phone;
        this.name = name;
    }

    public Code addBook (Book book){
        for (int i = 0; i < books.size(); i++){
            if (books.get(i).equals(book)){
                return Code.BOOK_ALREADY_CHECKED_OUT_ERROR;
            }
        }
        books.add(book);
        return Code.SUCCESS;
    }

    public Code removeBook (Book book){
        for (int i = 0; i < books.size(); i++){
            if (books.get(i).equals(book)){
                books.remove(i);
                return Code.SUCCESS;
            }
        }
        return Code.READER_DOESNT_HAVE_BOOK_ERROR;
    }

    public boolean hasBook (Book book){
        for (int i = 0; i < books.size(); i++){
            if (books.get(i).equals(book)){
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reader reader = (Reader) o;

        if (cardNumber != reader.cardNumber) return false;
        if (!name.equals(reader.name)) return false;
        return phone.equals(reader.phone);
    }

    @Override
    public int hashCode() {
        int result = cardNumber;
        result = 31 * result + name.hashCode();
        result = 31 * result + phone.hashCode();
        return result;
    }

    public String toString (){
        String str;
        str = name + " (#" + cardNumber + ") has checked out {";
        if (books.isEmpty()){
            return str + "no books}";
        }
        str = str + books.get(0).toString();
        for (int i = 1; i < books.size(); i++){
            str = ", " + str + books.get(i).toString();
        }
        str = str + "}";
        return str;
    }

    public int getBookCount (){
        int bookCount = 0;
        for (int i = 0; i < books.size(); i++){
            bookCount++;
        }
        return bookCount;
    }

    // getters and setters

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }
}
