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
            com.formdev.flatlaf.themes.FlatMacDarkLaf.setup();
        } catch (Exception ignored) {
        }

        frame = new JDialog((Frame) null, "Library Application - Windows GUI Mode", true);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setSize(650, 500);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void stop() {
        if (frame != null && frame.isVisible()) {
            frame.dispose();
        }
    }

    @Override
    public void initMainScreen(Runnable onViewBooks, Runnable onSearchBook, Runnable onBorrowBook, Runnable onReturnBook, Runnable onAddBook,
            Runnable onDeleteBook, Runnable onSwitchView, Runnable onExit) {
        JPanel mainPanel = new JPanel(new GridLayout(9, 1, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        JLabel titleLabel = new JLabel("Library Control Panel", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        mainPanel.add(titleLabel);
        JButton btnView = new JButton("View Books");
        JButton btnSearch = new JButton("Search Book");
        JButton btnAdd = new JButton("Add Book");
        JButton btnDelete = new JButton("Delete Book");
        JButton btnBorrow = new JButton("Borrow Book");
        JButton btnReturn = new JButton("Return Book");
        JButton btnSwitch = new JButton("Switch Mode");
        JButton btnExit = new JButton("Exit");

        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 16);
        btnView.setFont(buttonFont);
        btnSearch.setFont(buttonFont);
        btnAdd.setFont(buttonFont);
        btnDelete.setFont(buttonFont);
        btnBorrow.setFont(buttonFont);
        btnReturn.setFont(buttonFont);
        btnSwitch.setFont(buttonFont);
        btnExit.setFont(buttonFont);

        btnSearch.setBackground(new Color(243, 156, 18));
        btnSearch.setForeground(Color.WHITE);

        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);

        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);

        btnBorrow.setBackground(new Color(52, 152, 219));
        btnBorrow.setForeground(Color.WHITE);

        btnReturn.setBackground(new Color(241, 196, 15));
        btnReturn.setForeground(Color.BLACK);

        btnSwitch.setBackground(new Color(142, 68, 173));
        btnSwitch.setForeground(Color.WHITE);

        btnView.addActionListener(e -> onViewBooks.run());
        btnSearch.addActionListener(e -> onSearchBook.run());
        btnAdd.addActionListener(e -> onAddBook.run());
        btnDelete.addActionListener(e -> onDeleteBook.run());
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
        mainPanel.add(btnSearch);
        mainPanel.add(btnAdd);
        mainPanel.add(btnDelete);
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
