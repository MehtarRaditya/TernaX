/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DataAccessObject.KaryawanDAO;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import Models.Karyawan;
import javafx.scene.control.Alert;
import utility.Session;

/**
 * FXML Controller class
 *
 * @author Muham
 */
public class PageLoginController implements Initializable {
    
    public static Karyawan karyawan;
    @FXML
    private TextField txtUser;
    @FXML
    private TextField txtPass;
    //mengambil class karyawanDAO di package DAO
    private KaryawanDAO karyawanDAO;
    @FXML
    private Button btnLogin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        karyawan = null;
        karyawanDAO = new KaryawanDAO();
        btnLogin.setText("LOGIN");
    }    

   
    @FXML 
    private void handleButtonLogin(ActionEvent event) {
        if (btnLogin.getText().equals("LOGIN")) {
            // 1. Validasi Input Kosong
            if (txtUser.getText().isEmpty() || txtPass.getText().isEmpty()) {
                showAlert("Error", "Username dan Password tidak boleh kosong!");
                return;
            }

            try {
                // 2. Cek Login ke Database
                karyawan = karyawanDAO.checkLogin(txtUser.getText(), txtPass.getText());

                if (karyawan != null) {
                    // --- LOGIN BERHASIL ---
                    
                    // Simpan sesi user yang login
                    Session.setLoggedInKaryawan(karyawan);
                    
                    String role = karyawan.getRole();
                    String fxmlFile = "";

                    // 3. LOGIKA PEMBAGIAN ROLE (ROUTING)
                    if (role != null) {
                        switch (role.toLowerCase()) {
                            case "manager":
                                fxmlFile = "/Views/ManagerKaryawan.fxml";
                                break;
                            case "peternak":
                                fxmlFile = "/Views/peternakHewan.fxml";
                                break;
                            case "logistik":
                                fxmlFile = "/Views/LogistikDashboard.fxml";
                                break;
                            case "kasir":
                            case "penjual":
                                fxmlFile = "/Views/PenjualanDashboard.fxml"; 
                                break;
                            default:
                                showAlert("Error", "Role tidak dikenali: " + role);
                                return;
                        }
                    }

                    System.out.println("Login Berhasil! Role: " + role + " -> Ke: " + fxmlFile);

                    // 4. PROSES PINDAH HALAMAN
                    // Gunakan getClass().getResource() agar aman
                    URL url = getClass().getResource(fxmlFile);
                    
                    if (url == null) {
                        // Fallback manual jika getResource gagal
                        File file = new File("src/main/java" + fxmlFile);
                        if (file.exists()) {
                            url = file.toURI().toURL();
                        } else {
                            throw new IOException("File FXML tidak ditemukan: " + fxmlFile);
                        }
                    }

                    Parent root = FXMLLoader.load(url);
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.centerOnScreen(); 
                    stage.show();

                } else {
                    // --- LOGIN GAGAL ---
                    showAlert("Gagal", "Username atau Password salah!");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error Database", "Gagal terhubung ke database: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error File", "Gagal memuat halaman dashboard: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Terjadi kesalahan tak terduga.");
            }
        }
    }

    // Helper method untuk Alert JavaFX
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
