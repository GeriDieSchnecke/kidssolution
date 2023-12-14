package KSWABackend.Model;

import javax.swing.*;

public class KSWATeacher {
    private int id;
    private String name;
    private String prename;
    private ImageIcon image; // Verwendet ImageIcon für das Bild, Sie können hier auch andere Datentypen verwenden

    // Konstruktor
    public KSWATeacher(int id, String name, String prename, ImageIcon image) {
        this.id = id;
        this.name = name;
        this.prename = prename;
        this.image = image;
    }

    // Getter und Setter für die Eigenschaften des Lehrers
    public int getId() {
        return id;
    }

    public void setId(int id) {
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