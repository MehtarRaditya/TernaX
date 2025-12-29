/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author USER
 */
public class RiwayatKonsumsi {
    private int idPembelian;
    private String tanggal;
    private String namaKonsumsi; // Dari tabel konsumsi
    private String tipe;         // Dari tabel konsumsi
    private int kuantitas;       // Dari tabel detail_pembelian

    public RiwayatKonsumsi(int idPembelian, String tanggal, String namaKonsumsi, String tipe, int kuantitas) {
        this.idPembelian = idPembelian;
        this.tanggal = tanggal;
        this.namaKonsumsi = namaKonsumsi;
        this.tipe = tipe;
        this.kuantitas = kuantitas;
    }

    // Getter (Wajib ada untuk TableView)
    public int getIdPembelian() { return idPembelian; }
    public String getTanggal() { return tanggal; }
    public String getNamaKonsumsi() { return namaKonsumsi; }
    public String getTipe() { return tipe; }
    public int getKuantitas() { return kuantitas; }
}
