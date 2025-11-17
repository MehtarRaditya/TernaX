package Models;

import java.util.ArrayList;

public class Pemasukkan extends Budget{
    private ArrayList<Produk> daftarPenjualan;
    public Pemasukkan(int id, String tanggalBudgetDibuat, String namaBudget, String deskripsiBudget, int biaya) {
        super(id, tanggalBudgetDibuat, namaBudget, deskripsiBudget, biaya);
        daftarPenjualan = new ArrayList<>();
    }
    public void tambahTransaksi(Produk produk) {
        System.out.println("Tambah transaksi");
        daftarPenjualan.add(produk);
    }
}
