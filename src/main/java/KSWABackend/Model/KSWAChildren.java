package KSWABackend.Model;

import java.util.ArrayList;
import java.util.List;

public class KSWAChildren {
    private Long id;
    private String chprename;
    private String chname;
    private String chbirthday;
    private List<KSWASubject> chsubjects;

    public KSWAChildren(String chprename, String chname, String chbirthday, ArrayList<KSWASubject> chsubjects) {
        this.chprename = chprename;
        this.chname = chname;
        this.chbirthday = chbirthday;
        this.chsubjects = chsubjects;
    }

    public KSWAChildren() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChprename() {
        return chprename;
    }

    public void setChprename(String chprename) {
        this.chprename = chprename;
    }

    public String getChname() {
        return chname;
    }

    public void setChname(String chname) {
        this.chname = chname;
    }

    public String getChbirthday() {
        return chbirthday;
    }

    public void setChbirthday(String chbirthday) {
        this.chbirthday = chbirthday;
    }

    public List<KSWASubject> getChsubjects() {
        return chsubjects;
    }

    public void setChsubjects(List<KSWASubject> chsubjects) {
        this.chsubjects = chsubjects;
    }

}

