package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ManagerKaryawanController {

    @FXML
    private Button btnClose;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnNavBar;

    @FXML
    private Button btnTambah;

    @FXML
    private AnchorPane editDataKaryawan;

    @FXML
    private AnchorPane inputDataKaryawan;

    @FXML
    private ChoiceBox<?> kelaminHewan;

    @FXML
    private ChoiceBox<?> kelaminHewan1;

    @FXML
    private TableView<?> tableKaryawan;

    @FXML
    private AnchorPane tampilanTabKaryawan;

    @FXML
    private Text totalKaryawan;

    @FXML
    private Text totalLogistik;

    @FXML
    private Text totalPenjual;

    @FXML
    private Text totalPeternak;

    @FXML
    private TextArea txtAkun;

    @FXML
    private TextArea txtAkun1;

    @FXML
    private TextArea txtGaji;

    @FXML
    private TextArea txtGaji1;

    @FXML
    private TextArea txtNama;

    @FXML
    private TextArea txtNama1;

    @FXML
    private TextArea txtPassword;

    @FXML
    private TextArea txtPassword1;

    @FXML
    void btnClose(ActionEvent event) {
        inputDataKaryawan.setVisible(false);
        editDataKaryawan.setVisible(false);
    }

    @FXML
    void btnDelete(ActionEvent event) {

    }

    @FXML
    void btnEdit(ActionEvent event) {
        editDataKaryawan.setVisible(true);
    }

    @FXML
    void btnKaryawan(ActionEvent event) {

    }

    @FXML
    void btnKirim(ActionEvent event) {

    }

    @FXML
    void btnLogout(ActionEvent event) {

    }

    @FXML
    void btnSave(ActionEvent event) {

    }

    @FXML
    void btnTambah(ActionEvent event) {
        inputDataKaryawan.setVisible(true);
    }

    @FXML
    void btnTernak(ActionEvent event) {

    }

}
