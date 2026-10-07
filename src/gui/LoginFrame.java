package gui;

import exception.InvalidLoginException;
import model.User;
import service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField emailField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private AuthService authService = new AuthService("data/users.txt");

    public LoginFrame() {
        setTitle("Campus Complaint Tracker - Login");
        setSize(420, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Password:")); panel.add(passwordField);
        JButton loginButton = new JButton("Login");
        JButton clearButton = new JButton("Clear");
        panel.add(loginButton); panel.add(clearButton);
        panel.add(new JLabel("Student: aarav@campus.edu / student123"));
        panel.add(new JLabel("Admin: admin@campus.edu / admin123"));
        add(panel);

        loginButton.addActionListener(e -> login());
        clearButton.addActionListener(e -> { emailField.setText(""); passwordField.setText(""); });
    }

    private void login() {
        try {
            User user = authService.login(emailField.getText(), new String(passwordField.getPassword()));
            dispose();
            if (user.getRole().equals("ADMIN")) new AdminDashboard(user).setVisible(true);
            else new StudentDashboard(user).setVisible(true);
        } catch (InvalidLoginException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            passwordField.setText("");
        }
    }
}
