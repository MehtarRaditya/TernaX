package Models;

import java.util.ArrayList;

public class Hewan {
    private int id;
    private String jenis;
    private String kelamin;
    private int berat;
    private int usia;
    private String kondisi;
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

    public String getPemilik() {
        return pemilik;
    }

    public void setPemilik(String pemilik) {
        this.pemilik = pemilik;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public int getUsia() {
        return usia;
    }

    public void setUsia(int usia) {
        this.usia = usia;
    }

    public int getBerat() {
        return berat;
    }

    public void setBerat(int berat) {
        this.berat = berat;
    }

    public String getKelamin() {
        return kelamin;
    }

    public void setKelamin(String kelamin) {
        this.kelamin = kelamin;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double HitungDaging(){
        return 0;
    }
}
