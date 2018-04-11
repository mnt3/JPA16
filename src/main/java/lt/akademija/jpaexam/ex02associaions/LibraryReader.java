package lt.akademija.jpaexam.ex02associaions;

import java.util.ArrayList;
import java.util.List;

public class LibraryReader {

    private Long id;
    private String firstName;
    private String lastName;

    private List<LibraryReaderAddress> addresses;
    private List<Book> borrowedBooks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Book> getBorrowedBooks() {
        if (borrowedBooks == null) {
            borrowedBooks = new ArrayList<>();
        }
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public List<LibraryReaderAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<LibraryReaderAddress> addresses) {
        this.addresses = addresses;
    }

    public void addBorrowedBook(Book b) {
        throw new UnsupportedOperationException();
    }
}
