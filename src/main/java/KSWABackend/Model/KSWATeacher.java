package KSWABackend.Model;

import javax.swing.*;

public class KSWATeacher {
    private String id;
    private String name;
    private String prename;
    private ImageIcon image;

    public KSWATeacher(String id, String name, String prename, ImageIcon image) {
        this.id = id;
        this.name = name;
        this.prename = prename;
        this.image = image;
    }

    public KSWATeacher(String id, String name) {
        this.id = id;
        this.name = name;
        this.prename = prename;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrename() {
        return prename;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }

    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }
}
