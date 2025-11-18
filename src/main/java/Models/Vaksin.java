package Models;

public class Vaksin extends Konsumsi {
    private String targetPenyakit;
    private String jadwalVaksinasi;
    
    public Vaksin(String id, String name, double stok, String tanggalExpire, String targetPenyakit,
            String jadwalVaksinasi) {
        super(id, name, stok, tanggalExpire);
        this.targetPenyakit = targetPenyakit;
        this.jadwalVaksinasi = jadwalVaksinasi;
    }

    public String getTargetPenyakit() {
        return targetPenyakit;
    }

    public void setTargetPenyakit(String targetPenyakit) {
        this.targetPenyakit = targetPenyakit;
    }

    public String getJadwalVaksinasi() {
        return jadwalVaksinasi;
    }

    public void setJadwalVaksinasi(String jadwalVaksinasi) {
        this.jadwalVaksinasi = jadwalVaksinasi;
    }

    
    public void cekJadwalVaksinasi(){

    }

    public boolean cekTargetPenyakit(Hewan hewan) {
    // misalnya cocok kalau kondisi hewan mengandung nama penyakit target
    if (hewan == null || hewan.kondisi == null || targetPenyakit == null) {
        return false;
    }

    if (hewan.kondisi.toLowerCase().contains(targetPenyakit.toLowerCase())) {
        System.out.println("✅ Vaksin cocok untuk hewan ini (target penyakit sesuai).");
        return true;
    } else {
        System.out.println("❌ Vaksin tidak cocok (target penyakit tidak sesuai).");
        return false;
    }
}

}
