/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DataAccessObject.DetailPembelianDAO;
import DataAccessObject.KonsumsiDAO;
import DataAccessObject.PembelianDAO;
import Models.DetailPembelian;
import Models.DetailPembelianItem;
import Models.Konsumsi;
import Models.Pembelian;
import java.io.IOException;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Muham
 */
public class LogistikDashboardController implements Initializable {

    // --- DAO & DATA ---
    private final KonsumsiDAO konsumsiDAO = new KonsumsiDAO();
    private final PembelianDAO pembelianDAO = new PembelianDAO();
    private final DetailPembelianDAO detailPembelianDAO = new DetailPembelianDAO();
    
    private ObservableList<DetailPembelianItem> keranjang = FXCollections.observableArrayList();
    private List<Konsumsi> allKonsumsi;

    // --- FXML UI COMPONENTS ---
    @FXML private Button btHewan;
    @FXML private Button btnProduk;
    @FXML private Button btnKonsum;
    @FXML private Button btnLogout;
    
    // Tabel Keranjang (di FXML namanya tableHewan, tapi isinya kita pakai buat konsumsi)
    @FXML private TableView<DetailPembelianItem> tableHewan; 
    
    // Kita perlu menambahkan Kolom ini secara manual di Controller 
    // (Pastikan di SceneBuilder fx:id nya juga diset atau dilink)
    @FXML private TableColumn<DetailPembelianItem, String> colNamaKonsum; 
    @FXML private TableColumn<DetailPembelianItem, String> colTipe;
    @FXML private TableColumn<DetailPembelianItem, Integer> colKuantitas;

    @FXML private Button btnTambah; // Tombol "Tambah ke Keranjang"
    @FXML private Button btnDelate; // Tombol "Hapus Item"
    @FXML private Button btnTambahKonsumsi; // Tombol "Buka Form"
    
    @FXML private Pane sceneNambahKonsumsi; // Panel Form
    
    @FXML private DatePicker dtPickPembelian;
    @FXML private ComboBox<String> ChbKonsumsi; // Combo Tipe
    @FXML private ComboBox<Konsumsi> chbNamaKonsum; // Combo Nama
    @FXML private TextField txtKuantitas;
    @FXML private Button btnKembali; // Tombol Simpan ke Database (misal ada di FXML)

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 0. Sembunyikan panel form di awal (opsional)
        if (sceneNambahKonsumsi != null) {
            sceneNambahKonsumsi.setVisible(false);
        }

        // 1. Ambil semua data konsumsi
        allKonsumsi = konsumsiDAO.getAll();

        // 2. Isi ComboBox Tipe (Distinct)
        ObservableList<String> tipeList = FXCollections.observableArrayList(
                allKonsumsi.stream()
                        .map(Konsumsi::getTipe)
                        .filter(t -> t != null && !t.isBlank())
                        .distinct()
                        .collect(Collectors.toList())
        );
        ChbKonsumsi.setItems(tipeList);

        // 3. Setup ComboBox Nama
        chbNamaKonsum.setDisable(true);
        setKonsumsiComboBoxDisplay(chbNamaKonsum);

        // 4. Setup Tabel (Mapping Kolom)
        // Pastikan Anda sudah membuat TableColumn di FXML atau inject manual
        if (colNamaKonsum != null) colNamaKonsum.setCellValueFactory(new PropertyValueFactory<>("namaKonsumsi"));
        if (colTipe != null) colTipe.setCellValueFactory(new PropertyValueFactory<>("tipe"));
        if (colKuantitas != null) colKuantitas.setCellValueFactory(new PropertyValueFactory<>("kuantitas"));
        
        tableHewan.setItems(keranjang);

