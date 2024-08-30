package LabExer2;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Initialize user credentials
        LibraryManagementSystem.getUserCredentials().put("username", "password");
        
        SwingUtilities.invokeLater(() -> {
            LoginDialog loginDialog = new LoginDialog(null);
            if (loginDialog.isLoggedIn()) {
                new LibraryManagementSystem().setVisible(true);
            }
        });
    }
}
