package service;

import exception.InvalidLoginException;
import model.Admin;
import model.Student;
import model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Handles login and logout operations for the application.
 */
public class AuthService {
    private String usersFilePath;

    public AuthService(String usersFilePath) {
        this.usersFilePath = usersFilePath;
    }

    /**
     * Reads the users file and returns the matching Student or Admin object.
     */
    public User login(String email, String password) throws InvalidLoginException {
        if (isEmpty(email) || isEmpty(password)) {
            throw new InvalidLoginException("Email and password cannot be empty.");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(usersFilePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] userData = line.split("\\|");

                if (userData.length != 5) {
                    continue;
                }

                String userId = userData[0].trim();
                String name = userData[1].trim();
                String storedEmail = userData[2].trim();
                String storedPassword = userData[3].trim();
                String role = userData[4].trim().toUpperCase();

                if (storedEmail.equalsIgnoreCase(email.trim())
                        && storedPassword.equals(password)) {
                    if (role.equals("STUDENT")) {
                        return new Student(userId, name, storedEmail, storedPassword);
                    }

                    if (role.equals("ADMIN")) {
                        return new Admin(userId, name, storedEmail, storedPassword);
                    }
                }
            }
        } catch (IOException exception) {
            throw new InvalidLoginException(
                    "Could not read users file: " + exception.getMessage()
            );
        }

        throw new InvalidLoginException("Invalid email or password.");
    }

    /**
     * Clears the current login in this simple console version.
     */
    public void logout(User user) {
        if (user != null) {
            System.out.println("Logged out: " + user.getEmail());
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
