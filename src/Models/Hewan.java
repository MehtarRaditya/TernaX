package Models;

public class Hewan {
    protected int id;
    protected String jenis;
    protected String kelamin;
    protected int berat;
    protected int usia;
    protected String kondisi;
    public String pemilik;

    protected Hewan(int id, String jenis, String kelamin, int berat, int usia, String kondisi) {
        this.id = id;
        this.jenis = jenis;
        this.kelamin = kelamin;
        this.berat = berat;
        this.usia = usia;
        this.kondisi = kondisi;
    }
}
