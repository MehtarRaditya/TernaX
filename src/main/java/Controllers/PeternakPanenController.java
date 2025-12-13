/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DataAccessObject.HewanDAO;
import Models.Produk;
import DataAccessObject.ProdukDAO;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class PeternakPanenController implements Initializable {

    @FXML
    private Button btHewan;
    @FXML
    private Button btnProduk;
    @FXML
    private Button btnKonsum;
    @FXML
    private Button btnLogout;
    @FXML
    private Text totalDagingSapi;
    @FXML
    private Label totalDaging;
    @FXML
    private Label totalDagingAyam;
    @FXML
    private Text totalTelur;
    @FXML
    private Label totaltelur;
    @FXML
    private Label totalSusu;
    
    @FXML
    private TableView<Produk> tvProduk;
    @FXML
    private TableColumn<Produk, String> dateColProduk;
    private TableColumn<Produk, String> hewanColProduk;
    @FXML
    private TableColumn<Produk, String> tipeColProduk;
    @FXML
    private TableColumn<Produk, Double> kuantitasColProduk;
    
    private ObservableList<Produk> dataProduk;
    private ProdukDAO produkDAO;
    private HewanDAO hewanDAO;
    @FXML
    private TableColumn<?, ?> idColProduk;
    @FXML
    private TableColumn<?, ?> idHewanCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        produkDAO = new ProdukDAO();
        hewanDAO = new HewanDAO();
        dateColProduk.setCellValueFactory(new PropertyValueFactory<>("tanggalDiperoleh"));
        tipeColProduk.setCellValueFactory(new PropertyValueFactory<>("tipe"));
        kuantitasColProduk.setCellValueFactory(new PropertyValueFactory<>("kuantitas"));
        idColProduk.setCellValueFactory(new PropertyValueFactory<>("id"));
        idHewanCol.setCellValueFactory(new PropertyValueFactory<>("idHewan"));
        
        loadDataFromDatabaseProduk();
        
        
    }

    private void loadDataFromDatabaseProduk() {
        List<Produk> list = produkDAO.getAll();
        dataProduk = FXCollections.observableArrayList(list);
        tvProduk.setItems(dataProduk);
        
        double hitungDagingSapi = 0;
        double hitungDagingAyam = 0;
        double hitungTelur = 0;
        double hitungSusu = 0;
        
        for (Produk p : list) {
            String tipe = p.getTipe(); 
            double qty = p.getKuantitas();
            int idHewan = p.getIdHewan(); // Ambil ID Hewan dari produk
            
            if (tipe != null) {
                if (tipe.equalsIgnoreCase("Daging")) {
                    // CEK JENIS HEWANNYA APA?
                    String jenisHewan = hewanDAO.getJenisById(idHewan);
                    
                    if (jenisHewan.equalsIgnoreCase("Sapi")) {
                        hitungDagingSapi += qty;
                    } else if (jenisHewan.equalsIgnoreCase("Ayam")) {
                        hitungDagingAyam += qty;
                    }
                    
                } else if (tipe.equalsIgnoreCase("Telur")) {
                    hitungTelur += qty;
                } else if (tipe.equalsIgnoreCase("Susu")) {
                    hitungSusu += qty;
                }
            }
        }
        
        // --- TAMPILKAN KE LABEL ---
        
        
        // Total Daging Sapi Saja
        if(totalDaging != null) { // Pastikan fx:id label ini ada
            totalDaging.setText(String.valueOf(hitungDagingSapi+ " Kg"));
        }
        
        // Total Daging Ayam Saja
        if(totalDagingAyam != null) { // Pastikan fx:id label ini ada
            totalDagingAyam.setText(String.valueOf(hitungDagingAyam) + " Kg");
        }

        if(totaltelur != null) totaltelur.setText(String.valueOf(hitungTelur + " Butir"));
        if(totalSusu != null) totalSusu.setText(String.valueOf(hitungSusu + " Liter"));
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
        loadDataFromDatabaseProduk();
    }

    @FXML
    private void btnKonsum(ActionEvent event) {
        pindahScene(event, "/Views/pageLogin.fxml");
    }

    @FXML
    private void btnLogout(ActionEvent event) {
    }
    
    private void pindahScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Gagal pindah ke: " + fxmlPath);
        }
    }
    
}
