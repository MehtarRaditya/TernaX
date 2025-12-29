package Models;

public class KatalogProduk {
    private int id;
    private String namaProduk;
    private String satuan;
    private double harga; // Tambahan biar lengkap
    private double stok;

    public KatalogProduk(int id, String namaProduk, String satuan, double harga) {
        this.id = id;
        this.namaProduk = namaProduk;
        this.satuan = satuan;
        this.harga = harga;
    }
    
    public KatalogProduk(int id, String namaProduk, String satuan, double harga, double stok) {
        this.id = id;
        this.namaProduk = namaProduk;
        this.satuan = satuan;
        this.harga = harga;
        this.stok = stok; // <--- Disimpan di sini
    }

    public int getId() { return id; }
    public String getNamaProduk() { return namaProduk; }
    public String getSatuan() { return satuan; }
    public double getHarga() { return harga; }
    // 3. GETTER BARU (Wajib ada buat TableView)
    public double getStok() { 
        return stok; 
    }
    
    // Setter (Opsional, buat update stok tanpa reload DB)
    public void setStok(double stok) {
        this.stok = stok;
    }

    // Override toString PENTING buat ComboBox
    @Override
    public String toString() {
        return namaProduk; 
    }
}