package Models;

public class Konsumsi {
    private String id;
    private String name;
    private double stok;
    private String tanggalExpire;
    
    public Konsumsi(String id, String name, double stok, String tanggalExpire) {
        this.id = id;
        this.name = name;
        this.stok = stok;
        this.tanggalExpire = tanggalExpire;
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

    public double getStok() {
        return stok;
    }

    public void setStok(double stok) {
        this.stok = stok;
    }

    public String getTanggalExpire() {
        return tanggalExpire;
    }

    public void setTanggalExpire(String tanggalExpire) {
        this.tanggalExpire = tanggalExpire;
    }

    public void tampilkanKonsumsi(){
        System.out.println("id: " + this.getId());
        System.out.println("Nama: " + this.getName());
        System.out.println("Stok: " + this.getStok());
        System.out.println("Tanggal Expire: " + this.getTanggalExpire());
    }
}
