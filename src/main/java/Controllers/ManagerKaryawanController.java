package Controllers;

import DataAccessObject.KaryawanDAO;
import Models.Karyawan;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ManagerKaryawanController implements Initializable {

    // --- Components UI ---
    @FXML private Button btnNavBar;
    @FXML private AnchorPane editDataKaryawan;
    @FXML private AnchorPane inputDataKaryawan;
    @FXML private AnchorPane tampilanTabKaryawan;

    // --- Statistik (Label Angka) ---
    @FXML private Text totalKaryawan;
    @FXML private Text totalLogistik;
    @FXML private Text totalPenjual;
    @FXML private Text totalPeternak;

    // --- Navbar ---
    @FXML private Button btnNavbar;
    @FXML private Button btnWarning;
    @FXML private Button btnGrey;
    @FXML private Button btnGreen;
    @FXML private Text Hallo_Manager;

    // --- Tabel (Karyawan) ---
    @FXML private TableView<Karyawan> tableKaryawan;
    @FXML private TableColumn<Karyawan, String> colID;
    @FXML private TableColumn<Karyawan, String> colNama;
    @FXML private TableColumn<Karyawan, String> colAkun;
    @FXML private TableColumn<Karyawan, String> colPassword;
    @FXML private TableColumn<Karyawan, String> colRole;
    @FXML private TableColumn<Karyawan, Integer> colGaji;
    @FXML private TableColumn<Karyawan, String> colTanggalRekrut;

    // --- Form Edit ---
    @FXML private TextArea txtNamaEdit;
    @FXML private TextArea txtAkunEdit;
    @FXML private TextArea txtPasswordEdit;
    @FXML private ChoiceBox<String> kelaminHewanEdit; // Ini ChoiceBox Role Edit
    @FXML private TextArea txtGajiEdit;
    @FXML private DatePicker tanggalRekrutEdit;
    @FXML private Button btnSimpanEdit;
    @FXML private Button btnTutupEdit;

    // --- Form Add ---
    @FXML private TextArea txtNamaAdd;
    @FXML private TextArea txtAkunAdd;
    @FXML private TextArea txtPasswordAdd;
    @FXML private ChoiceBox<String> roleAdd;
    @FXML private TextArea txtGajiAdd;
    @FXML private DatePicker dateTglRekrutAdd;
    @FXML private Button btnKirimAdd;
    @FXML private Button btnTutupAdd;

    // --- DATA & DAO ---
    private KaryawanDAO karyawanDAO;
    private ObservableList<Karyawan> listKaryawan;
    private Karyawan selectedKaryawan; 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        karyawanDAO = new KaryawanDAO();
        listKaryawan = FXCollections.observableArrayList();

        // 1. Setup Kolom Tabel
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAkun.setCellValueFactory(new PropertyValueFactory<>("akun"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colGaji.setCellValueFactory(new PropertyValueFactory<>("gaji"));
        colTanggalRekrut.setCellValueFactory(new PropertyValueFactory<>("tanggalDirekrut"));

        // 2. Isi Pilihan Role
        ObservableList<String> roles = FXCollections.observableArrayList("Manager", "Peternak", "Logistik", "Kasir");
        roleAdd.setItems(roles);
        if (kelaminHewanEdit != null) {
            kelaminHewanEdit.setItems(roles);
        }

        // 3. Sembunyikan Panel saat awal
        if (inputDataKaryawan != null) inputDataKaryawan.setVisible(false);
        if (editDataKaryawan != null) editDataKaryawan.setVisible(false);

        // 4. Load Data Awal & Hitung Statistik
        refreshTable();
    }

    private void refreshTable() {
        listKaryawan.clear();
        listKaryawan.addAll(karyawanDAO.getAll());
        tableKaryawan.setItems(listKaryawan);
        
        // Update Statistik setiap kali tabel direfresh
        updateStatistik();
    }
    
    // --- METHOD PERBAIKAN STATISTIK ---
    private void updateStatistik() {
        if (listKaryawan == null) return;

        // Hitung manual dari list yang sudah diambil dari database
        int countPeternak = 0;
        int countLogistik = 0;
        int countKasir = 0; // Kasir = Penjual

        for (Karyawan k : listKaryawan) {
            String role = k.getRole();
            if (role != null) {
                if (role.equalsIgnoreCase("Peternak")) countPeternak++;
                else if (role.equalsIgnoreCase("Logistik")) countLogistik++;
                else if (role.equalsIgnoreCase("Kasir") || role.equalsIgnoreCase("Penjual")) countKasir++;
            }
        }

        // Set Text ke Label UI
        if (totalKaryawan != null) totalKaryawan.setText(String.valueOf(listKaryawan.size()));
        if (totalPeternak != null) totalPeternak.setText(String.valueOf(countPeternak));
        if (totalLogistik != null) totalLogistik.setText(String.valueOf(countLogistik));
        if (totalPenjual != null) totalPenjual.setText(String.valueOf(countKasir));
    }

    // ================= ACTION BUTTONS =================

    // --- 1. TAMBAH DATA ---
    @FXML
    private void handleActionTambahKaryawan(ActionEvent event) {
        inputDataKaryawan.setVisible(true);
        editDataKaryawan.setVisible(false);
        clearFormAdd();
    }

    @FXML
    private void btnKirimAdd(ActionEvent event) {
        try {
            String nama = txtNamaAdd.getText();
            String user = txtAkunAdd.getText();
            String pass = txtPasswordAdd.getText();
            String role = roleAdd.getValue();
            String gajiStr = txtGajiAdd.getText();
            LocalDate tgl = dateTglRekrutAdd.getValue();

            if (nama.isEmpty() || user.isEmpty() || role == null || gajiStr.isEmpty() || tgl == null) {
                showAlert("Error", "Semua field harus diisi!");
                return;
            }

            Karyawan k = new Karyawan();
            
            // --- PERBAIKAN UTAMA: GENERATE ID OTOMATIS ---
            // Kita pakai waktu sekarang biar unik. Ambil 6 digit terakhir saja biar ga kepanjangan.
            // Contoh hasil ID: "882910"
            String idUnik = String.valueOf(System.currentTimeMillis()).substring(7);
            k.setId(idUnik);
            // ----------------------------------------------

            k.setName(nama);
            k.setAkun(user);
            k.setPassword(pass);
            k.setRole(role);
            k.setGaji(Integer.parseInt(gajiStr));
            k.setTanggalDirekrut(tgl.toString());

            if (karyawanDAO.addKaryawan(k)) {
                showAlert("Sukses", "Karyawan berhasil ditambahkan!");
                refreshTable(); 
                inputDataKaryawan.setVisible(false);
            } else {
                showAlert("Gagal", "Gagal menyimpan ke database.");
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "Gaji harus berupa angka!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Terjadi kesalahan: " + e.getMessage());
        }
    }

    // --- 2. EDIT DATA ---
    @FXML
    private void handleActionEditKaryawan(ActionEvent event) {
        selectedKaryawan = tableKaryawan.getSelectionModel().getSelectedItem();
        if (selectedKaryawan == null) {
            showAlert("Warning", "Pilih karyawan di tabel dulu!");
            return;
        }

        editDataKaryawan.setVisible(true);
        inputDataKaryawan.setVisible(false);

        // Isi form dengan data lama
        txtNamaEdit.setText(selectedKaryawan.getName());
        txtAkunEdit.setText(selectedKaryawan.getAkun());
        txtPasswordEdit.setText(selectedKaryawan.getPassword());
        kelaminHewanEdit.setValue(selectedKaryawan.getRole());
        txtGajiEdit.setText(String.valueOf(selectedKaryawan.getGaji()));
        
        if (selectedKaryawan.getTanggalDirekrut() != null && !selectedKaryawan.getTanggalDirekrut().isEmpty()) {
            try {
                tanggalRekrutEdit.setValue(LocalDate.parse(selectedKaryawan.getTanggalDirekrut()));
            } catch (Exception e) {}
        }
    }

    @FXML
    private void btnSaveEdit(ActionEvent event) {
        if (selectedKaryawan == null) return;

        try {
            selectedKaryawan.setName(txtNamaEdit.getText());
            selectedKaryawan.setAkun(txtAkunEdit.getText());
            selectedKaryawan.setPassword(txtPasswordEdit.getText());
            selectedKaryawan.setRole(kelaminHewanEdit.getValue());
            selectedKaryawan.setGaji(Integer.parseInt(txtGajiEdit.getText()));
            
            if (tanggalRekrutEdit.getValue() != null) {
                selectedKaryawan.setTanggalDirekrut(tanggalRekrutEdit.getValue().toString());
            }

            if (karyawanDAO.updateKaryawan(selectedKaryawan)) {
                showAlert("Sukses", "Data karyawan berhasil diupdate!");
                refreshTable(); // Statistik otomatis terupdate
                editDataKaryawan.setVisible(false);
            } else {
                showAlert("Gagal", "Gagal update database.");
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "Gaji harus berupa angka bulat!");
        }
    }
    
    // PERBAIKAN: Method ini harus ada @FXML agar tidak error LoadException
    @FXML
    private void btnKirimEdit(ActionEvent event) {
        btnSaveEdit(event);
    }

    // --- 3. HAPUS DATA ---
    @FXML
    private void handleActionDeleteKaryawan(ActionEvent event) {
        Karyawan selected = tableKaryawan.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Pilih karyawan yang mau dihapus!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Yakin hapus " + selected.getName() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            if (karyawanDAO.deleteKaryawan(selected.getId())) {
                showAlert("Sukses", "Karyawan dihapus.");
                refreshTable(); // Statistik otomatis terupdate
            }
        }
    }

    // ================= HELPER & NAVIGATION =================

    @FXML
    void btnClose(ActionEvent event) {
        inputDataKaryawan.setVisible(false);
        editDataKaryawan.setVisible(false);
    }
    
    // PERBAIKAN: Pastikan ini ada @FXML
    @FXML 
    void btnCloseEdit(ActionEvent event) { 
        editDataKaryawan.setVisible(false); 
    }
    
    // PERBAIKAN: Pastikan ini ada @FXML (mungkin nama tombolnya btnTutupAdd di FXML)
    @FXML
    private void btnCloseAdd(ActionEvent event) {
        inputDataKaryawan.setVisible(false);
    }
    
    // Jika tombolnya bernama btnTutupAdd di FXML:
    @FXML
    private void btnTutupAdd(ActionEvent event) {
        inputDataKaryawan.setVisible(false);
    }

    private void clearFormAdd() {
        txtNamaAdd.clear();
        txtAkunAdd.clear();
        txtPasswordAdd.clear();
        txtGajiAdd.clear();
        roleAdd.setValue(null);
        dateTglRekrutAdd.setValue(LocalDate.now());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void pindahHalaman(ActionEvent event, String fxmlFile) {
        try {
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            java.net.URL url = getClass().getResource("/Views/" + fxmlFile); 
            
            if (url == null) {
                java.io.File file = new java.io.File("src/main/java/Views/" + fxmlFile);
                url = file.toURI().toURL();
            }

            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(url);
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            System.out.println("Gagal pindah ke " + fxmlFile);
            e.printStackTrace();
        }
    }

    @FXML private void btnLogout(ActionEvent event) { pindahHalaman(event, "Login.fxml"); }
    @FXML private void handleToPeternakanManager(ActionEvent event) { pindahHalaman(event, "ManagerPeternakan.fxml"); }
    @FXML private void handleToDashboardManager(ActionEvent event) { pindahHalaman(event, "ManagerChart.fxml"); }

    // PERBAIKAN: Method ini harus ada @FXML agar tombol btnKaryawan di FXML tidak error
    @FXML
    private void btnKaryawan(ActionEvent event) {
        refreshTable();
    }
}