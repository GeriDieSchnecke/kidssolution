import KSWABackend.Model.KSWATeacher;
import org.junit.jupiter.api.Test;
import javax.swing.ImageIcon;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class KSWATeacherTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        int teacherId = 1;
        String teacherName = "John";
        String teacherPrename = "Doe";
        ImageIcon teacherImage = new ImageIcon("path/to/image.jpg");

        KSWATeacher teacher = new KSWATeacher(teacherId, teacherName, teacherPrename, teacherImage);

        // Act
        teacher.setId(2);
        teacher.setName("Alice");
        teacher.setPrename("Smith");
        ImageIcon newImage = new ImageIcon("path/to/newimage.jpg");
        teacher.setImage(newImage);

        // Assert
        assertEquals(2, teacher.getId());
        assertEquals("Alice", teacher.getName());
        assertEquals("Smith", teacher.getPrename());
        assertEquals(newImage, teacher.getImage());
    }
}
