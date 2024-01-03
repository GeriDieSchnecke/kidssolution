package KSWABackend;

import KSWABackend.Authentication.KSWAUserAuthentication;
import KSWABackend.Coverter.KSWAExcelConverter;
import KSWABackend.Licencing.Licencing;
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
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static KSWABackend.Authentication.KSWAUserAuthentication.*;
import static KSWABackend.Coverter.KSWAExcelConverter.importExcel;

public class KSWAApplicationGUI extends JFrame {
    private JTable childrenTable;
    private JTable subjectsTable;

    private DefaultTableModel childrenTableModel;
    private DefaultTableModel subjectsTableModel;
    private DefaultTableModel testsTableModel;
    private final List<KSWAChildren> childrenList = new ArrayList<>();
    private JPanel mainPanel;

    private final Map<String, String> userCredentials;
    private boolean isLoggedIn;
    private JButton logoutButton;

    private JButton showGradesChartButton;

    private JButton profileButton;

    private KSWATeacher currentTeacher;


    private JTextField childrenFilterField;

    private JTextField subjectsFilterField;

    private JTextField testsFilterField;

    public KSWAApplicationGUI() {
        userCredentials = new HashMap<>();
        isLoggedIn = false;
        initializeUI();
        displayChildrenData();
        addShowGradesChartButton();
        addLoginAndRegisterComponents();
        if (!isLoggedIn) {
            addTeacherOnStartup();
        }
        addTeacherProfileButton();
        addChildrenProfileButton();
        hideUnlicencedComponents(currentTeacher);
    }
    public KSWAApplicationGUI(KSWATeacher teacher) {
        this.currentTeacher = teacher;
        userCredentials = new HashMap<>();
        isLoggedIn = true;
        initializeUI();
        displayChildrenData();
        addShowGradesChartButton();
        addLoginAndRegisterComponents();
        if (!isLoggedIn) {
            addTeacherOnStartup();
        }
        addTeacherProfileButton();
        addChildrenProfileButton();
        hideUnlicencedComponents(currentTeacher);
    }

