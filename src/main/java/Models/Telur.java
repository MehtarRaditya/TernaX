package Models;

public class Telur extends Produk {
    private final String TIPE = "Telur";
    private int butirStok;
    private int butirTerjual;

    
    public Telur(String id, String tanggalDiperoleh, String tipe, boolean layakJual, String kualitas, int butirStok,
            int butirTerjual) {
        super(id, tanggalDiperoleh, tipe, layakJual, kualitas);
        this.setTipe(TIPE);
        this.butirStok = butirStok;
        this.butirTerjual = butirTerjual;
    }

    public int getButirStok() {
        return butirStok;
    }
    public void setButirStok(int butirStok) {
        this.butirStok = butirStok;
    }
    public int getButirTerjual() {
        return butirTerjual;
    }
    public void setButirTerjual(int butirTerjual) {
        this.butirTerjual = butirTerjual;
    } 
}
