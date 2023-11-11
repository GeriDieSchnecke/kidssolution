package KSWABackend.Model;

import java.util.Date;

public class KSWATest {
    private double tegrade;
    private String tename;
    private double tefactor;
    private Date tedate;
    private long id;

    public KSWATest(double tegrade, String tename, double tefactor, Date tedate, long id) {
        this.tegrade = tegrade;
        this.tename = tename;
        this.tefactor = tefactor;
        this.tedate = tedate;
        this.id = id;
    }

    public KSWATest() {
    }

    public double getTegrade() {
        return tegrade;
    }

    public void setTegrade(double tegrade) {
        this.tegrade = tegrade;
    }

    public String getTename() {
        return tename;
    }

    public void setTename(String tename) {
        this.tename = tename;
    }

    public double getTefactor() {
        return tefactor;
    }

    public void setTefactor(double tefactor) {
        this.tefactor = tefactor;
    }

    public Date getTedate() {
        return tedate;
    }

    public void setTedate(Date tedate) {
        this.tedate = tedate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
