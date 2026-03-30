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
                this::handleBorrowBook,
                this::handleReturnBook,
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
