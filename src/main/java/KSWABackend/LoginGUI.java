package KSWABackend;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel usernameLabel; // Hinzugefügt
    private JLabel passwordLabel; // Hinzugefügt
    public LoginGUI(KSWAApplicationGUI kswaApplicationGUI) {
        initializeUI();
    }

    private void initializeUI() {
        // ... (Rest des vorhandenen LoginGUI-Codes bleibt unverändert) ...

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> performLogin());

        // Füge alle UI-Elemente zum Login-Panel hinzu
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);

        add(loginPanel);

        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void performLogin() {
        // Hier überprüfst du die Anmeldeinformationen
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (validateUserCredentials(username, password)) {
            JOptionPane.showMessageDialog(null, "Login Successful!");
            openMainApplication();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Username or Password!");
        }
    }

    private boolean validateUserCredentials(String username, String password) {
        // Beispiel: Überprüfung der Anmeldeinformationen mit hartcodierten Daten
        // Du solltest diese Logik durch eine Datenbankabfrage oder einen anderen Speicherort ersetzen

        Map<String, String> userCredentials = new HashMap<>();
        // Fülle die Map mit gültigen Benutzeranmeldeinformationen
        userCredentials.put("exampleUser", "examplePassword");
        userCredentials.put("anotherUser", "anotherPassword");

        // Überprüfe, ob der eingegebene Benutzername in der Map existiert und das Passwort übereinstimmt
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    private void openMainApplication() {
        // Öffne die Hauptanwendung, wenn die Anmeldeinformationen korrekt sind
        new KSWAApplicationGUI(this); // Erstelle die Hauptanwendung GUI und übergebe LoginGUI als Referenz
        dispose(); // Schließe das Login-Fenster nach erfolgreicher Anmeldung
    }

    public void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI(new KSWAApplicationGUI(this)));
    }
}
