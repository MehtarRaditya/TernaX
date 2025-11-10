package Models;

import java.util.ArrayList;

public class Ayam extends Hewan{
    private int telur;

    public Ayam(int id, String jenis, String kelamin, int berat, int usia, String kondisi, ArrayList<String> kandungan,
            int telur) {
        super(id, jenis, kelamin, berat, usia, kondisi, kandungan);
        this.telur = telur;
    }

    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getJenis() {
        return jenis;
    }

    public int getTelur() {
        return telur;
    }

    public void setTelur(int telur) {
        this.telur = telur;
    }

    
}
