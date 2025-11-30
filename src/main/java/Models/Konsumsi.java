package Models;

public class Konsumsi {
    private int id;
    private String name;
    private String tipe;
    private int idLogistik;
    private String tanggalExpire;

    public Konsumsi(int id, String name, String tipe, int idLogistik, String tanggalExpire) {
        this.id = id;
        this.name = name;
        this.tipe = tipe;
        this.idLogistik = idLogistik;
        this.tanggalExpire = tanggalExpire;
    }

    public Konsumsi(String name, String tipe, int idLogistik, String tanggalExpire) {
        this.name = name;
        this.tipe = tipe;
        this.idLogistik = idLogistik;
        this.tanggalExpire = tanggalExpire;
    }

    public Konsumsi(String name, String tipe, String tanggalExpire) {
        this.name = name;
        this.tipe = tipe;
        this.tanggalExpire = tanggalExpire;
    }
    
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public int getIdLogistik() {
        return idLogistik;
    }

    public void setIdLogistik(int idLogistik) {
        this.idLogistik = idLogistik;
    }

    public String getTanggalExpire() {
        return tanggalExpire;
    }

    public void setTanggalExpire(String tanggalExpire) {
        this.tanggalExpire = tanggalExpire;
    }
    
    
    

    
}
