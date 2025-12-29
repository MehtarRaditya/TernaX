package Controllers;

import DataAccessObject.PenjualanDAO;
import DataAccessObject.ProdukDAO;
import Models.Karyawan;
import Models.KatalogProduk;
import Models.KeranjangItem;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utility.Session;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class PenjualanController implements Initializable {

    // --- 1. BAGIAN TABEL KIRI (KATALOG) ---
    // Ubah <?> jadi <KatalogProduk>
    @FXML private TableView<KatalogProduk> tvKatalog;
    @FXML private TableColumn<KatalogProduk, String> colKatNama;
    @FXML private TableColumn<KatalogProduk, Double> colKatStok;   // Asumsi stok tipe Double/Int
    @FXML private TableColumn<KatalogProduk, String> colKatSatuan;
    @FXML private TableColumn<KatalogProduk, Double> colKatHarga;
    
    @FXML private Button btnTambah;

    // --- 2. BAGIAN TABEL KANAN (KERANJANG) ---
    // Ubah <?> jadi <KeranjangItem>
    @FXML private TableView<KeranjangItem> tvKeranjang;
    @FXML private TableColumn<KeranjangItem, String> colKerNama;
    @FXML private TableColumn<KeranjangItem, Double> colKerQty;
    @FXML private TableColumn<KeranjangItem, Double> colKerHarga;
    @FXML private TableColumn<KeranjangItem, Double> colKerSubtotal;
    
    @FXML private Button btnHapusItem;

    // --- 3. BAGIAN PEMBAYARAN ---
    @FXML private Label lblTotalHarga;
    @FXML private TextField txtUangBayar;
    @FXML private Button btnBayar;
    @FXML private Button btnKembali;

    // --- 4. VARIABEL GLOBAL (Non-FXML) ---
    private ProdukDAO produkDAO;       // Untuk ambil data stok barang
    private PenjualanDAO penjualanDAO; // Untuk simpan transaksi
    private ObservableList<KatalogProduk> listKatalog;
    private ObservableList<KeranjangItem> listKeranjang;
    private double totalBelanja = 0;
    @FXML
    private Spinner<Integer> spQtyInput;
    @FXML
    private Label lbNamaKasir;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inisialisasi DAO & List
        produkDAO = new ProdukDAO();
        penjualanDAO = new PenjualanDAO();
        listKeranjang = FXCollections.observableArrayList();

        // A. SETUP KOLOM TABEL KATALOG (KIRI)
        colKatNama.setCellValueFactory(new PropertyValueFactory<>("namaProduk"));
        colKatStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        colKatSatuan.setCellValueFactory(new PropertyValueFactory<>("satuan"));
        colKatHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));

        // B. SETUP KOLOM TABEL KERANJANG (KANAN)
        colKerNama.setCellValueFactory(new PropertyValueFactory<>("namaProduk"));
        colKerQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colKerHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colKerSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        // C. SETUP SPINNER (PENTING!)
        // Mengatur Spinner agar menerima angka integer mulai dari 1 sampai 100, default 1
        // Sesuaikan batas maksimum sesuai kebutuhan atau stok
        SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1);
        spQtyInput.setValueFactory(valueFactory);
        // Agar spinner bisa diedit manual (diketik angka)
        spQtyInput.setEditable(true);

        // D. LOAD DATA AWAL
        loadDataKatalog();
        
        // Hubungkan list keranjang ke tabel
        tvKeranjang.setItems(listKeranjang);
        
        // Set Total Awal 0
        lblTotalHarga.setText("Rp 0");
        
        Karyawan karyawan = Session.getLoggedInKaryawan();
        if (karyawan != null) {
            lbNamaKasir.setText(karyawan.getName()); // Asumsi getter nama adalah getName()
        } else {
            lbNamaKasir.setText("Kasir"); // Default jika null (misal testing tanpa login)
        }
    
    }    

    // Method untuk mengambil data dari Database ke Tabel Kiri
    private void loadDataKatalog() {
        // PENTING: Panggil method yang baru kita buat tadi
        listKatalog = FXCollections.observableArrayList(produkDAO.getProdukSiapJual());
        
        // Masukkan ke tabel
        tvKatalog.setItems(listKatalog);
    }

    @FXML
    private void handleTambahKeKeranjang(ActionEvent event) {
        // 1. Cek ada barang yang dipilih gak?
        KatalogProduk selected = tvKatalog.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Pilih produk dulu di tabel kiri!");
            return;
        }

        try {
            // 2. Ambil Input Jumlah DARI SPINNER
            // spQtyInput.getValue() mengembalikan Integer, kita cast ke double
            double qtyBeli = Double.valueOf(spQtyInput.getValue());

            // 3. Validasi Stok (Optional tapi disarankan)
             if (qtyBeli > selected.getStok()) {
                 showAlert("Stok Kurang", "Stok hanya tersedia: " + selected.getStok());
                 return;
             }

            // 4. Cek apakah barang sudah ada di keranjang?
            boolean isExist = false;
            for (KeranjangItem item : listKeranjang) {
                if (item.getIdProduk() == selected.getId()) {
                    // Kalau sudah ada, tambahkan qty-nya saja
                    double newQty = item.getQty() + qtyBeli;
                    
                    // Validasi total qty di keranjang vs stok (Opsional)
                    if (newQty > selected.getStok()) {
                        showAlert("Stok Limit", "Total di keranjang melebihi stok tersedia!");
                        return;
                    }
                    
                    item.setQty(newQty);
                    // Update subtotal juga perlu jika model tidak otomatis
                    item.setSubtotal(newQty * item.getHarga()); 
                    
                    isExist = true;
                    break;
                }
            }

            // 5. Kalau belum ada, buat item baru
            if (!isExist) {
                KeranjangItem itemBaru = new KeranjangItem(
                    selected.getId(),
                    selected.getNamaProduk(),
                    selected.getHarga(), 
                    qtyBeli
                );
                // Hitung subtotal awal manual jika di constructor tidak ada
                // itemBaru.setSubtotal(qtyBeli * selected.getHarga()); 
                
                listKeranjang.add(itemBaru);
            }

            // 6. Refresh Tampilan & Hitung Ulang
            tvKeranjang.refresh(); 
            hitungTotalBelanja();
            
            // Reset Spinner ke 1 setelah tambah
            spQtyInput.getValueFactory().setValue(1);

        } catch (Exception e) {
            showAlert("Error", "Terjadi kesalahan pada input jumlah!");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHapusItem(ActionEvent event) {
        // Ambil item yang dipilih di tabel kanan
        KeranjangItem selected = tvKeranjang.getSelectionModel().getSelectedItem();
        
        if (selected != null) {
            listKeranjang.remove(selected);
            hitungTotalBelanja(); // Hitung ulang total setelah dihapus
        } else {
            showAlert("Info", "Pilih barang di keranjang yang mau dihapus.");
        }
    }

    // Method Helper: Menjumlahkan Subtotal semua barang di keranjang
    private void hitungTotalBelanja() {
        totalBelanja = 0;
        for (KeranjangItem item : listKeranjang) {
            totalBelanja += item.getSubtotal();
        }
        // Tampilkan format Rupiah (tanpa desimal biar rapi)
        lblTotalHarga.setText(String.format("Rp %,.0f", totalBelanja));
    }

    @FXML
    private void handleBayar(ActionEvent event) {
        // 1. Validasi Keranjang
        if (listKeranjang.isEmpty()) {
            showAlert("Kosong", "Keranjang belanja masih kosong!");
            return;
        }

        try {
            // 2. Ambil Uang Pembayaran
            String inputUang = txtUangBayar.getText();
            if (inputUang.isEmpty()) {
                showAlert("Bayar", "Masukkan nominal uang pembayaran!");
                return;
            }
            
            double uangDibayar = Double.parseDouble(inputUang);

            // 3. Cek Kurang Bayar
            if (uangDibayar < totalBelanja) {
                showAlert("Kurang", "Uang kurang sebesar Rp " + (totalBelanja - uangDibayar));
                return;
            }

            // 4. Hitung Kembalian
            double kembalian = uangDibayar - totalBelanja;

            // 5. PROSES SIMPAN KE DATABASE (Panggil DAO)
            boolean sukses = penjualanDAO.simpanTransaksi(listKeranjang, totalBelanja, uangDibayar, kembalian);

            if (sukses) {
                showAlert("SUKSES", "Transaksi Berhasil!\nKembalian: Rp " + String.format("%,.0f", kembalian));
                
                // Reset Form untuk pelanggan berikutnya
                listKeranjang.clear();
                hitungTotalBelanja();
                txtUangBayar.clear();
                
                // Reload Katalog (supaya stok berkurang update di layar)
                loadDataKatalog(); 
            } else {
                showAlert("GAGAL", "Terjadi kesalahan saat menyimpan ke database.");
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "Input uang harus angka!");
        }
    }

    @FXML
    private void handleKembali(ActionEvent event) {
        try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btnKembali.getScene().getWindow();
            
            // 2. Cari file FXML tujuan
            java.io.File file = new java.io.File("src/main/java/Views/Login.fxml");
            
            // 3. Ubah jadi URL
            java.net.URL url = file.toURI().toURL();
            
            // 4. Load FXML
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(url);
            
            // 5. Pasang Scene Baru
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen(); 
            stage.show();
            
        } catch (Exception e) {
            System.out.println("Gagal Logout");
            e.printStackTrace();
        }
    }
    
    // Method Helper buat menampilkan Alert Pop-up
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
}