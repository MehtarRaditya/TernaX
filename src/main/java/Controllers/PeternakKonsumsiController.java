/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DataAccessObject.HewanDAO;
import DataAccessObject.KonsumsiDAO;
import DataAccessObject.PemakaianKonsumsiDAO;
import Models.Hewan;
import Models.Karyawan;
import Models.Konsumsi;
import Models.PemakaianKonsumsi;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import utility.Session;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class PeternakKonsumsiController implements Initializable {

    @FXML
    private Button btHewan;
    @FXML
    private Button btnProduk;
    @FXML
    private Button btnKonsum;
    @FXML
    private Button btnLogout;
    @FXML
    private TableView<Hewan> tvHewan;
    @FXML
    private TableColumn<Hewan, Integer> colIdHewan;
    @FXML
    private TableColumn<Hewan, String> colNamaHewan;
    @FXML
    private TableColumn<Hewan, Integer> colBobotHewan;

    @FXML
    private ChoiceBox<Konsumsi> chbKonsumsi;
    
    // ===== DAO =====
    private final HewanDAO hewanDAO = new HewanDAO();
    private final KonsumsiDAO konsumsiDAO = new KonsumsiDAO();
    private final PemakaianKonsumsiDAO pemakaianDAO = new PemakaianKonsumsiDAO();

    // ===== Data =====
    private final ObservableList<Hewan> hewanList = FXCollections.observableArrayList();
    private final ObservableList<Konsumsi> konsumsiList = FXCollections.observableArrayList();
    // MASTER DATA (Menampung SEMUA data dari database)
    private final ObservableList<Konsumsi> allKonsumsiList = FXCollections.observableArrayList();
    @FXML
    private TableColumn<?, ?> colPenyakit;
    @FXML
    private Spinner<Double> spnKuantitas;
    @FXML
    private Label lblStok;
    @FXML
    private Button BtnberiMkn;
    @FXML
    private Label lblStatus;
    @FXML
    private Label lblInfo1;
    @FXML
    private Label lblInfo2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
        loadHewan();
        loadKonsumsi();
        initFormComponents();
        
        lblStatus.setText("Silakan pilih hewan di tabel sebelum beri pakan!.");
        lblStatus.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
    }

    private void initTable() {
        colIdHewan.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNamaHewan.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        colBobotHewan.setCellValueFactory(new PropertyValueFactory<>("berat"));
        colPenyakit.setCellValueFactory(new PropertyValueFactory<>("penyakit")); 
        
        tvHewan.setItems(hewanList);

        // === LISTENER PENTING ===
        // Saat Hewan dipilih -> Jalankan Filter
        tvHewan.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                filterPakanPintar(newVal.getJenis());
            }
        });
    }
    
    // === [LOGIKA FILTER YANG SUDAH DIPERBAIKI] ===
    private void filterPakanPintar(String namaHewanLengkap) {
        // 1. Bersihkan pilihan lama
        chbKonsumsi.getSelectionModel().clearSelection();
        lblStok.setText("-");
        
        if (namaHewanLengkap == null) return;
        String hewanLower = namaHewanLengkap.toLowerCase();

        // 2. Tentukan Kata Kunci Hewan
        // Kita cari kata "sapi", "ayam", "kambing" di dalam nama hewan yg dipilih
        String keywordHewan = "";
        
        if (hewanLower.contains("sapi")) keywordHewan = "sapi";
        else if (hewanLower.contains("ayam")) keywordHewan = "ayam";
        else if (hewanLower.contains("kambing")) keywordHewan = "kambing";
        else if (hewanLower.contains("domba")) keywordHewan = "domba";
        else if (hewanLower.contains("bebek")) keywordHewan = "bebek";
        else keywordHewan = "umum"; // Kalau hewan tidak dikenal

        ObservableList<Konsumsi> filteredList = FXCollections.observableArrayList();

        // 3. Loop Semua Data Konsumsi
        for (Konsumsi k : allKonsumsiList) {
            String tipe = k.getTipe().toLowerCase(); // Contoh: "pakan sapi", "vitamin", "obat"

            // SYARAT 1: Tipe Vitamin -> MUNCUL SEMUA
            if (tipe.contains("vitamin")) {
                filteredList.add(k);
            }
            // SYARAT 2: Tipe Obat -> MUNCUL SEMUA
            else if (tipe.contains("obat")) {
                filteredList.add(k);
            }
            // SYARAT 3: Pakan -> Hanya muncul jika TIPE mengandung KATA KUNCI HEWAN
            // (Misal: keyword "sapi", maka "pakan sapi" masuk. "pakan ayam" tidak masuk)
            else if (!keywordHewan.equals("umum") && tipe.contains(keywordHewan)) {
                filteredList.add(k);
            }
        }

        // 4. Tampilkan Hasil
        chbKonsumsi.setItems(filteredList);
        
        // Debugging / Info Status
        if (filteredList.isEmpty()) {
            lblStatus.setText("Tidak ada pakan untuk " + keywordHewan);
            lblStatus.setStyle("-fx-text-fill: red;");
            // Fallback: Jika kosong banget, tampilkan semua biar tidak error
            chbKonsumsi.setItems(allKonsumsiList);
        } else {
            lblStatus.setText("Menampilkan: Obat, Vitamin & Pakan " + keywordHewan);
            lblStatus.setStyle("-fx-text-fill: blue;");
        }
    }
    
    private void initFormComponents() {
        SpinnerValueFactory<Double> valueFactory = 
            new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, 0.0, 1.0);
        spnKuantitas.setValueFactory(valueFactory);

        chbKonsumsi.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cekStokRealtime(newVal);
            }
        });
    }
    
    private void cekStokRealtime(Konsumsi k) {
        int stok = konsumsiDAO.getStokById(k.getId()); 
        
        // Cek Tipe untuk menentukan Satuan
        String tipe = k.getTipe().toLowerCase();
        String satuan = "Kg"; // Default untuk pakan
        
        if (tipe.contains("vitamin") || tipe.contains("obat")) {
            satuan = "Pcs"; // Bisa ganti jadi "Botol" atau "Ampul" sesukamu
        }
        
        // Set Text Label
        lblStok.setText(stok + " " + satuan);
        
        // Warnai Merah kalau Habis
        if (stok <= 0) {
            lblStok.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            lblStok.setText("Habis (0 " + satuan + ")");
        } else {
            lblStok.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
        }
    }

    private void loadHewan() {
        hewanList.clear();
        List<Hewan> allData = hewanDAO.getAll(); 
        
        // DEBUG: Cetak total data yang ditarik dari DB
        System.out.println("================ DEBUG DATA HEWAN ================");
        System.out.println("Total data di Database: " + allData.size());

        for (Hewan h : allData) {
            String kondisi = h.getKondisi();
            
            // Bersihkan spasi tidak sengaja (trim)
            if (kondisi != null) {
                kondisi = kondisi.trim(); 
            } else {
                kondisi = "KOSONG";
            }


            // LOGIKA FILTER (Dibuat lebih longgar)
            if (kondisi.equalsIgnoreCase("Alive") || 
                kondisi.equalsIgnoreCase("Hidup") || 
                kondisi.equalsIgnoreCase("Sehat")) {
                
                hewanList.add(h);
        }
        System.out.println("==================================================");
        
        // Peringatan kalau tabel kosong
        if (hewanList.isEmpty()) {
            System.out.println("Tabel Kosong! Cek apakah status di database benar-benar 'Alive'?");
        }
    }
    }

    private void loadKonsumsi() {
        allKonsumsiList.clear();
        List<Konsumsi> data = konsumsiDAO.getAll();
        allKonsumsiList.addAll(data); // Simpan ke Master List

        // Kosongkan di awal (biar user klik tabel dulu)
        chbKonsumsi.setItems(FXCollections.observableArrayList());

        chbKonsumsi.setConverter(new StringConverter<>() {
            @Override
            public String toString(Konsumsi k) {
                return (k == null) ? "" : (k.getName() + " (" + k.getTipe() + ")");
            }
            @Override
            public Konsumsi fromString(String string) { return null; }
        });
    }    

    @FXML
    private void btnHewan(ActionEvent event) {
        try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btHewan.getScene().getWindow();
            
            // 2. Cari file FXML tujuan
            java.io.File file = new java.io.File("src/main/java/Views/peternakHewan.fxml");
            
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

    @FXML
    private void btnProduk(ActionEvent event) {
        try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btnProduk.getScene().getWindow();
            
            // 2. Cari file FXML tujuan
            java.io.File file = new java.io.File("src/main/java/Views/peternakPanen.fxml");
            
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

    @FXML
    private void btnKonsum(ActionEvent event) {
    }

    @FXML
    private void btnLogout(ActionEvent event) {
         try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btnLogout.getScene().getWindow();
            
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

    @FXML
    private void handleButtonBeriMakan(ActionEvent event) {
        Karyawan karyawan = Session.getLoggedInKaryawan();
        if (karyawan == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Belum ada karyawan yang login.");
            return;
        }

        Hewan hewan = tvHewan.getSelectionModel().getSelectedItem();
        Konsumsi konsumsi = chbKonsumsi.getValue();
        Double qtyDouble = spnKuantitas.getValue();
        int qty = qtyDouble.intValue(); 

        if (hewan == null) {
            lblStatus.setText("Pilih hewan dulu!");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }
        if (konsumsi == null) {
            lblStatus.setText("Pilih jenis pakan!");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }
        if (qty <= 0) {
            lblStatus.setText("Kuantitas harus > 0");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }

        int stok = konsumsiDAO.getStokById(konsumsi.getId());
        if (stok < qty) {
            showAlert(Alert.AlertType.WARNING, "Stok Kurang", 
                    "Stok tersisa: " + stok + ", Dibutuhkan: " + qty);
            return;
        }

        PemakaianKonsumsi pk = new PemakaianKonsumsi();
        pk.setIdHewan(hewan.getId());
        pk.setIdKonsumsi(konsumsi.getId());
        pk.setKuantitas(qty);
        pk.setIdKaryawan(Integer.parseInt(karyawan.getId()));

        try {
            pemakaianDAO.beriPakan(pk); 

            lblStatus.setText("Berhasil: " + qty + "Kg ke " + hewan.getJenis());
            lblStatus.setStyle("-fx-text-fill: green;");
            
            cekStokRealtime(konsumsi);
            spnKuantitas.getValueFactory().setValue(0.0);
            
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data berhasil disimpan!");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
}
