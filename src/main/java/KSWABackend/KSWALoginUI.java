package KSWABackend;

import KSWABackend.Authentication.KSWAUserAuthentication;
import KSWABackend.Licencing.Licencing;
import KSWABackend.Model.KSWATeacher;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class KSWALoginUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField licenceField;

    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 400;

    public KSWALoginUI() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Children Organization Kid");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        add(panel);
        setupComponents(panel);

        setVisible(true);
    }

    private void setupComponents(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Children App");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, gbc);

        gbc.gridy++;
        addLabelAndTextField(panel, "Username:", usernameField = new JTextField(25), gbc); // Vergrößertes Textfeld

        gbc.gridy++;
        addLabelAndTextField(panel, "Password:", passwordField = new JPasswordField(25), gbc); // Vergrößertes Textfeld

        gbc.gridy++;
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(120, 30));
        panel.add(loginButton, gbc);

        gbc.gridy++;
        addLabelAndTextField(panel, "Licence:", licenceField = new JTextField(25), gbc); // Vergrößertes Textfeld

        gbc.gridy++;
        JLabel infoLabel = new JLabel("For registration, enter licence number");
        panel.add(infoLabel, gbc);

        gbc.gridy++;
        JButton registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(120, 30));
        panel.add(registerButton, gbc);

        loginButton.addActionListener(e -> {
            try {
                onLoginButtonClick();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        registerButton.addActionListener(e -> onRegisterButtonClick());
    }

    private void addLabelAndTextField(JPanel panel, String labelText, JTextField textField, GridBagConstraints gbc) {
        gbc.gridx = 0;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(label, gbc);

        gbc.gridx++;
        panel.add(textField, gbc);
    }

    private void onLoginButtonClick() throws IOException {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String teacherName;
        String teacherPassword;

        KSWATeacher authenticatedTeacher = KSWAUserAuthentication.authenticateUser(username, password);

        if (authenticatedTeacher.getName() == null) {
            //Default if not able to login
            teacherName = "test";
            teacherPassword = "pwd";
            username = "test";
            password = "pwd";
        }
        else {
            teacherName = authenticatedTeacher.getName();
            teacherPassword = authenticatedTeacher.getPassword();
            System.out.println(teacherName);
        }

        if (teacherName.equals(username) && teacherPassword.equals(password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            //KSWATeacher authenticatedTeacher = new KSWATeacher(username, "Teacher Name");
            loginSuccess(authenticatedTeacher);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password");
        }
    }

    private void onRegisterButtonClick(){
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String licenceID = licenceField.getText();

        if (username.isEmpty() || password.isEmpty() || licenceID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username, password, and licence number");
            return;
        }

        if (Licencing.validateLicence(licenceID)) {
            try {
                boolean isRegistered = KSWAUserAuthentication.registerUser(username, password, licenceID);
            } catch (Exception exception){
                exception.printStackTrace();
            }
            KSWATeacher registeredTeacher = new KSWATeacher(username, "Teacher Name");
            JOptionPane.showMessageDialog(this, "Registration successful!");
            loginSuccess(registeredTeacher);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid licence");
        }
    }

    private void loginSuccess(KSWATeacher authenticatedTeacher) {
        this.setVisible(false);
        KSWAApplicationGUI applicationGUI = new KSWAApplicationGUI(authenticatedTeacher);
        applicationGUI.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(KSWALoginUI::new);
    }
}