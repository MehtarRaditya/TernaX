package Models;
import java.util.ArrayList;
public class Peternak extends Karyawan {
    private ArrayList<Hewan> kandang;
    private Hewan hewan;
    public Peternak(String id, String name, String role, String tanggalDirekrut, float gaji, String akun, String password, ArrayList<Kandang> kandang) {
        super(id, name, role, tanggalDirekrut, gaji, akun, password);
        this.kandang = new ArrayList<Hewan>();
    }

    public void TambahHewan(Hewan hewan,Kandang kandang){
        if(kandang.getKapasitas() - kandang.getTerpakai() > 0){
            this.kandang.add(hewan);
            System.out.println("Hewan Ditambahkan... hewan dikandang sejumlah..." + this.kandang.size());
            kandang.setTerpakai(this.kandang.size());
        }
        else {
            System.out.println("Kapasitas sudah mencapai batas maksimum");
        }

    }

    public void updateKondisiHewan(Hewan hewan, String Kondisi){
        hewan.setKondisi(Kondisi);
        System.out.println("ID Hewan: " + this.hewan.getId());
        System.out.println("Kondisi: "+ this.hewan.getKondisi());
    }

//    public void updateKandang(Kandang kandang, int terpakai, int sisa){
//        kandang.setTerpakai(terpakai);
//        kandang.setSisa(sisa);
//    }
    
}