    private void hideUnlicencedComponents(KSWATeacher teacher){

        String[] licences = Licencing.getLicences(teacher.getId());

        if(licences[0].equals("invalid")) {
            showGradesChartButton.setEnabled(false);
            profileButton.setEnabled(false);
            return;
        }

        if(licences[1].equals("false")) {
            showGradesChartButton.setVisible(false);
        }

        if(licences[3].equals("false")) {
            profileButton.setVisible(false);
        }
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
        JTable testsTable = null;
        assert false;
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
            String id = "21";
            String username = JOptionPane.showInputDialog("Enter Username:");
            String password = JOptionPane.showInputDialog("Enter Password:");

            if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
                ImageIcon imagePath = readImageIconFromDesktop();

                userCredentials.put(username, password);
                currentTeacher = new KSWATeacher(id, username, "Teacher", imagePath);
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

    private void displayTeacherProfile() {
        if (currentTeacher != null) {
            JFrame profileFrame = new JFrame("Teacher Profile");
            profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            profileFrame.setSize(400, 400);

            JPanel profilePanel = new JPanel(new BorderLayout());
            profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            profilePanel.setBackground(Color.WHITE);

            JLabel titleLabel = new JLabel("Teacher Profile");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            profilePanel.add(titleLabel, BorderLayout.NORTH);

            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
            infoPanel.setBackground(Color.WHITE);

            JLabel idLabel = new JLabel("ID: " + currentTeacher.getId());
            idLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            idLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
            infoPanel.add(idLabel, BorderLayout.NORTH);

            if (currentTeacher.getImage() != null) {
                ImageIcon teacherImage = currentTeacher.getImage();
                Image scaledImage = teacherImage.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                ImageIcon scaledTeacherImage = new ImageIcon(scaledImage);
                JLabel imageLabel = new JLabel(scaledTeacherImage);
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
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

    private void displayChildrenProfile(KSWAChildren currentChild) {
        KSWAChildren selectedChild;
        if (currentChild != null) {
            selectedChild = currentChild;
        }
        else {
            int row_index = childrenTable.getSelectedRow();
            selectedChild = childrenList.get(row_index);
        }

        if (selectedChild != null) {
            JFrame profileFrame = new JFrame("Children Profile");
            profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            profileFrame.setSize(600, 400);

            JPanel profilePanel = new JPanel(new BorderLayout());
            profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            profilePanel.setBackground(Color.WHITE);

            JLabel titleLabel = new JLabel("Children Profile");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            profilePanel.add(titleLabel, BorderLayout.NORTH);

            JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));

            JLabel idLabel = new JLabel("ID: " + selectedChild.getId());
            idLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            infoPanel.add(idLabel);

            JLabel nameLabel = new JLabel("Name: " + selectedChild.getChprename() + " " + selectedChild.getChname());
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            infoPanel.add(nameLabel);

            JLabel birthdayLabel = new JLabel("Birthday: " + selectedChild.getChbirthday());
            birthdayLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            infoPanel.add(birthdayLabel);

            infoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
            infoPanel.setBackground(Color.WHITE);

            profilePanel.add(infoPanel, BorderLayout.CENTER);

            JPanel subjectsTestsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            subjectsTestsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            JPanel subjectsPanel = new JPanel(new BorderLayout());
            subjectsPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
            subjectsPanel.setBackground(Color.WHITE);

            JLabel subjectsLabel = new JLabel("Subjects");
            subjectsLabel.setFont(new Font("Arial", Font.BOLD, 20));
            subjectsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            subjectsPanel.add(subjectsLabel, BorderLayout.NORTH);

            List<KSWASubject> subjects = selectedChild.getChsubjects();
            String[] subjectNames = subjects.stream().map(KSWASubject::getSuname).toArray(String[]::new);

            JList<String> subjectsList = new JList<>(subjectNames);
            subjectsList.setFont(new Font("Arial", Font.PLAIN, 16));
            JScrollPane subjectsScrollPane = new JScrollPane(subjectsList);
            subjectsPanel.add(subjectsScrollPane, BorderLayout.CENTER);

            subjectsTestsPanel.add(subjectsPanel);

            JPanel testsPanel = new JPanel(new BorderLayout());
            testsPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
            testsPanel.setBackground(Color.WHITE);

            JLabel testsLabel = new JLabel("Tests");
            testsLabel.setFont(new Font("Arial", Font.BOLD, 20));
            testsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            testsPanel.add(testsLabel, BorderLayout.NORTH);

            List<KSWATest> tests = subjects.isEmpty() ? new ArrayList<>() : subjects.get(0).getTests();
            String[] testNames = tests.stream().map(KSWATest::getTename).toArray(String[]::new);

            JList<String> testsList = new JList<>(testNames);
            testsList.setFont(new Font("Arial", Font.PLAIN, 16));
            JScrollPane testsScrollPane = new JScrollPane(testsList);
            testsPanel.add(testsScrollPane, BorderLayout.CENTER);

            subjectsTestsPanel.add(testsPanel);

            profilePanel.add(subjectsTestsPanel, BorderLayout.SOUTH);

            profileFrame.add(profilePanel);
            profileFrame.setLocationRelativeTo(null);
            profileFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No child selected.");
        }
    }




    private void addChildrenProfileButton() {
        profileButton = createStyledButton("View Children's Profile", Color.LIGHT_GRAY);
        profileButton.addActionListener(e -> displayChildrenProfile(null));

        profileButton.setPreferredSize(new Dimension(200, 30));

        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        profilePanel.add(profileButton);
        profilePanel.setBackground(new Color(200, 200, 200));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        mainPanel.add(profilePanel, BorderLayout.NORTH);

        JPanel buttonPanel = (JPanel) ((BorderLayout) mainPanel.getLayout()).getLayoutComponent(BorderLayout.SOUTH);
        buttonPanel.add(profileButton);
        buttonPanel.revalidate();

        profileButton.setEnabled(false);

        childrenTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = childrenTable.getSelectedRow();
            profileButton.setEnabled(selectedRow >= 0 && selectedRow < childrenList.size());
        });
    }


