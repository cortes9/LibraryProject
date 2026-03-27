import java.time.LocalDate;
import java.util.Objects;

/**
 * Name; Luis Cortes Cedillo
 * Assigment; Book.java
 */


public class Book {
    private static final int ISBN_ =0;
    private static final int TITLE_ =0;
    private static final int SUBJECT_ =0;
    private static final int PAGE_COUNT_ =0;
    private static final int AUTHOR_ =0;
    private static final int DUE_DATE_=0;

    private String author;
    private LocalDate dueDate;
    private String isbn;
    private int pageCount;
    private String title;
    private String subject;


    public Book(String isbn, int pageCount, String title, String subject) {
        this.author = author;
        this.title = title;
        this.subject = subject;
        this.isbn = isbn;
        this.pageCount = pageCount;
        this.dueDate = dueDate();

    }

    //getters
    public  String getAuthor() {
        return author;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getSubject() {
        return subject;
    }

    public String getTitle() {
        return title;
    }

    //setter
    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString() {
        return title + " by " + author + " ISBN; " + isbn;
    }

    public boolean equals(Object o) {
        if (o == null) return false;

        Book other = (Book) o;

        return this.author.equals(other.author) && this.isbn.equals(other.isbn) && this.subject.equals(other.subject) &&
                this.title.equals(other.title) && this.pageCount == other.pageCount;

    }

    public  int hashCode() {
        return Objects.hash(author, isbn, subject, pageCount, title, pageCount);
    }

}
