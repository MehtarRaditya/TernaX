package Models;

public class Pakan extends Konsumsi {
    private String tipePakan;

    public Pakan(String id, String name, double stok, String tanggalExpire, String tipePakan) {
        super(id, name, stok, tanggalExpire);
        this.tipePakan = tipePakan;
    }

    public String getTipePakan() {
        return tipePakan;
    }

    public void setTipePakan(String tipePakan) {
        this.tipePakan = tipePakan;
    }


}
