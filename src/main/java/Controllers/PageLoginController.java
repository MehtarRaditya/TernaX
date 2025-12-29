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
    private void handleButtonLogin(ActionEvent event) throws IOException, SQLException {
    if (!btnLogin.getText().equals("LOGIN")) return;

    try {
        karyawan = karyawanDAO.checkLogin(txtUser.getText(), txtPass.getText());

        if (karyawan == null) {
            JOptionPane.showMessageDialog(null, "INVALID USERNAME/PASSWORD!!!");
            return;
        }

        // Simpan session
        Session.setLoggedInKaryawan(karyawan);

        // Ambil role dari DB
        String role = karyawan.getRole();
        String r = (role == null) ? "" : role.trim().toUpperCase();

        System.out.println("LOGIN BERHASIL! Objek karyawan: " + karyawan);
        System.out.println("Role dari DB: " + role);
        System.out.println("Sekarang mencoba memuat dashboard...");

        Stage stage = (Stage) btnLogin.getScene().getWindow();
        Parent root;

        // ===== PINDAH SCENE PAKAI IF ELSE =====
        if (r.equals("PETERNAK") || r.contains("PETERNAK")) {
            URL url = new File("src/main/java/Views/peternakKonsumsi.fxml").toURI().toURL();
            root = FXMLLoader.load(url);

        } else if (r.equals("MANAGER") || r.contains("MANAGER")) {
            URL url = new File("src/main/java/Views/managerDashboard.fxml").toURI().toURL();
            root = FXMLLoader.load(url);

        } else if (r.equals("LOGISTIK") || r.contains("LOGISTIK")) {
            URL url = new File("src/main/java/Views/LogistikDashboard.fxml").toURI().toURL();
            root = FXMLLoader.load(url);

        } else {
            JOptionPane.showMessageDialog(null, "Role tidak dikenali: " + role);
            return;
        }
        // ====================================

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    } catch (HeadlessException | IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}

}
