package hci.library.view;

import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import java.awt.Font;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.util.concurrent.atomic.AtomicBoolean;
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
            Font customFont = new Font("Consolas", Font.BOLD, 18);
            SwingTerminalFontConfiguration fontConfig = SwingTerminalFontConfiguration.newInstance(customFont);
            factory.setTerminalEmulatorFontConfiguration(fontConfig);
            terminal = factory.createTerminalEmulator();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
            gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(),
                    new EmptySpace(new TextColor.RGB(20, 20, 20)));
            Theme neonTheme = SimpleTheme.makeTheme(
                    true,
                    new TextColor.RGB(255, 105, 180),
                    new TextColor.RGB(30, 30, 30),
                    TextColor.ANSI.WHITE,
                    new TextColor.RGB(70, 70, 70),
                    TextColor.ANSI.WHITE,
                    new TextColor.RGB(199, 21, 133),
                    new TextColor.RGB(20, 20, 20));
            gui.setTheme(neonTheme);
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
    public void initMainScreen(Runnable onViewBooks, Runnable onBorrowBook, Runnable onReturnBook,
            Runnable onSwitchView, Runnable onExit) {
        Panel mainPanel = new Panel(new BorderLayout());

        Panel leftPanel = new Panel(new GridLayout(1));
        leftPanel.addComponent(new Button("View Books", onViewBooks));
        leftPanel.addComponent(new Button("Borrow Book", onBorrowBook));
        leftPanel.addComponent(new Button("Return Book", onReturnBook));
        leftPanel.addComponent(new Button("Switch to GUI", () -> {
            onSwitchView.run();
            if (mainWindow != null)
                mainWindow.close();
        }));
        leftPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        leftPanel.addComponent(new Button("Exit", () -> {
            onExit.run();
            if (mainWindow != null)
                mainWindow.close();
        }));

        Panel rightPanel = new Panel(new GridLayout(1));
        rightPanel.addComponent(
                new Label("Welcome to the Library Application.\nPlease choose an option from the left panel."));

        Panel bottomPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        bottomPanel.addComponent(new Label("[F1] Help "));
        bottomPanel.addComponent(new Label("[F10] Exit "));

        mainPanel.addComponent(leftPanel.withBorder(Borders.singleLine(" Menus")), BorderLayout.Location.LEFT);
        mainPanel.addComponent(rightPanel.withBorder(Borders.singleLine(" Content")), BorderLayout.Location.CENTER);
        mainPanel.addComponent(bottomPanel, BorderLayout.Location.BOTTOM);

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        fileMenu.add(new MenuItem("Help",
                () -> showMessage("Help", "Use arrow keys to navigate\nPress Enter to select\nF10 to exit")));
        fileMenu.add(new MenuItem("Exit", () -> {
            onExit.run();
            if (mainWindow != null)
                mainWindow.close();
        }));
        menuBar.add(fileMenu);

        mainWindow = new BasicWindow("Library Application");
        mainWindow.setHints(Arrays.asList(Window.Hint.FULL_SCREEN));

        mainWindow.setComponent(mainPanel);
        mainWindow.setMenuBar(menuBar);

        mainWindow.addWindowListener(new WindowListenerAdapter() {
            @Override
            public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
                if (keyStroke.getKeyType() == KeyType.F10) {
                    onExit.run();
                    mainWindow.close();
                } else if (keyStroke.getKeyType() == KeyType.F1) {
                    showMessage("Help", "Use arrow keys to navigate\nPress Enter to select\nF10 to exit");
                    deliverEvent.set(false);
                }
            }
        });
        gui.addWindowAndWait(mainWindow);
    }

    @Override
    public void showBooksDialog(List<Book> books) {
        com.googlecode.lanterna.gui2.table.Table<String> table = new com.googlecode.lanterna.gui2.table.Table<String>(
                "ID", "Title", "Author", "Status");
        for (Book book : books) {
            table.getTableModel().addRow(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.isBorrowed() ? "Borrowed" : "Available");
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
