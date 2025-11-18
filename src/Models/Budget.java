package Models;

public class Budget {
    private int id;
    private String TanggalBudgetDibuat;
    private String NamaBudget;
    private String DeskripsiBudget;
    private  int biaya;

    public Budget(int id, String tanggalBudgetDibuat, String namaBudget, String deskripsiBudget, int biaya) {
        this.id = id;
        TanggalBudgetDibuat = tanggalBudgetDibuat;
        NamaBudget = namaBudget;
        DeskripsiBudget = deskripsiBudget;
        this.biaya = biaya;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTanggalBudgetDibuat() {
        return TanggalBudgetDibuat;
    }

    public void setTanggalBudgetDibuat(String tanggalBudgetDibuat) {
        TanggalBudgetDibuat = tanggalBudgetDibuat;
    }

    public String getNamaBudget() {
        return NamaBudget;
    }

    public void setNamaBudget(String namaBudget) {
        NamaBudget = namaBudget;
    }

    public String getDeskripsiBudget() {
        return DeskripsiBudget;
    }

    public void setDeskripsiBudget(String deskripsiBudget) {
        DeskripsiBudget = deskripsiBudget;
    }

    public int getBiaya() {
        return biaya;
    }

    public void setBiaya(int biaya) {
        this.biaya = biaya;
    }

    public void displayTransaksi(){
        System.out.println("ID: " + this.getId());
        System.out.println("Tanggal: " + this.getTanggalBudgetDibuat());
        System.out.println("Nama: " + this.getNamaBudget());
        System.out.println("Deskripsi: " + this.getDeskripsiBudget());
        System.out.println("Biaya: " + this.getBiaya());
    }
}
