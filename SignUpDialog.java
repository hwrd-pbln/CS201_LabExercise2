package LabExer2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpDialog extends JDialog {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField birthdayField;
    private JTextField phoneField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public SignUpDialog(LoginDialog loginDialog) {
        super(loginDialog, "Sign Up", true);
        setSize(400, 300);
        setLocationRelativeTo(loginDialog);
        setLayout(new GridLayout(8, 2));
        add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        add(firstNameField);
        add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        add(lastNameField);
        add(new JLabel("Birthday:"));
        birthdayField = new JTextField();
        add(birthdayField);
        add(new JLabel("Phone Number:"));
        phoneField = new JTextField();
        add(phoneField);
        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);
        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);
        add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        add(confirmPasswordField);
        JButton signUpButton = new JButton("Sign Up");
        JButton cancelButton = new JButton("Cancel");
        add(signUpButton);
        add(cancelButton);

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        pack();
        setVisible(true);
    }

    private void handleSignUp() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String birthday = birthdayField.getText().trim();
        String phone = phoneField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and passwords cannot be empty.", "Sign Up Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Sign Up Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (LibraryManagementSystem.getUserCredentials().containsKey(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.", "Sign Up Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add user to credentials
        LibraryManagementSystem.getUserCredentials().put(username, password);
        JOptionPane.showMessageDialog(this, "Sign-up successful! You can now log in.", "Sign Up Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
