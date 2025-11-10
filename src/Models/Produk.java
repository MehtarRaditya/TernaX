package Models;

public class Produk {
    private String id;
    private String tanggalDiperoleh;
    private String tipe;
    private boolean layakJual;
    private String kualitas;
    
    public Produk(String id, String tanggalDiperoleh, String tipe, boolean layakJual, String kualitas) {
        this.id = id;
        this.tanggalDiperoleh = tanggalDiperoleh;
        this.tipe = tipe;
        this.layakJual = layakJual;
        this.kualitas = kualitas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTanggalDiperoleh() {
        return tanggalDiperoleh;
    }

    public void setTanggalDiperoleh(String tanggalDiperoleh) {
        this.tanggalDiperoleh = tanggalDiperoleh;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public boolean isLayakJual() {
        return layakJual;
    }

    public void setLayakJual(boolean layakJual) {
        this.layakJual = layakJual;
    }

    public String getKualitas() {
        return kualitas;
    }

    public void setKualitas(String kualitas) {
        this.kualitas = kualitas;
    }

    public void displayProduk(){
        System.out.println("id: " + getId());
        System.out.println("Tipe: " + getTipe());
        System.out.println("Tanggal Diperoleh: " + getTanggalDiperoleh());
        System.out.println("Kelayakan: " + isLayakJual());
        System.out.println("Kualitas: " + getKualitas());
    }
}
