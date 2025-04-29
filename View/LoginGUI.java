package View;

import javax.swing.*;
import java.awt.*;
import documents.databaseManager;

public class LoginGUI extends JFrame {
    public LoginGUI() {
        setTitle("Property Management System - Login");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main content panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Title label
        JLabel title = new JLabel("Login to Property Management System");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JLabel roleLabel = new JLabel("Role:");
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Tenant", "Landlord"});

        formPanel.add(userLabel); formPanel.add(userField);
        formPanel.add(passLabel); formPanel.add(passField);
        formPanel.add(roleLabel); formPanel.add(roleComboBox);

        panel.add(formPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        panel.add(buttonPanel);

        add(panel);

        // Database logic
        databaseManager db = new databaseManager();

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            if (!username.isEmpty() && !password.isEmpty()) {
                if (db.validateUser(username, password, role)) {
                    PropertyManagementGUI mainApp = new PropertyManagementGUI(role);
                    mainApp.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials or role.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
            }
        });

        registerButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            if (!username.isEmpty() && !password.isEmpty()) {
                if (db.registerUser(username, password, role)) {
                    JOptionPane.showMessageDialog(this, "Registration successful. You can now log in.");
                } else {
                    JOptionPane.showMessageDialog(this, "Username already exists.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}
