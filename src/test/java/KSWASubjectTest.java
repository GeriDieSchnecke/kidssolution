import KSWABackend.Model.KSWASubject;
import KSWABackend.Model.KSWATest;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KSWASubjectTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        String subjectName = "Math";
        double subjectGrade = 85.5;
        List<KSWATest> testList = new ArrayList<>();
        long subjectId = 1;

        KSWASubject subject = new KSWASubject();

        // Act
        subject.setSuname(subjectName);
        subject.setSugrade(subjectGrade);
        subject.setTests((ArrayList<KSWATest>) testList);
        subject.setId(subjectId);

        // Assert
        assertEquals(subjectName, subject.getSuname());
        assertEquals(subjectGrade, subject.getSugrade());
        assertEquals(testList, subject.getTests());
        assertEquals(subjectId, subject.getId());
    }
}
