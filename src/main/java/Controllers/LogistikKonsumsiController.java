package Controllers;

import DataAccessObject.DetailPembelianDAO;
import DataAccessObject.KonsumsiDAO;
import DataAccessObject.PembelianDAO;
import Models.DetailPembelian;
import Models.DetailPembelianItem;
import Models.Konsumsi;
import Models.RiwayatKonsumsi;
import Models.Pembelian;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LogistikKonsumsiController implements Initializable {

    // --- DAO & DATA ---
    private final KonsumsiDAO konsumsiDAO = new KonsumsiDAO();
    private final PembelianDAO pembelianDAO = new PembelianDAO();
    private final DetailPembelianDAO detailPembelianDAO = new DetailPembelianDAO();
    
    private ObservableList<DetailPembelianItem> keranjang = FXCollections.observableArrayList();
    private List<Konsumsi> allKonsumsi;

    // --- FXML UI COMPONENTS ---
    @FXML private Button btnLogout;
    @FXML private Button btnKonsum;
    @FXML private Button btnQC;
    
    // TABEL KERANJANG
    @FXML private TableView<DetailPembelianItem> tableHewan; 
    @FXML private TableColumn<DetailPembelianItem, String> colNamaKonsum;
    @FXML private TableColumn<DetailPembelianItem, String> colTipe;
    @FXML private TableColumn<DetailPembelianItem, Integer> colKuantitas;

    // TOMBOL MENU
    @FXML private Button btnTambah; // Tombol Tambah ke Keranjang
    @FXML private Button btnTambahKonsumsi; // Tombol Buka Panel
    
    // PANEL INPUT PEMBELIAN
    @FXML private Pane sceneNambahKonsumsi; 
    @FXML private DatePicker dtPickPembelian;
    @FXML private ComboBox<String> ChbKonsumsi; // TIPE
    @FXML private ComboBox<Konsumsi> chbNamaKonsum; // NAMA
    @FXML private TextField txtKuantitas;
    @FXML private Button btnKembali;
    @FXML private Button btnDelete;
    @FXML private Button btnSubmit;
    
    // TABEL RIWAYAT
    @FXML private TableView<RiwayatKonsumsi> tvDetailTransaksi;
    @FXML private TableColumn<RiwayatKonsumsi, Integer> col_idPebelian;
    @FXML private TableColumn<RiwayatKonsumsi, String> colTanggal;
    @FXML private TableColumn<RiwayatKonsumsi, String> colNamaKonsum2;
    @FXML private TableColumn<RiwayatKonsumsi, Integer> colQty;

    // LABEL STATISTIK
    @FXML private Label totalPakanAyam;
    @FXML private Label totalPakanSapi;
    @FXML private Label totalObat;
    @FXML private Label totalVitamin;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("=== MEMULAI INITIALIZE LOGISTIK KONSUMSI ===");
        
        // 1. Sembunyikan panel input di awal
        if (sceneNambahKonsumsi != null) sceneNambahKonsumsi.setVisible(false);
        
        // 2. Load Data Master & Statistik
        loadDataKonsumsi();
        hitungTotalStok();
        
        // 3. Setup Tabel Keranjang (Input)
        if (colNamaKonsum != null) colNamaKonsum.setCellValueFactory(new PropertyValueFactory<>("namaKonsumsi"));
        if (colTipe != null) colTipe.setCellValueFactory(new PropertyValueFactory<>("tipe"));
        if (colKuantitas != null) colKuantitas.setCellValueFactory(new PropertyValueFactory<>("kuantitas"));
        if (tableHewan != null) tableHewan.setItems(keranjang);

        // 4. Setup Tabel Riwayat
        setupTabelRiwayat();
        
        // 5. Setup Listener ComboBox Tipe (PENTING AGAR NAMA MUNCUL)
        if (ChbKonsumsi != null) {
            ChbKonsumsi.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                filterNamaByTipe(newVal);
            });
        }
        
        // 6. Setup Tampilan ComboBox Nama (Agar muncul nama, bukan ID memori)
        if (chbNamaKonsum != null) {
            setKonsumsiComboBoxDisplay(chbNamaKonsum);
        }
    }
    
    // --- SETUP & LOAD DATA ---

    private void setupTabelRiwayat() {
        if (tvDetailTransaksi == null) return;

        // Setup Kolom
        if (col_idPebelian != null) col_idPebelian.setCellValueFactory(new PropertyValueFactory<>("idPembelian"));
        if (colTanggal != null) colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        if (colNamaKonsum2 != null) colNamaKonsum2.setCellValueFactory(new PropertyValueFactory<>("namaKonsumsi"));
        if (colQty != null) colQty.setCellValueFactory(new PropertyValueFactory<>("kuantitas"));

        // Load Data
        refreshTabelRiwayat();
    }
    
    private void refreshTabelRiwayat() {
        if (tvDetailTransaksi != null) {
            ObservableList<RiwayatKonsumsi> dataRiwayat = FXCollections.observableArrayList(
                pembelianDAO.getAllRiwayat()
            );
            tvDetailTransaksi.setItems(dataRiwayat);
        }
    }

    private void loadDataKonsumsi() {
        allKonsumsi = konsumsiDAO.getAll();
        
        if (allKonsumsi == null || allKonsumsi.isEmpty()) {
            System.out.println("ERROR: Data Konsumsi Kosong.");
            return;
        }

        // Ambil Tipe Unik untuk ComboBox
        ObservableList<String> tipeList = FXCollections.observableArrayList(
                allKonsumsi.stream()
                        .map(Konsumsi::getTipe)
                        .filter(t -> t != null && !t.isBlank())
                        .distinct()
                        .collect(Collectors.toList())
        );
        
        if (ChbKonsumsi != null) {
            ChbKonsumsi.setItems(tipeList);
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
    
    private void hitungTotalStok() {
        if (allKonsumsi == null) return;

        // Helper function kecil untuk sum
        updateLabelStok(totalPakanAyam, "Pakan Ayam");
        updateLabelStok(totalPakanSapi, "Pakan Sapi");
        updateLabelStok(totalVitamin, "Vitamin");
        updateLabelStok(totalObat, "Obat");
    }
    
    private void updateLabelStok(Label lbl, String tipe) {
        if (lbl != null) {
            int sum = allKonsumsi.stream()
                    .filter(k -> tipe.equalsIgnoreCase(k.getTipe()))
                    .mapToInt(Konsumsi::getStok)
                    .sum();
            lbl.setText(String.valueOf(sum));
        }
    }

    // --- ACTION HANDLERS ---

    @FXML
    private void btnTambahKonsumsi(ActionEvent event) {
        // Buka Panel Input
        if (sceneNambahKonsumsi != null) {
            sceneNambahKonsumsi.setVisible(true);
            txtKuantitas.clear();
            if (ChbKonsumsi != null) ChbKonsumsi.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void handleButtonTambah(ActionEvent event) {
        // 1. Validasi
        if (dtPickPembelian.getValue() == null) {
            showAlert("Warning", "Pilih Tanggal Pembelian terlebih dahulu!");
            return;
        }

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

            // 2. Masukkan ke Keranjang (Cek duplikasi)
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

            // 3. Refresh UI
            tableHewan.refresh();
            txtKuantitas.clear();
            dtPickPembelian.setDisable(true); // Kunci tanggal

        } catch (NumberFormatException e) {
            showAlert("Error", "Kuantitas harus berupa angka bulat!");
        }
    }
    
    @FXML
    private void btnTambah(ActionEvent event) {
        handleButtonTambah(event);
    }

    @FXML
    private void handleButtonHapus(ActionEvent event) {
        DetailPembelianItem selected = tableHewan.getSelectionModel().getSelectedItem();
        if (selected != null) {
            keranjang.remove(selected);
            // Jika keranjang kosong, buka kunci tanggal
            if (keranjang.isEmpty()) {
                dtPickPembelian.setDisable(false);
            }
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
            // Simpan Transaksi Header
            Pembelian transaksi = new Pembelian(tanggal, 0);
            int idBaru = pembelianDAO.insertAndGetId(transaksi);

            if (idBaru != -1) {
                // Simpan Detail & Update Stok
                for (DetailPembelianItem item : keranjang) {
                    DetailPembelian detail = new DetailPembelian(idBaru, item.getIdKonsumsi(), item.getKuantitas());
                    detailPembelianDAO.insert(detail);
                    
                    // Update DB
                    konsumsiDAO.UpdateStok(item.getIdKonsumsi(), item.getKuantitas());
                    
                    // Update Local List (agar statistik langsung update)
                    for(Konsumsi k : allKonsumsi) {
                         if(k.getId() == item.getIdKonsumsi()) {
                             k.setStok(k.getStok() + item.getKuantitas());
                             break;
                         }
                    }
                }

                showAlert("Sukses", "Transaksi Berhasil Disimpan!");
                
                // Reset UI
                keranjang.clear();
                sceneNambahKonsumsi.setVisible(false);
                dtPickPembelian.setDisable(false);
                dtPickPembelian.setValue(null);

                // Refresh Data Tampilan
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
        if (sceneNambahKonsumsi != null) {
            sceneNambahKonsumsi.setVisible(false);
        }
        txtKuantitas.clear();
        // Jangan clear keranjang disini agar user bisa lanjut nanti
    }

    // --- NAVIGASI ---

    @FXML
    private void handleToQC(ActionEvent event) {
        movePage(event, "LogistikQC.fxml");
    }

    @FXML
    private void handeToLogout(ActionEvent event) {
        movePage(event, "Login.fxml");
    }
    
    private void movePage(ActionEvent event, String fxmlFile) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            File file = new File("src/main/java/Views/" + fxmlFile);
            URL url = file.toURI().toURL();
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            System.out.println("Gagal pindah ke " + fxmlFile);
            e.printStackTrace();
        }
    }

    // --- HELPER METHODS ---

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
}