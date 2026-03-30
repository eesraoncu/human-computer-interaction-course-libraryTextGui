package hci.library.view;

import hci.library.model.Book;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SwingView implements ConsoleView {

    private JDialog frame;

    @Override
    public void start() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        frame = new JDialog((Frame) null, "Library Application - Windows GUI Mode", true);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void stop() {
        if (frame != null && frame.isVisible()) {
            frame.dispose();
        }
    }

    @Override
    public void initMainScreen(Runnable onViewBooks, Runnable onBorrowBook, Runnable onReturnBook,
            Runnable onSwitchView, Runnable onExit) {
        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        JButton btnView = new JButton("View All Books");
        JButton btnBorrow = new JButton("Borrow Book");
        JButton btnReturn = new JButton("Return Book");
        JButton btnSwitch = new JButton("Switch to Text Mode (TUI)");
        JButton btnExit = new JButton("Exit Application");

        btnView.addActionListener(e -> onViewBooks.run());
        btnBorrow.addActionListener(e -> onBorrowBook.run());
        btnReturn.addActionListener(e -> onReturnBook.run());

        btnSwitch.addActionListener(e -> {
            onSwitchView.run();
            frame.dispose();
        });

        btnExit.addActionListener(e -> {
            onExit.run();
            frame.dispose();
        });

        mainPanel.add(btnView);
        mainPanel.add(btnBorrow);
        mainPanel.add(btnReturn);
        mainPanel.add(btnSwitch);
        mainPanel.add(btnExit);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    @Override
    public void showBooksDialog(List<Book> books) {
        String[] columns = { "ID", "Title", "Author", "Status" };
        Object[][] data = new Object[books.size()][4];

        for (int i = 0; i < books.size(); i++) {
            data[i][0] = books.get(i).getId();
            data[i][1] = books.get(i).getTitle();
            data[i][2] = books.get(i).getAuthor();
            data[i][3] = books.get(i).isBorrowed() ? "Borrowed" : "Available";
        }

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JOptionPane.showMessageDialog(frame, scrollPane, "Books List", JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public String promptInput(String title, String prompt) {
        return JOptionPane.showInputDialog(frame, prompt, title, JOptionPane.QUESTION_MESSAGE);
    }
}
