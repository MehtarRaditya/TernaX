package Models;

import java.util.ArrayList;

public class Hewan {
    private int id;
    private String jenis;
    private String kelamin;
    private double berat;
    private int usia;
    private String kondisi;
    private String pemilik;
    private String penyakit;

    public Hewan(int id, String jenis, String kelamin, double berat, int usia, String kondisi, String pemilik, String penyakit) {
        this.id = id;
        this.jenis = jenis;
        this.kelamin = kelamin;
        this.berat = berat;
        this.usia = usia;
        this.kondisi = kondisi;
        this.pemilik = pemilik;
        this.penyakit = penyakit;
    }
    
    public Hewan(String jenis, String kelamin, double berat, int usia, String kondisi, String penyakit) {
        this.jenis = jenis;
        this.kelamin = kelamin;
        this.berat = berat;
        this.usia = usia;
        this.kondisi = kondisi;
        this.penyakit = penyakit;
    }

    public Hewan(String jenis, String kelamin, double berat, int usia, String kondisi, String pemilik, String penyakit) {
        this.jenis = jenis;
        this.kelamin = kelamin;
        this.berat = berat;
        this.usia = usia;
        this.kondisi = kondisi;
        this.pemilik = pemilik;
        this.penyakit = penyakit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getKelamin() {
        return kelamin;
    }

    public void setKelamin(String kelamin) {
        this.kelamin = kelamin;
    }

    public double getBerat() {
        return berat;
    }

    public void setBerat(double berat) {
        this.berat = berat;
    }

    public int getUsia() {
        return usia;
    }

    public void setUsia(int usia) {
        this.usia = usia;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public String getPemilik() {
        return pemilik;
    }

    public void setPemilik(String pemilik) {
        this.pemilik = pemilik;
    }

    public String getPenyakit() {
        return penyakit;
    }

    public void setPenyakit(String penyakit) {
        this.penyakit = penyakit;
    }
}
