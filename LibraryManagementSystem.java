package LabExer2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class LibraryManagementSystem extends JFrame {
    private ArrayList<Book> books = new ArrayList<>();
    private Stack<Book> bookChanges = new Stack<>(); 
    private JTable table;
    private DefaultTableModel tableModel;
    private static Map<String, String> userCredentials = new HashMap<>();
    
    // Fixed array of categories
    private String[] categories = {"Fiction", "Non-Fiction", "Science", "History", "Horror/Thriller", "Action"};

    public LibraryManagementSystem() {
        setTitle("Library Management System");
        setSize(800, 400); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Table setup
        String[] columnNames = {"Index", "Title", "Author", "Category"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 10, 10); 
        
        //Buttons
        JButton addButton = createButton("Add Book");
        JButton removeButton = createButton("Remove Book");
        JButton searchButton = createButton("Search Book");
        JButton totalButton = createButton("Total Books");
        JButton sortButton = createButton("Sort by Title"); 
        JButton undoButton = createButton("Undo"); // Undo button
        JButton logOutButton = createButton("Log Out");
        
        // Add buttons to the panel with GridBagConstraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(addButton, gbc);
        gbc.gridy++;
        buttonPanel.add(removeButton, gbc);
        gbc.gridy++;
        buttonPanel.add(searchButton, gbc);
        gbc.gridy++;
        buttonPanel.add(totalButton, gbc);
        gbc.gridy++;
        buttonPanel.add(sortButton, gbc);
        gbc.gridy++;
        buttonPanel.add(undoButton, gbc); // Add undo button to the panel
        gbc.gridy++;
        buttonPanel.add(logOutButton, gbc);
        
        add(buttonPanel, BorderLayout.WEST); // Place the button panel on the left
        
        // Add action listeners to buttons
        addButton.addActionListener(e -> addBook());
        removeButton.addActionListener(e -> removeBook());
        searchButton.addActionListener(e -> searchBook());
        totalButton.addActionListener(e -> displayTotalBooks());
        sortButton.addActionListener(e -> {
            sortBooksByTitle();
            JOptionPane.showMessageDialog(LibraryManagementSystem.this, "Books sorted by title!");
        });
        undoButton.addActionListener(e -> undoLastChange()); // Undo action
        logOutButton.addActionListener(e -> System.exit(0));
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40)); 
        return button;
    }

    private void addBook() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JComboBox<String> categoryComboBox = new JComboBox<>(categories); // Use fixed categories

        Object[] message = {
            "Title:", titleField,
            "Author:", authorField,
            "Category:", categoryComboBox // dropdown for category
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String author = authorField.getText();
            String category = (String) categoryComboBox.getSelectedItem();
            Book newBook = new Book(title, author, category);
            
            books.add(newBook);
            bookChanges.push(newBook); // Push the new book to the stack
            JOptionPane.showMessageDialog(this, "Book added!");
            viewBooks(); // Refresh the table after adding a book
        }
    }

    private void removeBook() {
        String indexString = JOptionPane.showInputDialog(this, "Enter the index of the book to remove:");
        try {
            int index = Integer.parseInt(indexString) - 1;
            if (index >= 0 && index < books.size()) {
                Book removedBook = books.remove(index);
                bookChanges.push(removedBook); // Push removed book to stack for undo
                JOptionPane.showMessageDialog(this, "Book removed.");
                viewBooks(); // Refresh the table after removing a book
            } else {
                JOptionPane.showMessageDialog(this, "Invalid index.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    private void searchBook() {
        String[] options = {"Index", "Title", "Author"};
        int choice = JOptionPane.showOptionDialog(this, "Search by:", "Search Book",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            searchBookByIndex();
        } else if (choice == 1) {
            searchBookByTitle();
        } else if (choice == 2) {
            searchBookByAuthor();
        }
    }

    private void searchBookByIndex() {
        String indexString = JOptionPane.showInputDialog(this, "Enter the index of the book to search:");
        try {
            int index = Integer.parseInt(indexString) - 1;
            if (index >= 0 && index < books.size()) {
                Book book = books.get(index);
                JOptionPane.showMessageDialog(this, book.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Invalid index.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    private void searchBookByTitle() {
        String title = JOptionPane.showInputDialog(this, "Enter the title of the book to search:");
        ArrayList<Book> foundBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                foundBooks.add(book);
            }
        }
        if (foundBooks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No book found with the title \"" + title + "\".");
        } else {
            StringBuilder resultMessage = new StringBuilder("Books with the title \"" + title + "\":\n\n");
            for (Book book : foundBooks) {
                resultMessage.append(book.toString()).append("\n\n");
            }
            JOptionPane.showMessageDialog(this, resultMessage.toString());
        }
    }

    private void searchBookByAuthor() {
        String author = JOptionPane.showInputDialog(this, "Enter the author of the book to search:");
        ArrayList<Book> foundBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                foundBooks.add(book);
            }
        }
        if (foundBooks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No book found by the author \"" + author + "\".");
        } else {
            StringBuilder resultMessage = new StringBuilder("Books by author \"" + author + "\":\n\n");
            for (Book book : foundBooks) {
                resultMessage.append(book.toString()).append("\n\n");
            }
            JOptionPane.showMessageDialog(this, resultMessage.toString());
        }
    }

    private void viewBooks() {

        tableModel.setRowCount(0);
        // Populate table with book data
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            Object[] rowData = {i + 1, book.getTitle(), book.getAuthor(), book.getCategory()};
            tableModel.addRow(rowData);
        }
    }

    private void displayTotalBooks() {
        JOptionPane.showMessageDialog(this, "The library currently has " + books.size() + (books.size() == 1 ? " book." : " books."));
    }

    private void sortBooksByTitle() {
        for (int i = 1; i < books.size(); i++) {
            Book key = books.get(i);
            int j = i - 1;

            while (j >= 0 && books.get(j).getTitle().compareToIgnoreCase(key.getTitle()) > 0) {
                j--;
            }
            books.add(j + 1, key);
            books.remove(i + 1);
        }
        viewBooks(); 
    }

    private void undoLastChange() {
        if (!bookChanges.isEmpty()) {
            Book lastChange = bookChanges.pop();
            books.remove(lastChange); 
            JOptionPane.showMessageDialog(this, "Last change undone.");
            viewBooks(); 
        } else {
            JOptionPane.showMessageDialog(this, "No changes to undo.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryManagementSystem librarySystem = new LibraryManagementSystem();
            librarySystem.setVisible(true);
        });
    }
}

class Book {
    private String title;
    private String author;
    private String category;

    public Book(String title, String author, String category) {
        this.title = title;
        this.author = author;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\nAuthor: " + author + "\nCategory: " + category;
    }
}
