package Models;

public class Kandang {
    private String jenis;
    private int kapasitas;
    private int terisi;
    private int luas;
    private String tanggal_dibangun;
    private String tanggalPerawatanTerakhir;

    public Kandang(String jenis, int kapasitas, int terisi, int luas, String tanggal_dibangun, String tanggalPerawatanTerakhir) {
        this.jenis = jenis;
        this.kapasitas = kapasitas;
        this.terisi = terisi;
        this.luas = luas;
        this.tanggal_dibangun = tanggal_dibangun;
        this.tanggalPerawatanTerakhir = tanggalPerawatanTerakhir;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }

    public int getTerisi() {
        return terisi;
    }

    public void setTerisi(int terisi) {
        this.terisi = terisi;
    }

    public int getLuas() {
        return luas;
    }

    public void setLuas(int luas) {
        this.luas = luas;
    }

    public String getTanggalDibangun() {
        return tanggal_dibangun;
    }

    public void setTanggalDibangun(String tanggal_dibangun) {
        this.tanggal_dibangun = tanggal_dibangun;
    }

    public String getTanggalPerawatanTerakhir() {
        return tanggalPerawatanTerakhir;
    }

    public void setTanggalPerawatanTerakhir(String tanggalPerawatanTerakhir) {
        this.tanggalPerawatanTerakhir = tanggalPerawatanTerakhir;
    }

    public void TampilkanInfoKandang(){
        System.out.println("Jenis Kandang: " + this.jenis);
        System.out.println("Kapasitas: " + this.kapasitas);
        System.out.println("Terpakai: " + this.terisi);
        System.out.println("Sisa: " + this.luas);
        System.out.println("Tanggal Dibangun: " + this.tanggal_dibangun);
        System.out.println("Tanggal Perawatan Terakhir: " + this.tanggalPerawatanTerakhir);
    }

}
