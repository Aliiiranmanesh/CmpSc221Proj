package View;

import javax.swing.*;
import java.awt.*;

/**
 * Login window for Property Management System
 */
public class LoginGUI extends JFrame {
    public LoginGUI() {
        setTitle("Property Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Tenant", "Landlord"});

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            if (!username.isEmpty() && !password.isEmpty()) {
                // In a real system, we would validate from database
                // Here we simulate login success
                PropertyManagementGUI mainApp = new PropertyManagementGUI(role);
                mainApp.setVisible(true);
                dispose(); // Close login window
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
            }
        });

        registerButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Registration is currently simulated.\nYou can login directly.");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginGUI().setVisible(true);
        });
    }
}