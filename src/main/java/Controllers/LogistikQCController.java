package Controllers;

import DataAccessObject.ProdukDAO;
import Models.Produk; // Pastikan pakai Model Produk yang kamu punya
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LogistikQCController implements Initializable {

    // --- DAO & DATA ---
    private final ProdukDAO produkDAO = new ProdukDAO();
    private ObservableList<Produk> listPending = FXCollections.observableArrayList();
    private Produk selectedProduk; // Untuk menyimpan produk yang sedang diklik

    // --- FXML COMPONENTS ---
    @FXML private Button btnKonsum;
    @FXML private Button btnLogout;
    @FXML private Button btnQC;
    
    // TABEL
    @FXML private TableView<Produk> tableHewan; // Pastikan fx:id di FXML adalah tableHewan (atau sesuaikan)
    @FXML private TableColumn<Produk, String> colNamaProduk;
    @FXML private TableColumn<Produk, String> colTanggal;
    @FXML private TableColumn<Produk, Double> colKuantitas; // Asumsi di Model Produk tipe double
    @FXML private TableColumn<Produk, String> colStatus;

    @FXML private TextField txtSelectedProduk;
    @FXML private ToggleGroup groupStatus;
    @FXML private RadioButton rbLayak;
    @FXML private RadioButton rbTidakLayak;
    @FXML private Button btnSimpanStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("=== START LOGISTIK QC ===");
        
        // 1. Setup Kolom Tabel (Pakai Lambda biar aman dari Null Pointer)
        if (colNamaProduk != null) 
            colNamaProduk.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaProduk()));
        
        if (colTanggal != null) 
            colTanggal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTanggalDiperoleh()));
        
        if (colKuantitas != null) 
            colKuantitas.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getKuantitas()).asObject());
        
        if (colStatus != null) 
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatusKelayakan()));

        // 2. Load Data Pending
        loadDataPending();
        if (tableHewan != null) {
            tableHewan.setItems(listPending);
            
            // 3. Listener: Pas Tabel diklik, isi form di samping/bawah
            tableHewan.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    selectedProduk = newVal;
                    txtSelectedProduk.setText(newVal.getNamaProduk() + " (" + newVal.getKuantitas() + ")");
                    // Reset Radio Button
                    if (groupStatus != null) groupStatus.selectToggle(null);
                }
            });
        }
    }

    private void loadDataPending() {
        listPending.clear();
        // Panggil method baru di DAO
        listPending.addAll(produkDAO.getPendingList());
    }

    private void handleButtonSimpan(ActionEvent event) {
        // 1. Validasi
        if (selectedProduk == null) {
            showAlert("Peringatan", "Pilih produk di tabel dulu!");
            return;
        }

        RadioButton selectedRadio = (RadioButton) groupStatus.getSelectedToggle();
        if (selectedRadio == null) {
            showAlert("Peringatan", "Pilih status Layak atau Tidak Layak!");
            return;
        }

        // 2. Tentukan Status Baru
        String statusBaru = (selectedRadio == rbLayak) ? "Layak" : "Tidak Layak";

        // 3. Update Database
        boolean sukses = produkDAO.updateStatusQC(selectedProduk.getId(), statusBaru);

        if (sukses) {
            showAlert("Sukses", "Status berhasil diubah menjadi: " + statusBaru);
            
            // 4. Refresh Tabel (Data yang sudah QC akan hilang dari list Pending)
            loadDataPending();
            tableHewan.refresh();
            
            // Reset Form
            txtSelectedProduk.clear();
            groupStatus.selectToggle(null);
            selectedProduk = null;
        } else {
            showAlert("Gagal", "Database Error saat update status.");
        }
    }

    // --- NAVIGASI ---
    @FXML 
    private void handleToKonsum(ActionEvent event) { 
    try {
            
            javafx.stage.Stage stage = (javafx.stage.Stage) btnKonsum.getScene().getWindow();
            
            // 2. Cari file FXML tujuan
            java.io.File file = new java.io.File("src/main/java/Views/LogistikDashboard.fxml");
            
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
            System.out.println("Gagal Pindah ke Tampilan Logistik Dashboard!");
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Method dummy jika ada di FXML tapi tidak terpakai
    @FXML private void handleToSimpanStatus(ActionEvent event) { handleButtonSimpan(event); }

    @FXML
    private void handleToQC(ActionEvent event) {
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
}