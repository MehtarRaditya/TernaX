/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DataAccessObject.HewanDAO;
import DataAccessObject.KaryawanDAO;
import DataAccessObject.ProdukDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import Models.Hewan;
import Models.Produk;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Muham
 */
public class PeternakDashboardController implements Initializable {

    @FXML
    private TextField txtJenis;
    @FXML
    private TextField txtBerat;
    @FXML
    private TextField txtUsia;
    @FXML
    private TextField txtKelamin;
    @FXML
    private TextField txtKondisi;
    @FXML
    private TextField txtPenyakit;
    @FXML
    private TableView<Hewan> tvHewan;
    @FXML
    private TableColumn<Hewan, String> JenisCol;
    @FXML
    private TableColumn<Hewan, Double> BeratCol;
    @FXML
    private TableColumn<Hewan, Integer> UsiaCol;
    @FXML
    private TableColumn<Hewan, String> KelaminCol;
    @FXML
    private TableColumn<Hewan, String> KondisiCol;
    @FXML
    private TableColumn<Hewan, String> PenyakitCol;
    
    private HewanDAO hewanDAO;
    private ObservableList<Hewan> dataHewan;
    private ObservableList<Produk> dataProduk;
    @FXML
    private TableView<Produk> tvProduk;
    @FXML
    private TableColumn<Produk, String> dateColProduk;
    @FXML
    private TableColumn<Produk, String> hewanColProduk;
    @FXML
    private TableColumn<Produk, String> tipeColProduk;
    @FXML
    private TableColumn<Produk, String> kuantitasColProduk;
    @FXML
    private TextField txtKuantitas;
    @FXML
    private DatePicker txtDateProduk;
    @FXML
    private ChoiceBox<String> chcTipe;
    
    private ObservableList<String> tipeAyam = FXCollections.observableArrayList("Daging", "Telur");
    private ObservableList<String> tipeSapi = FXCollections.observableArrayList("Daging", "Susu");

    private ProdukDAO produkDAO;   // tambahin DAO produk
    @FXML
    private TableColumn<Produk, Integer> idColProduk;
    @FXML
    private TableColumn<Hewan, Integer> idHewanCol;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        hewanDAO = new HewanDAO();
        produkDAO = new ProdukDAO(); 
        
        // Setup kolom TableView
        JenisCol.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        BeratCol.setCellValueFactory(new PropertyValueFactory<>("berat"));
        UsiaCol.setCellValueFactory(new PropertyValueFactory<>("usia"));
        KelaminCol.setCellValueFactory(new PropertyValueFactory<>("kelamin"));
        KondisiCol.setCellValueFactory(new PropertyValueFactory<>("kondisi"));
        PenyakitCol.setCellValueFactory(new PropertyValueFactory<>("penyakit"));
        dateColProduk.setCellValueFactory(new PropertyValueFactory<>("tanggalDiperoleh"));
        hewanColProduk.setCellValueFactory(new PropertyValueFactory<>("idHewan"));
        tipeColProduk.setCellValueFactory(new PropertyValueFactory<>("tipe"));
        kuantitasColProduk.setCellValueFactory(new PropertyValueFactory<>("kuantitas"));
        idColProduk.setCellValueFactory(new PropertyValueFactory<>("id"));
        idHewanCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        
        
