package hci.library.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import hci.library.model.Book;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;

public class LanternaView implements ConsoleView {
    private Terminal terminal;
    private Screen screen;
    private MultiWindowTextGUI gui;
    private Window mainWindow;

    @Override
    public void start() {
        try {
            DefaultTerminalFactory factory = new DefaultTerminalFactory();
            // Force Lanterna to open a separate Java swing window for the terminal 
            // This prevents issues with stty.exe missing in Windows/PowerShell environments.
            // Explicitly creating an emulator ensures it won't try to inherit an invalid console state.
            terminal = factory.createTerminalEmulator();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
            gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            if (screen != null) {
                screen.stopScreen();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initMainScreen(Runnable onViewBooks, Runnable onBorrowBook, Runnable onReturnBook, Runnable onExit) {
        Panel panel = new Panel(new GridLayout(1));

        panel.addComponent(new Button("View All Books", onViewBooks));
        panel.addComponent(new Button("Borrow Book", onBorrowBook));
        panel.addComponent(new Button("Return Book", onReturnBook));
        panel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        panel.addComponent(new Button("Exit", () -> {
            onExit.run();
            mainWindow.close();
        }));

        mainWindow = new BasicWindow("Library Application");
        mainWindow.setHints(Arrays.asList(Window.Hint.CENTERED));
        mainWindow.setComponent(panel);
        gui.addWindowAndWait(mainWindow);
    }

    @Override
    public void showBooksDialog(List<Book> books) {
        com.googlecode.lanterna.gui2.table.Table<String> table = new com.googlecode.lanterna.gui2.table.Table<String>("ID", "Title", "Author", "Status");
        for (Book book : books) {
            table.getTableModel().addRow(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.isBorrowed() ? "Borrowed" : "Available"
            );
        }

        Panel panel = new Panel(new BorderLayout());
        panel.addComponent(table, BorderLayout.Location.CENTER);

        BasicWindow window = new BasicWindow("Books List");
        window.setComponent(panel);
        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        Button closeBtn = new Button("Close", window::close);
        panel.addComponent(closeBtn, BorderLayout.Location.BOTTOM);

        gui.addWindowAndWait(window);
    }

    @Override
    public void showMessage(String title, String message) {
        MessageDialog.showMessageDialog(gui, title, message, MessageDialogButton.OK);
    }

    @Override
    public String promptInput(String title, String prompt) {
        return TextInputDialog.showDialog(gui, title, prompt, "");
    }
}
