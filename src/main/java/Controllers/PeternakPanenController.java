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
import javafx.scene.control.TableCell;
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
        // hewanDAO = new HewanDAO(); // Gak perlu lagi
        
        // 1. SETUP KOLOM TABEL (Sesuaikan dengan Getter di Model Produk.java)
        idColProduk.setCellValueFactory(new PropertyValueFactory<>("id"));
        idHewanCol.setCellValueFactory(new PropertyValueFactory<>("idHewan"));
        dateColProduk.setCellValueFactory(new PropertyValueFactory<>("tanggalDiperoleh")); // Ganti 'tanggalDiperoleh' jadi 'tanggalPanen'
        
        // PENTING: tipeColProduk sekarang menampilkan 'namaProduk' dari hasil JOIN
        tipeColProduk.setCellValueFactory(new PropertyValueFactory<>("namaProduk")); 
        
        kuantitasColProduk.setCellValueFactory(new PropertyValueFactory<>("kuantitas"));
        
        // 2. SETUP FORMATTING CELL (Untuk Satuan Kg/Liter/Butir)
        setupKuantitasCellFactory();
        
        // 3. LOAD DATA
        loadDataFromDatabaseProduk();
    }
    
    private void setupKuantitasCellFactory() {
        kuantitasColProduk.setCellFactory(column -> new TableCell<Produk, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    // Ambil baris data saat ini
                    Produk p = getTableView().getItems().get(getIndex());
                    String satuan = p.getSatuan(); // Pastikan getter getSatuan() ada di Model
                    
                    // Format angka: Jika "Butir" hilangkan koma, selain itu biarkan
                    if (satuan != null && satuan.equalsIgnoreCase("Kg")) {
                         setText(String.format("%.1f Kg", item)); // 10.5 Kg
                    } else if (satuan != null && satuan.equalsIgnoreCase("Liter")) {
                         setText(String.format("%.1f Liter", item));
                    } else {
                         setText(item.intValue() + " " + satuan); // 10 Butir
                    }
                }
            }
        });
    }

    private void loadDataFromDatabaseProduk() {
        // Ambil data yang sudah di-JOIN (ada namaProduk & satuan)
        List<Produk> list = produkDAO.getAll(); 
        dataProduk = FXCollections.observableArrayList(list);
        tvProduk.setItems(dataProduk);

        // --- LOGIKA PERHITUNGAN TOTAL ---
        double hitungDagingSapi = 0;
        double hitungDagingAyam = 0;
        double hitungTelur = 0;
        double hitungSusu = 0;

        for (Produk p : list) {
            // Ambil Nama Produk (Contoh: "Daging Sapi Premium", "Telur Ayam")
            String nama = p.getNamaProduk().toLowerCase(); 
            double qty = p.getKuantitas();

            if (nama.contains("sapi")) {
                if (nama.contains("daging")) {
                    hitungDagingSapi += qty;
                } else if (nama.contains("susu")) {
                    hitungSusu += qty;
                }
            } 
            else if (nama.contains("ayam")) {
                if (nama.contains("daging")) {
                    hitungDagingAyam += qty;
                } else if (nama.contains("telur")) {
                    hitungTelur += qty;
                }
            }
        }

        // --- UPDATE LABEL TAMPILAN ---
        if(totalDaging != null) totalDaging.setText(String.format("%.1f Kg", hitungDagingSapi));
        if(totalDagingAyam != null) totalDagingAyam.setText(String.format("%.1f Kg", hitungDagingAyam));
        if(totaltelur != null) totaltelur.setText((int)hitungTelur + " Butir");
        if(totalSusu != null) totalSusu.setText(String.format("%.1f Liter", hitungSusu));
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
        try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btnKonsum.getScene().getWindow();
            
            // 2. Cari file FXML tujuan
            java.io.File file = new java.io.File("src/main/java/Views/peternakKonsumsi.fxml");
            
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
