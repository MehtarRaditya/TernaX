package Models;

public class Daging extends Produk{
    private final String TIPE = "Daging";
    private float beratStok;
    private float beratTerjual;

    public Daging(String id, String tanggalDiperoleh, String tipe, boolean layakJual, String kualitas, float beratStok, float beratTerjual) {
        super(id, tanggalDiperoleh, tipe, layakJual, kualitas);
        this.setTipe(TIPE);
        this.beratStok = beratStok;
        this.beratTerjual = beratTerjual;
        //TODO Auto-generated constructor stub
    }

    public float getBeratStok() {
        return beratStok;
    }

    public void setBeratStok(float beratStok) {
        this.beratStok = beratStok;
    }

    public float getBeratTerjual() {
        return beratTerjual;
    }

    public void setBeratTerjual(float beratTerjual) {
        this.beratTerjual = beratTerjual;
    }

    

    
    
    
}
