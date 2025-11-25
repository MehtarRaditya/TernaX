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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Buat scheduler dengan satu thread
        scheduler = Executors.newSingleThreadScheduledExecutor();

        // Definisikan tugas yang akan dijalankan
        Runnable updateDataTask = new Runnable() {
            @Override
            public void run() {
                // Kode di dalam sini akan berjalan di background thread
                System.out.println("Mengambil data terbaru dari database...");

                int jumlahKaryawan = karyawanDAO.getJumlahKaryawan();
                int jumlahAyam = ternakDAO.getJumlahTernakByJenis("Ayam");
                int jumlahSapi = ternakDAO.getJumlahTernakByJenis("Sapi");
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
    }
    
    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            System.out.println("Mematikan scheduler dashboard...");
            scheduler.shutdown();
        }
    }
}
