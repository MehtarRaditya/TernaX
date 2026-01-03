package Models;

public class Penjualan {
    // --- Atribut Asli Database ---
    private int id;
    private double totalHarga; // Subtotal per item (untuk tampilan tabel)
    private double uangDibayar;
    private double kembalian;
    private String tanggalTransaksi; // String biar mudah ditampilkan

    // --- Atribut Tambahan (Untuk Tampilan Tabel Manager) ---
    private String namaProduk;
    private String kategori;
    private int kuantitas;
    private double hargaSatuan;

    // --- GETTER & SETTER LENGKAP ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getTotalHarga() { return totalHarga; }
    public void setTotalHarga(double totalHarga) { this.totalHarga = totalHarga; }

    public String getTanggalTransaksi() { return tanggalTransaksi; }
    public void setTanggalTransaksi(String tanggalTransaksi) { this.tanggalTransaksi = tanggalTransaksi; }

    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public int getKuantitas() { return kuantitas; }
    public void setKuantitas(int kuantitas) { this.kuantitas = kuantitas; }

    public double getHargaSatuan() { return hargaSatuan; }
    public void setHargaSatuan(double hargaSatuan) { this.hargaSatuan = hargaSatuan; }
}