    private void addTeacherProfileButton() {
        JButton profileButton = createStyledButton("View Teachers Profile", Color.LIGHT_GRAY);
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


    private void addLoginAndRegisterComponents() {
            int verticalSpacing = 10;
            JButton loginButton = createStyledButton("Login", Color.WHITE);
            JButton registerButton = createStyledButton("Register", Color.CYAN);
            JButton logoutButton = createStyledButton("Logout", Color.WHITE);
            logoutButton.addActionListener(e -> performLogout());
            loginButton.addActionListener(e -> showLoginDialog());
            registerButton.addActionListener(e -> showRegisterDialog());

            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
            loginPanel.add(Box.createVerticalStrut(verticalSpacing));
            loginPanel.add(loginButton);
            loginPanel.add(Box.createVerticalStrut(verticalSpacing));
            loginPanel.add(registerButton);
            loginPanel.add(Box.createVerticalStrut(verticalSpacing));
            loginPanel.add(logoutButton);
            loginPanel.add(Box.createVerticalStrut(verticalSpacing));
            loginPanel.setBackground(new Color(200, 200, 200));
            loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            mainPanel.add(loginPanel, BorderLayout.WEST);
    }


    public void showLoginDialog() {
        if (!isLoggedIn) {
            String username = JOptionPane.showInputDialog("Enter Username:");
            String password = JOptionPane.showInputDialog("Enter Password:");

            if (validateUserCredentials(username, password)) {
                isLoggedIn = true;
                JOptionPane.showMessageDialog(null, "Login Successful!");
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
        try {
            createSheetIfNotExists();

            KSWATeacher isUserAuthenticated = authenticateUser(username, password);
            System.out.println("Is User Authenticated: " + isUserAuthenticated);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showRegisterDialog() {
        String newUsername = JOptionPane.showInputDialog("Enter New Username:");
        String newPassword = JOptionPane.showInputDialog("Enter New Password:");
        String newLicence = JOptionPane.showInputDialog("Enter Licence:");

        try {
            createSheetIfNotExists();

            boolean isUserRegistered = KSWAUserAuthentication.registerUser(newUsername, newPassword, newLicence);
            System.out.println("Is User Registered: " + isUserRegistered);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addLogoutButton() {
        logoutButton = createStyledButton("Logout", Color.WHITE);
        logoutButton.setEnabled(false);
        logoutButton.addActionListener(e -> performLogout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(logoutButton, BorderLayout.EAST);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }


    private void performLogout() {
        isLoggedIn = false;
        JOptionPane.showMessageDialog(null, "Logged out successfully!");

        dispose();

        SwingUtilities.invokeLater(KSWALoginUI::new);
    }


    private void addShowGradesChartButton() {
        showGradesChartButton = new JButton("Show Grades Chart");
        showGradesChartButton.setFont(new Font("Arial", Font.BOLD, 14));
        showGradesChartButton.setBackground(Color.WHITE);

        showGradesChartButton.addActionListener(e -> displayGradesChart());

        JPanel buttonPanel = (JPanel) ((BorderLayout) mainPanel.getLayout()).getLayoutComponent(BorderLayout.SOUTH);
        buttonPanel.add(showGradesChartButton);
        buttonPanel.revalidate();
    }


    private void displayGradesChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (KSWAChildren child : childrenList) {
            List<KSWASubject> subjects = child.getChsubjects();
            for (KSWASubject subject : subjects) {
                double subjectGrade = subject.getSugrade();
                dataset.addValue(subjectGrade, subject.getSuname(), child.getId());
            }
        }

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
        setTitle("Children Organization Kid");
        setSize(1200, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);

        childrenTableModel = new DefaultTableModel(new String[]{"Child ID", "Child Name", "Birthday"}, 0);
        subjectsTableModel = new DefaultTableModel(new String[]{"Subject Name", "Grade", "Tests"}, 0);
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

        JButton addChildrenButton = createStyledButton("Add Child", Color.WHITE);
        JButton addSubjectsButton = createStyledButton("Add Subjects", Color.WHITE);
        JButton addTestsButton = createStyledButton("Add Tests", Color.WHITE);

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

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 20, 10));
        filterPanel.add(new JLabel("Filter Children:"));
        filterPanel.add(childrenFilterField);
        filterPanel.add(new JLabel("Filter Subjects:"));
        filterPanel.add(subjectsFilterField);
        filterPanel.add(new JLabel("Filter Tests:"));
        filterPanel.add(testsFilterField);

        mainPanel.add(filterPanel, BorderLayout.BEFORE_FIRST_LINE);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JButton exportExcelButton = new JButton("Export to Excel");
        exportExcelButton.setFont(new Font("Arial", Font.BOLD, 16));
        exportExcelButton.addActionListener(e -> {
            String absPathWorkingdir = System.getProperty("user.dir");
            String filePath = absPathWorkingdir + "\\KidsSolutionExport.xlsx";
            KSWAExcelConverter.exportExcel(childrenList, filePath);
            JOptionPane.showMessageDialog(null, "Excel file has been created successfully!");
        });
        buttonPanel.add(exportExcelButton);

        JButton importExcelButton = new JButton("Import from Excel");
        importExcelButton.setFont(new Font("Arial", Font.BOLD, 16));
        importExcelButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Excel File");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx", "xls"));
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();
                List<KSWAChildren> importedData = importExcel(filePath);
                childrenList.addAll(importedData);
                displayChildrenData();
            }
        });
        buttonPanel.add(importExcelButton);

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        return button;
    }


