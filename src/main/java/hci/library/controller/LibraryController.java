package hci.library.controller;

import hci.library.model.Book;
import hci.library.model.Library;
import hci.library.view.ConsoleView;

import java.util.List;

public class LibraryController {
    private Library library;
    private ConsoleView view;
    private boolean switchRequested = false;

    public LibraryController(Library library, ConsoleView view) {
        this.library = library;
        this.view = view;
    }

    public boolean isSwitchRequested() {
        return switchRequested;
    }

    public void start() {
        switchRequested = false;
        view.start();
        view.initMainScreen(
                this::handleViewBooks,
                this::handleSearchBook,
                this::handleBorrowBook,
                this::handleReturnBook,
                this::handleAddBook,
                this::handleDeleteBook,
                () -> {
                    switchRequested = true;
                },
                this::handleExit);
        view.stop();
    }

    private void handleViewBooks() {
        List<Book> books = library.getAllBooks();
        view.showBooksDialog(books);
    }

    private void handleSearchBook() {
        String query = view.promptInput("Search Book", "Enter title, author, or ID:");
        if (query != null && !query.trim().isEmpty()) {
            List<Book> results = library.searchBooks(query.trim());
            if (results.isEmpty()) {
                view.showMessage("Search Results", "No books found matching: " + query);
            } else {
                view.showBooksDialog(results);
            }
        }
    }

    private void handleAddBook() {
        String id = view.promptInput("Add Book", "Enter Book ID:");
        if (id == null || id.trim().isEmpty())
            return;

        if (library.findBookById(id.trim()) != null) {
            view.showMessage("Error", "A book with this ID already exists!");
            return;
        }

        String title = view.promptInput("Add Book", "Enter Book Title:");
        if (title == null || title.trim().isEmpty())
            return;

        String author = view.promptInput("Add Book", "Enter Book Author:");
        if (author == null || author.trim().isEmpty())
            return;

        library.addBook(new Book(id.trim(), title.trim(), author.trim()));
        view.showMessage("Success", "Book successfully added to the library!");
    }

    private void handleDeleteBook() {
        String id = view.promptInput("Delete Book", "Enter Book ID to delete:");
        if (id != null && !id.trim().isEmpty()) {
            boolean success = library.removeBook(id.trim());
            if (success) {
                view.showMessage("Success", "Book deleted successfully!");
            } else {
                view.showMessage("Error", "Book not found.");
            }
        }
    }

    private void handleBorrowBook() {
        String id = view.promptInput("Borrow Book", "Enter Book ID to borrow:");
        if (id != null && !id.trim().isEmpty()) {
            boolean success = library.borrowBook(id.trim());
            if (success) {
                view.showMessage("Success", "Book borrowed successfully!");
            } else {
                view.showMessage("Error", "Book not found or already borrowed.");
            }
        }
    }

    private void handleReturnBook() {
        String id = view.promptInput("Return Book", "Enter Book ID to return:");
        if (id != null && !id.trim().isEmpty()) {
            boolean success = library.returnBook(id.trim());
            if (success) {
                view.showMessage("Success", "Book returned successfully!");
            } else {
                view.showMessage("Error", "Book not found or not borrowed by you.");
            }
        }
    }

    private void handleExit() {
    }
}
