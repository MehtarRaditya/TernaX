package Controllers;

import DataAccessObject.HewanDAO;
import DataAccessObject.KaryawanDAO;
import DataAccessObject.KonsumsiDAO;
import DataAccessObject.PemakaianKonsumsiDAO;
import DataAccessObject.ProdukDAO;
import DataAccessObject.PembelianDAO;
import DataAccessObject.PenjualanDAO; 
import Models.Hewan;
import Models.Karyawan;
import Models.Konsumsi;
import Models.PemakaianKonsumsi;
import Models.Produk;
import Models.RiwayatKonsumsi;
import Models.Penjualan;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

/**
 * FXML Controller class for Manager Peternakan Dashboard
 * REVISI: Menggunakan Double untuk semua satuan numerik.
 */
public class ManagerPeternakanController implements Initializable {

    // --- 1. KOMPONEN UI ---

    @FXML private AnchorPane tampilanTabPeternakan;
    @FXML private Button btnKaryawan;
    @FXML private Button btnDashboard;
    @FXML private Button btnWarning; // Tombol Logout

    // A. STATISTIK HEWAN
    @FXML private Label lblTotalSapi;
    @FXML private Label lblTotalAyam;
    @FXML private ChoiceBox<String> cbFilterSakit; 
    @FXML private Label lblHewanSakit;

    // B. TABEL PEMAKAIAN PAKAN (LOGISTIK KELUAR)
    @FXML private TableView<PemakaianKonsumsi> tabelPemakaian;
    @FXML private TableColumn<PemakaianKonsumsi, String> colTglPakai;
    @FXML private TableColumn<PemakaianKonsumsi, String> colNamaPakan;
    @FXML private TableColumn<PemakaianKonsumsi, Double> colJmlPakai; // Ubah ke Double
    @FXML private TableColumn<PemakaianKonsumsi, String> colHewanTujuan;

    // C. TABEL RIWAYAT PANEN (PRODUK MASUK)
    @FXML 
    private TableView<Produk> tabelRiwayatPanen; 
    @FXML private TableColumn<Produk, String> colRwNamaProduk;
    @FXML private TableColumn<Produk, String> colRwTanggalDiperoleh;
    @FXML private TableColumn<Produk, Double> colRwJumlahPanen; // Ubah ke Double
    @FXML private TableColumn<Produk, String> colRwStatusKelayakan;
    @FXML private TableColumn<Produk, String> colRwPemeriksa;

    // D. NOTIFIKASI
    @FXML private ChoiceBox<Karyawan> cbPilihPeternak;
    @FXML private TextArea txtPesanNotif;
    @FXML private Button btnKirimNotif;
    @FXML private Label lblStatusNotif;

    // E. TABEL STOK GUDANG
    @FXML private TableView<Konsumsi> tabelStokKonsumsi;
    @FXML private TableColumn<Konsumsi, String> colNamaKonsumsi;
    @FXML private TableColumn<Konsumsi, String> colTipeKonsumsi;
    @FXML private TableColumn<Konsumsi, Double> colStok; // Ubah ke Double

    // F. TABEL RIWAYAT PEMBELIAN (LOGISTIK MASUK)
    @FXML private TableView<RiwayatKonsumsi> tabelPembelian;
    @FXML private TableColumn<RiwayatKonsumsi, String> colTglBeli;
    @FXML private TableColumn<RiwayatKonsumsi, String> colItemBeli;
    @FXML private TableColumn<RiwayatKonsumsi, Double> colJumlahPembelian; // Ubah ke Double
    @FXML private TableColumn<RiwayatKonsumsi, String> colNamaPembeli;

    // G. STATISTIK PRODUK
    @FXML private Label lblTotalDagingSapi;
    @FXML private Label lblTotalSusu;
    @FXML
    private Label lblTotalDagingAyam; 
    @FXML private Label lblTotalTelur;
    @FXML private Label lblTotalLayak;
    @FXML private Label lblTotalTidakLayak;
    
    // H. TABEL PENJUALAN
    @FXML private Label lblTotalOmzetTabel;
    @FXML
    private TableView<Penjualan> tblPenjualan;
    @FXML private TableColumn<Penjualan, Integer> colIdJual; // ID biarkan Integer/String
    @FXML private TableColumn<Penjualan, String> colTanggal;
    @FXML private TableColumn<Penjualan, String> colNamaProdukJual; 
    @FXML private TableColumn<Penjualan, String> colKategori;
    @FXML private TableColumn<Penjualan, Double> colKuantitas; // Ubah ke Double
    @FXML private TableColumn<Penjualan, Double> colHargaSatuan;
    @FXML private TableColumn<Penjualan, Double> colTotalHarga;

