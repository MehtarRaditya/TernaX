package Models;

public class Produk {
    private int idKatalog;
    private int id;
    private String tanggalDiperoleh;
    private String tipe;
    private double kuantitas;
    private String kualitas;
    private int idHewan;
    private String statusKelayakan;
    
    private String namaProduk; 
    private String satuan;

    public Produk(int id, String tanggalDiperoleh, String tipe, double kuantitas, String kualitas, int idHewan) {
        this.id = id;
        this.tanggalDiperoleh = tanggalDiperoleh;
        this.tipe = tipe;
        this.kuantitas = kuantitas;
        this.kualitas = kualitas;
        this.idHewan = idHewan;
    }

    public Produk(int id, String tanggalDiperoleh, String tipe, double kuantitas, String kualitas) {
        this.id = id;
        this.tanggalDiperoleh = tanggalDiperoleh;
        this.tipe = tipe;
        this.kuantitas = kuantitas;
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

    public Produk(int idKatalog,int idHewan, String tanggalDiperoleh, double kuantitas,  String statusKelayakan) {
        this.idKatalog = idKatalog;
        this.tanggalDiperoleh = tanggalDiperoleh;
        this.kuantitas = kuantitas;
        this.idHewan = idHewan;
        this.statusKelayakan = statusKelayakan;
    }

    public Produk(int idKatalog, int id, String tanggalDiperoleh, double kuantitas, int idHewan, String statusKelayakan, String namaProduk, String satuan) {
        this.idKatalog = idKatalog;
        this.id = id;
        this.tanggalDiperoleh = tanggalDiperoleh;
        this.kuantitas = kuantitas;
        this.idHewan = idHewan;
        this.statusKelayakan = statusKelayakan;
        this.namaProduk = namaProduk;
        this.satuan = satuan;
    }

    public Produk( int id, int idKatalog,int idHewan,String tanggalDiperoleh, double kuantitas, String statusKelayakan, String namaProduk, String satuan) {
        this.idKatalog = idKatalog;
        this.id = id;
        this.tanggalDiperoleh = tanggalDiperoleh;
        this.tipe = tipe;
        this.kuantitas = kuantitas;
        this.kualitas = kualitas;
        this.idHewan = idHewan;
        this.statusKelayakan = statusKelayakan;
        this.namaProduk = namaProduk;
        this.satuan = satuan;
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

    public double getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(double kuantitas) {
        this.kuantitas = kuantitas;
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

    public int getIdKatalog() {
        return idKatalog;
    }

    public void setIdKatalog(int idKatalog) {
        this.idKatalog = idKatalog;
    }

    public String getStatusKelayakan() {
        return statusKelayakan;
    }

    public void setStatusKelayakan(String statusKelayakan) {
        this.statusKelayakan = statusKelayakan;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
    
    
    
    
    

}
