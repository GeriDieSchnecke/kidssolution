import KSWABackend.Model.KSWAChildren;
import KSWABackend.Model.KSWASubject;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KSWAChildrenTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        String firstName = "Alice";
        String lastName = "Smith";
        String birthday = "2008-05-15";
        List<KSWASubject> subjects = new ArrayList<>();

        KSWAChildren child = new KSWAChildren();

        // Act
        child.setChprename(firstName);
        child.setChname(lastName);
        child.setChbirthday(birthday);
        child.setChsubjects(subjects);

        // Assert
        assertEquals(firstName, child.getChprename());
        assertEquals(lastName, child.getChname());
        assertEquals(birthday, child.getChbirthday());
        assertEquals(subjects, child.getChsubjects());
    }
}