    // --- 2. DAO INSTANCES ---
    private final HewanDAO hewanDAO = new HewanDAO();
    private final ProdukDAO produkDAO = new ProdukDAO();
    private final KonsumsiDAO konsumsiDAO = new KonsumsiDAO();
    private final PemakaianKonsumsiDAO pemakaianDAO = new PemakaianKonsumsiDAO();
    private final PembelianDAO pembelianDAO = new PembelianDAO();
    private final KaryawanDAO karyawanDAO = new KaryawanDAO();
    private final PenjualanDAO penjualanDAO = new PenjualanDAO();
    // I. TABEL DAFTAR HEWAN (Tambahan Baru)
    @FXML private TableView<Hewan> tvDaftarHewan; // Pastikan fx:id di FXML: tvDaftarHewan
    
    @FXML private TableColumn<Hewan, Integer> colIDHewanDFHEWAN;      // ID biasanya Integer
    @FXML private TableColumn<Hewan, String> colJenisDFHewan;         // String
    @FXML private TableColumn<Hewan, Double> colBeratDFHewan;         // Berat pakai Double
    @FXML private TableColumn<Hewan, Integer> colUsiaDFHewan;         // Usia biasanya Integer (bulan/tahun)
    @FXML private TableColumn<Hewan, String> colJenisKelaminDFHewan;  // String
    @FXML private TableColumn<Hewan, String> colPemilikDFHewaN;       // String (Perhatikan huruf 'N' besar di akhir sesuai FXML kamu)
    @FXML private TableColumn<Hewan, String> colPenyakitDFHewan;      // String

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        refreshAllData();
        setupStatistikHewanInteraktif();
        setupNotifikasiForm();
    }

    private void refreshAllData() {
        loadDataStok();
        loadDataPemakaian();
        loadDataPembelian();
        loadDataPanen();
        loadDataPenjualan();
        loadStatistikProduk();
        loadDataDaftarHewan();
    }

    // ==========================================================
    // 3. SETUP TABLE COLUMNS (Semua Integer diganti Double)
    // ==========================================================
    private void setupTableColumns() {
        // A. Tabel Stok
        colNamaKonsumsi.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTipeKonsumsi.setCellValueFactory(new PropertyValueFactory<>("tipe"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok")); // Pastikan Model return Double

        // B. Tabel Pemakaian
        colTglPakai.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colNamaPakan.setCellValueFactory(new PropertyValueFactory<>("namaKonsumsi"));
        colJmlPakai.setCellValueFactory(new PropertyValueFactory<>("kuantitas")); // Pastikan Model return Double
        colHewanTujuan.setCellValueFactory(new PropertyValueFactory<>("idHewan")); 

        // C. Tabel Pembelian
        colTglBeli.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colItemBeli.setCellValueFactory(new PropertyValueFactory<>("namaKonsumsi"));
        colJumlahPembelian.setCellValueFactory(new PropertyValueFactory<>("kuantitas")); // Pastikan Model return Double
        colNamaPembeli.setCellValueFactory(new PropertyValueFactory<>("namaKaryawan"));

        // D. Tabel Panen
        colRwNamaProduk.setCellValueFactory(new PropertyValueFactory<>("namaProduk"));
        colRwTanggalDiperoleh.setCellValueFactory(new PropertyValueFactory<>("tanggalDiperoleh"));
        colRwJumlahPanen.setCellValueFactory(new PropertyValueFactory<>("kuantitas")); // Pastikan Model return Double
        colRwStatusKelayakan.setCellValueFactory(new PropertyValueFactory<>("statusKelayakan"));
        colRwPemeriksa.setCellValueFactory(new PropertyValueFactory<>("namaPemeriksa"));

        // E. Tabel Penjualan
        colIdJual.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalTransaksi"));
        colNamaProdukJual.setCellValueFactory(new PropertyValueFactory<>("namaProduk"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        colKuantitas.setCellValueFactory(new PropertyValueFactory<>("kuantitas")); // Pastikan Model return Double
        colHargaSatuan.setCellValueFactory(new PropertyValueFactory<>("hargaSatuan"));
        colTotalHarga.setCellValueFactory(new PropertyValueFactory<>("totalHarga"));
        
        // F. Tabel Daftar Hewan
        // F. Tabel Daftar Hewan (Tambahan)
        // Pastikan di Model Hewan ada getter: getId(), getJenis(), getBerat(), dll.
        colIDHewanDFHEWAN.setCellValueFactory(new PropertyValueFactory<>("id"));
        colJenisDFHewan.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        colBeratDFHewan.setCellValueFactory(new PropertyValueFactory<>("berat")); 
        colUsiaDFHewan.setCellValueFactory(new PropertyValueFactory<>("usia"));
        
        // Cek model kamu: getKelamin() atau getJenisKelamin()? Sesuaikan string di bawah:
        colJenisKelaminDFHewan.setCellValueFactory(new PropertyValueFactory<>("kelamin")); 
        
        // Cek model kamu: getPemilik() atau getNamaPemilik()?
        colPemilikDFHewaN.setCellValueFactory(new PropertyValueFactory<>("pemilik")); 
        
        colPenyakitDFHewan.setCellValueFactory(new PropertyValueFactory<>("penyakit"));
    }

    // ==========================================================
    // 4. LOAD DATA 
    // ==========================================================
    private void loadDataStok() {
        tabelStokKonsumsi.setItems(FXCollections.observableArrayList(konsumsiDAO.getAll()));
    }

    private void loadDataPemakaian() {
        tabelPemakaian.setItems(FXCollections.observableArrayList(pemakaianDAO.getAllForManager()));
    }

    private void loadDataPembelian() {
        tabelPembelian.setItems(FXCollections.observableArrayList(pembelianDAO.getAllRiwayatForManager()));
    }

    private void loadDataPanen() {
        tabelRiwayatPanen.setItems(FXCollections.observableArrayList(produkDAO.getAllForManager()));
    }

    private void loadDataPenjualan() {
        List<Penjualan> listPenjualan = penjualanDAO.getAll();
        if (listPenjualan != null) {
            tblPenjualan.setItems(FXCollections.observableArrayList(listPenjualan));
            
            double totalOmzet = listPenjualan.stream()
                .mapToDouble(Penjualan::getTotalHarga)
                .sum();
            lblTotalOmzetTabel.setText("Total Omzet: Rp " + String.format("%,.0f", totalOmzet));
        }
    }
    
    private void loadDataDaftarHewan() {
        List<Hewan> listHewan = hewanDAO.getAllForManager();
        
        // Masukkan ke tabel
        tvDaftarHewan.setItems(FXCollections.observableArrayList(listHewan));
    }

    private void setupStatistikHewanInteraktif() {
        updateStatistikHewanLabel();
        
        cbFilterSakit.setItems(FXCollections.observableArrayList("Semua", "Sakit", "Sehat"));
        cbFilterSakit.getSelectionModel().selectFirst();
        
        cbFilterSakit.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateStatistikHewanLabel();
        });
    }

    private void updateStatistikHewanLabel() {
        List<Hewan> allHewan = hewanDAO.getAll();
        if (allHewan == null) return;

        // Count (jumlah ekor) kita casting ke double
        double countSapi = (double) allHewan.stream().filter(h -> "Sapi".equalsIgnoreCase(h.getJenis()) && isHidup(h.getKondisi())).count();
        double countAyam = (double) allHewan.stream().filter(h -> "Ayam".equalsIgnoreCase(h.getJenis()) && isHidup(h.getKondisi())).count();

        lblTotalSapi.setText(formatDouble(countSapi) + " Ekor");
        lblTotalAyam.setText(formatDouble(countAyam) + " Ekor");

        double countSakit = (double) allHewan.stream().filter(h -> 
                isHidup(h.getKondisi()) && 
                (h.getKondisi().toLowerCase().contains("sakit") || 
                (h.getPenyakit() != null && !h.getPenyakit().equals("-") && !h.getPenyakit().equalsIgnoreCase("Sehat")))
        ).count();

        String filter = cbFilterSakit.getValue();
        if ("Sakit".equals(filter)) {
            lblHewanSakit.setText(formatDouble(countSakit) + " Ekor (Sakit)");
        } else if ("Sehat".equals(filter)) {
            double totalHidup = countSapi + countAyam;
            lblHewanSakit.setText(formatDouble(totalHidup - countSakit) + " Ekor (Sehat)");
        } else {
            lblHewanSakit.setText(formatDouble(countSakit) + " Ekor Sakit");
        }
    }

    private boolean isHidup(String kondisi) {
        return kondisi != null && !kondisi.equalsIgnoreCase("Mati") && !kondisi.equalsIgnoreCase("Death");
    }

    private void loadStatistikProduk() {
        // 1. STATISTIK QC (LAYAK / TIDAK LAYAK)
        // Ambil dari tabel 'produk' (Riwayat Panen)
        int jumlahLayak = produkDAO.getJumlahStatusQC("Layak", true);
        int jumlahTidakLayak = produkDAO.getJumlahStatusQC("Layak", false); // Yang TIDAK Layak

        lblTotalLayak.setText(jumlahLayak + " Item");
        lblTotalTidakLayak.setText(jumlahTidakLayak + " Item");

        // 2. STATISTIK STOK REAL-TIME
        
        double stokSapi = produkDAO.getTotalStokRealTime("Daging Sapi Premium"); // Misal: "Daging Sapi Premium"
        double stokAyam = produkDAO.getTotalStokRealTime("Daging Ayam Potong"); // Misal: "Daging Ayam Potong"
        double stokSusu = produkDAO.getTotalStokRealTime("Susu Sapi Murni"); // Misal: "Susu Murni"
        double stokTelur = produkDAO.getTotalStokRealTime("Telur Ayam Negeri"); // Misal: "Telur Ayam Kampung"

        // Update Label
        lblTotalDagingSapi.setText(formatDouble(stokSapi) + " Kg");
        lblTotalDagingAyam.setText(formatDouble(stokAyam) + " Kg"); // Pastikan id label benar (tadi lblTotalAyam1?)
        lblTotalSusu.setText(formatDouble(stokSusu) + " Liter");
        lblTotalTelur.setText(formatDouble(stokTelur) + " Butir");
    }

    // Helper biar angka bulat tidak ada koma (misal 10.0 jadi 10)
    private String formatDouble(double value) {
        if (value == (long) value)
            return String.format("%d", (long) value);
        else
            return String.format("%s", value);
    }

    private double sumStokByKeyword(List<Produk> list, String keyword) {
        return list.stream()
                .filter(p -> p.getNamaProduk() != null && 
                             p.getNamaProduk().toLowerCase().contains(keyword) && 
                             "Layak".equalsIgnoreCase(p.getStatusKelayakan()))
                .mapToDouble(Produk::getKuantitas) // Asumsi getKuantitas() di Model Produk me-return double
                .sum();
    }
    
    

    // ==========================================================
    // 6. NOTIFIKASI
    // ==========================================================
    private void setupNotifikasiForm() {
        List<Karyawan> listKaryawan = karyawanDAO.getAll();
        if (listKaryawan == null) return;

        List<Karyawan> peternakOnly = listKaryawan.stream()
                .filter(k -> "Peternak".equalsIgnoreCase(k.getRole()))
                .collect(Collectors.toList());
        
        cbPilihPeternak.setItems(FXCollections.observableArrayList(peternakOnly));
        
        cbPilihPeternak.setConverter(new StringConverter<Karyawan>() {
            @Override
            public String toString(Karyawan k) { return (k != null) ? k.getName() : ""; }
            @Override
            public Karyawan fromString(String string) { return null; }
        });
    }

    @FXML
    private void handleKirimNotifikasi(ActionEvent event) {
        Karyawan penerima = cbPilihPeternak.getValue();
        String isiPesan = txtPesanNotif.getText();

        if (penerima == null || isiPesan.trim().isEmpty()) {
            lblStatusNotif.setText("Error: Pilih tujuan & isi pesan!");
            lblStatusNotif.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            utility.NotifikasiService.kirimPesan("Manager", penerima.getName(), isiPesan);
            lblStatusNotif.setText("Berhasil kirim ke " + penerima.getName());
            lblStatusNotif.setStyle("-fx-text-fill: green;");
            txtPesanNotif.clear();
        } catch (Exception e) {
            lblStatusNotif.setText("Gagal mengirim notifikasi.");
            e.printStackTrace();
        }
    }

    // ==========================================================
    // 7. NAVIGASI
    // ==========================================================
    @FXML private void handleToKaryawanManager(ActionEvent event) { 
    try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btnKaryawan.getScene().getWindow();
            
            // 2. Cari file FXML tujuan
            java.io.File file = new java.io.File("src/main/java/Views/ManagerKaryawan.fxml");
            
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
            System.out.println("Gagal pindah ke halaman Produk/Panen!");
            e.printStackTrace();
        }
    }
    
    @FXML private void handleToDashboardManager(ActionEvent event) {
    try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btnDashboard.getScene().getWindow();
            
            // 2. Cari file FXML tujuan
            java.io.File file = new java.io.File("src/main/java/Views/ManagerChart.fxml");
            
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
            System.out.println("Gagal pindah ke halaman Produk/Panen!");
            e.printStackTrace();
        }
    }
    @FXML private void handleToLogout(ActionEvent event) { 
    try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btnWarning.getScene().getWindow();
            
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
            System.out.println("Gagal pindah ke halaman Produk/Panen!");
            e.printStackTrace();
        }
    }
    
    
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}