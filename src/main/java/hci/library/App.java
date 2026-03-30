package hci.library;

import hci.library.controller.LibraryController;
import hci.library.model.Library;
import hci.library.view.ConsoleView;
import hci.library.view.LanternaView;
import hci.library.view.SwingView;

import java.util.Locale;

public class App {
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);

        Library library = new Library();
        boolean useSwing = false;
        while (true) {
            ConsoleView view = useSwing ? new SwingView() : new LanternaView();
            LibraryController controller = new LibraryController(library, view);
            controller.start();
            if (controller.isSwitchRequested()) {
                useSwing = !useSwing;
            } else {
                break;
            }
        }
    }
}
