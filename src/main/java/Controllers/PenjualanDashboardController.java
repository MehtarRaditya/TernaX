/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DataAccessObject.PenjualanDAO;
import DataAccessObject.ProdukDAO;
import Models.PenjualanDetail;
import Models.Produk;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Muham
 */
public class PenjualanDashboardController implements Initializable {

    @FXML
    private TableView<Produk> tvProduk;
    @FXML
    private TableColumn<Produk, String> colIdProduk;
    @FXML
    private TableColumn<Produk, String> colJenisProduk;
    @FXML
    private TableColumn<Produk, String> colKualitasProduk;
    @FXML
    private TableColumn<Produk, Double> colStokProduk;
    @FXML
    private TableColumn<Produk, Integer> ColHewanID;
    @FXML
    private TextField txtKuantitas;
    @FXML
    private TableView<PenjualanDetail> tvKeranjang;
    @FXML
    private TableColumn<PenjualanDetail, String> colIdKeranjang;
    @FXML
    private TableColumn<PenjualanDetail, String> ColJenisKeranjang;
    @FXML
    private TableColumn<PenjualanDetail, Double> ColQuantityKeranjang;
    private final ProdukDAO produkDAO = new ProdukDAO();
    private final PenjualanDAO penjualanDAO = new PenjualanDAO();

    private final ObservableList<Produk> dataProduk = FXCollections.observableArrayList();
    private final ObservableList<PenjualanDetail> keranjang = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colIdProduk.setCellValueFactory(new PropertyValueFactory<>("id"));
        colJenisProduk.setCellValueFactory(new PropertyValueFactory<>("tipe"));       // jenis = tipe
        colKualitasProduk.setCellValueFactory(new PropertyValueFactory<>("kualitas"));
        colStokProduk.setCellValueFactory(new PropertyValueFactory<>("kuantitas"));  // stok = kuantitas
        ColHewanID.setCellValueFactory(new PropertyValueFactory<>("idHewan"));

        // ===== TABEL KERANJANG =====
        colIdKeranjang.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColJenisKeranjang.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        ColQuantityKeranjang.setCellValueFactory(new PropertyValueFactory<>("qty"));

        tvProduk.setItems(dataProduk);
        tvKeranjang.setItems(keranjang);

        loadProduk();
    }
    private void loadProduk() {
        try {
            dataProduk.setAll(produkDAO.getAll());
        } catch (Exception e) {
            showAlert("Error", "Gagal load produk:\n" + e.getMessage());
        }
    }

    @FXML
    private void handleButtonTambah(ActionEvent event) {
        Produk produk = tvProduk.getSelectionModel().getSelectedItem();
        if (produk == null) {
            showAlert("Peringatan", "Pilih produk terlebih dahulu!");
            return;
        }

        double qty;
        try {
            qty = Double.parseDouble(txtKuantitas.getText());
        } catch (Exception e) {
            showAlert("Peringatan", "Kuantitas harus berupa angka!");
            return;
        }

        if (qty <= 0) {
            showAlert("Peringatan", "Kuantitas harus lebih dari 0!");
            return;
        }

        // validasi stok dari tabel (validasi DB ada di DAO)
        if (qty > produk.getKuantitas()) {
            showAlert("Peringatan", "Stok tidak mencukupi!\nStok tersedia: " + produk.getKuantitas());
            return;
        }

        // Jika produk sudah ada di keranjang → tambah qty (ala Shopee)
        for (PenjualanDetail item : keranjang) {
            if (item.getProduk().getId().equals(produk.getId())) {
                item.setQty(item.getQty() + qty);
                tvKeranjang.refresh();
                txtKuantitas.clear();
                return;
            }
        }

        // Kalau belum ada → tambah item baru
        keranjang.add(new PenjualanDetail(produk, qty));
        txtKuantitas.clear();
    }

    @FXML
    private void handleButtonHapus(ActionEvent event) {
        PenjualanDetail selected = tvKeranjang.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih item keranjang yang ingin dihapus!");
            return;
        }
        keranjang.remove(selected);
    }

    @FXML
    private void handleButtonKirim(ActionEvent event) {
        if (keranjang.isEmpty()) {
            showAlert("Peringatan", "Keranjang masih kosong!");
            return;
        }

        try {
            penjualanDAO.checkout(keranjang);

            showAlert("Sukses", "Penjualan berhasil dikirim!");

            keranjang.clear();
            loadProduk(); // refresh stok terbaru

        } catch (Exception e) {
            showAlert("Gagal", "Gagal mengirim penjualan:\n" + e.getMessage());
        }
    }
     private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}
