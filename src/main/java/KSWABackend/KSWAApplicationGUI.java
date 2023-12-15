package KSWABackend;

import KSWABackend.Coverter.KSWAExcelConverter;
import KSWABackend.Model.KSWAChildren;
import KSWABackend.Model.KSWASubject;
import KSWABackend.Model.KSWATeacher;
import KSWABackend.Model.KSWATest;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;

public class KSWAApplicationGUI extends JFrame {
    private JTable childrenTable;
    private JTable subjectsTable;

    private JTable testsTable;
    private DefaultTableModel childrenTableModel;
    private DefaultTableModel subjectsTableModel;
    private DefaultTableModel testsTableModel;
    private final List<KSWAChildren> childrenList = new ArrayList<>();
    private JPanel mainPanel;
    private javax.swing.Timer timer;

    private Map<String, String> userCredentials; // Eine temporäre Speicherung von Benutzerdaten
    private boolean isLoggedIn;
    private JButton logoutButton;

    private KSWATeacher currentTeacher;

    private JTextField childrenFilterField;

    private JTextField subjectsFilterField;

    private JTextField testsFilterField;

    public KSWAApplicationGUI() {
        userCredentials = new HashMap<>();
        isLoggedIn = false;
        initializeUI();
        // Initialisierung des Timers, der alle 10 Sekunden ausgeführt wird
        scheduleDataUpdates();
        displayChildrenData();
        addShowGradesChartButton();
        addLoginAndRegisterComponents();
        if (!isLoggedIn) {
            addTeacherOnStartup();
        }
        addTeacherProfileButton();
    }

