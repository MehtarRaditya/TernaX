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
    private void handleButtonLogin(ActionEvent event) throws IOException, SQLException{
        if(btnLogin.getText().equals("LOGIN")){
            try{
                karyawan = karyawanDAO.checkLogin(txtUser.getText(), txtPass.getText());
                if(karyawan != null){
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    URL url = new File("src/main/java/Views/ManagerDashboard.fxml").toURI().toURL();
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
        
    }
}
