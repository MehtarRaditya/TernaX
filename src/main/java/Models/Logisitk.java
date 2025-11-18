package Models;

public class Logisitk extends Karyawan {

    public Logisitk(String id, String name, String role, String tanggalDirekrut, float gaji, String akun,
            String password) {
        super(id, name, role, tanggalDirekrut, gaji, akun, password);
        //TODO Auto-generated constructor stub
    }
    
    public boolean cekKelayakanProduk(Produk produk){
        boolean kelayakan;

        if (produk.getKualitas().equalsIgnoreCase("Sangat Baik") || produk.getKualitas().equalsIgnoreCase("Baik") || produk.getKualitas().equalsIgnoreCase("Cukup") && produk.isLayakJual() == true){
            kelayakan = true;
            produk.setLayakJual(kelayakan);
        } else{
            kelayakan = false;
            produk.setLayakJual(kelayakan);
        }

        return produk.isLayakJual();
    }
}
