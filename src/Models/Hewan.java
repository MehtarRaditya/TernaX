package Models;

import java.util.ArrayList;

public class Hewan {
    protected int id;
    protected String jenis;
    protected String kelamin;
    protected int berat;
    protected int usia;
    protected String kondisi;
    public String pemilik;
    private ArrayList<String> kandungan;

    protected Hewan(int id, String jenis, String kelamin, int berat, int usia, String kondisi, ArrayList<String> kandungan) {
        this.id = id;
        this.jenis = jenis;
        this.kelamin = kelamin;
        this.berat = berat;
        this.usia = usia;
        this.kondisi = kondisi;
        this.kandungan = new ArrayList<>();
    }

     public ArrayList<String> getKandungan() {
        return kandungan;
    }
}
