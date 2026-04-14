# Library Application (HCI Project)

## Project Purpose
This project is a Human-Computer Interaction (HCI) application focused on understanding the MVC design pattern and a dynamic switching mechanism between a Console (TUI) and a GUI. It allows users to view library books, add, delete, search, borrow, and return books.

## MVC Architecture
The project is designed utilizing the classic MVC (Model-View-Controller) architecture. The core working software is built upon this architecture, while more advanced concept designs of the project are available on Figma.
* **Model:** The layer containing the data structures and state (`Book` and `Library` classes).
* **View:** There are two distinct user interfaces in the system: Text-based (via Lanterna) and Graphical (via Java Swing). Menus and features are fully synchronized across both views to maintain functional equality.
* **Controller:** The central component that manages the two views, handles the business logic involving the Model, and dictates the application flow.

## How to Open Text Mode (TUI)
When the application is launched, it starts in **Text Mode (TUI)** by default. Powered by the Lanterna library, a window-like Text Mode environment runs in the command line (console), responding to keyboard shortcuts and directional keys (↑, ↓, Enter).

## How to Open GUI Mode
While the application is running, you can transition to a modern-looking (FlatLaf-themed) Java Swing GUI by selecting the **"Switch Mode"** option in the Text Mode main menu. In the GUI Mode, all interactions take place via visual buttons (using a mouse) and are functionally identical to the Text Mode features.

## How to Switch Between Modes
At any given moment (the option is available in both interfaces), by selecting the **"Switch Mode"** menu item or button:
The active interface closes smoothly, the Controller is notified, and the secondary mode is launched. Because the library's dataset is centrally maintained within a unified Model, transitions cause no data loss, and workflows continue seamlessly seamlessly.

---

### Compilation and Execution

You can run the project via Maven from the command line:

```bash
mvn clean compile exec:java
```
Alternatively, you can directly run the `hci.library.App` main class from your preferred IDE (e.g., IntelliJ IDEA, Eclipse, VS Code).
