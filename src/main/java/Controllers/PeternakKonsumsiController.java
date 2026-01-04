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
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
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
    @FXML
    private Button btnNotifikasi;
    @FXML
    private Label lblJumlahNotif;
    @FXML
    private VBox panelNotifikasi;
    @FXML
    private ListView<String> listPesanNotif;

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
        
        // Sembunyikan panel notif di awal
        if (panelNotifikasi != null) panelNotifikasi.setVisible(false);
        
        // Cek Notifikasi Awal
        refreshNotifikasi();
        
    }

    private void initTable() {
        colIdHewan.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNamaHewan.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        colBobotHewan.setCellValueFactory(new PropertyValueFactory<>("berat"));
        colPenyakit.setCellValueFactory(new PropertyValueFactory<>("penyakit")); 
        
        tvHewan.setItems(hewanList);

        tvHewan.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                filterPakanPintar(newVal.getJenis());
            }
        });
    }
    
    private void filterPakanPintar(String namaHewanLengkap) {
        chbKonsumsi.getSelectionModel().clearSelection();
        lblStok.setText("-");
        
        if (namaHewanLengkap == null) return;
        String hewanLower = namaHewanLengkap.toLowerCase();
        String keywordHewan = "";
        
        if (hewanLower.contains("sapi")) keywordHewan = "sapi";
        else if (hewanLower.contains("ayam")) keywordHewan = "ayam";
        else if (hewanLower.contains("kambing")) keywordHewan = "kambing";
        else if (hewanLower.contains("domba")) keywordHewan = "domba";
        else if (hewanLower.contains("bebek")) keywordHewan = "bebek";
        else keywordHewan = "umum"; 

        ObservableList<Konsumsi> filteredList = FXCollections.observableArrayList();

        for (Konsumsi k : allKonsumsiList) {
            String tipe = k.getTipe().toLowerCase(); 

            if (tipe.contains("vitamin")) {
                filteredList.add(k);
            }
            else if (tipe.contains("obat")) {
                filteredList.add(k);
            }
            else if (!keywordHewan.equals("umum") && tipe.contains(keywordHewan)) {
                filteredList.add(k);
            }
        }

        chbKonsumsi.setItems(filteredList);
        
        if (filteredList.isEmpty()) {
            lblStatus.setText("Tidak ada pakan untuk " + keywordHewan);
            lblStatus.setStyle("-fx-text-fill: red;");
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
        String tipe = k.getTipe().toLowerCase();
        String satuan = "Kg"; 
        
        if (tipe.contains("vitamin") || tipe.contains("obat")) {
            satuan = "Pcs"; 
        }
        
        lblStok.setText(stok + " " + satuan);
        
        if (stok <= 0) {
            lblStok.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            lblStok.setText("Habis (0 " + satuan + ")");
        } else {
            lblStok.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
        }
    }

    // 1. UPDATE LOAD HEWAN (Agar Tabel hanya isi hewan milik sendiri)
    private void loadHewan() {
        hewanList.clear();
        
        // Ambil User Login
        Karyawan userLogin = Session.getLoggedInKaryawan();
        if (userLogin == null) return;

        // Panggil Method Filter di DAO
        List<Hewan> myData = hewanDAO.getByPeternak(String.valueOf(userLogin.getId()));
        
        for (Hewan h : myData) {
            String kondisi = h.getKondisi();
            if (kondisi != null) kondisi = kondisi.trim(); 
            else kondisi = "KOSONG";

            // Filter status Hidup
            if (kondisi.equalsIgnoreCase("Alive") || 
                kondisi.equalsIgnoreCase("Hidup") || 
                kondisi.equalsIgnoreCase("Sehat")) {
                hewanList.add(h);
            }
        }
    }

    private void loadKonsumsi() {
        allKonsumsiList.clear();
        
        // 1. Ambil Semua Data Konsumsi dari Database
        List<Konsumsi> data = konsumsiDAO.getAll();
        allKonsumsiList.addAll(data);

        // 2. Kosongkan ComboBox dulu
        chbKonsumsi.setItems(FXCollections.observableArrayList());

        // 3. Atur Converter (Agar yang tampil nama, tapi yang disimpan objek)
        chbKonsumsi.setConverter(new StringConverter<>() {
            @Override
            public String toString(Konsumsi k) {
                // Tampilkan Nama + Tipe (Contoh: "Vitamin B Komplex (Vitamin)")
                return (k == null) ? "" : (k.getName() + " (" + k.getTipe() + ")");
            }

            @Override
            public Konsumsi fromString(String string) {
                // Tidak perlu diimplementasikan untuk ComboBox read-only
                return null; 
            }
        });
    }    

    // ===== TOMBOL BERI MAKAN (SUDAH DIPERBAIKI) =====
    @FXML
    private void handleButtonBeriMakan(ActionEvent event) {
        // 1. Cek Karyawan Login
        Karyawan karyawan = Session.getLoggedInKaryawan();
        if (karyawan == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Belum ada karyawan yang login.");
            return;
        }

        // 2. Ambil Data dari Inputan
        Hewan hewan = tvHewan.getSelectionModel().getSelectedItem();
        Konsumsi konsumsi = chbKonsumsi.getValue(); // <--- INI OBJEK KONSUMSI LENGKAP
        Double qtyDouble = spnKuantitas.getValue();
        int qty = qtyDouble.intValue(); 

        // 3. Validasi Input Kosong
        if (hewan == null) {
            lblStatus.setText("Pilih hewan dulu!");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }
        if (konsumsi == null) {
            lblStatus.setText("Pilih jenis pakan/vitamin!");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }
        if (qty <= 0) {
            lblStatus.setText("Kuantitas harus > 0");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }

        // --- DEBUGGING (Bisa dihapus nanti) ---
        // Cek di konsol apakah ID yang diambil benar
        System.out.println("DEBUG: Pilihan User -> " + konsumsi.getName() + " (ID: " + konsumsi.getId() + ")");
        
        // 4. Validasi Stok (Pakai ID dari objek konsumsi yang dipilih)
        int stok = konsumsiDAO.getStokById(konsumsi.getId());
        if (stok < qty) {
            showAlert(Alert.AlertType.WARNING, "Stok Kurang", 
                      "Stok " + konsumsi.getName() + " tersisa: " + stok + ", Dibutuhkan: " + qty);
            return;
        }

        // 5. Siapkan Objek Pemakaian
        PemakaianKonsumsi pk = new PemakaianKonsumsi();
        pk.setIdHewan(hewan.getId());
        
        // --- PERBAIKAN UTAMA: AMBIL ID LANGSUNG DARI OBJEK ---
        pk.setIdKonsumsi(konsumsi.getId()); // Pastikan ini mengambil ID yang benar (misal Vitamin=7)
        
        pk.setKuantitas(qty);
        pk.setIdKaryawan(Integer.parseInt(String.valueOf(karyawan.getId())));

        try {
            // 6. Simpan ke Database
            pemakaianDAO.beriPakan(pk); 

            // 7. Feedback Sukses
            lblStatus.setText("Berhasil: " + qty + " " + konsumsi.getName() + " ke " + hewan.getJenis());
            lblStatus.setStyle("-fx-text-fill: green;");
            
            // Update Info Stok Realtime di Label
            cekStokRealtime(konsumsi);
            
            // Reset Spinner
            spnKuantitas.getValueFactory().setValue(0.0);
            
            // Refresh Notifikasi
            refreshNotifikasi();
            
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data berhasil disimpan!");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ===== LOGIKA NOTIFIKASI =====
    private void refreshNotifikasi() {
        // Cek Login
        Karyawan userLogin = Session.getLoggedInKaryawan();
        if (userLogin == null) return;

        ObservableList<String> semuaNotifikasi = FXCollections.observableArrayList();

        // A. Pesan dari Manager (Tetap ambil, karena biasanya personal by Nama)
        List<String> pesanManager = utility.NotifikasiService.getPesanUntuk(userLogin.getName());
        if (pesanManager != null && !pesanManager.isEmpty()) {
            semuaNotifikasi.addAll(pesanManager);
        }

        // B. Riwayat Aktivitas (PERBAIKAN DI SINI)
        // Pakai method baru getRiwayatByPeternak(id)
        List<String> riwayatAktivitas = pemakaianDAO.getRiwayatByPeternak(String.valueOf(userLogin.getId()));
        
        if (riwayatAktivitas != null && !riwayatAktivitas.isEmpty()) {
            semuaNotifikasi.addAll(riwayatAktivitas);
        }

        // C. Update UI
        if (!semuaNotifikasi.isEmpty()) {
            lblJumlahNotif.setVisible(true);
            lblJumlahNotif.setText(String.valueOf(semuaNotifikasi.size()));
            listPesanNotif.setItems(semuaNotifikasi);
        } else {
            lblJumlahNotif.setVisible(false);
            listPesanNotif.setItems(FXCollections.observableArrayList("Belum ada pesan / aktivitas."));
        }
    }

    @FXML
    private void handleKlikNotifikasi(ActionEvent event) {
       // Cek dulu panelnya null atau tidak (Safety)
        if (panelNotifikasi == null) return;

        boolean isVisible = panelNotifikasi.isVisible();
        panelNotifikasi.setVisible(!isVisible); // Toggle (Buka/Tutup)
        
        // Jika panel BARU SAJA DIBUKA (sebelumnya false, sekarang mau jadi true)
        if (!isVisible) {
            refreshNotifikasi(); // Refresh data terbaru
            panelNotifikasi.toFront(); // Pastikan panel muncul di paling depan layer
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // ===== NAVIGASI =====
    @FXML private void btnHewan(ActionEvent event) {
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

    private void movePage(ActionEvent event, String fxml) {
        try {
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            java.io.File file = new java.io.File("src/main/java/Views/" + fxml);
            java.net.URL url = file.toURI().toURL();
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(url);
            stage.setScene(new javafx.scene.Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            System.out.println("Gagal pindah ke " + fxml);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleActionToProdukPeternak(ActionEvent event) {
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
    private void handleActionToProdukKonsumsi(ActionEvent event) {
    }

    @FXML
    private void handleActionLogout(ActionEvent event) {
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
    
   
}
