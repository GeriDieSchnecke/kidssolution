package KSWABackend.Model;

import java.util.ArrayList;
import java.util.List;

public class KSWASubject {
    private String suname;
    private double sugrade;
    private List<KSWATest> tests;
    private long id;

    private KSWAChildren children;

    public KSWASubject(String suname, double sugrade, ArrayList<KSWATest> tests, long id) {
        this.suname = suname;
        this.sugrade = sugrade;
        this.tests = tests;
        this.id = id;
    }

    public KSWASubject() {
    }

    public String getSuname() {
        return suname;
    }

    public void setSuname(String suname) {
        this.suname = suname;
    }

    public double getSugrade() {
        return sugrade;
    }

    public void setSugrade(double sugrade) {
        this.sugrade = sugrade;
    }

    public List<KSWATest> getTests() {
        return tests;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTests(List<KSWATest> tests) {
        this.tests = tests;
    }

    public KSWAChildren getChildren() {
        return children;
    }

    public void setChildren(KSWAChildren children) {
        this.children = children;
    }
}


