package hci.library.view;

import hci.library.model.Book;
import java.util.List;

public interface ConsoleView {
    void start();

    void stop();

    void initMainScreen(Runnable onViewBooks, Runnable onSearchBook, Runnable onBorrowBook, Runnable onReturnBook, Runnable onAddBook,
            Runnable onDeleteBook, Runnable onSwitchView, Runnable onExit);

    void showBooksDialog(List<Book> books);

    void showMessage(String title, String message);

    String promptInput(String title, String prompt);
}
