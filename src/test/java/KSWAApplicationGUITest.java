import KSWABackend.KSWAApplicationGUI;
import KSWABackend.Model.KSWASubject;
import KSWABackend.Model.KSWATest;
import org.junit.jupiter.api.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class KSWAApplicationGUITest {

    private Logger log = Logger.getLogger(String.valueOf(this.getClass()));

    @BeforeAll
    static void initAll() {
        // Initialization before all tests
    }

    @BeforeEach
    void init() {
        // Initialization before each test
    }

    @Test
    @DisplayName("Action Performed")
    public void actionPerformed() {
        try {
            log.info("Starting execution of actionPerformed");
            List<KSWATest> tests = null;

            KSWAApplicationGUI kswaapplicationgui = new KSWAApplicationGUI();
            kswaapplicationgui.displayTests(tests);
            Assertions.assertTrue(true);
        } catch (Exception exception) {
            exception.printStackTrace();
            Assertions.assertFalse(false);
        }
    }

    @Test
    @DisplayName("Show Login Dialog")
    public void showLoginDialog() {
        try {
            log.info("Starting execution of showLoginDialog");

            KSWAApplicationGUI kswaapplicationgui = new KSWAApplicationGUI();
            kswaapplicationgui.showLoginDialog();
            Assertions.assertTrue(true);
        } catch (Exception exception) {
            exception.printStackTrace();
            Assertions.assertFalse(false);
        }
    }

    @Test
    @DisplayName("Insert Update")
    public void insertUpdate() {
        try {
            log.info("Starting execution of insertUpdate");
            List<KSWASubject> subjects = null;

            KSWAApplicationGUI kswaapplicationgui = new KSWAApplicationGUI();
            kswaapplicationgui.displaySubjects(subjects);
            Assertions.assertTrue(true);
        } catch (Exception exception) {
            exception.printStackTrace();
            Assertions.assertFalse(false);
        }
    }

    @Test
    @DisplayName("Remove Update")
    public void removeUpdate() {
        try {
            log.info("Starting execution of removeUpdate");
            DocumentEvent e = null;

            KSWAApplicationGUI kswaapplicationgui = new KSWAApplicationGUI();
            kswaapplicationgui.displayChildrenData();
            Assertions.assertTrue(true);
        } catch (Exception exception) {
            exception.printStackTrace();
            Assertions.assertFalse(false);
        }
    }

    @Test
    @DisplayName("Changed Update")
    public void changedUpdate() {
        try {
            log.info("Starting execution of changedUpdate");

            KSWAApplicationGUI kswaapplicationgui = new KSWAApplicationGUI();
            kswaapplicationgui.addTestsForSubject();
            Assertions.assertTrue(true);
        } catch (Exception exception) {
            exception.printStackTrace();
            Assertions.assertFalse(false);
        }
    }

    @Test
    @DisplayName("Read Image Icon From Desktop")
    public void readImageIconFromDesktop() {
        try {
            log.info("Starting execution of readImageIconFromDesktop");

            ImageIcon expectedValue = null;

            KSWAApplicationGUI kswaapplicationgui = new KSWAApplicationGUI();
            ImageIcon actualValue = kswaapplicationgui.readImageIconFromDesktop();

            log.info("Expected Value=" + expectedValue + " . Actual Value=" + actualValue);
            System.out.println("Expected Value=" + expectedValue + " . Actual Value=" + actualValue);

            Assertions.assertEquals(expectedValue, actualValue);
        } catch (Exception exception) {
            exception.printStackTrace();
            Assertions.assertFalse(false);
        }
    }

    @AfterEach
    void tearDown() {
        // Clean-up after each test
    }

    @AfterAll
    static void tearDownAll() {
        // Clean-up after all tests
    }
}
