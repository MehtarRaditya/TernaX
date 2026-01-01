/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Muham
 */
public class Pembelian {
    private int id;
    private String tanggalPembelian;
    private int id_karyawan;
    
    private String namaKonsumsi;
    private int kuantitas;
    private String tipe;

    public Pembelian(int id, String tanggalPembelian, int id_karyawan) {
        this.id = id;
        this.tanggalPembelian = tanggalPembelian;
        this.id_karyawan = id_karyawan;
    }
    
    public Pembelian(int id, String tanggalPembelian, String namaKonsumsi, String tipe, int kuantitas) {
        this.id = id;
        this.tanggalPembelian = tanggalPembelian;
        this.namaKonsumsi = namaKonsumsi;
        this.tipe = tipe;
        this.kuantitas = kuantitas;
    }

    public Pembelian(String tanggalPembelian, int id_karyawan) {
        this.tanggalPembelian = tanggalPembelian;
        this.id_karyawan = id_karyawan;
    }
    
    

    // --- GETTER & SETTER TAMBAHAN (Wajib Ada) ---
    public String getNamaKonsumsi() { return namaKonsumsi; }
    public int getKuantitas() { return kuantitas; }
    public String getTipe() { return tipe; }

    // ... (Getter Setter lama biarkan saja) ...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTanggalPembelian() { return tanggalPembelian; }
    public int getId_karyawan() { return id_karyawan; }  
}