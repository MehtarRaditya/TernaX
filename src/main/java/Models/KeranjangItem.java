package Models;

public class KeranjangItem {
    private int idProduk; // ID dari Katalog
    private String namaProduk;
    private double harga;
    private double qty;
    
    public KeranjangItem(int idProduk, String namaProduk, double harga, double qty) {
        this.idProduk = idProduk;
        this.namaProduk = namaProduk;
        this.harga = harga;
        this.qty = qty;
    }

    public int getIdProduk() { return idProduk; }
    public String getNamaProduk() { return namaProduk; }
    public double getHarga() { return harga; }
    public double getQty() { return qty; }
    
    // Hitung subtotal otomatis (Harga x Qty)
    public double getSubtotal() { 
        return harga * qty; 
    }

    // Setter untuk update Qty jika barang ditambah lagi
    public void setQty(double qty) { 
        this.qty = qty; 
    }

    public void setSubtotal(double d) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}