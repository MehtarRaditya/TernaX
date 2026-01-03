/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utility.DatabaseConnection;
import javafx.scene.chart.CategoryAxis;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class ManagerChartController implements Initializable {

    @FXML private LineChart<String, Number> lineChartPanen;
    @FXML private AreaChart<String, Number> areaChartPenjualan;
    @FXML private BarChart<String, Number> barChartPakan;
    @FXML private PieChart pieChartKesehatan;

    @FXML private Label lblSaranPanen;
    @FXML private Label lblSaranPenjualan;
    @FXML private Label lblSaranStok;
    @FXML private Label lblSaranKesehatan;
    
    @FXML
    private Label lblPenjualan;
    @FXML
    private Label lblPanen;
    @FXML
    private Button btnNavBar;
    @FXML
    private Button btnNavbar;
    @FXML
    private Button btnWarning;
    @FXML
    private Button btnNavBarKar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDataPanenBulanan();   // <--- Ganti jadi Bulanan
        loadDataPenjualan();
        loadDataPakan();
        loadDataPenyakit();       // <--- Ganti jadi khusus Penyakit
        
        CategoryAxis xAxis = (CategoryAxis) barChartPakan.getXAxis();
        
        // Paksa rotasi jadi 0 derajat (Horizontal)
        xAxis.setTickLabelRotation(0);
    }    
    
    private void loadDataPanenBulanan() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        
        // --- [BARU] UPDATE JUDUL LABEL SECARA DINAMIS ---
        java.time.LocalDate today = java.time.LocalDate.now();
        String namaBulan = today.getMonth().toString(); // Contoh: DECEMBER
        int tahun = today.getYear();                    // Contoh: 2025
        
        // Ubah teks Label di atas chart
        lblPanen.setText("Produksi Mingguan (" + namaBulan + " " + tahun + ")");
        
        // Nama Series
        series.setName("Total Panen (Kg/Ltr)");

        // QUERY SQL:
        // Filter Bulan Ini & Tahun Ini, Grouping per Minggu
        String sql = "SELECT CONCAT('Minggu ', FLOOR((DAY(tanggal_diperoleh) - 1) / 7) + 1) as minggu_ke, " +
                     "SUM(kuantitas) as total " +
                     "FROM produk " +
                     "WHERE status_kelayakan = 'Layak' " +
                     "AND MONTH(tanggal_diperoleh) = MONTH(CURDATE()) " +
                     "AND YEAR(tanggal_diperoleh) = YEAR(CURDATE()) " +
                     "GROUP BY minggu_ke " +
                     "ORDER BY minggu_ke ASC";
        
        double totalBulanIni = 0;
        int jumlahMinggu = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                double qty = rs.getDouble("total");
                String labelMinggu = rs.getString("minggu_ke"); 

                series.getData().add(new XYChart.Data<>(labelMinggu, qty));
                
                totalBulanIni += qty;
                jumlahMinggu++;
            }
        } catch (Exception e) { 
            System.out.println("Gagal load panen: " + e.getMessage());
        }

        lineChartPanen.getData().clear();
        lineChartPanen.getData().add(series);

        // --- UPDATE SARAN ---
        if (jumlahMinggu > 0) {
            String totalKg = String.format("%.0f Kg", totalBulanIni);
            
            // Misal target 500 Kg per bulan
            if (totalBulanIni > 500) {
                lblSaranPanen.setText("✅ Produktivitas Tinggi! Total: " + totalKg);
                lblSaranPanen.setStyle("-fx-text-fill: green; -fx-font-style: italic;");
            } else if (totalBulanIni > 100) {
                lblSaranPanen.setText("ℹ️ Stabil. Total bulan ini: " + totalKg);
                lblSaranPanen.setStyle("-fx-text-fill: #2e7d32; -fx-font-style: italic;");
            } else {
                lblSaranPanen.setText("⚠️ Rendah (" + totalKg + "). Cek kesehatan & pakan!");
                lblSaranPanen.setStyle("-fx-text-fill: red; -fx-font-style: italic;");
            }
        } else {
            lblSaranPanen.setText("ℹ️ Belum ada panen bulan ini.");
            lblSaranPanen.setStyle("-fx-text-fill: #666;");
        }
    }

    // ==========================================================
    // 2. ANALISIS PENYAKIT (Pie Chart) - Khusus Hewan Sakit
    // ==========================================================
    private void loadDataPenyakit() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        
        String sql = "SELECT penyakit, COUNT(*) as jumlah FROM hewan " +
                     "WHERE LOWER(kondisi) IN ('alive', 'hidup', 'sehat') " +
                     "GROUP BY penyakit";
        
        int totalSehat = 0;
        int totalSakit = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String penyakit = rs.getString("penyakit");
                int jumlah = rs.getInt("jumlah");
                if (penyakit == null) penyakit = "";

                if (penyakit.equalsIgnoreCase("Sehat") || penyakit.equals("-") || penyakit.trim().isEmpty()) {
                    totalSehat += jumlah;
                } else {
                    pieData.add(new PieChart.Data(penyakit, jumlah));
                    totalSakit += jumlah;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        if (totalSehat > 0) {
            pieData.add(0, new PieChart.Data("Sehat", totalSehat));
        }

        pieChartKesehatan.setData(pieData);

        // --- SARAN KESEHATAN ---
        if (totalSakit > 0) {
            lblSaranKesehatan.setText("🚑 PERHATIAN: Ada " + totalSakit + " hewan sakit.");
            lblSaranKesehatan.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            lblSaranKesehatan.setText("🌟 Zona Aman. Semua sehat.");
            lblSaranKesehatan.setStyle("-fx-text-fill: blue; -fx-font-style: italic;");
        }
    }

    // ==========================================================
    // 3. ANALISIS PENJUALAN (Omzet) - Tetap Harian/Mingguan biar detail
    // ==========================================================
    private void loadDataPenjualan() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        
        // --- [BARU] UPDATE JUDUL LABEL SECARA DINAMIS ---
        java.time.LocalDate today = java.time.LocalDate.now();
        String namaBulan = today.getMonth().toString(); // Contoh: DECEMBER
        int tahun = today.getYear();                    // Contoh: 2025
        
        // Ubah teks Label di atas chart
        lblPenjualan.setText("Omzet Mingguan (" + namaBulan + " " + tahun + ")");
        
        // Nama Legenda di bawah
        series.setName("Total Rupiah"); 

        // QUERY SQL (Tetap sama)
        String sql = "SELECT CONCAT('Minggu ', FLOOR((DAY(tanggal) - 1) / 7) + 1) as minggu_ke, " +
                     "SUM(total_harga) as omzet " +
                     "FROM penjualan " + 
                     "WHERE MONTH(tanggal) = MONTH(CURDATE()) " +
                     "AND YEAR(tanggal) = YEAR(CURDATE()) " +
                     "GROUP BY minggu_ke " +
                     "ORDER BY minggu_ke ASC";
        
        double totalOmzetBulanIni = 0;
        int jumlahMingguAdaData = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                double omzet = rs.getDouble("omzet");
                String labelMinggu = rs.getString("minggu_ke"); 

                series.getData().add(new XYChart.Data<>(labelMinggu, omzet));
                
                totalOmzetBulanIni += omzet;
                jumlahMingguAdaData++;
            }
        } catch (Exception e) { 
             System.out.println("Error Load Penjualan: " + e.getMessage());
        }

        areaChartPenjualan.getData().clear();
        areaChartPenjualan.getData().add(series);

        // --- UPDATE SARAN ---
        if (jumlahMingguAdaData > 0) {
            String omzetJuta = String.format("%.1f Jt", totalOmzetBulanIni / 1000000.0);
            
            if (totalOmzetBulanIni > 20000000) { 
                lblSaranPenjualan.setText("🚀 Luar Biasa! Bulan ini tembus Rp " + omzetJuta);
                lblSaranPenjualan.setStyle("-fx-text-fill: green; -fx-font-style: italic; -fx-font-weight: bold;");
            } else if (totalOmzetBulanIni > 5000000) {
                lblSaranPenjualan.setText("📈 On Track. Total sementara: Rp " + omzetJuta);
                lblSaranPenjualan.setStyle("-fx-text-fill: #2e7d32; -fx-font-style: italic;");
            } else {
                lblSaranPenjualan.setText("📉 Masih sepi (Rp " + omzetJuta + "). Genjot promosi!");
                lblSaranPenjualan.setStyle("-fx-text-fill: #d32f2f; -fx-font-style: italic;");
            }
        } else {
            lblSaranPenjualan.setText("ℹ️ Belum ada transaksi bulan ini.");
            lblSaranPenjualan.setStyle("-fx-text-fill: #666;");
        }
    }

    // ==========================================================
    // 4. ANALISIS STOK (Logistik)
    // ==========================================================
    private void loadDataPakan() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Stok");

        String sql = "SELECT nama_produk, total_stok FROM detail_produk";
        List<String> barangKritis = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nama = rs.getString("nama_produk");
                double stok = rs.getDouble("total_stok");
                series.getData().add(new XYChart.Data<>(nama, stok));
                if (stok < 20) barangKritis.add(nama);
            }
        } catch (Exception e) { e.printStackTrace(); }

        barChartPakan.getData().add(series);

        if (!barangKritis.isEmpty()) {
            lblSaranStok.setText("🚨 Restock: " + String.join(", ", barangKritis));
            lblSaranStok.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            lblSaranStok.setText("✅ Stok Aman.");
            lblSaranStok.setStyle("-fx-text-fill: green; -fx-font-style: italic;");
        }
    }

    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void handleToKaryawan(ActionEvent event) {
        try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btnNavBarKar.getScene().getWindow();
            
            // 2. Cari file FXML tujuan
            java.io.File file = new java.io.File("src/main/java/Views/ManagerKaryawan.fxml");
            
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
            System.out.println("Gagal pindah ke halaman Manager Karyawan");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleToPeternakan(ActionEvent event) {
        try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btnNavBar.getScene().getWindow();
            
            // 2. Cari file FXML tujuan
            java.io.File file = new java.io.File("src/main/java/Views/ManagerPeternakan.fxml");
            
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
            System.out.println("Gagal pindah ke halaman Manager Karyawan");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleToAnalisisData(ActionEvent event) {
    }

    @FXML
    private void handleToLogout(ActionEvent event) {
        try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btnWarning.getScene().getWindow();
            
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
}
