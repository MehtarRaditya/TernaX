/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DataAccessObject.KonsumsiDAO;
import DataAccessObject.TransaksiKonsumsiDAO;
import DataAccessObject.ViewLogistikDAO;
import Models.Konsumsi;
import Models.LogistikRow;
import Models.TransaksiKonsumsi;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Muham
 */
public class LogistikDashboardController implements Initializable {

    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtKuantitas;
    @FXML
    private DatePicker dateDibeli;
    @FXML
    private DatePicker dateExpired;
    @FXML
    private TableView<LogistikRow> tvLogistik;
    @FXML
    private TableColumn<LogistikRow, String> namaCol;
    @FXML
    private TableColumn<LogistikRow, String> tipeCol;
    @FXML
    private TableColumn<LogistikRow, Integer> kuantitasCol;
    @FXML
    private TableColumn<LogistikRow, String> tanggalBeliCol;
    @FXML
    private TableColumn<LogistikRow, String> tanggalExpCol;
    @FXML
    private ChoiceBox<String> chcTipe;
    
    private ObservableList<LogistikRow> dataLogistik;
 
    private ObservableList<String> tipe = FXCollections.observableArrayList("Obat", "Vitamin", "Pakan");
    private KonsumsiDAO konsumsiDAO ;
    private TransaksiKonsumsiDAO tkDAO;
    private ViewLogistikDAO viewLogistikDAO;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        chcTipe.setItems(tipe);
        chcTipe.setValue(tipe.get(0));
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        tipeCol.setCellValueFactory(new PropertyValueFactory<>("tipe"));
        kuantitasCol.setCellValueFactory(new PropertyValueFactory<>("kuantitas"));
        tanggalBeliCol.setCellValueFactory(new PropertyValueFactory<>("tanggalBeli"));
        tanggalExpCol.setCellValueFactory(new PropertyValueFactory<>("tanggalExp"));
        viewLogistikDAO = new ViewLogistikDAO();
        konsumsiDAO = new KonsumsiDAO();
        tkDAO = new TransaksiKonsumsiDAO();
        loadDataFromDatabase();
    }    

    private void loadDataFromDatabase() {
        List<LogistikRow> list = viewLogistikDAO.getAll();
        dataLogistik = FXCollections.observableArrayList(list);
        tvLogistik.setItems(dataLogistik);
    }
    @FXML
    private void handleButtonTambah(ActionEvent event) {
        String tipe = chcTipe.getValue();
        String nama = txtNama.getText();
        String strkuantitas = txtKuantitas.getText();
        int kuantitas = Integer.parseInt(strkuantitas);
        String tanggalDibeli = dateDibeli.getValue().toString();
        String tanggalExpired = dateExpired.getValue().toString();
        Konsumsi k1 = new Konsumsi(nama, tipe, tanggalExpired);
        String tanggalKeluar = "belum tau";
        TransaksiKonsumsi k2 = new TransaksiKonsumsi(tanggalDibeli, kuantitas);
        int idKonsumsiBaru = konsumsiDAO.addKonsumsi(k1);
        if (idKonsumsiBaru == -1) {
        System.err.println("Gagal insert konsumsi.");
        return;
    }
        k2.setIdKonsumsi(idKonsumsiBaru);
        tkDAO.addKonsumsi(k2);
        loadDataFromDatabase();
        
    }

    @FXML
    private void handleButtonEdit(ActionEvent event) {
    }

    @FXML
    private void handleButtonDelete(ActionEvent event) {
        LogistikRow selected = tvLogistik.getSelectionModel().getSelectedItem();
    if (selected == null) {
        System.out.println("Pilih dulu baris yang mau dihapus.");
        return;
    }

    int idTransaksi = selected.getIdTransaksi();
    tkDAO.deleteById(idTransaksi);
        System.out.println(idTransaksi);
    loadDataFromDatabase();
    }
    
}