    private void addChildrenOnly() {
        String id = JOptionPane.showInputDialog("Enter Child ID:");
        String firstName = JOptionPane.showInputDialog("Enter Child's First Name:");
        String lastName = JOptionPane.showInputDialog("Enter Child's Last Name:");
        String birthday = JOptionPane.showInputDialog("Enter Child's Birthday (YYYY-MM-DD):");

        KSWAChildren newChild = new KSWAChildren(id, firstName, lastName, birthday, null);
        childrenList.add(newChild);

        displayChildrenData();
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

                KSWASubject newSubject = new KSWASubject(subjectName, subjectGrade, new ArrayList<>(), 0);

                selectedChild.getChsubjects().add(newSubject);

                int option = JOptionPane.showConfirmDialog(null, "Add another Subject?", "Add Subject", JOptionPane.YES_NO_OPTION);
                addMoreSubjects = option == JOptionPane.YES_OPTION;
            }

            displaySubjects(selectedChild.getChsubjects());
        } else {
            JOptionPane.showMessageDialog(null, "Please select a child from the table first.");
        }
    }



    public void addTestsForSubject() {
        if (childrenList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No children available. Please add a child first.");
            return;
        }

        List<Long> childIds = childrenList.stream()
                .map(KSWAChildren::getId).toList();


        Long selectedChildId = (Long) JOptionPane.showInputDialog(
                null,
                "Select a Child ID:",
                "Add Tests for Subject",
                JOptionPane.QUESTION_MESSAGE,
                null,
                childIds.toArray(new Long[0]),
                childIds.get(0)
        );

        KSWAChildren selectedChild = null;
        for (KSWAChildren child : childrenList) {
            if (child.getId().equals(selectedChildId)) {
                selectedChild = child;
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
            String testDate = JOptionPane.showInputDialog("Enter Date for the Test (YYYY-MM-DD):");
            long testId = generateRandomId();

            KSWATest newTest = new KSWATest(testGrade, testName, testFactor, parseDate(testDate), testId);
            List<KSWATest> childrentests = selectedSubject.getTests();
            childrentests.add(newTest);

            selectedSubject.setTests(childrentests);

            int option = JOptionPane.showConfirmDialog(null, "Add another Test?", "Add Test", JOptionPane.YES_NO_OPTION);
            addMoreTests = (option == JOptionPane.YES_OPTION);
        }

        displayTests(selectedSubject.getTests());
    }

    private Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void displayChildrenData() {
        childrenTableModel.setRowCount(0);

        if (childrenList != null && !childrenList.isEmpty()) {
            for (KSWAChildren child : childrenList) {
                childrenTableModel.addRow(new Object[]{child.getId(), child.getChprename() + " " + child.getChname(), child.getChbirthday()});
            }
        }

        ListSelectionModel selectionModel = childrenTable.getSelectionModel();

        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = childrenTable.getSelectedRow();

                if (selectedRow >= 0) {
                    assert childrenList != null;
                    if (selectedRow < childrenList.size()) {
                        KSWAChildren selectedChild = childrenList.get(selectedRow);
                        displaySubjects(selectedChild.getChsubjects());
                    }
                }
            }
        });
    }

    public void displaySubjects(List<KSWASubject> subjects) {
        subjectsTableModel.setRowCount(0);

        if (subjects != null && !subjects.isEmpty()) {
            for (KSWASubject subject : subjects) {
                String testsInfo = getTestsInfo(subject.getTests());
                subjectsTableModel.addRow(new Object[]{subject.getSuname(), subject.getSugrade(), testsInfo});
            }
        }

        subjectsTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = subjectsTable.getSelectedRow();
            if (selectedRow >= 0) {
                assert subjects != null;
                if (selectedRow < subjects.size()) {
                    KSWASubject selectedSubject = subjects.get(selectedRow);
                    displayTests(selectedSubject.getTests());
                }
            }
        });
    }

    private String getTestsInfo(List<KSWATest> tests) {
        StringBuilder testsInfo = new StringBuilder();
        if (tests != null && !tests.isEmpty()) {
            for (KSWATest test : tests) {
                testsInfo.append(test.getTename()).append(": ").append(test.getTegrade()).append(", ");
            }
            if (testsInfo.length() >= 2) {
                testsInfo.delete(testsInfo.length() - 2, testsInfo.length());
            }
        }
        return testsInfo.toString();
    }


    public void displayTests(List<KSWATest> tests) {
        testsTableModel.setRowCount(0);

        if (tests != null && !tests.isEmpty()) {
            for (KSWATest test : tests) {
                Object[] objs = new Object[3];
                objs[0] =  test.getTename();
                objs[1] = test.getTegrade();
                objs[2] = test.getTefactor();
                objs[3] = test.getTedate();
                testsTableModel.addRow(Arrays.stream(objs).toArray());
            }
        }
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(KSWAApplicationGUI::new);
    }
}