        // Load data dari database saat aplikasi dimulai
        loadDataFromDatabase();
        loadDataFromDatabaseProduk();
        tvHewan.getSelectionModel().selectedItemProperty().addListener((obs, oldHewan, newHewan) -> {
        if (newHewan == null) {
            chcTipe.getItems().clear();
            chcTipe.setValue(null);
            return;
        }

        String jenis = newHewan.getJenis(); // "Ayam" / "Sapi" dst

        if (jenis.equalsIgnoreCase("Ayam")) {
            chcTipe.setItems(tipeAyam);   // [Daging, Telur]
        } else if (jenis.equalsIgnoreCase("Sapi")) {
            chcTipe.setItems(tipeSapi);   // [Daging, Susu]
        } else {
            chcTipe.getItems().clear();
        }

        if (!chcTipe.getItems().isEmpty()) {
            chcTipe.setValue(chcTipe.getItems().get(0)); // auto pilih pertama
        }
    });}
    
    private void loadDataFromDatabase() {
        List<Hewan> list = hewanDAO.getAll();
        dataHewan = FXCollections.observableArrayList(list);
        tvHewan.setItems(dataHewan);
    }
    
    private void loadDataFromDatabaseProduk() {
        List<Produk> list = produkDAO.getAllRiwayat();
        dataProduk = FXCollections.observableArrayList(list);
        tvProduk.setItems(dataProduk);
    }
    
    private void clearForm() {
        txtJenis.clear();
        txtBerat.clear();
        txtUsia.clear();
        txtKelamin.clear();
        txtKondisi.clear();
        txtPenyakit.clear();
    }

    @FXML
    private void handleActionTambah(ActionEvent event) {
        String jenis = txtJenis.getText();
        String beratStr = txtBerat.getText();
        String usiaStr = txtUsia.getText();
        String kelamin = txtKelamin.getText();
        String kondisi = txtKondisi.getText();
        String penyakit = txtPenyakit.getText();
        
        if (jenis.isEmpty() || beratStr.isEmpty() || kondisi.isEmpty() || penyakit.isEmpty()) {
            System.out.println("Yang Itu itu wajib diisi ya");
            return;
        }
        try{
            
            double berat = Double.parseDouble(beratStr);
            int usia = Integer.parseInt(usiaStr);
        
            Hewan baru = new Hewan(jenis, kelamin, berat, usia, kondisi, penyakit);
        
            // Simpan ke database via DAO
            hewanDAO.add(baru);
        
            // Refresh TableView
            loadDataFromDatabase();
        
            // Kosongkan form
            clearForm();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }

    @FXML
    private void handleActionHapus(ActionEvent event) {
        Hewan selectedHewan = tvHewan.getSelectionModel().getSelectedItem();
        if (selectedHewan != null) {
            // Hapus dari database via DAO
            hewanDAO.delete(selectedHewan.getId());
            
            // Refresh TableView
            loadDataFromDatabase();
        } else {
            System.out.println("Pilih data yang akan dihapus!");
        }
    }
    
    // Method untuk membersihkan input form

    @FXML
    private void handleActionEdit(ActionEvent event) {
        Hewan selectedHewan = tvHewan.getSelectionModel().getSelectedItem();
        if (selectedHewan != null) {
            // Ambil data dari form
            selectedHewan.setJenis(txtJenis.getText());
            selectedHewan.setBerat(Double.parseDouble(txtBerat.getText()));
            selectedHewan.setKelamin(txtKelamin.getText());
            selectedHewan.setKondisi(txtKondisi.getText());
            selectedHewan.setPenyakit(txtPenyakit.getText());
            selectedHewan.setUsia(Integer.parseInt(txtUsia.getText()));
            // NIP tidak perlu diubah karena primary key
            
            // Update di database via DAO
            hewanDAO.update(selectedHewan);
            
            // Refresh TableView
            loadDataFromDatabase();
            clearForm();
        } else {
            System.out.println("Pilih data yang akan diupdate!");
        }
    }

    @FXML
    private void handleSubmitProduk(ActionEvent event) {
        loadDataFromDatabaseProduk();
        Hewan selectedHewan = tvHewan.getSelectionModel().getSelectedItem();
        if (selectedHewan == null) {
        System.out.println("Pilih dulu hewan di tabel!");
        return;
    }
        int id_hewan = selectedHewan.getId();
        String kuantitas = txtKuantitas.getText();
        String tanggal = txtDateProduk.getValue().toString();
        String tipe = chcTipe.getValue();
        double kuantitass = Double.parseDouble(kuantitas);
        String kualitas = "Belum diisi";   
        
        try{
        Produk baru = new Produk(tanggal,tipe,kuantitass,kualitas,id_hewan);
        produkDAO.addProduk(baru);
        System.out.println("Tipe produk: " + tipe);
        if("daging".equalsIgnoreCase(tipe)){
            hewanDAO.updateKondisi(id_hewan, "Death");
            selectedHewan.setKondisi("Death");
            loadDataFromDatabase();
        }
        loadDataFromDatabaseProduk();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    
    
}
