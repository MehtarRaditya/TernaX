package Models;

public class Ayam extends Hewan{
    private int telur;

    public Ayam(int id, String jenis, String kelamin, int berat, int usia, String kondisi, int telur) {
        super(id, jenis, kelamin, berat, usia, kondisi);
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
}
