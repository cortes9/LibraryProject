import Utilities.Code;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Name: Luis Cortes
 * Assignment: Library.java
 */
public class Library {

    public static final int LENDING_LIMIT = 5;

    private HashMap<Book, Integer> books;
    private static int libraryCard = 0;
    private String name;
    private List<Reader> readers;
    private HashMap<String, Shelf> shelves;

    public Library(String name) {
        this.name = name;
        books = new HashMap<>();
        readers = new ArrayList<>();
        shelves = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Code init(String filename) {
        Scanner scan;

        try {
            File file = new File(filename);
            if (!file.exists()) {
                file = new File("CSVs/" + filename);
            }
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            return Code.FILE_NOT_FOUND_ERROR;
        }

        int bookCount = convertInt(scan.nextLine(), Code.BOOK_COUNT_ERROR);
        if (bookCount < 0) {
            return errorCode(bookCount);
        }
        Code code = initBooks(bookCount, scan);
        if (code != Code.SUCCESS) {
            return code;
        }
        listBooks();

        int shelfCount = convertInt(scan.nextLine(), Code.SHELF_COUNT_ERROR);
        if (shelfCount < 0) {
            return errorCode(shelfCount);
        }
        code = initShelves(shelfCount, scan);
        if (code != Code.SUCCESS) {
            return code;
        }
        listShelves(true);

        int readerCount = convertInt(scan.nextLine(), Code.READER_COUNT_ERROR);
        if (readerCount < 0) {
            return errorCode(readerCount);
        }
        code = initReader(readerCount, scan);
        if (code != Code.SUCCESS) {
            return code;
        }
        listReaders(true);

        return Code.SUCCESS;
    }

    public Code initBooks(int bookCount, Scanner scan) {
        if (bookCount < 1) {
            return Code.LIBRARY_ERROR;
        }

        for (int i = 0; i < bookCount; i++) {
            String line = scan.nextLine();
            String[] parts = line.split(",");

            if (parts.length < 6) {
                return Code.BOOK_RECORD_COUNT_ERROR;
            }

            int pageCount = convertInt(parts[Book.PAGE_COUNT_], Code.PAGE_COUNT_ERROR);
            if (pageCount <= 0) {
                return Code.PAGE_COUNT_ERROR;
            }

            LocalDate dueDate = convertDate(parts[Book.DUE_DATE_], Code.DATE_CONVERSION_ERROR);
            if (dueDate == null) {
                return Code.DATE_CONVERSION_ERROR;
            }

            Book book = new Book(
                    parts[Book.ISBN_],
                    parts[Book.TITLE_],
                    parts[Book.SUBJECT_],
                    pageCount,
                    parts[Book.AUTHOR_],
                    dueDate
            );

            addBook(book);
        }

        return Code.SUCCESS;
    }

    public Code initShelves(int shelfCount, Scanner scan) {
        if (shelfCount < 1) {
            return Code.SHELF_COUNT_ERROR;
        }

        for (int i = 0; i < shelfCount; i++) {
            String line = scan.nextLine();
            String[] parts = line.split(",");

            int shelfNumber = convertInt(parts[Shelf.SHELF_NUMBER_], Code.SHELF_NUMBER_PARSE_ERROR);
            if (shelfNumber < 0) {
                return Code.SHELF_NUMBER_PARSE_ERROR;
            }

            Shelf shelf = new Shelf(shelfNumber, parts[Shelf.SUBJECT_]);
            addShelf(shelf);
        }

        if (shelves.size() == shelfCount) {
            return Code.SUCCESS;
        }

        System.out.println("Number of shelves doesn't match expected");
        return Code.SHELF_NUMBER_PARSE_ERROR;
    }

    public Code initReader(int readerCount, Scanner scan) {
        if (readerCount <= 0) {
            return Code.READER_COUNT_ERROR;
        }

        for (int i = 0; i < readerCount; i++) {
            String line = scan.nextLine();
            String[] parts = line.split(",");

            int cardNumber = convertInt(parts[Reader.CARD_NUMBER_], Code.READER_CARD_NUMBER_ERROR);
            Reader reader = new Reader(cardNumber, parts[Reader.NAME_], parts[Reader.PHONE_]);
            addReader(reader);

            int bookCount = Integer.parseInt(parts[Reader.BOOK_COUNT_]);

            for (int j = 0; j < bookCount; j++) {
                int isbnIndex = Reader.BOOK_START_ + (j * 2);
                int dateIndex = isbnIndex + 1;

                Book libBook = getBookByISBN(parts[isbnIndex]);
                if (libBook == null) {
                    System.out.println("ERROR");
                    continue;
                }

                LocalDate dueDate = convertDate(parts[dateIndex], Code.DATE_CONVERSION_ERROR);

                Book readerBook = new Book(
                        libBook.getISBN(),
                        libBook.getTitle(),
                        libBook.getSubject(),
                        libBook.getPageCount(),
                        libBook.getAuthor(),
                        dueDate
                );

                checkOutBook(reader, readerBook);
            }
        }

        return Code.SUCCESS;
    }

    public Code addBook(Book newBook) {
        if (books.containsKey(newBook)) {
            books.put(newBook, books.get(newBook) + 1);
            System.out.println(books.get(newBook) + " copies of " + newBook.getTitle() + " in the stacks");
        } else {
            books.put(newBook, 1);
            System.out.println(newBook.getTitle() + " added to the stacks.");
        }

        Shelf shelf = getShelf(newBook.getSubject());
        if (shelf != null) {
            shelf.addBook(newBook);
            return Code.SUCCESS;
        }

        System.out.println("No shelf for " + newBook.getSubject() + " books");
        return Code.SHELF_EXISTS_ERROR;
    }

    public Code returnBook(Reader reader, Book book) {
        if (reader == null || !reader.hasBook(book)) {
            if (reader != null) {
                System.out.println(reader.getName() + " doesn't have " + book.getTitle() + " checked out");
            }
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        }

        if (!books.containsKey(book)) {
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        System.out.println(reader.getName() + " is returning " + book);
        Code code = reader.removeBook(book);

        if (code == Code.SUCCESS) {
            return returnBook(book);
        }

        System.out.println("Could not return " + book);
        return code;
    }

    public Code returnBook(Book book) {
        Shelf shelf = getShelf(book.getSubject());
        if (shelf == null) {
            System.out.println("No shelf for " + book);
            return Code.SHELF_EXISTS_ERROR;
        }
        return shelf.addBook(book);
    }

    @Deprecated
    public Code addBookToShelf(Book book, Shelf shelf) {
        Code code = returnBook(book);
        if (code == Code.SUCCESS) {
            return Code.SUCCESS;
        }

        if (!shelf.getSubject().equals(book.getSubject())) {
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }

        code = shelf.addBook(book);
        if (code == Code.SUCCESS) {
            System.out.println(book + " added to shelf");
            return Code.SUCCESS;
        }

        System.out.println("Could not add " + book + " to shelf");
        return code;
    }

    public int listBooks() {
        int total = 0;

        for (Book book : books.keySet()) {
            int count = books.get(book);
            total += count;
            System.out.println(count + " copies of " + book);
        }

        return total;
    }

    public Code checkOutBook(Reader reader, Book book) {
        if (reader == null || !readers.contains(reader)) {
            System.out.println("Reader doesn't have an account here");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }

        if (reader.getBookCount() >= LENDING_LIMIT) {
            System.out.println(reader.getName() + " has reached the lending limit, (" + LENDING_LIMIT + ")");
            return Code.BOOK_LIMIT_REACHED_ERROR;
        }

        if (!books.containsKey(book)) {
            System.out.println("ERROR: could not find " + book);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        Shelf shelf = getShelf(book.getSubject());
        if (shelf == null) {
            System.out.println("no shelf for " + book.getSubject() + " books!");
            return Code.SHELF_EXISTS_ERROR;
        }

        if (shelf.getBookCount(book) < 1) {
            System.out.println("ERROR: no copies of " + book + " remain");
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        Code code = reader.addBook(book);
        if (code != Code.SUCCESS) {
            System.out.println("Couldn't checkout " + book);
            return code;
        }

        code = shelf.removeBook(book);
        if (code == Code.SUCCESS) {
            System.out.println(book + " checked out successfully");
        }

        return code;
    }

    public Book getBookByISBN(String isbn) {
        for (Book book : books.keySet()) {
            if (book.getISBN().equals(isbn)) {
                return book;
            }
        }

        System.out.println("ERROR: Could not find a book with isbn: " + isbn);
        return null;
    }

    public int listShelves() {
        return listShelves(false);
    }

    public int listShelves(boolean showBooks) {
        for (Shelf shelf : shelves.values()) {
            if (showBooks) {
                System.out.println(shelf.listBooks());
            } else {
                System.out.println(shelf);
            }
        }
        return shelves.size();
    }

    public Code addShelf(String shelfSubject) {
        Shelf shelf = new Shelf(shelves.size() + 1, shelfSubject);
        return addShelf(shelf);
    }

    public Code addShelf(Shelf shelf) {
        if (shelves.containsKey(shelf.getSubject())) {
            System.out.println("ERROR: Shelf already exists " + shelf);
            return Code.SHELF_EXISTS_ERROR;
        }

        if (shelf.getShelfNumber() <= 0) {
            shelf.setShelfNumber(shelves.size() + 1);
        }

        shelves.put(shelf.getSubject(), shelf);

        for (Book book : books.keySet()) {
            if (book.getSubject().equals(shelf.getSubject())) {
                int count = books.get(book);
                for (int i = 0; i < count; i++) {
                    shelf.addBook(book);
                }
            }
        }

        return Code.SUCCESS;
    }

    public Shelf getShelf(Integer shelfNumber) {
        for (Shelf shelf : shelves.values()) {
            if (shelf.getShelfNumber() == shelfNumber) {
                return shelf;
            }
        }

        System.out.println("No shelf number " + shelfNumber + " found");
        return null;
    }

    public Shelf getShelf(String subject) {
        if (shelves.containsKey(subject)) {
            return shelves.get(subject);
        }

        System.out.println("No shelf for " + subject + " books");
        return null;
    }

    public int listReaders() {
        return listReaders(false);
    }

    public int listReaders(boolean showBooks) {
        for (Reader reader : readers) {
            if (showBooks) {
                System.out.println(reader.getName() + "(#" + reader.getCardNumber() + ") has the following books:");
                System.out.println(reader.getBooks());
            } else {
                System.out.println(reader);
            }
        }
        return readers.size();
    }

    public Reader getReaderByCard(int cardNumber) {
        for (Reader reader : readers) {
            if (reader.getCardNumber() == cardNumber) {
                return reader;
            }
        }

        System.out.println("Could not find a reader with card #" + cardNumber);
        return null;
    }

    public Code addReader(Reader reader) {
        if (readers.contains(reader)) {
            System.out.println(reader.getName() + " already has an account!");
            return Code.READER_ALREADY_EXISTS_ERROR;
        }

        for (Reader r : readers) {
            if (r.getCardNumber() == reader.getCardNumber()) {
                System.out.println(r.getName() + " and " + reader.getName() + " have the same card number!");
                return Code.READER_CARD_NUMBER_ERROR;
            }
        }

        readers.add(reader);
        System.out.println(reader.getName() + " added to the library!");

        if (reader.getCardNumber() > libraryCard) {
            libraryCard = reader.getCardNumber();
        }

        return Code.SUCCESS;
    }

    public Code removeReader(Reader reader) {
        if (!readers.contains(reader)) {
            System.out.println(reader + " is not part of this Library");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }

        if (reader.getBookCount() > 0) {
            System.out.println(reader.getName() + " must return all books!");
            return Code.READER_STILL_HAS_BOOKS_ERROR;
        }

        readers.remove(reader);
        return Code.SUCCESS;
    }

    public static int convertInt(String recordCountString, Code code) {
        try {
            return Integer.parseInt(recordCountString);
        } catch (NumberFormatException e) {
            switch (code) {
                case BOOK_COUNT_ERROR:
                    System.out.println("Error: Could not read number of books");
                    break;
                case PAGE_COUNT_ERROR:
                    System.out.println("Error: could not parse page count");
                    break;
                case DATE_CONVERSION_ERROR:
                    System.out.println("Error: Could not parse date component");
                    break;
                default:
                    System.out.println("Error: Unknown conversion error");
                    break;
            }

            System.out.println("Value which caused the error: " + recordCountString);
            System.out.println("Error message: " + code.getMessage());
            return code.getCode();
        }
    }

    public static LocalDate convertDate(String date, Code errorCode) {
        if (date.equals("0000")) {
            return LocalDate.of(1970, 1, 1);
        }

        String[] parts = date.split("-");
        if (parts.length != 3) {
            System.out.println("ERROR: date conversion error, could not parse " + date);
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970, 1, 1);
        }

        int year = convertInt(parts[0], errorCode);
        int month = convertInt(parts[1], errorCode);
        int day = convertInt(parts[2], errorCode);

        if (year < 0 || month < 0 || day < 0) {
            System.out.println("Error converting date: Year " + year);
            System.out.println("Error converting date: Month " + month);
            System.out.println("Error converting date: Dat " + day);
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970, 1, 1);
        }

        return LocalDate.of(year, month, day);
    }

    public static int getLibraryCardNumber() {
        return libraryCard + 1;
    }

    private Code errorCode(int codeNumber) {
        for (Code code : Code.values()) {
            if (code.getCode() == codeNumber) {
                return code;
            }
        }
        return Code.UNKNOWN_ERROR;
    }
}