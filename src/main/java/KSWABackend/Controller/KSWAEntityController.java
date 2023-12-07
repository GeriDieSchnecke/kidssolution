package KSWABackend.Controller;

import KSWABackend.Model.KSWAChildren;
import KSWABackend.Model.KSWASubject;
import KSWABackend.Model.KSWATest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KSWAEntityController {
    private List<KSWAChildren> childrenList;
    private List<KSWASubject> subjectList;
    private List<KSWATest> testList;

    public KSWAEntityController() {
        this.childrenList = new ArrayList<KSWAChildren>();
        this.subjectList = new ArrayList<KSWASubject>();
        this.testList = new ArrayList<KSWATest>();
    }

    public void addChildren(KSWAChildren children) {
        childrenList.add(children);
    }

    public void removeChildren(long id) {
        childrenList.removeIf(child -> child.getId() == id);
    }

    public Optional<KSWAChildren> findChildrenById(long id) {
        return childrenList.stream().filter(child -> child.getId() == id).findFirst();
    }

    public void addSubject(KSWASubject subject) {
        subjectList.add(subject);
    }

    public void removeSubject(long id) {
        subjectList.removeIf(subject -> subject.getId() == id);
    }

    public Optional<KSWASubject> findSubjectById(long id) {
        return subjectList.stream().filter(subject -> subject.getId() == id).findFirst();
    }

    public void addTest(KSWATest test) {
        testList.add(test);
    }

    public void removeTest(long id) {
        testList.removeIf(test -> test.getId() == id);
    }

    public Optional<KSWATest> findTestById(long id) {
        return testList.stream().filter(test -> test.getId() == id).findFirst();
    }

    public List<KSWAChildren> getAllChildren() {
        return childrenList;
    }

    public List<KSWASubject> getAllSubjects() {
        return subjectList;
    }

    public List<KSWATest> getAllTests() {
        return testList;
    }
}
