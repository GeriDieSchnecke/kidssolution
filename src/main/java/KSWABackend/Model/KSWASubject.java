package KSWABackend.Model;

import java.util.ArrayList;

public class KSWASubject {
    private String suname;
    private double sugrade;
    private ArrayList<KSWATest> tests;
    private long id;

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

    public ArrayList<KSWATest> getTests() {
        return tests;
    }

    public void setTests(ArrayList<KSWATest> tests) {
        this.tests = tests;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}


