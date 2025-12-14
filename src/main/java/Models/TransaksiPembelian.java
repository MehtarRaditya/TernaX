/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Muham
 */
public class TransaksiPembelian {
    private int id;
    private String tanggalPembelian;
    private int id_karyawan;

    public TransaksiPembelian(int id, String tanggalPembelian, int id_karyawan) {
        this.id = id;
        this.tanggalPembelian = tanggalPembelian;
        this.id_karyawan = id_karyawan;
    }

    public TransaksiPembelian(String tanggalPembelian, int id_karyawan) {
        this.tanggalPembelian = tanggalPembelian;
        this.id_karyawan = id_karyawan;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTanggalPembelian() {
        return tanggalPembelian;
    }

    public void setTanggalPembelian(String tanggalPembelian) {
        this.tanggalPembelian = tanggalPembelian;
    }

    public int getId_karyawan() {
        return id_karyawan;
    }

    public void setId_karyawan(int id_karyawan) {
        this.id_karyawan = id_karyawan;
    }
    
}
