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
//    private void handleButtonLogin(ActionEvent event) { // HAPUS "throws" clause
//    if (btnLogin.getText().equals("LOGIN")) {
//        try {
//            // Cek login ke database
//            karyawan = karyawanDAO.checkLogin(txtUser.getText(), txtPass.getText());
//            
//            if (karyawan != null) {
//                System.out.println("Login berhasil untuk: " + karyawan.getAkun()); // Asumsi ada getUsername()
//                
//                // Muat tampilan dashboard
//                Parent root = FXMLLoader.load(getClass().getResource("/Views/peternakHewan.fxml"));
//                
//                // Ganti scene
//                Stage stage = (Stage) btnLogin.getScene().getWindow();
//                Scene scene = new Scene(root);
//                stage.setScene(scene);
//                stage.show(); // TAMBAHKAN INI
//                
//            } else {
//                // Tampilkan pesan error menggunakan Alert JavaFX
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Login Gagal");
//                alert.setHeaderText(null);
//                alert.setContentText("Username atau Password salah!");
//                alert.showAndWait();
//            }
//            
//        } catch (IOException e) {
//            // Tangani error jika file FXML tidak ditemukan
//            System.err.println("Error IO: " + e.getMessage());
//            e.printStackTrace();
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Kesalahan Aplikasi");
//            alert.setHeaderText("File Tidak Ditemukan");
//            alert.setContentText("Tidak dapat memuat halaman dashboard.");
//            alert.showAndWait();
//            
//        } catch (SQLException e) {
//            // Tangani error dari database
//            System.err.println("Error Database: " + e.getMessage());
//            e.printStackTrace();
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Kesalahan Database");
//            alert.setHeaderText("Koneksi Gagal");
//            alert.setContentText("Tidak dapat terhubung ke database.");
//            alert.showAndWait();
//            
//        } catch (Exception e) {
//            // Tangani error lainnya
//            System.err.println("Error tak terduga: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}
    private void handleButtonLogin(ActionEvent event) throws IOException, SQLException{
        if(btnLogin.getText().equals("LOGIN")){
            try{
                karyawan = karyawanDAO.checkLogin(txtUser.getText(), txtPass.getText());
                if(karyawan != null){
                    Session.setLoggedInKaryawan(karyawan);
                    System.out.println("LOGIN BERHASIL! Objek karyawan: " + karyawan);
                    System.out.println("Sekarang mencoba memuat dashboard...");
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    URL url = new File("src/main/java/Views/peternakHewan.fxml").toURI().toURL();
                    Parent root = FXMLLoader.load(url);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                }else{
                    JOptionPane.showMessageDialog(null, "INVALID USERNAME/PASSWORD!!!");
                }
            }catch(HeadlessException | IOException e){
                e.printStackTrace();
            }
        }
        
//    }
//    private void handleButtonLoggin(ActionEvent event) throws IOException {
//    System.out.println("=== BUTTON DIKLIK ===");
//    
//    try {
//        String akun = txtUser.getText();
//        String pass = txtPass.getText();
//        
//        System.out.println("Username: " + akun);
//        System.out.println("Password: " + pass);
//        
//        if(akun.isEmpty() || pass.isEmpty()){
//            System.out.println("Username atau password kosong!");
//            JOptionPane.showMessageDialog(null, "Username dan Password tidak boleh kosong!");
//            return;
//        }
//        
//        System.out.println("Mengecek login ke database...");
//        Karyawan loggedInKaryawan = karyawanDAO.checkLogin(akun, pass);
//        
//        if (loggedInKaryawan != null) {
//            System.out.println("LOGIN BERHASIL!");
//            System.out.println("Nama Karyawan: " + loggedInKaryawan.getName());
//            
//            System.out.println("Mencoba load Dashboard.fxml...");
//            
//            Stage stage = (Stage) btnLogin.getScene().getWindow();
//            System.out.println("Stage didapat: " + stage);
//            
//            URL url = new File("src/main/java/view/Dashboard.fxml").toURI().toURL();
//            System.out.println("URL Dashboard: " + url);
//            
//            Parent root = FXMLLoader.load(url);
//            System.out.println("Root berhasil di-load!");
//            
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
//            
//            System.out.println("=== SCENE BERHASIL DIGANTI ===");
//            
//        } else {
//            System.out.println("LOGIN GAGAL! Username/password salah");
//            JOptionPane.showMessageDialog(null, "INVALID USERNAME/PASSWORD!!!");
//        }
//        
//    } catch (SQLException e) {
//        System.out.println("ERROR DATABASE: " + e.getMessage());
//        e.printStackTrace();
//    } catch (IOException e) {
//        System.out.println("ERROR LOAD FXML: " + e.getMessage());
//        e.printStackTrace();
//    } catch (Exception e) {
//        System.out.println("ERROR LAINNYA: " + e.getMessage());
//        e.printStackTrace();
//    }
}
    /*private void handleButtonLoggin(ActionEvent event) throws IOException, SQLException {
        String akun = txtUser.getText();
        String pass = txtPass.getText();
        if(akun.isEmpty() || pass.isEmpty()){
            System.out.println("Errorr...");
            return;
        }
        Karyawan loggedInKaryawan = karyawanDAO.checkLogin(akun, pass);
        if(loggedInKaryawan != null){
            try{
                //FXMLLoader loader = new FXMLLoader(getClass().getResource("src/main/java/view/Dashboard.fxml"));
                URL url = new File("src/main/java/view/Dashboard.fxml").toURI().toURL();
                Parent root = FXMLLoader.load(url);
                
                //DashboardController dashboardController = loader.getController();
                //dashboardController.setKaryawanData(loggedInKaryawan);
                
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                
            }catch (IOException e) {
                e.printStackTrace();
        }
        }
    }*/
    
}
