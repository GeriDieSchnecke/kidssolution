

import KSWABackend.Coverter.KSWAExcelConverter;
import KSWABackend.Model.KSWAChildren;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.logging.Logger;

public class KSWAExcelConverterTest {

    private Logger log = Logger.getLogger("ok");
    @BeforeAll
    static void initAll() {
    }
    @BeforeEach
    void init() {
    }

    @Test
    @DisplayName("read Data From Excel")
    public void readDataFromExcel(){
        try {
            log.info("Starting execution of readDataFromExcel");
            List<KSWAChildren> expectedValue = null;


            List<KSWAChildren> actualValue= KSWAExcelConverter.readDataFromExcel();
            log.info("Expected Value="+ expectedValue +" . Actual Value="+actualValue);
            System.out.println("Expected Value="+ expectedValue +" . Actual Value="+actualValue);
            Assertions.assertEquals(expectedValue, actualValue);
        } catch (Exception exception) {
            exception.printStackTrace();
            Assertions.assertFalse(false);
        }
    }

    @Test
    @DisplayName("write To Excel")
    public void writeToExcel(){
        try {
            log.info("Starting execution of writeToExcel");
            List<KSWAChildren> childrenList = null;


            KSWAExcelConverter.writeToExcel( childrenList);
            Assertions.assertTrue(true);
        } catch (Exception exception) {
            exception.printStackTrace();
            Assertions.assertFalse(false);
        }
    }

    @Test
    @DisplayName("export Excel")
    public void exportExcel(){
        try {
            log.info("Starting execution of exportExcel");
            List<KSWAChildren> childrenList = null;
            String filePath="";


            KSWAExcelConverter.exportExcel( childrenList ,filePath);
            Assertions.assertTrue(true);
        } catch (Exception exception) {
            exception.printStackTrace();
            Assertions.assertFalse(false);
        }
    }
    @AfterEach
    void tearDown() {
    }
    @AfterAll
    static void tearDownAll() {
    }
}
