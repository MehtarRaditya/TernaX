/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author USER
 */
public class DetailPembelian {
    private int id;
    private int idPembelian;
    private int idKonsumsi;
    private int kuantitas;

    public DetailPembelian() {
    }
    
    

    public DetailPembelian(int id, int idPembelian, int idKonsumsi, int kuantitas) {
        this.id = id;
        this.idPembelian = idPembelian;
        this.idKonsumsi = idKonsumsi;
        this.kuantitas = kuantitas;
    }
    
    public DetailPembelian(int idPembelian, int idKonsumsi, int kuantitas) {
        this.idPembelian = idPembelian;
        this.idKonsumsi = idKonsumsi;
        this.kuantitas = kuantitas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPembelian() {
        return idPembelian;
    }

    public void setIdPembelian(int idPembelian) {
        this.idPembelian = idPembelian;
    }

    public int getIdKonsumsi() {
        return idKonsumsi;
    }

    public void setIdKonsumsi(int idKonsumsi) {
        this.idKonsumsi = idKonsumsi;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(int kuantitas) {
        this.kuantitas = kuantitas;
    }
}
