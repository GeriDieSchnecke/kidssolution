package KSWABackend;

import KSWABackend.Authentication.KSWAUserAuthentication;
import KSWABackend.Model.KSWATeacher;
import KSWABackend.Licencing.Licencing;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class KSWALoginUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField licenceField;



    private KSWAApplicationGUI applicationGUI;  // Declare as a member variable

    public KSWALoginUI() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 220);
        setLocationRelativeTo(null);


        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 165, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        JLabel licenceLabel = new JLabel("Licence:");
        licenceLabel.setBounds(10, 110, 80, 25);
        panel.add(licenceLabel);

        licenceField = new JTextField(20);
        licenceField.setBounds(100, 110, 165, 25);
        panel.add(licenceField);

        JLabel licenceNumberLabel = new JLabel("For registration enter licence number");
        licenceNumberLabel.setBounds(100, 140, 240, 25);
        panel.add(licenceNumberLabel);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(10, 140, 80, 25);
        panel.add(registerButton);

        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());
                    KSWATeacher authenticatedTeacher = KSWAUserAuthentication.authenticateUser(username, password);

                    if (authenticatedTeacher != null) {
                        JOptionPane.showMessageDialog(KSWALoginUI.this, "Login successful!");
                        loginSuccess(authenticatedTeacher);  // Weiterleitung an KSWAApplicationGUI
                    } else {
                        JOptionPane.showMessageDialog(KSWALoginUI.this, "Invalid username or password");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String licenceID = licenceField.getText();

                if (username.isEmpty() || password.isEmpty() || licenceID.isEmpty()) {
                    JOptionPane.showMessageDialog(KSWALoginUI.this, "Please enter username, password and licencenumber");
                    return; // Methode beenden, falls Eingaben fehlen
                }

                try {
                    boolean isRegistered = KSWAUserAuthentication.registerUser(username, password, licenceID);

                    if (Licencing.validateLicence(licenceID)){
                        //pass
                    } else {
                        JOptionPane.showMessageDialog(KSWALoginUI.this, "Licence Invalid");
                    }

                    if (isRegistered) {
                        KSWATeacher authenticatedTeacher = KSWAUserAuthentication.authenticateUser(username, password);

                        JOptionPane.showMessageDialog(KSWALoginUI.this, "Registration successful!");
                        loginSuccess(authenticatedTeacher);  // Weiterleitung an KSWAApplicationGUI
                    } else {
                        JOptionPane.showMessageDialog(KSWALoginUI.this, "Username already exists or licence invalid");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    // Main Start
    private void loginSuccess(KSWATeacher authenticatedTeacher) {
        // Login-UI ausblenden
        this.setVisible(false);

        // Anwendungsgui initialisieren und anzeigen
        applicationGUI = new KSWAApplicationGUI(authenticatedTeacher);
        applicationGUI.setVisible(true);
    }
// Main Start
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new KSWALoginUI();
            }
        });
    }
}
