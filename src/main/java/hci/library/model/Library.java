package hci.library.model;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> books;

    public Library() {
        this.books = new ArrayList<>();
        addBook(new Book("1", "The Lord of the Rings", "J.R.R. Tolkien"));
        addBook(new Book("2", "1984", "George Orwell"));
        addBook(new Book("3", "Pride and Prejudice", "Jane Austen"));
        addBook(new Book("4", "The Great Gatsby", "F. Scott Fitzgerald"));
        addBook(new Book("5", "To Kill a Mockingbird", "Harper Lee"));
    }

    public void addBook(Book book) {
        if (!books.contains(book)) {
            books.add(book);
        }
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public Book findBookById(String id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                return book;
            }
        }
        return null;
    }

    public boolean removeBook(String id) {
        Book book = findBookById(id);
        if (book != null) {
            books.remove(book);
            return true;
        }
        return false;
    }

    public boolean borrowBook(String id) {
        Book book = findBookById(id);
        if (book != null && !book.isBorrowed()) {
            book.setBorrowed(true);
            return true;
        }
        return false;
    }

    public boolean returnBook(String id) {
        Book book = findBookById(id);
        if (book != null && book.isBorrowed()) {
            book.setBorrowed(false);
            return true;
        }
        return false;
    }

    public List<Book> searchBooks(String query) {
        List<Book> result = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) return result;
        String lowerQuery = query.toLowerCase();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(lowerQuery) || 
                book.getAuthor().toLowerCase().contains(lowerQuery) ||
                book.getId().toLowerCase().contains(lowerQuery)) {
                result.add(book);
            }
        }
        return result;
    }
}
