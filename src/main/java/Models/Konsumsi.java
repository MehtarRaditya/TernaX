package Models;

public class Konsumsi {
    private int id;
    private String name;
    private String tipe;
    private int Stok;

    public Konsumsi(int id, String name, String tipe, int Stok) {
        this.id = id;
        this.name = name;
        this.tipe = tipe;
        this.Stok = Stok;
    }

    
    public Konsumsi() {
    }

    
    
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

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public int getStok() {
        return Stok;
    }

    public void setStok(int Stok) {
        this.Stok = Stok;
    }
}
