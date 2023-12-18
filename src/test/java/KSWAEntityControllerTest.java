import KSWABackend.Controller.KSWAEntityController;
import KSWABackend.Model.KSWAChildren;
import KSWABackend.Model.KSWASubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KSWAEntityControllerTest {
    private KSWAEntityController controller;

    @BeforeEach
    public void setUp() {
        controller = new KSWAEntityController();
    }

    @Test
    public void testAddAndRemoveChildren() {
        // Test Add Children
        KSWAChildren children = new KSWAChildren("Alice", "Test", "Female", null);
        controller.addChildren(children);
        List<KSWAChildren> childrenList = controller.getAllChildren();
        assertEquals(1, childrenList.size());
        assertEquals(children, childrenList.get(0));

        // Test Remove Children
        controller.removeChildren(1);
        childrenList = controller.getAllChildren();
        assertEquals(0, childrenList.size());
    }

    @Test
    public void testFindChildrenById() {
        // Test Find Children By Id
        KSWAChildren children = new KSWAChildren("Alice", "Test", "Female", null);
        controller.addChildren(children);
        Optional<KSWAChildren> foundChildren = controller.findChildrenById(1);
        assertTrue(foundChildren.isPresent());
        assertEquals(children, foundChildren.get());

        // Test Find Non-Existent Children By Id
        Optional<KSWAChildren> nonExistentChildren = controller.findChildrenById(2);
        assertFalse(nonExistentChildren.isPresent());
    }


    @Test
    public void testAddAndRemoveSubject() {
        // Test Add Subject
        KSWASubject subject = new KSWASubject("Math", 90.0, new ArrayList<>(), 1);
        controller.addSubject(subject);
        List<KSWASubject> subjectList = controller.getAllSubjects();
        assertEquals(1, subjectList.size());
        assertEquals(subject, subjectList.get(0));

        // Test Remove Subject
        controller.removeSubject(1);
        subjectList = controller.getAllSubjects();
        assertEquals(0, subjectList.size());
    }

    @Test
    public void testFindSubjectById() {
        // Test Find Subject By Id
        KSWASubject subject = new KSWASubject("Math", 90.0, new ArrayList<>(), 1);
        controller.addSubject(subject);
        Optional<KSWASubject> foundSubject = controller.findSubjectById(1);
        assertTrue(foundSubject.isPresent());
        assertEquals(subject, foundSubject.get());

        // Test Find Non-Existent Subject By Id
        Optional<KSWASubject> nonExistentSubject = controller.findSubjectById(2);
        assertFalse(nonExistentSubject.isPresent());
    }

}
