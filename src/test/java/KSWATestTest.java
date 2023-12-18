import KSWABackend.Model.KSWATest;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class KSWATestTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        double testGrade = 90.5;
        String testName = "Math Test";
        double testFactor = 1.2;
        Date testDate = new Date();
        long testId = 1;

        KSWATest test = new KSWATest();

        // Act
        test.setTegrade(testGrade);
        test.setTename(testName);
        test.setTefactor(testFactor);
        test.setTedate(testDate);
        test.setId(testId);

        // Assert
        assertEquals(testGrade, test.getTegrade());
        assertEquals(testName, test.getTename());
        assertEquals(testFactor, test.getTefactor());
        assertEquals(testDate, test.getTedate());
        assertEquals(testId, test.getId());
    }
}
