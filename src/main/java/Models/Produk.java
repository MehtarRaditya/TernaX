package Models;

public class Produk {
    private int id;
    private String tanggalDiperoleh;
    private String tipe;
    private double kuantitas;
    private String kualitas;
    private int idHewan;
    
    
    public Produk(int id, String tanggalDiperoleh, String tipe, double kuantitas, String kualitas) {
        this.id = id;
        this.tanggalDiperoleh = tanggalDiperoleh;
        this.tipe = tipe;
        this.kuantitas = kuantitas;
        this.kualitas = kualitas;
    }

    public Produk(int id, String tanggalDiperoleh, String tipe, String kualitas) {
        this.id = id;
        this.tanggalDiperoleh = tanggalDiperoleh;
        this.tipe = tipe;
        this.kualitas = kualitas;
    }

    public Produk(String tanggalDiperoleh, String tipe, double kuantitas) {
        this.tanggalDiperoleh = tanggalDiperoleh;
        this.tipe = tipe;
        this.kuantitas = kuantitas;
    }

    public Produk(String tanggalDiperoleh, String tipe, double kuantitas, String kualitas, int idHewan) {
        this.tanggalDiperoleh = tanggalDiperoleh;
        this.tipe = tipe;
        this.kuantitas = kuantitas;
        this.kualitas = kualitas;
        this.idHewan = idHewan;
    }
    
    

    public Produk() {
    }
    
    
    
    
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getKualitas() {
        return kualitas;
    }

    public void setKualitas(String kualitas) {
        this.kualitas = kualitas;
    }

    public int getIdHewan() {
        return idHewan;
    }

    public void setIdHewan(int idHewan) {
        this.idHewan = idHewan;
    }

    public double getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(double kuantitas) {
        this.kuantitas = kuantitas;
    }
    

}
