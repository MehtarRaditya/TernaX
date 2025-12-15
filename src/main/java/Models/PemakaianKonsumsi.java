/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Muham
 */
public class PemakaianKonsumsi {
    private int id;
    private int idHewan;
    private int idKonsumsi;
    private int idKaryawan;
    private int kuantitas;
    private String tanggal;

    public PemakaianKonsumsi(int id, int idHewan, int idKonsumsi, int idKaryawan, int kuantitas, String tanggal) {
        this.id = id;
        this.idHewan = idHewan;
        this.idKonsumsi = idKonsumsi;
        this.idKaryawan = idKaryawan;
        this.kuantitas = kuantitas;
        this.tanggal = tanggal;
    }

    public PemakaianKonsumsi() {
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdHewan() {
        return idHewan;
    }

    public void setIdHewan(int idHewan) {
        this.idHewan = idHewan;
    }

    public int getIdKonsumsi() {
        return idKonsumsi;
    }

    public void setIdKonsumsi(int idKonsumsi) {
        this.idKonsumsi = idKonsumsi;
    }

    public int getIdKaryawan() {
        return idKaryawan;
    }

    public void setIdKaryawan(int idKaryawan) {
        this.idKaryawan = idKaryawan;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(int kuantitas) {
        this.kuantitas = kuantitas;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
    
    
}
