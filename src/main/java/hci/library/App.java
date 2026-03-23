package hci.library;

import hci.library.controller.LibraryController;
import hci.library.model.Library;
import hci.library.view.ConsoleView;
import hci.library.view.LanternaView;

import java.util.Locale;

public class App {
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);

        Library library = new Library();
        ConsoleView view = new LanternaView();
        LibraryController controller = new LibraryController(library, view);

        controller.start();
    }
}
