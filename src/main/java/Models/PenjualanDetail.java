package Models;

public class PenjualanDetail {
    private Produk produk;
    private double qty;

    public PenjualanDetail(Produk produk, double qty) {
        this.produk = produk;
        this.qty = qty;
    }

    public Produk getProduk() {
        return produk;
    }

    public void setProduk(Produk produk) {
        this.produk = produk;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }
    public int getId(){
        return produk.getId();
    }
    public String getJenis(){
        return produk.getTipe();
    }
    
}
