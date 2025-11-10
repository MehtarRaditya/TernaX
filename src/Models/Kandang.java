package Models;

public class Kandang {
    private String nama;
    private int kapasitas;
    private int terpakai;
    private int sisa;
    private String tanggal_terpakai;
    private String tanggalPerawatanTerakhir;

    public Kandang(String nama, int kapasitas, int terpakai, int sisa, String tanggal_terpakai, String tanggalPerawatanTerakhir) {
        this.nama = nama;
        this.kapasitas = kapasitas;
        this.terpakai = terpakai;
        this.sisa = sisa;
        this.tanggal_terpakai = tanggal_terpakai;
        this.tanggalPerawatanTerakhir = tanggalPerawatanTerakhir;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }

    public int getTerpakai() {
        return terpakai;
    }

    public void setTerpakai(int terpakai) {
        this.terpakai = terpakai;
    }

    public int getSisa() {
        return sisa;
    }

    public void setSisa(int sisa) {
        this.sisa = sisa;
    }

    public String getTanggal_terpakai() {
        return tanggal_terpakai;
    }

    public void setTanggal_terpakai(String tanggal_terpakai) {
        this.tanggal_terpakai = tanggal_terpakai;
    }

    public String getTanggalPerawatanTerakhir() {
        return tanggalPerawatanTerakhir;
    }

    public void setTanggalPerawatanTerakhir(String tanggalPerawatanTerakhir) {
        this.tanggalPerawatanTerakhir = tanggalPerawatanTerakhir;
    }

    public void TampilkanInfoKandang(){
        System.out.println("Nama Kandang: " + this.nama);
        System.out.println("Kapasitas: " + this.kapasitas);
        System.out.println("Terpakai: " + this.terpakai);
        System.out.println("Sisa: " + this.sisa);
        System.out.println("Tanggal Terakhir: " + this.tanggal_terpakai);
        System.out.println("Tanggal Perawatan: " + this.tanggalPerawatanTerakhir);
    }

}
