/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Muham
 */
public class TransaksiKonsumsi {
    int id;
    int idKaryawan;
    int idKonsumsi;
    String tanggalDibeli;
    String tanggalKeluar;
    int kuantitas;

    public TransaksiKonsumsi(int id, int idKaryawan, int idKonsumsi, String tanggalDibeli, String tanggalKeluar, int kuantitas) {
        this.id = id;
        this.idKaryawan = idKaryawan;
        this.idKonsumsi = idKonsumsi;
        this.tanggalDibeli = tanggalDibeli;
        this.tanggalKeluar = tanggalKeluar;
        this.kuantitas = kuantitas;
    }

    public TransaksiKonsumsi(String tanggalDibeli, String tanggalKeluar, int kuantitas) {
        this.tanggalDibeli = tanggalDibeli;
        this.tanggalKeluar = "belum tahu";
        this.kuantitas = kuantitas;
    }

    public TransaksiKonsumsi(String tanggalDibeli, int kuantitas) {
        this.tanggalDibeli = tanggalDibeli;
        this.kuantitas = kuantitas;
    }
    
    

    public TransaksiKonsumsi() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdKaryawan() {
        return idKaryawan;
    }

    public void setIdKaryawan(int idKaryawan) {
        this.idKaryawan = idKaryawan;
    }

    public int getIdKonsumsi() {
        return idKonsumsi;
    }

    public void setIdKonsumsi(int idKonsumsi) {
        this.idKonsumsi = idKonsumsi;
    }

    public String getTanggalDibeli() {
        return tanggalDibeli;
    }

    public void setTanggalDibeli(String tanggalDibeli) {
        this.tanggalDibeli = tanggalDibeli;
    }

    public String getTanggalKeluar() {
        return tanggalKeluar;
    }

    public void setTanggalKeluar(String tanggalKeluar) {
        this.tanggalKeluar = tanggalKeluar;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(int kuantitas) {
        this.kuantitas = kuantitas;
    }
    
    
}


