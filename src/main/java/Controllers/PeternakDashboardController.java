/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DataAccessObject.HewanDAO;
import DataAccessObject.KaryawanDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import Models.Hewan;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        hewanDAO = new HewanDAO();
        
        // Setup kolom TableView
        JenisCol.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        BeratCol.setCellValueFactory(new PropertyValueFactory<>("berat"));
        UsiaCol.setCellValueFactory(new PropertyValueFactory<>("usia"));
        KelaminCol.setCellValueFactory(new PropertyValueFactory<>("kelamin"));
        KondisiCol.setCellValueFactory(new PropertyValueFactory<>("kondisi"));
        PenyakitCol.setCellValueFactory(new PropertyValueFactory<>("penyakit"));
        
        // Load data dari database saat aplikasi dimulai
        loadDataFromDatabase();
        
    }
    private void loadDataFromDatabase() {
        List<Hewan> list = hewanDAO.getAll();
        dataHewan = FXCollections.observableArrayList(list);
        tvHewan.setItems(dataHewan);
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
    }

    @FXML
    private void handleActionEdit(ActionEvent event) {
    }
    
}
