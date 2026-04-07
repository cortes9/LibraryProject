import Utilities.Code;
import java.util.HashMap;
import java.util.Objects;

/**
 * Name: Luis Cortes
 * Assignment: Shelf.java
 */
public class Shelf {

    public static final int SHELF_NUMBER_ = 0;
    public static final int SUBJECT_ = 1;

    private HashMap<Book, Integer> books;
    private int shelfNumber;
    private String subject;

    public Shelf() {
        books = new HashMap<>();
    }

    public Shelf(int shelfNumber, String subject) {
        this.shelfNumber = shelfNumber;
        this.subject = subject;
        books = new HashMap<>();
    }

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
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

    @Override
    public String toString() {
        return shelfNumber + " : " + subject;
    }

    public Code addBook(Book book) {
        if (books.containsKey(book)) {
            books.put(book, books.get(book) + 1);
            return Code.SUCCESS;
        }

        if (book.getSubject().equals(subject)) {
            books.put(book, 1);
            return Code.SUCCESS;
        }

        return Code.SHELF_SUBJECT_MISMATCH_ERROR;
    }

    public Code removeBook(Book book) {
        if (!books.containsKey(book)) {
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        if (books.get(book) == 0) {
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        books.put(book, books.get(book) - 1);
        return Code.SUCCESS;
    }

    public int getBookCount(Book book) {
        if (books.containsKey(book)) {
            return books.get(book);
        }
        return -1;
    }

    public String listBooks() {
        int count = books.size();
        String result = count + " books on shelf: " + toString();

        if (count == 1) {
            result = "1 book on shelf: " + toString();
        }

        for (Book b : books.keySet()) {
            result += "\n" + b + " " + books.get(b);
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shelf)) return false;

        Shelf other = (Shelf) o;
        return shelfNumber == other.shelfNumber &&
                Objects.equals(subject, other.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shelfNumber, subject);
    }
}