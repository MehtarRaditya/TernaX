package Models;

import java.util.ArrayList;

public class Pengeluaran extends Budget{
    ArrayList<Produk> daftarPenjualan;
    public Pengeluaran(int id, String tanggalBudgetDibuat, String namaBudget, String deskripsiBudget, int biaya) {
        super(id, tanggalBudgetDibuat, namaBudget, deskripsiBudget, biaya);
        daftarPenjualan = new ArrayList<>();
    }
    public void tambahkanTransaksiPembelian(Produk produk){
        daftarPenjualan.add(produk);
    }
}
