/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DataAccessObject.HewanDAO;
import DataAccessObject.KaryawanDAO;
import DataAccessObject.ProdukDAO;
import Models.Karyawan;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Muham
 */
public class ManagerDashboardController implements Initializable {

    @FXML
    private Label labelKaryawan;
    @FXML
    private Label labelAyam;
    @FXML
    private Label labelSapi;

    /**
     * Initializes the controller class.
     */
    private final KaryawanDAO karyawanDAO = new KaryawanDAO();
    private final HewanDAO ternakDAO = new HewanDAO();
    private final ProdukDAO produkDAO = new ProdukDAO();
    
     private ScheduledExecutorService scheduler;
    @FXML
    private Label labelSusu;
    @FXML
    private Label labelDaging;
    @FXML
    private Label labelTelur;
    @FXML
    private TableView<Karyawan> tvKaryawan;
    @FXML
    private TableColumn<Karyawan, Integer> idCol;
    @FXML
    private TableColumn<Karyawan, String> namaCol;
    @FXML
    private TableColumn<Karyawan, String> akunCol;
    @FXML
    private TableColumn<Karyawan, String> passCol;
    @FXML
    private TableColumn<Karyawan, String> roleCol;
    @FXML
    private TableColumn<Karyawan, String> gajiCol;
    @FXML
    private TableColumn<Karyawan, String> tanggalCol;
    @FXML
    private Label labelKaryawan1;
    @FXML
    private Label labelAyam1;
    @FXML
    private Label labelSapi1;
    @FXML
    private Label labelDaging1;
    @FXML
    private Label labelSusu1;
    @FXML
    private Label labelTelur1;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtAkun;
    @FXML
    private TextField txtPass;
    @FXML
    private TextField txtGaji;
    @FXML
    private DatePicker dateTanggalRekrut;
    @FXML
    private ChoiceBox<String> chcRole;
    private ObservableList<Karyawan> dataKaryawan;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        karyawanDAO = new KaryawanDAO();
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        namaCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        akunCol.setCellValueFactory(new PropertyValueFactory<>("akun"));
        passCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        gajiCol.setCellValueFactory(new PropertyValueFactory<>("gaji"));
        tanggalCol.setCellValueFactory(new PropertyValueFactory<>("tanggalDirekrut"));
        chcRole.setItems(FXCollections.observableArrayList(
            "Logistik",
            "Peternak",
            "Penjual"
    ));
    // optional: langsung pilih default
    chcRole.getSelectionModel().selectFirst();
        // Buat scheduler dengan satu thread
        scheduler = Executors.newSingleThreadScheduledExecutor();

        // Definisikan tugas yang akan dijalankan
        Runnable updateDataTask = new Runnable() {
            @Override
            public void run() {
                // Kode di dalam sini akan berjalan di background thread
                System.out.println("Mengambil data terbaru dari database...");

                int jumlahKaryawan = karyawanDAO.getJumlahKaryawan();
                int jumlahAyam = ternakDAO.getJumlahHewanByJenis("Ayam");
                int jumlahSapi = ternakDAO.getJumlahHewanByJenis("Sapi");
                int jumlahSusu = produkDAO.getJumlahProdukByJenis("Susu");
                int jumlahDaging = produkDAO.getJumlahProdukByJenis("Daging");
                int jumlahTelur = produkDAO.getJumlahProdukByJenis("Telur");

                // Perbarui UI di JavaFX Application Thread
                Platform.runLater(() -> {
                    labelKaryawan.setText(String.valueOf(jumlahKaryawan));
                    labelAyam.setText(String.valueOf(jumlahAyam));
                    labelSapi.setText(String.valueOf(jumlahSapi));
                    labelSusu.setText(String.valueOf(jumlahSusu));
                    labelDaging.setText(String.valueOf(jumlahDaging));
                    labelTelur.setText(String.valueOf(jumlahTelur));
                });
            }
        };

        // Jalankan tugas pertama kali secara langsung (delay 0 detik)
        // lalu ulangi setiap 5 detik
        scheduler.scheduleAtFixedRate(updateDataTask, 0, 5, TimeUnit.SECONDS);
        loadDataFromDatabase();
        
    }
    private void loadDataFromDatabase() {
        List<Karyawan> list = karyawanDAO.getAll();
        dataKaryawan = FXCollections.observableArrayList(list);
        tvKaryawan.setItems(dataKaryawan);
    }
    
    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            System.out.println("Mematikan scheduler dashboard...");
            scheduler.shutdown();
        }
    }
    private void clearForm() {
        txtId.clear();
        txtNama.clear();
        txtGaji.clear();
        txtPass.clear();
        txtAkun.clear();
    }

    @FXML
    private void handleButtonDelete(ActionEvent event) {
        Karyawan selectedKaryawan = tvKaryawan.getSelectionModel().getSelectedItem();
        if (selectedKaryawan != null) {
            // Hapus dari database via DAO
            karyawanDAO.deleteKaryawan(String.valueOf(selectedKaryawan.getId()));
            
            // Refresh TableView
            loadDataFromDatabase();
        } else {
            System.out.println("Pilih data yang akan dihapus!");
        }
    }

    @FXML
    private void handleButtonEdit(ActionEvent event) {
        Karyawan selectedKaryawan = tvKaryawan.getSelectionModel().getSelectedItem();
        if (selectedKaryawan != null) {
            // Ambil data dari form
            selectedKaryawan.setName(txtNama.getText());
            selectedKaryawan.setGaji(Integer.parseInt(txtGaji.getText()));
            selectedKaryawan.setAkun(txtAkun.getText());
            selectedKaryawan.setPassword(txtPass.getText());
            selectedKaryawan.setRole(chcRole.getValue());
//            selectedKaryawan.setUsia(Integer.parseInt(txtUsia.getText()));
            // NIP tidak perlu diubah karena primary key
            
            // Update di database via DAO
            karyawanDAO.updateKaryawan(selectedKaryawan);
            
            // Refresh TableView
            loadDataFromDatabase();
            clearForm();
        } else {
            System.out.println("Pilih data yang akan diupdate!");
        }
    }

    @FXML
    private void handleButtonAdd(ActionEvent event) {
        String id = txtId.getText();
        String name = txtNama.getText();
        String role = chcRole.getValue();
        String tanggalDirekrut = dateTanggalRekrut.getValue().toString();
        int gaji = Integer.parseInt(txtGaji.getText());
        String akun = txtAkun.getText();
        String password = txtPass.getText();
        if (id.isEmpty() || name.isEmpty() || role.isEmpty() || tanggalDirekrut.isEmpty()) {
            System.out.println("Yang Itu itu wajib diisi ya");
            return;
        }
        try{
            Karyawan baru = new Karyawan();
        
            // Simpan ke database via DAO
            karyawanDAO.addKaryawan(baru);
            
            // Refresh TableView
            loadDataFromDatabase();
            clearForm();
            // Kosongkan form
            //clearForm();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
