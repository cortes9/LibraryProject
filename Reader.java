import Utilities.Code;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: Luis Cortes Cedillo
 * Assigment: Reader.java
 */

public class Reader {
    public static final int CARD_NUMBER_ = 0;
    public static final int NAME_ = 1;
    public static final int PHONE = 2;
    public static final int BOOK_COUNT_ = 3;
    public static final int BOOK_START_ = 4;

    private int cardNumber;
    private String name;
    private String phone;
    private List<Book> books;

    public Reader(int cardNumber, String name, String phone){
        this.cardNumber = cardNumber;
        this.name = name;
        this.phone = phone;
        this.books = new ArrayList<>();

    }

    public Code addBook(Book book){
        if (hasBook(book)){
            return Code.BOOK_ALREADY_CHECKED_OUT_ERROR;
        }
        book.add(book);
        return Code.SUCCESS;

    }

    public Code removeBook(Book book){
        if (!hasBook(book)){
            return Code.READE_DOSENT_HAVE_BOOK_ERROR;
        }
        if (books.remove(book)){
            return Code.SUCCESS;
        }
        return Code.READER_COULD_NOT_REMOVE_BOOK_ERROR
    }

    public boolean hasBook(Book book){
        return book.contains(book);
    }

    public int getBookCount(){
        return books.size();
    }

    public int getBookSCount(){
        return books.size();
    }

    public List<Book> getBooks(){
        return books;

    }


}

