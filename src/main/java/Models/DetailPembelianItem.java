/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Muham
 */
public class DetailPembelianItem {
    private Konsumsi konsumsi;
    private int kuantitas;

    public DetailPembelianItem(Konsumsi konsumsi, int kuantitas) {
        this.konsumsi = konsumsi;
        this.kuantitas = kuantitas;
    }

    public Konsumsi getKonsumsi() {
        return konsumsi;
    }

    public void setKonsumsi(Konsumsi konsumsi) {
        this.konsumsi = konsumsi;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(int kuantitas) {
        this.kuantitas = kuantitas;
    }
    
    public String getNamaKonsumsi() {
        return konsumsi.getName();
    }
    
     public String getTipe() {
        return konsumsi.getTipe();
    }
     
     public int getIdKonsumsi() {
        return konsumsi.getId();
    }
    
}