    private void filterChildren() {
        String filterText = childrenFilterField.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) childrenTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        childrenTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + filterText));
    }

    private void filterSubjects() {
        String filterText = subjectsFilterField.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) subjectsTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        subjectsTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + filterText));
    }

    private void filterTests() {
        String filterText = testsFilterField.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) testsTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        testsTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + filterText));
    }

    private void addTeacherOnStartup() {
        int option = JOptionPane.showConfirmDialog(null,
                "Do you want to create a new teacher account?",
                "Create Teacher Account",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            int id = 21;
            String username = JOptionPane.showInputDialog("Enter Username:");
            String password = JOptionPane.showInputDialog("Enter Password:");

            if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
                ImageIcon imagePath = readImageIconFromDesktop();

                userCredentials.put(username, password);
                currentTeacher = new KSWATeacher(id, username, "Teacher", imagePath); // Erstelle einen neuen Lehrer mit Bildpfad
                JOptionPane.showMessageDialog(null, "Teacher account created successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Username or Password!");
            }
        }
    }

    public ImageIcon readImageIconFromDesktop() {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + "/Desktop");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            return new ImageIcon(file.getAbsolutePath());
        } else {
            return null;
        }
    }

    // Methode zur Anzeige des Lehrerprofils
    private void displayTeacherProfile() {
        if (currentTeacher != null) {
            JFrame profileFrame = new JFrame("Teacher Profile");
            profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            profileFrame.setSize(400, 400); // Setzen einer festen Größe für das Profilfenster

            JPanel profilePanel = new JPanel(new BorderLayout());
            profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            profilePanel.setBackground(Color.WHITE); // Hintergrundfarbe auf Weiß setzen

            JLabel titleLabel = new JLabel("Teacher Profile");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            profilePanel.add(titleLabel, BorderLayout.NORTH);

            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
            infoPanel.setBackground(Color.WHITE); // Hintergrundfarbe des Info-Panels auf Weiß setzen

            JLabel idLabel = new JLabel("ID: " + currentTeacher.getId());
            idLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            idLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
            infoPanel.add(idLabel, BorderLayout.NORTH);

            // Lehrerbild hinzufügen, falls verfügbar
            if (currentTeacher.getImage() != null) {
                ImageIcon teacherImage = currentTeacher.getImage();
                Image scaledImage = teacherImage.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                ImageIcon scaledTeacherImage = new ImageIcon(scaledImage);
                JLabel imageLabel = new JLabel(scaledTeacherImage);
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Rahmen verstärken
                infoPanel.add(imageLabel, BorderLayout.CENTER);
            }

            JLabel nameLabel = new JLabel("Name: " + currentTeacher.getName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
            nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
            infoPanel.add(nameLabel, BorderLayout.SOUTH);

            profilePanel.add(infoPanel, BorderLayout.CENTER);

            profileFrame.add(profilePanel);
            profileFrame.setLocationRelativeTo(null);
            profileFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No teacher logged in.");
        }
    }



    // Methode zum Hinzufügen des Buttons zur Anzeige des Lehrerprofils
    private void addTeacherProfileButton() {
        JButton profileButton = createStyledButton("View Profile", Color.LIGHT_GRAY);
        profileButton.addActionListener(e -> displayTeacherProfile());

        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        profilePanel.add(profileButton);
        profilePanel.setBackground(new Color(200, 200, 200));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        mainPanel.add(profilePanel, BorderLayout.NORTH);

        JPanel buttonPanel = (JPanel) ((BorderLayout) mainPanel.getLayout()).getLayoutComponent(BorderLayout.SOUTH);
        buttonPanel.add(profileButton);
        buttonPanel.revalidate();
    }

    private void scheduleDataUpdates() {
        int delay = 0; // Starte sofort
        int interval = 10000; // Wiederhole alle 10 Sekunden

        timer = new javax.swing.Timer(interval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hier werden Lese- und Schreiboperationen durchgeführt
                List<KSWAChildren> childrenList = KSWAExcelConverter.readDataFromExcel();
                // Hier könntest du Daten bearbeiten

                KSWAExcelConverter.writeToExcel(childrenList); // oder exportExcel(), je nach Bedarf
            }
        });

        timer.setInitialDelay(delay);
        timer.start();
    }
    private void addLoginAndRegisterComponents() {
        JButton loginButton = createStyledButton("Login", Color.YELLOW);
        JButton registerButton = createStyledButton("Register", Color.CYAN);

        loginButton.addActionListener(e -> showLoginDialog());
        registerButton.addActionListener(e -> showRegisterDialog());

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        int verticalSpacing = 10; // Ändern Sie diesen Wert je nach gewünschtem Abstand

        loginPanel.add(Box.createVerticalStrut(verticalSpacing)); // Abstand oben

        loginPanel.add(loginButton);
        loginPanel.add(Box.createVerticalStrut(verticalSpacing)); // Abstand zwischen den Buttons
        loginPanel.add(registerButton);

        loginPanel.add(Box.createVerticalStrut(verticalSpacing)); // Abstand unten

        loginPanel.setBackground(new Color(200, 200, 200));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        mainPanel.add(loginPanel, BorderLayout.WEST);
    }


    private void showLoginDialog() {
        if (!isLoggedIn) {
            String username = JOptionPane.showInputDialog("Enter Username:");
            String password = JOptionPane.showInputDialog("Enter Password:");

            if (validateUserCredentials(username, password)) {
                isLoggedIn = true;
                JOptionPane.showMessageDialog(null, "Login Successful!");
                // Hier kannst du weitere Aktionen nach erfolgreichem Login ausführen
                if (logoutButton != null) {
                    logoutButton.setEnabled(true);
                } else {
                    addLogoutButton();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Username or Password!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "You are already logged in!");
        }
    }

    private boolean validateUserCredentials(String username, String password) {
        // Hier würdest du normalerweise die eingegebenen Anmeldeinformationen mit einer Datenbank oder einem anderen Speicherort vergleichen
        // Hier ist ein Beispiel mit fest codierten Anmeldeinformationen
        userCredentials.put("exampleUser", "examplePassword");
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    private void showRegisterDialog() {
        String newUsername = JOptionPane.showInputDialog("Enter New Username:");
        String newPassword = JOptionPane.showInputDialog("Enter New Password:");

        if (newUsername != null && newPassword != null && !newUsername.isEmpty() && !newPassword.isEmpty()) {
            userCredentials.put(newUsername, newPassword);
            JOptionPane.showMessageDialog(null, "Registration Successful!");
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Username or Password!");
        }
    }

    private void addLogoutButton() {
        logoutButton = createStyledButton("Logout", Color.MAGENTA);
        logoutButton.setEnabled(false);
        logoutButton.addActionListener(e -> performLogout());

        JPanel buttonPanel = (JPanel) ((BorderLayout) mainPanel.getLayout()).getLayoutComponent(BorderLayout.SOUTH);
        buttonPanel.add(logoutButton);
        buttonPanel.revalidate();
    }

    private void performLogout() {
        isLoggedIn = false;
        JOptionPane.showMessageDialog(null, "Logged out successfully!");
        logoutButton.setEnabled(false);
        // Hier könntest du andere notwendige Aktionen nach dem Logout ausführen
    }

    private void addShowGradesChartButton() {
        JButton showGradesChartButton = new JButton("Show Grades Chart");
        showGradesChartButton.setFont(new Font("Arial", Font.BOLD, 14));
        showGradesChartButton.setBackground(Color.RED);

        showGradesChartButton.addActionListener(e -> displayGradesChart());

        JPanel buttonPanel = (JPanel) ((BorderLayout) mainPanel.getLayout()).getLayoutComponent(BorderLayout.SOUTH);
        buttonPanel.add(showGradesChartButton);
        buttonPanel.revalidate();
    }

    private void displayGradesChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Hier müssen Sie die Daten für das Diagramm einfügen
        // Angenommen, Sie haben Zugriff auf die Daten der Kinder, Fächer und Tests

        for (KSWAChildren child : childrenList) {
            List<KSWASubject> subjects = child.getChsubjects();
            for (KSWASubject subject : subjects) {
                double subjectGrade = subject.getSugrade();
                dataset.addValue(subjectGrade, subject.getSuname(), child.getId());
            }
        }

        // Erstellen des Balkendiagramms mit JFreeChart
        JFreeChart barChart = ChartFactory.createBarChart(
                "Grades Overview", "Child ID", "Grades",
                dataset);

        ChartPanel chartPanel = new ChartPanel(barChart);
        JFrame frame = new JFrame("Grades Chart");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private void initializeUI() {
        setTitle("KSWA Excel Converter");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);

        childrenTableModel = new DefaultTableModel(new String[]{"Child ID", "Child Name", "Birthday"}, 0);
        subjectsTableModel = new DefaultTableModel(new String[]{"Subject Name", "Grade"}, 0);
        testsTableModel = new DefaultTableModel(new String[]{"Test Name", "Grade", "Factor", "Date"}, 0);

        childrenTable = new JTable(childrenTableModel);
        subjectsTable = new JTable(subjectsTableModel);

        JScrollPane childrenScrollPane = new JScrollPane(childrenTable);
        JScrollPane subjectsScrollPane = new JScrollPane(subjectsTable);

        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(childrenScrollPane);
        panel.add(subjectsScrollPane);

        mainPanel.add(panel, BorderLayout.CENTER);

        getContentPane().setBackground(new Color(240, 240, 240));

        JButton addChildrenButton = createStyledButton("Add Child", Color.BLUE);
        JButton addSubjectsButton = createStyledButton("Add Subjects", Color.GREEN);
        JButton addTestsButton = createStyledButton("Add Tests", Color.ORANGE);

        addChildrenButton.addActionListener(e -> addChildrenOnly());
        addSubjectsButton.addActionListener(e -> addSubjectsForChild());
        addTestsButton.addActionListener(e -> addTestsForSubject());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(addChildrenButton);
        buttonPanel.add(addSubjectsButton);
        buttonPanel.add(addTestsButton);
        buttonPanel.setBackground(new Color(200, 200, 200));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        childrenFilterField = new JTextField();
        childrenFilterField.setPreferredSize(new Dimension(150, 25));
        childrenFilterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterChildren();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterChildren();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterChildren();
            }
        });

        // Filterfeld für Fächer
        subjectsFilterField = new JTextField();
        subjectsFilterField.setPreferredSize(new Dimension(150, 25));
        subjectsFilterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterSubjects();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterSubjects();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterSubjects();
            }
        });

        // Filterfeld für Tests
        testsFilterField = new JTextField();
        testsFilterField.setPreferredSize(new Dimension(150, 25));
        testsFilterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTests();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTests();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTests();
            }
        });

        // Hinzufügen der Filterfelder zu Ihrem UI
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        filterPanel.add(new JLabel("Filter Children:"));
        filterPanel.add(childrenFilterField);
        filterPanel.add(new JLabel("Filter Subjects:"));
        filterPanel.add(subjectsFilterField);
        filterPanel.add(new JLabel("Filter Tests:"));
        filterPanel.add(testsFilterField);

        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        return button;
    }

    // Method to load data - Replace this with your data retrieval logic
    private void addData() {
        int selectedRow = childrenTable.getSelectedRow();

        if (selectedRow >= 0 && selectedRow < childrenList.size()) {
            KSWAChildren existingChild = childrenList.get(selectedRow);

            boolean addMoreSubjects = true;

            while (addMoreSubjects) {
                String subjectName = JOptionPane.showInputDialog("Enter Subject Name for the Child:");
                double subjectGrade = Double.parseDouble(JOptionPane.showInputDialog("Enter Grade for the Subject:"));

                KSWASubject newSubject = new KSWASubject(subjectName, subjectGrade, new ArrayList<>(), 0); // Set an appropriate ID

                boolean addMoreTests = true;

                while (addMoreTests) {
                    String testName = JOptionPane.showInputDialog("Enter Test Name for the Subject:");
                    double testGrade = Double.parseDouble(JOptionPane.showInputDialog("Enter Grade for the Test:"));
                    double testFactor = Double.parseDouble(JOptionPane.showInputDialog("Enter Factor for the Test:"));
                    // Assuming date input as string for simplicity
                    String testDate = JOptionPane.showInputDialog("Enter Date for the Test (YYYY-MM-DD):");

                    KSWATest newTest = new KSWATest(testGrade, testName, testFactor, null, 0); // Set an appropriate ID and manage the Date object properly

                    newSubject.getTests().add(newTest);

                    int option = JOptionPane.showConfirmDialog(null, "Add another Test?", "Add Test", JOptionPane.YES_NO_OPTION);
                    addMoreTests = option == JOptionPane.YES_OPTION;
                }

                existingChild.getChsubjects().add(newSubject);

                int option = JOptionPane.showConfirmDialog(null, "Add another Subject?", "Add Subject", JOptionPane.YES_NO_OPTION);
                addMoreSubjects = option == JOptionPane.YES_OPTION;
            }

            displayChildrenData(); // Refresh the display after adding data
        } else {
            JOptionPane.showMessageDialog(null, "Please select a child from the table first.");
        }
    }

    // Method to add children - Replace this with your data retrieval logic
    private void addChildrenOnly() {
        String id = JOptionPane.showInputDialog("Enter Child ID:");
        String firstName = JOptionPane.showInputDialog("Enter Child's First Name:");
        String lastName = JOptionPane.showInputDialog("Enter Child's Last Name:");
        String birthday = JOptionPane.showInputDialog("Enter Child's Birthday (YYYY-MM-DD):");

        KSWAChildren newChild = new KSWAChildren(id, firstName, lastName, birthday, null);
        childrenList.add(newChild);

        displayChildrenData(); // Aktualisieren Sie die Anzeige nach dem Hinzufügen des neuen Kindes
    }

    private void addSubjectsForChild() {
        if (childrenList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No children available. Please add a child first.");
            return;
        }

        int selectedRow = childrenTable.getSelectedRow();

        if (selectedRow >= 0 && selectedRow < childrenList.size()) {
            KSWAChildren selectedChild = childrenList.get(selectedRow);

            boolean addMoreSubjects = true;

            while (addMoreSubjects) {
                String subjectName = JOptionPane.showInputDialog("Enter Subject Name for the Child:");
                double subjectGrade = Double.parseDouble(JOptionPane.showInputDialog("Enter Grade for the Subject:"));

                KSWASubject newSubject = new KSWASubject(subjectName, subjectGrade, new ArrayList<>(), 0); // Set an appropriate ID

                selectedChild.getChsubjects().add(newSubject);

                int option = JOptionPane.showConfirmDialog(null, "Add another Subject?", "Add Subject", JOptionPane.YES_NO_OPTION);
                addMoreSubjects = option == JOptionPane.YES_OPTION;
            }

            displaySubjects(selectedChild.getChsubjects()); // Refresh the display after adding subjects for the child
        } else {
            JOptionPane.showMessageDialog(null, "Please select a child from the table first.");
        }
    }



    private void addTestsForSubject() {
        if (childrenList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No children available. Please add a child first.");
            return;
        }

        String[] childIds = childrenList.stream().map(KSWAChildren::getId).toArray(String[]::new);

        String selectedChildId = (String) JOptionPane.showInputDialog(null, "Select a Child ID:",
                "Add Tests for Subject", JOptionPane.QUESTION_MESSAGE, null, childIds, childIds[0]);

        KSWAChildren selectedChild = null;
        for (KSWAChildren child : childrenList) {
            if (child.getId().equals(selectedChildId)) {
                selectedChild = child;
                break;
            }
        }

        if (selectedChild == null) {
            JOptionPane.showMessageDialog(null, "Child not found.");
            return;
        }

        if (selectedChild.getChsubjects().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No subjects available for the selected child. Please add subjects first.");
            return;
        }

        List<KSWASubject> subjects = selectedChild.getChsubjects();
        String[] subjectNames = subjects.stream().map(KSWASubject::getSuname).toArray(String[]::new);

        String selectedSubjectName = (String) JOptionPane.showInputDialog(null, "Select a Subject:",
                "Add Tests for Subject", JOptionPane.QUESTION_MESSAGE, null, subjectNames, subjectNames[0]);

        KSWASubject selectedSubject = null;
        for (KSWASubject subject : subjects) {
            if (subject.getSuname().equals(selectedSubjectName)) {
                selectedSubject = subject;
                break;
            }
        }

        if (selectedSubject == null) {
            JOptionPane.showMessageDialog(null, "Subject not found.");
            return;
        }

        boolean addMoreTests = true;
        while (addMoreTests) {
            String testName = JOptionPane.showInputDialog("Enter Test Name for the Subject:");
            double testGrade = Double.parseDouble(JOptionPane.showInputDialog("Enter Grade for the Test:"));
            double testFactor = Double.parseDouble(JOptionPane.showInputDialog("Enter Factor for the Test:"));
            // Assuming date input as string for simplicity
            String testDate = JOptionPane.showInputDialog("Enter Date for the Test (YYYY-MM-DD):");

            KSWATest newTest = new KSWATest(testGrade, testName, testFactor, new Date(testDate), 0); // Set an appropriate ID and manage the Date object properly

            // Hier wird der neue Test zum ausgewählten Fach des ausgewählten Kindes hinzugefügt
            selectedSubject.getTests().add(newTest);

            int option = JOptionPane.showConfirmDialog(null, "Add another Test?", "Add Test", JOptionPane.YES_NO_OPTION);
            addMoreTests = option == JOptionPane.YES_OPTION;

            displayTests(selectedSubject.getTests()); // Refresh the display after adding tests for the subject
        }
    }


    // Method to display children data
    private void displayChildrenData() {
        childrenTableModel.setRowCount(0); // Clear previous data

        if (childrenList != null && !childrenList.isEmpty()) {
            for (KSWAChildren child : childrenList) {
                childrenTableModel.addRow(new Object[]{child.getId(), child.getChprename() + " " + child.getChname(), child.getChbirthday()});
            }
        }

        childrenTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = childrenTable.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < childrenList.size()) {
                KSWAChildren selectedChild = childrenList.get(selectedRow);
                displaySubjects(selectedChild.getChsubjects());
            }
        });
    }

    // Method to display subjects
    private void displaySubjects(List<KSWASubject> subjects) {
        subjectsTableModel.setRowCount(0); // Clear previous data

        if (subjects != null && !subjects.isEmpty()) {
            for (KSWASubject subject : subjects) {
                subjectsTableModel.addRow(new Object[]{subject.getSuname(), subject.getSugrade()});
            }
        }

        subjectsTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = subjectsTable.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < subjects.size()) {
                KSWASubject selectedSubject = subjects.get(selectedRow);
                displayTests(selectedSubject.getTests());
            }
        });
    }

    // Method to display tests
    private void displayTests(List<KSWATest> tests) {
        testsTableModel.setRowCount(0); // Clear previous data

        if (tests != null && !tests.isEmpty()) {
            for (KSWATest test : tests) {
                testsTableModel.addRow(new Object[]{test.getTename(), test.getTegrade(), test.getTefactor(), test.getTedate()});
            }
        }
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new KSWAApplicationGUI());
    }
}

