/*
    File Name: ReaderTest.java
    Abstract: This file holds test variables and objects as well as
              tests for the constructor, getters and setters, and other
              methods found within Reader.java.
    Author: Andrea Ultreras
    Date: 02/22/21
 */
import Utilities.Code;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {
    //DECLARE VARIABLES--------------------------------------------------------
    Reader read;
    Reader read1;
    Reader read2;
    Reader read3;
    Reader read4;

    int cardNumber;
    String name;
    String phone;
    List<Book> books;

    int cardNumber_;
    String name_;
    String phone_;
    List<Book> books_;

    Book bookTest;

    //CONSTRUCTOR & DESTRUCTOR-------------------------------------------------
    @BeforeEach
    void setUp() {
        //constructor test---
        read =null;
        assertNull(read);
        read = new Reader(0, "", "");
        assertNotNull(read);

        //field setting and getter test---
        cardNumber = 1;
        name = "Drew Clinkenbeard";
        phone = "831-582-4007";
        read1 = new Reader(cardNumber, name, phone);

        //equality test---
        read2 = new Reader(2, "Jennifer Clinkenbeard", "831-555-6284");
        read3 = new Reader(2, "Jennifer Clinkenbeard", "831-555-6284");

        //setter test---
        read4 = new Reader(cardNumber, name, phone);
        cardNumber_ = 3;
        name_ = "Monte Ray";
        phone_ = "555-555-4444";

        //other variable declarations---
        bookTest = new Book("5297", "Count of Monte Cristo", "Adventure", 999, "Alexandrea Dumas", LocalDate.of(2021, 1, 1));

    }

    @AfterEach
    void tearDown() {
        read = null;
        read1 = null;
        read2 = null;
        read3 = null;
        read4 = null;
        cardNumber = 0;
        name = null;
        phone = null;
        books = null;
        cardNumber_ = 0;
        name_ = null;
        phone_ = null;
        books_ = null;
    }

    //GETTERS AND SETTERS------------------------------------------------------
    @Test
    void getCardNumber() {
        assertEquals(cardNumber, read1.getCardNumber());                //field setting and getter test
        assertNotEquals(read1.getCardNumber(), read2.getCardNumber());  //equality test
        assertEquals(read2.getCardNumber(), read3.getCardNumber());     //equality test
    }

    @Test
    void setCardNumber() {
        read4.setCardNumber(cardNumber_);                   //set value to new parameter, setter test
        assertNotEquals(cardNumber, read4.getCardNumber()); //setter test
        assertEquals(cardNumber_, read4.getCardNumber());   //setter test
    }

    @Test
    void getName() {
        assertEquals(name, read1.getName());
        assertNotEquals(read1.getName(), read2.getName());
        assertEquals(read2.getName(), read3.getName());
    }

    @Test
    void setName() {
        read4.setName(name_);
        assertNotEquals(name, read4.getName());
        assertEquals(name_, read4.getName());
    }

    @Test
    void getPhone() {
        assertEquals(phone, read1.getPhone());
        assertNotEquals(read1.getPhone(), read2.getPhone());
        assertEquals(read2.getPhone(), read3.getPhone());
    }

    @Test
    void setPhone() {
        read4.setPhone(phone_);
        assertNotEquals(phone, read4.getPhone());
        assertEquals(phone_, read4.getPhone());
    }

    @Test
    void getBooks() {
        assertEquals(new ArrayList<Book>(), read1.getBooks());  //use ArrayList to declare list as empty not null bc getBooks is empty not null
        read1.addBook(bookTest);                                //add a book to increase size, note: did not use books.add(bookTest)
        assertNotEquals(read1.getBooks(), read2.getBooks());    //has (1,0)
        assertEquals(read2.getBooks(), read3.getBooks());       //has (0,0)
    }

    @Test
    void setBooks() {
        books_ = new ArrayList<Book>();           //set list to 0
        read4.setBooks(books_);                   //assign books_ to read4
        read4.addBook(bookTest);                  //add a book to list
        assertNotEquals(books, read4.getBooks()); //has (0,1)
        assertEquals(books_, read4.getBooks());   //has (1,1)
    }

    //OTHER FUNCTIONS----------------------------------------------------------
    @Test
    void addBook() {
        //TODO: how to check status code
        Reader reader = new Reader(0, "", "");
        assertEquals(reader.addBook(bookTest), Code.SUCCESS);
        assertNotEquals(reader.addBook(bookTest), Code.SUCCESS);    //shows that the same book can't be added twice
        assertEquals(reader.addBook(bookTest), Code.BOOK_ALREADY_CHECKED_OUT_ERROR);    //shows that the book was already added
    }

    @Test
    void removeBook() {
        Reader reader = new Reader(0, "", "");
        assertEquals(reader.removeBook(bookTest), Code.READER_DOESNT_HAVE_BOOK_ERROR);
        reader.addBook(bookTest);
        assertEquals(reader.removeBook(bookTest), Code.SUCCESS);
    }

    @Test
    void hasBook() {
        Reader reader = new Reader(0, "", "");
        assertFalse(reader.hasBook(bookTest));
        reader.addBook(bookTest);
        assertTrue(reader.hasBook(bookTest));
    }

    @Test
    void getBookCount() {
        Reader reader = new Reader(0, "", "");
        assert reader.getBookCount() == 0;      //use reader.getBookCount() not books.size()
        reader.addBook(bookTest);
        assert reader.getBookCount() == 1;
        reader.removeBook(bookTest);
        assert reader.getBookCount() == 0;
    }

    static class ShelfTest {
        Shelf shelf;
        Shelf shelf1;
        Shelf shelf2;
        Shelf shelf3;
        Shelf shelf4;
        HashMap<Book, Integer> books;
        Book book;
        Book book1;

        //CONSTRUCTOR & DESTRUCTOR-------------------------------------------------
        @BeforeEach
        void setUp() {
            //constructor test---
            shelf = null;
            assertNull(shelf);
            shelf = new Shelf();
            assertNotNull(shelf);

            //field setting and getter test---
            books = new HashMap<>();
            shelf1 = new Shelf();

            //equality test---
            shelf2 = new Shelf();
            shelf3 = new Shelf();

            //setter test---
            shelf4 = new Shelf();

            //other variables---
            book = new Book("34-w-34", "Dune", "sci-fi", 235, "Frank Herbert", LocalDate.of(2021,2,10));
            book1 = new Book("5297", "Count of Monte Cristo", "Adventure", 999, "Alexandrea Dumas", LocalDate.of(2021, 1, 1));
        }

        @AfterEach
        void tearDown() {
            shelf = null;
            shelf1 = null;
            shelf2 = null;
            shelf3 = null;
            shelf4 = null;
            books = null;
            book = null;
            book1 = null;
        }

        //GETTERS & SETTERS--------------------------------------------------------
        @Test
        void getShelfNumber() {
            shelf1.setShelfNumber(1);
            assertEquals(1, shelf1.getShelfNumber());                  //field setting and getter test
            shelf2.setShelfNumber(2);
            assertNotEquals(shelf1.getShelfNumber(), shelf2.getShelfNumber());  //equality test
            shelf3.setShelfNumber(2);
            assertEquals(shelf2.getShelfNumber(), shelf3.getShelfNumber());     //equality test
        }

        @Test
        void setShelfNumber() {
            shelf4.setShelfNumber(1);                               //set value to new parameter, setter test
            assertNotEquals(0, shelf4.getShelfNumber());  //setter test
            assertEquals(1, shelf4.getShelfNumber());       //setter test
        }

        //---------------------------------
        @Test
        void getSubject() {
            shelf1.setSubject("sci-fi");
            assertEquals("sci-fi", shelf1.getSubject());
            shelf2.setSubject("adventure");
            assertNotEquals(shelf1.getSubject(), shelf2.getSubject());
            shelf3.setSubject("adventure");
            assertEquals(shelf2.getSubject(), shelf3.getSubject());
        }

        @Test
        void setSubject() {
            shelf4.setSubject("education");
            assertNotEquals("adventure", shelf4.getSubject());
            assertEquals("education", shelf4.getSubject());
        }

        //---------------------------------
        @Test
        void getBooks() {
            HashMap<Book, Integer> books1 = new HashMap<>();
            books1.put(book, 1);

            shelf1.setBooks(books);
            assertEquals(books, shelf1.getBooks());
            shelf2.setBooks(books1);
            assertNotEquals(shelf1.getBooks(), shelf2.getBooks());
            shelf3.setBooks(books1);
            assertEquals(shelf2.getBooks(), shelf3.getBooks());
        }

        @Test
        void setBooks() {
            HashMap<Book, Integer> books1 = new HashMap<>();
            books1.put(book, 1);

            shelf4.setBooks(books1);
            assertNotEquals(books, shelf4.getBooks());
            assertEquals(books1, shelf4.getBooks());
        }

        //OTHER FUNCTIONS----------------------------------------------------------
        @Test
        void getBookCount() {
            shelf.setSubject("sci-fi");
            Random random = new Random();
            Integer count = random.nextInt(5) + 1;    //gets random # range is 0-5
            for (int i = 0; i < count; i++) {               //adds random number of books to shelf
                shelf.addBook(book);
            }

            assertEquals(count, shelf.getBookCount(book));
            shelf.removeBook(book);
            assertNotEquals(count, shelf.getBookCount(book));
            assertNotEquals(count, shelf.getBookCount(book1));
        }

        @Test
        void addBook() {
            shelf.setSubject("sci-fi");
            assertEquals(Code.SUCCESS, shelf.addBook(book));             //add book to shelf
            assertEquals(1, shelf.getBookCount(book));           //check the count
            assertEquals(Code.SUCCESS, shelf.addBook(book));             //add the book again
            assertEquals(2, shelf.getBookCount(book));           //check the count
            assertEquals(Code.SHELF_SUBJECT_MISMATCH_ERROR, shelf.addBook(book1));  //adds a mismatching book to shelf
        }
        @Test
        void removeBook() {
            shelf.setSubject("sci-fi");
            assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, shelf.removeBook(book));    //removes book that doesn't exist
            shelf.addBook(book);                                    //add the book to shelf
            assertEquals(1, shelf.getBookCount(book));      //check the count
            assertEquals(Code.SUCCESS, shelf.removeBook(book));     //removes book that does exist
            assertEquals(0, shelf.getBookCount(book));      //check the count
            assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, shelf.removeBook(book));    //removes book with inventory 0
            assertEquals(0, shelf.getBookCount(book));      //check the count
        }

        @Test
        void listBooks() {
            String listBook = "0 books on shelf: 1 : sci-fi";
            shelf.setSubject("sci-fi");
            shelf.setShelfNumber(1);
            assertEquals(listBook.trim(), shelf.listBooks().trim());
            System.out.println(shelf.listBooks());
        }
    }
}