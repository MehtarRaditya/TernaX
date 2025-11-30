/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Muham
 */
public class LogistikRow {
    private String nama;
    private String tipe;
    private int kuantitas;
    private String tanggalBeli;
    private String tanggalExp;
    private int idTransaksi;
    private int idKonsumsi;

    public LogistikRow(String nama, String tipe, int kuantitas, String tanggalBeli, String tanggalExp, int idTransaksi, int idKonsumsi) {
        this.nama = nama;
        this.tipe = tipe;
        this.kuantitas = kuantitas;
        this.tanggalBeli = tanggalBeli;
        this.tanggalExp = tanggalExp;
        this.idTransaksi = idTransaksi;
        this.idKonsumsi = idKonsumsi;
    }
    

    public LogistikRow(String nama, String tipe, int kuantitas, String tanggalBeli, String tanggalExp) {
        this.nama = nama;
        this.tipe = tipe;
        this.kuantitas = kuantitas;
        this.tanggalBeli = tanggalBeli;
        this.tanggalExp = tanggalExp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(int kuantitas) {
        this.kuantitas = kuantitas;
    }

    public String getTanggalBeli() {
        return tanggalBeli;
    }

    public void setTanggalBeli(String tanggalBeli) {
        this.tanggalBeli = tanggalBeli;
    }

    public String getTanggalExp() {
        return tanggalExp;
    }

    public void setTanggalExp(String tanggalExp) {
        this.tanggalExp = tanggalExp;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public int getIdKonsumsi() {
        return idKonsumsi;
    }

    public void setIdKonsumsi(int idKonsumsi) {
        this.idKonsumsi = idKonsumsi;
    }
    
    
}
