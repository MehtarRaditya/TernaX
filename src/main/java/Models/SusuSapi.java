package Models;

public class SusuSapi extends Produk {
    private final String TIPE = "Susu Sapi";
    private float literStok;
    private float literTerjual;
    
    public SusuSapi(String id, String tanggalDiperoleh, String tipe, boolean layakJual, String kualitas,
            float literStok, float literTerjual) {
        super(id, tanggalDiperoleh, tipe, layakJual, kualitas);
        this.setTipe(TIPE);
        this.literStok = literStok;
        this.literTerjual = literTerjual;
    }

    public float getLiterStok() {
        return literStok;
    }

    public void setLiterStok(float literStok) {
        this.literStok = literStok;
    }

    public float getLiterTerjual() {
        return literTerjual;
    }

    public void setLiterTerjual(float literTerjual) {
        this.literTerjual = literTerjual;
    }

    

    
}