        // 5. Listener: Saat Tipe dipilih, filter Nama
        ChbKonsumsi.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                chbNamaKonsum.getItems().clear();
                chbNamaKonsum.getSelectionModel().clearSelection();
                chbNamaKonsum.setDisable(true);
                return;
            }

            List<Konsumsi> filtered = allKonsumsi.stream()
                    .filter(k -> newVal.equalsIgnoreCase(k.getTipe()))
                    .collect(Collectors.toList());

            chbNamaKonsum.setItems(FXCollections.observableArrayList(filtered));
            chbNamaKonsum.getSelectionModel().clearSelection();
            chbNamaKonsum.setDisable(filtered.isEmpty());
            
            if (!filtered.isEmpty()) {
                chbNamaKonsum.getSelectionModel().select(0);
            }
        });
    }    

    // --- HELPER UNTUK COMBOBOX DISPLAY ---
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

    // --- NAVIGASI SIDEBAR ---
    @FXML
    private void btnHewan(ActionEvent event) {
        try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btnProduk.getScene().getWindow();
            
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
        // Sudah di halaman ini
    }

    @FXML
    private void btnLogout(ActionEvent event) {
        try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btnProduk.getScene().getWindow();
            
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
    
    // --- LOGIKA FORM ---

    // Tombol untuk MEMBUKA form input
    @FXML
    private void btnTambahKonsumsi(ActionEvent event) {
        sceneNambahKonsumsi.setVisible(true);
        // Reset form jika perlu
        txtKuantitas.clear();
        ChbKonsumsi.getSelectionModel().clearSelection();
    }

    // Tombol di dalam form: Masukkan ke Keranjang (Tabel)
    @FXML
    private void handleButtonTambah(ActionEvent event) {
        // Gunakan logika yang sama dengan LogistikDashboard
        String tipe = ChbKonsumsi.getValue();
        Konsumsi selected = chbNamaKonsum.getValue(); 

        if (tipe == null || selected == null) {
            System.out.println("Pilih tipe dan nama konsumsi dulu!");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(txtKuantitas.getText().trim());
        } catch (NumberFormatException e) {
            System.out.println("Kuantitas harus angka!");
            return;
        }

        if (qty <= 0) return;

        // Cek duplicate di keranjang
        for (DetailPembelianItem item : keranjang) {
            if (item.getIdKonsumsi() == selected.getId()) {
                item.setKuantitas(item.getKuantitas() + qty);
                tableHewan.refresh();
                txtKuantitas.clear();
                return;
            }
        }

        // Tambah baru
        keranjang.add(new DetailPembelianItem(selected, qty));
        txtKuantitas.clear();
    }
    
    // Ini mungkin mapping untuk btnTambah di FXML (Redirect ke logic di atas)
    @FXML
    private void btnTambah(ActionEvent event) {
        handleButtonTambah(event);
    }

    // Tombol Hapus Item dari Tabel
    @FXML
    private void handleButtonHapus(ActionEvent event) {
        DetailPembelianItem selectedItem = tableHewan.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            keranjang.remove(selectedItem);
        } else {
            System.out.println("Pilih item dulu!");
        }
    }
    
    @FXML
    private void btnDelate(ActionEvent event) {
        handleButtonHapus(event);
    }

    // Tombol SIMPAN TRANSAKSI KE DB
    @FXML
    private void handleButtonSimpan(ActionEvent event) {
        if (dtPickPembelian.getValue() == null) {
            System.out.println("Tanggal belum diisi!");
            return;
        }
        if (keranjang.isEmpty()) {
            System.out.println("Keranjang kosong!");
            return;
        }

        try {
            // 1. Insert Header Transaksi
            String tanggal = dtPickPembelian.getValue().toString();
            // Asumsi: ID Karyawan diambil dari Session di DAO atau pass 0 jika DAO handle session
            Pembelian transaksi = new Pembelian(tanggal, 0);
            int idPembelianBaru = pembelianDAO.insertAndGetId(transaksi);

            if (idPembelianBaru != -1) {
                // 2. Insert Detail & Update Stok
                for (DetailPembelianItem item : keranjang) {
                    DetailPembelian detail = new DetailPembelian(
                            idPembelianBaru,
                            item.getIdKonsumsi(),
                            item.getKuantitas()
                    );
                    detailPembelianDAO.insert(detail);
                    konsumsiDAO.UpdateStok(item.getIdKonsumsi(), item.getKuantitas());
                }

                // 3. Sukses & Reset
                keranjang.clear();
                tableHewan.refresh();
                dtPickPembelian.setValue(null);
                txtKuantitas.clear();
                sceneNambahKonsumsi.setVisible(false); // Tutup panel setelah simpan
                System.out.println("Transaksi Berhasil!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Gagal Database: " + e.getMessage());
        }
    }
    
    // --- HELPER NAVIGASI ---
    private void pindahScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal pindah ke: " + fxmlPath);
        }
    }

    @FXML
    private void handleBtnKembali(ActionEvent event) {
        // Tutup panel form input
        if (sceneNambahKonsumsi != null) {
            sceneNambahKonsumsi.setVisible(false);
        }
        // Bersihkan inputan
        txtKuantitas.clear();
        ChbKonsumsi.getSelectionModel().clearSelection();
    }
}
