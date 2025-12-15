package Controllers;

import DataAccessObject.HewanDAO;
import DataAccessObject.KonsumsiDAO;
import DataAccessObject.PemakaianKonsumsiDAO;
import Models.Hewan;
import Models.Karyawan;
import Models.Konsumsi;
import Models.PemakaianKonsumsi;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import utility.Session;

public class PemberianPakanDashboardController implements Initializable {

    @FXML
    private TableView<Hewan> tvHewan;
    @FXML
    private TableColumn<Hewan, Integer> colIdHewan;
    @FXML
    private TableColumn<Hewan, String> colNamaHewan;
    @FXML
    private TableColumn<Hewan, Integer> colBobotHewan;

    @FXML
    private ChoiceBox<Konsumsi> chbKonsumsi;
    @FXML
    private TextField txtKuantitas;

    // ===== DAO =====
    private final HewanDAO hewanDAO = new HewanDAO();
    private final KonsumsiDAO konsumsiDAO = new KonsumsiDAO();
    private final PemakaianKonsumsiDAO pemakaianDAO = new PemakaianKonsumsiDAO();

    // ===== Data =====
    private final ObservableList<Hewan> hewanList = FXCollections.observableArrayList();
    private final ObservableList<Konsumsi> konsumsiList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
        loadHewan();
        loadKonsumsi();
    }

    private void initTable() {
        colIdHewan.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNamaHewan.setCellValueFactory(new PropertyValueFactory<>("jenis"));   // sesuaikan kalau fieldmu beda
        colBobotHewan.setCellValueFactory(new PropertyValueFactory<>("berat")); // sesuaikan kalau fieldmu beda

        tvHewan.setItems(hewanList);
    }

    private void loadHewan() {
        hewanList.clear();
        List<Hewan> data = hewanDAO.getAll();
        hewanList.addAll(data);
    }

    private void loadKonsumsi() {
        konsumsiList.clear();
        List<Konsumsi> data = konsumsiDAO.getAll();
        konsumsiList.addAll(data);

        chbKonsumsi.setItems(konsumsiList);

        // Biar tampil nama konsumsi (bukan "Models.Konsumsi@xxxx")
        chbKonsumsi.setConverter(new StringConverter<>() {
            @Override
            public String toString(Konsumsi k) {
                return (k == null) ? "" : (k.getName() + " (" + k.getTipe() + ")");
            }
            @Override
            public Konsumsi fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    private void handleButtonBeriMakan(ActionEvent event) {
        // 1) Validasi login karyawan
        Karyawan karyawan = Session.getLoggedInKaryawan();
        if (karyawan == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Belum ada karyawan yang login.");
            return;
        }

        // 2) Ambil hewan yang dipilih
        Hewan hewan = tvHewan.getSelectionModel().getSelectedItem();
        if (hewan == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih hewan dulu di tabel!");
            return;
        }

        // 3) Ambil konsumsi yang dipilih
        Konsumsi konsumsi = chbKonsumsi.getValue();
        if (konsumsi == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih konsumsi dulu!");
            return;
        }

        // 4) Validasi kuantitas (INT)
        int qty;
        try {
            qty = Integer.parseInt(txtKuantitas.getText().trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Kuantitas harus angka bulat dan > 0.");
            return;
        }

        // 5) (opsional tapi disarankan) cek stok dulu
        int stok = konsumsiDAO.getStokById(konsumsi.getId()); // buat method ini kalau belum ada
        if (stok < 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal mengambil stok konsumsi.");
            return;
        }
        if (stok < qty) {
            showAlert(Alert.AlertType.ERROR, "Stok tidak cukup",
                    "Stok " + konsumsi.getName() + " tersisa " + stok + ", butuh " + qty);
            return;
        }

        // 6) Buat object pemakaian
        PemakaianKonsumsi pk = new PemakaianKonsumsi();
        pk.setIdHewan(hewan.getId());
        pk.setIdKonsumsi(konsumsi.getId());
        pk.setKuantitas(qty);
        // id_karyawan boleh di-set di DAO dari Session, tapi kalau model ada:
        pk.setIdKaryawan(Integer.parseInt(karyawan.getId()));

        // 7) Eksekusi DAO (insert histori + update stok dalam 1 transaksi)
        try {
        pemakaianDAO.beriPakan(pk);

        // kalau sampai sini = SUKSES
        showAlert(Alert.AlertType.INFORMATION, "Berhasil",
                "Pemberian pakan berhasil dicatat.\nStok berkurang: " + qty);

        txtKuantitas.clear();

        } catch (Exception e) {
            // kalau DAO kamu throw RuntimeException
            showAlert(Alert.AlertType.ERROR, "Gagal",
                    "Pemberian pakan gagal. Silakan cek data.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
