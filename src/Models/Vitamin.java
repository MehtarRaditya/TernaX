package Models;

public class Vitamin extends Obat {
    private String kandungan;

    public Vitamin(String id, String name, double stok, String tanggalExpire, float dosis, String indikasi,
            String kandungan) {
        super(id, name, stok, tanggalExpire, dosis, indikasi);
        this.kandungan = kandungan;
    }

    public String getKandungan() {
        return kandungan;
    }

    public boolean cocokUntuk(Hewan hewan) {
        for (String kV : hewan.getKandungan()) {
            if (kV.equalsIgnoreCase(kandungan)) {
                return true;
            }
        }
        return false; // tidak ada kecocokan
    }
}
