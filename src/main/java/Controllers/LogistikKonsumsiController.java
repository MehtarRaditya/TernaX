package Controllers;

import DataAccessObject.DetailPembelianDAO;
import DataAccessObject.KonsumsiDAO;
import DataAccessObject.PembelianDAO;
import Models.DetailPembelian;
import Models.DetailPembelianItem;
import Models.Konsumsi;
import Models.RiwayatKonsumsi;
import Models.Pembelian;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class LogistikKonsumsiController implements Initializable {

    // --- DAO & DATA ---
    private final KonsumsiDAO konsumsiDAO = new KonsumsiDAO();
    private final PembelianDAO pembelianDAO = new PembelianDAO();
    private final DetailPembelianDAO detailPembelianDAO = new DetailPembelianDAO();
    
    private ObservableList<DetailPembelianItem> keranjang = FXCollections.observableArrayList();
    private List<Konsumsi> allKonsumsi;

    @FXML private Button btnLogout;
    
    @FXML private TableView<DetailPembelianItem> tableHewan; // Tabel Keranjang
    @FXML private TableColumn<DetailPembelianItem, String> colNamaKonsum;
    @FXML private TableColumn<DetailPembelianItem, String> colTipe;
    @FXML private TableColumn<DetailPembelianItem, Integer> colKuantitas;

    @FXML private Button btnTambah; // Tombol Tambah ke Keranjang
    @FXML private Button btnTambahKonsumsi; // Tombol Buka Panel
    
    @FXML private Pane sceneNambahKonsumsi; // Panel Form Input
    @FXML private DatePicker dtPickPembelian;
    @FXML private ComboBox<String> ChbKonsumsi; // ComboBox TIPE
    @FXML private ComboBox<Konsumsi> chbNamaKonsum; // ComboBox NAMA
    @FXML private TextField txtKuantitas;
    @FXML
    private Button btnKembali;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnQC;
    @FXML
    private TableColumn<RiwayatKonsumsi, Integer> col_idPebelian;
    @FXML
    private TableColumn<RiwayatKonsumsi, String> colTanggal;
    @FXML
    private Label totalPakanAyam;
    @FXML
    private Label totalPakanSapi;
    @FXML
    private Label totalObat;
    @FXML
    private Label totalVitamin;
    @FXML
    private Button btnKonsum;
    @FXML
    private TableView<RiwayatKonsumsi> tvDetailTransaksi;
    @FXML
    private TableColumn<RiwayatKonsumsi, String> colNamaKonsum2;
    @FXML
    private TableColumn<RiwayatKonsumsi, Integer> colQty;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("=== MEMULAI INITIALIZE ===");
        
        // ... (Kode initialize panel input kamu yang lama) ...
        if (sceneNambahKonsumsi != null) sceneNambahKonsumsi.setVisible(false);
        loadDataKonsumsi();
        hitungTotalStok();
        
        // Setup Tabel Keranjang (Input)
        if (colNamaKonsum != null) colNamaKonsum.setCellValueFactory(new PropertyValueFactory<>("namaKonsumsi"));
        if (colTipe != null) colTipe.setCellValueFactory(new PropertyValueFactory<>("tipe"));
        if (colKuantitas != null) colKuantitas.setCellValueFactory(new PropertyValueFactory<>("kuantitas"));
        if (tableHewan != null) tableHewan.setItems(keranjang);

        // ... (Listener ComboBox dll) ...
        
        // --- [BARU] SETUP TABEL RIWAYAT TRANSAKSI ---
        setupTabelRiwayat();
    }
    
    private void setupTabelRiwayat() {
        if (tvDetailTransaksi == null) {
            System.err.println("WARNING: fx:id 'tvDetailTransaksi' belum dilink di SceneBuilder!");
            return;
        }

        // 1. Setup Kolom (Menggunakan PropertyValueFactory sesuai nama variabel di Model RiwayatKonsumsi)
        if (col_idPebelian != null) col_idPebelian.setCellValueFactory(new PropertyValueFactory<>("idPembelian"));
        if (colTanggal != null) colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        if (colNamaKonsum2 != null) colNamaKonsum2.setCellValueFactory(new PropertyValueFactory<>("namaKonsumsi"));
        if (colQty != null) colQty.setCellValueFactory(new PropertyValueFactory<>("kuantitas"));

        // 2. Load Data dari Database
        refreshTabelRiwayat();
    }
    
    private void refreshTabelRiwayat() {
        ObservableList<RiwayatKonsumsi> dataRiwayat = FXCollections.observableArrayList(
            pembelianDAO.getAllRiwayat()
        );
        tvDetailTransaksi.setItems(dataRiwayat);
    }
    

    private void loadDataKonsumsi() {
        allKonsumsi = konsumsiDAO.getAll();
        
        // DEBUG: Cek apakah data masuk?
        if (allKonsumsi == null || allKonsumsi.isEmpty()) {
            System.out.println("ERROR: Database Konsumsi KOSONG atau Gagal Query!");
            showAlert("Error Data", "Data Tipe Pakan tidak ditemukan di Database.");
            return;
        }

        System.out.println("Sukses Load Data: " + allKonsumsi.size() + " items.");

        // Ambil Tipe Unik untuk ComboBox
        ObservableList<String> tipeList = FXCollections.observableArrayList(
                allKonsumsi.stream()
                        .map(Konsumsi::getTipe) // Pastikan method getTipe() ada di Model Konsumsi
                        .filter(t -> t != null && !t.isBlank())
                        .distinct()
                        .collect(Collectors.toList())
        );
        
        if (ChbKonsumsi != null) {
            ChbKonsumsi.setItems(tipeList);
            System.out.println("Isi ComboBox Tipe: " + tipeList);
        }
    }

    private void filterNamaByTipe(String tipeDipilih) {
        if (chbNamaKonsum == null) return;

        if (tipeDipilih == null) {
            chbNamaKonsum.getItems().clear();
            chbNamaKonsum.setDisable(true);
            return;
        }

        List<Konsumsi> filtered = allKonsumsi.stream()
                .filter(k -> tipeDipilih.equalsIgnoreCase(k.getTipe()))
                .collect(Collectors.toList());

        chbNamaKonsum.setItems(FXCollections.observableArrayList(filtered));
        chbNamaKonsum.setDisable(filtered.isEmpty());
        
        if (!filtered.isEmpty()) {
            chbNamaKonsum.getSelectionModel().selectFirst();
        }
    }

    // --- TOMBOL-TOMBOL ---

    @FXML
    private void btnTambahKonsumsi(ActionEvent event) {
        // Buka Panel Input
        if (sceneNambahKonsumsi != null) {
            sceneNambahKonsumsi.setVisible(true);
            // Reset Form
            txtKuantitas.clear();
            if (ChbKonsumsi != null) ChbKonsumsi.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void handleButtonTambah(ActionEvent event) {
        // --- [BARU] 1. Cek Tanggal Dulu Sebelum Tambah Item ---
        if (dtPickPembelian.getValue() == null) {
            showAlert("Warning", "Pilih Tanggal Pembelian terlebih dahulu!");
            return;
        }

        // 1. Validasi Input Lainnya
        String tipe = (ChbKonsumsi != null) ? ChbKonsumsi.getValue() : null;
        Konsumsi selected = (chbNamaKonsum != null) ? chbNamaKonsum.getValue() : null;

        if (tipe == null || selected == null) {
            showAlert("Warning", "Pilih Tipe dan Nama Konsumsi dulu!");
            return;
        }

        if (txtKuantitas.getText().isEmpty()) {
            showAlert("Warning", "Isi jumlah kuantitas!");
            return;
        }

        try {
            int qty = Integer.parseInt(txtKuantitas.getText().trim());
            if (qty <= 0) {
                showAlert("Warning", "Jumlah harus lebih dari 0");
                return;
            }

            // 2. Masukkan ke Keranjang
            boolean exists = false;
            for (DetailPembelianItem item : keranjang) {
                if (item.getIdKonsumsi() == selected.getId()) {
                    item.setKuantitas(item.getKuantitas() + qty);
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                keranjang.add(new DetailPembelianItem(selected, qty));
            }

            // 3. Refresh Tabel
            tableHewan.refresh();
            txtKuantitas.clear();
            
            // --- [BARU] 2. Kunci Tanggal agar tidak bisa diubah ---
            dtPickPembelian.setDisable(true); 

        } catch (NumberFormatException e) {
            showAlert("Error", "Kuantitas harus berupa angka bulat!");
        }
    }
    
    // Mapping tombol Tambah di FXML ke logic di atas
    private void btnTambah(ActionEvent event) {
        handleButtonTambah(event);
    }

    @FXML
    private void handleButtonHapus(ActionEvent event) {
        DetailPembelianItem selected = tableHewan.getSelectionModel().getSelectedItem();
        if (selected != null) {
            keranjang.remove(selected);
        } else {
            showAlert("Info", "Pilih item yang mau dihapus");
        }
    }
    
    @FXML
    private void handleButtonSimpan(ActionEvent event) {
        if (dtPickPembelian.getValue() == null) {
            showAlert("Warning", "Pilih Tanggal Pembelian!");
            return;
        }
        if (keranjang.isEmpty()) {
            showAlert("Warning", "Keranjang kosong!");
            return;
        }

        try {
            String tanggal = dtPickPembelian.getValue().toString();
            // Asumsi 0 atau session ID karyawan
            Pembelian transaksi = new Pembelian(tanggal, 0);
            
            int idBaru = pembelianDAO.insertAndGetId(transaksi);

            if (idBaru != -1) {
                for (DetailPembelianItem item : keranjang) {
                    DetailPembelian detail = new DetailPembelian(idBaru, item.getIdKonsumsi(), item.getKuantitas());
                    detailPembelianDAO.insert(detail);
                    konsumsiDAO.UpdateStok(item.getIdKonsumsi(), item.getKuantitas());
                    for(Konsumsi k : allKonsumsi) {
                         if(k.getId() == item.getIdKonsumsi()) {
                             k.setStok(k.getStok() + item.getKuantitas());
                             break;
                         }
                     }
                }

                showAlert("Sukses", "Transaksi Berhasil Disimpan!");
                // Reset Form Input
                keranjang.clear();
                sceneNambahKonsumsi.setVisible(false);
                dtPickPembelian.setDisable(false);
                dtPickPembelian.setValue(null);

                // [BARU] REFRESH TABEL RIWAYAT
                refreshTabelRiwayat();
                hitungTotalStok();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Gagal", "Database Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleBtnKembali(ActionEvent event) {
        // Tombol Batal/Tutup Form
        if (sceneNambahKonsumsi != null) {
            sceneNambahKonsumsi.setVisible(false);
        }
        txtKuantitas.clear();
    }

    // --- HELPER ---
    private void setKonsumsiComboBoxDisplay(ComboBox<Konsumsi> combo) {
        combo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Konsumsi item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Konsumsi item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleToQC(ActionEvent event) {
        try {
            
            javafx.stage.Stage stage = (javafx.stage.Stage) btnQC.getScene().getWindow();
            
            // 2. Cari file FXML tujuan
            java.io.File file = new java.io.File("src/main/java/Views/LogistikQC.fxml");
            
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
            System.out.println("Gagal Pindah ke Tampilan QC Logistik!");
            e.printStackTrace();
        }
    }

    @FXML
    private void handeToLogout(ActionEvent event) {
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
            System.out.println("Gagal Logout!");
            e.printStackTrace();
        }
    }
    
    // Method baru untuk menghitung total stok per kategori
    private void hitungTotalStok() {
        if (allKonsumsi == null) return;

        // 1. Hitung Pakan Ayam
        int sumPakanAyam = allKonsumsi.stream()
                .filter(k -> "Pakan Ayam".equalsIgnoreCase(k.getTipe()))
                .mapToInt(Konsumsi::getStok)
                .sum();
        if (totalPakanAyam != null) totalPakanAyam.setText(String.valueOf(sumPakanAyam));

        // 2. Hitung Pakan Sapi
        int sumPakanSapi = allKonsumsi.stream()
                .filter(k -> "Pakan Sapi".equalsIgnoreCase(k.getTipe()))
                .mapToInt(Konsumsi::getStok)
                .sum();
        if (totalPakanSapi != null) totalPakanSapi.setText(String.valueOf(sumPakanSapi));

        // 3. Hitung Vitamin
        int sumVitamin = allKonsumsi.stream()
                .filter(k -> "Vitamin".equalsIgnoreCase(k.getTipe()))
                .mapToInt(Konsumsi::getStok)
                .sum();
        if (totalVitamin != null) totalVitamin.setText(String.valueOf(sumVitamin));

        // 4. Hitung Obat
        int sumObat = allKonsumsi.stream()
                .filter(k -> "Obat".equalsIgnoreCase(k.getTipe()))
                .mapToInt(Konsumsi::getStok)
                .sum();
        if (totalObat != null) totalObat.setText(String.valueOf(sumObat));
    }
    
    
}