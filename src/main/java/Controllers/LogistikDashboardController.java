/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DataAccessObject.DetailPembelianDAO;
import DataAccessObject.KonsumsiDAO;
import DataAccessObject.TransaksiPembelianDAO;
import Models.DetailPembelian;
import Models.DetailPembelianItem;
import Models.Konsumsi;
import Models.TransaksiPembelian;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
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

    private ObservableList<DetailPembelianItem> keranjang = FXCollections.observableArrayList();
    private final KonsumsiDAO konsumsiDAO = new KonsumsiDAO();
    private final TransaksiPembelianDAO transaksiPembelianDAO = new TransaksiPembelianDAO();
    private final DetailPembelianDAO detailPembelianDAO = new DetailPembelianDAO();
    @FXML
    private DatePicker dtPickPembelian;
    @FXML
    private TextField txtKuantitas;
    @FXML
    private Button handleButtonSimpan;
    @FXML
    private TableColumn<DetailPembelianItem, String> colNamaKonsum;
    @FXML
    private TableColumn<DetailPembelianItem, String> colTipe;
    @FXML
    private TableColumn<DetailPembelianItem, Integer> colKuantitas;
    @FXML
    private ComboBox<Konsumsi> chbNamaKonsum;
    @FXML
    private ComboBox<String> ChbKonsumsi;
    @FXML
    private TableView<DetailPembelianItem> tvkeranjang;
    private ObservableList<DetailPembelianItem> dataKeranjang;

    /**
     * Initializes the controller class.
     */
    private final Map<String, List<String>> konsumsiByTipe = new HashMap<>();

    private final ObservableList<String> tipeList = FXCollections.observableArrayList(
            "Pakan Ayam", "Pakan Sapi", "Vitamin", "Obat"
    );

    private final ObservableList<String> pakanAyam = FXCollections.observableArrayList(
            "Jagung", "Cacing", "KFC"
    );

    private final ObservableList<String> pakanSapi = FXCollections.observableArrayList(
            "Rumput gajah", "Dedak Padi"
    );

    private final ObservableList<String> vitamin = FXCollections.observableArrayList(
            "Vitamin larut lemak", "Vitamin B Kompleks", "Vitamin C"
    );

    private final ObservableList<String> obat = FXCollections.observableArrayList(
            "Antibiotik/Antibakteri", "Antiparasit"
    );

    private List<Konsumsi> allKonsumsi;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1) ambil semua konsumsi dari DB
        allKonsumsi = konsumsiDAO.getAll();

        // 2) isi combo TIPE (distinct)
        ObservableList<String> tipeList = FXCollections.observableArrayList(
                allKonsumsi.stream()
                        .map(Konsumsi::getTipe)
                        .filter(t -> t != null && !t.isBlank())
                        .distinct()
                        .collect(Collectors.toList())
        );
        ChbKonsumsi.setItems(tipeList);

        // 3) combo nama dikunci dulu
        chbNamaKonsum.setDisable(true);

        // 4) biar ComboBox<Konsumsi> tampil nama saja
        setKonsumsiComboBoxDisplay(chbNamaKonsum);

        // 5) init table
        colNamaKonsum.setCellValueFactory(new PropertyValueFactory<>("namaKonsumsi"));
        colTipe.setCellValueFactory(new PropertyValueFactory<>("tipe"));
        colKuantitas.setCellValueFactory(new PropertyValueFactory<>("kuantitas"));
        tvkeranjang.setItems(keranjang);

        // 6) listener: ketika tipe dipilih, isi nama konsumsi
        ChbKonsumsi.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                chbNamaKonsum.getItems().clear();
                chbNamaKonsum.getSelectionModel().clearSelection();
                chbNamaKonsum.setDisable(true);
                return;
            }

            List<Konsumsi> filtered = allKonsumsi.stream()
                    .filter(k -> newVal.equalsIgnoreCase(k.getTipe()))
                    .collect(Collectors.toList());

            chbNamaKonsum.setItems(FXCollections.observableArrayList(filtered));
            chbNamaKonsum.getSelectionModel().clearSelection();
            chbNamaKonsum.setDisable(filtered.isEmpty());

            // auto pilih pertama (opsional)
            if (!filtered.isEmpty()) {
                chbNamaKonsum.getSelectionModel().select(0);
            }
        });
    }

    private void setKonsumsiComboBoxDisplay(ComboBox<Konsumsi> combo) {
        combo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Konsumsi item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Konsumsi item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
    }

    @FXML
    private void handleButtonTambah(ActionEvent event) {
        String tipe = ChbKonsumsi.getValue();
        Konsumsi selected = chbNamaKonsum.getValue(); // <-- object Konsumsi

        if (tipe == null) {
            System.out.println("Pilih tipe dulu!");
            return;
        }
        if (selected == null) {
            System.out.println("Pilih nama konsumsi dulu!");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(txtKuantitas.getText().trim());
        } catch (Exception e) {
            System.out.println("Kuantitas harus angka!");
            return;
        }
        if (qty <= 0) {
            System.out.println("Kuantitas harus > 0!");
            return;
        }

        // item sama -> tambah qty
        for (DetailPembelianItem item : keranjang) {
            if (item.getIdKonsumsi() == selected.getId()) {
                item.setKuantitas(item.getKuantitas() + qty);
                tvkeranjang.refresh();
                txtKuantitas.clear();
                return;
            }
        }

        keranjang.add(new DetailPembelianItem(selected, qty));
        txtKuantitas.clear();
    }

    @FXML
    private void handleButtonHapus(ActionEvent event) {
        DetailPembelianItem selectedItem = tvkeranjang.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            System.out.println("Pilih item yang mau dihapus!");
            return;
        }
        keranjang.remove(selectedItem);
    }

    @FXML
    private void handleButtonSimpan(ActionEvent event) throws SQLException {
        if (dtPickPembelian.getValue() == null) {
            System.out.println("Tanggal pembelian belum diisi!");
            return;
        }
        if (keranjang.isEmpty()) {
            System.out.println("Keranjang masih kosong!");
            return;
        }

        // 1) header transaksi pembelian
        String tanggal = dtPickPembelian.getValue().toString();
        TransaksiPembelian transaksi = new TransaksiPembelian(tanggal, 0); // id_karyawan dari Session di DAO
        int idPembelianBaru = transaksiPembelianDAO.insertAndGetId(transaksi);

        if (idPembelianBaru == -1) {
            System.out.println("Gagal simpan transaksi pembelian!");
            return;
        }

        // 2) detail + update stok
        for (DetailPembelianItem item : keranjang) {
            DetailPembelian detail = new DetailPembelian(
                    idPembelianBaru,
                    item.getIdKonsumsi(),
                    item.getKuantitas()
            );
            detailPembelianDAO.insert(detail);

            konsumsiDAO.UpdateStok(item.getIdKonsumsi(), item.getKuantitas());
        }

        // 3) reset
        keranjang.clear();
        tvkeranjang.refresh();
        dtPickPembelian.setValue(null);
        ChbKonsumsi.getSelectionModel().clearSelection();
        chbNamaKonsum.getItems().clear();
        chbNamaKonsum.setDisable(true);
        txtKuantitas.clear();

        System.out.println("Berhasil simpan transaksi pembelian. ID: " + idPembelianBaru);
    }

    @FXML
    private void handleTipeChanged(ActionEvent event) {

    }
}
