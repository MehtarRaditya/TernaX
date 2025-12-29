/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DataAccessObject.HewanDAO;
import DataAccessObject.ProdukDAO;
import Models.Hewan;
import Models.KatalogProduk;
import Models.Produk;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.util.Duration;


/**
 * FXML Controller class
 *
 * @author USER
 */
public class PeternakHewanController implements Initializable {

    @FXML
    private AnchorPane peternakTampilan;
    @FXML
    private Label totalAyam;
    @FXML
    private Label totalSapi;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnTambah1;
    
    @FXML
    private Button btHewan;
    @FXML
    private Button btnProduk;
    @FXML
    private Button btnKonsum;
    @FXML
    private Button btnLogout;
    @FXML
    private AnchorPane displayAddHewan;
    @FXML
    private TextArea txtBerat;
    @FXML
    private TextArea txtUsia;
    @FXML
    private TextArea txtKondisi;
    @FXML
    private TextArea txtPenyakit;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnKembali;
    @FXML
    private TextField txtKuantitas;
    @FXML
    private DatePicker txtDateProduk;
    @FXML
    private ChoiceBox<KatalogProduk> chcTipe;
    @FXML
    private Button btnKembaliProduk;
    @FXML
    private ChoiceBox<String> chcJenis;
    @FXML
    private ChoiceBox<String> chcKelamin;
    @FXML
    private TableColumn<Hewan, String> JenisCol;
    @FXML
    private TableColumn<Hewan, Double> BeratCol;
    @FXML
    private TableColumn<Hewan, Integer> UsiaCol;
    @FXML
    private TableColumn<Hewan, String> KelaminCol;
    @FXML
    private TableColumn<Hewan, String> KondisiCol;
    @FXML
    private TableColumn<Hewan, String> PenyakitCol;
    
    private ProdukDAO produkDAO; 
    private HewanDAO hewanDAO;// tambahin DAO produk
    
    private ObservableList<Hewan> dataHewan;
    @FXML
    private TableView<Hewan> tvHewan;
    
    private ObservableList<String> tipeHewan = FXCollections.observableArrayList("Sapi", "Ayam");
    private ObservableList<String> jenisKelamin = FXCollections.observableArrayList("Jantan", "Betina");
    @FXML
    private Button btnSimpanTambah;
    @FXML
    private Button btnPanen;
    @FXML
    private Button btnpanenn;
    @FXML
    private AnchorPane displayPanenProduk;
    
    private List<KatalogProduk> listMasterKatalog;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hewanDAO = new HewanDAO();
        produkDAO = new ProdukDAO();
        
        // matikan tombol saat aplikasi baru dibuka 
        if (btnpanenn != null) {
        btnpanenn.setDisable(true);
    
    if (btnEdit != null) btnEdit.setDisable(true);
        
        // pasang Listener (Pemantau) pada Tabel
        tvHewan.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // jika ada hewan yang dipilih = nyalakan tombol
                if (btnEdit != null) btnEdit.setDisable(false);
                if (btnpanenn != null) btnpanenn.setDisable(false);
            } else {
                // jika user klik tempat kosong (batal pilih) = matikan tombol
                if (btnEdit != null) btnEdit.setDisable(true);
                if (btnpanenn != null) btnpanenn.setDisable(true);
            }
        });
        
        peternakTampilan.setOnMouseClicked(event -> {
            // KUNCI PENGAMAN:
                // popup Tambah atau popup panen lagi keluar tidak bisa batalin pilihan
                if ((displayAddHewan != null && displayAddHewan.isVisible()) || 
                    (displayPanenProduk != null && displayPanenProduk.isVisible())) {
                    return; //
                }

                // cek apa yang diklik (Logic sebelumnya)
                javafx.scene.Node target = (javafx.scene.Node) event.getTarget();
                boolean klikAman = false;
                
                while (target != null && target != peternakTampilan) {
                    if (target instanceof Button || 
                        target instanceof TextField || 
                        target instanceof DatePicker || 
                        target instanceof ChoiceBox ||
                        target instanceof TableView) {
                        klikAman = true;
                        break;
                    }
                    target = target.getParent();
                }

                // Kalau klik di ruang kosong DAN tidak ada popup, baru batal pilih
                if (!klikAman) {
                    tvHewan.getSelectionModel().clearSelection();
                }
        });
        
        tvHewan.setOnMouseClicked(event -> {
            // mengecek apakah user klik di area tabel tapi bkn di baris data
            javafx.scene.Node node = event.getPickResult().getIntersectedNode();
            boolean isRow = false;
            
            // loop ke atas untuk cari tahu apakah yang diklik itu TableRow
            while (node != null && node != tvHewan) {
                if (node instanceof javafx.scene.control.TableRow && 
                    ((javafx.scene.control.TableRow<?>) node).getItem() != null) {
                    isRow = true; // klik baris data
                    break;
                }
                node = node.getParent();
            }
            
            // Kalau bukan baris (berarti area putih kosong), hapus seleksi
            if (!isRow) {
                tvHewan.getSelectionModel().clearSelection();
            }
        });
        
        loadDataFromDatabase();
        
        JenisCol.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        BeratCol.setCellValueFactory(new PropertyValueFactory<>("berat"));
        UsiaCol.setCellValueFactory(new PropertyValueFactory<>("usia"));
        KelaminCol.setCellValueFactory(new PropertyValueFactory<>("kelamin"));
        KondisiCol.setCellValueFactory(new PropertyValueFactory<>("kondisi"));
        PenyakitCol.setCellValueFactory(new PropertyValueFactory<>("penyakit"));
        
        chcJenis.setItems(tipeHewan);
        chcJenis.setValue("Sapi"); // Default
        
        chcKelamin.setItems(jenisKelamin);
        chcKelamin.setValue("Jantan"); // Default
        
        //Sembunyikan Popup saat awal
        if(displayAddHewan != null) {
            displayAddHewan.setVisible(false);
        }
        
        txtUsia.textProperty().addListener((observable, oldValue, newValue) -> {
            updatePromptBerat();
        });

        // 2. Pasang mata-mata di pilihan JENIS (Sapi/Ayam)
        chcJenis.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updatePromptBerat();
        });
      
        // Load data dari database saat aplikasi dimulai
        loadDataFromDatabase();
    }
    }

    private void loadDataFromDatabase() {
        // 1. Ambil SEMUA data dari database (termasuk yang mati)
        List<Hewan> allData = hewanDAO.getAll();
        
        // 2. FILTER: Ambil hanya yang MASIH HIDUP (Kondisi != "Mati")
        // Ini akan mencakup status: "Alive", "Sehat", "Sakit", dll.
        List<Hewan> hewanHidup = allData.stream()
                .filter(h -> h.getKondisi() != null && !h.getKondisi().equalsIgnoreCase("Mati"))
                .collect(Collectors.toList());
                
        // 3. Masukkan data yang sudah difilter ke Tabel
        dataHewan = FXCollections.observableArrayList(hewanHidup);
        tvHewan.setItems(dataHewan);
        
        // 4. Hitung TOTAL untuk LABEL (Pakai list 'hewanHidup', BUKAN 'allData')
        
        // Hitung Ayam Hidup
        long countAyam = hewanHidup.stream()
                .filter(h -> h.getJenis().equalsIgnoreCase("Ayam"))
                .count();

        // Hitung Sapi Hidup
        long countSapi = hewanHidup.stream()
                .filter(h -> h.getJenis().equalsIgnoreCase("Sapi"))
                .count();

        // 5. Update Text Label (Null Safety check biar gak error)
        if(totalAyam != null) totalAyam.setText(String.valueOf(countAyam));
        if(totalSapi != null) totalSapi.setText(String.valueOf(countSapi));
        
    }

    private void clearForm() {
        txtBerat.clear();
        txtUsia.clear();
        txtKondisi.clear();
        txtPenyakit.clear();
        chcJenis.setValue("Sapi");
        chcKelamin.setValue("Jantan");
    }
    
    @FXML
    private void btnEdit(ActionEvent event) {
        Hewan selected = tvHewan.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showFancyAlert("ERROR", "Gagal", "Pilih hewan di tabel dulu!");
            return;
        }
        
        // Isi Form dengan data lama
        chcJenis.setValue(selected.getJenis());
        chcKelamin.setValue(selected.getKelamin());
        txtBerat.setText(String.valueOf(selected.getBerat()));
        txtUsia.setText(String.valueOf(selected.getUsia()));
        txtKondisi.setText(selected.getKondisi());
        txtPenyakit.setText(selected.getPenyakit());

        // --- ATURAN EDIT ---
        chcJenis.setDisable(true);    // Jenis gabisa ganti
        chcKelamin.setDisable(true);  // Kelamin gabisa ganti
        txtKondisi.setDisable(true);  // Kondisi gabisa ganti
        
        // Penyakit BOLEH GANTI (Misal dari Sehat jadi Flu)
        txtPenyakit.setDisable(false); 
        // -------------------

        if (displayAddHewan != null) {
            displayAddHewan.setVisible(true);
            tvHewan.setDisable(true);
        }
        if (btnSimpanTambah != null) btnSimpanTambah.setVisible(false);
        if (btnUpdate != null) btnUpdate.setVisible(true);
    }

    @FXML
    private void btnTambah(ActionEvent event) {
        clearForm(); // Bersihkan form dulu
        
        // 1. Jenis & Kelamin -> BOLEH PILIH (Karena hewan baru)
        chcJenis.setDisable(false);
        chcKelamin.setDisable(false);
        
        // 2. KONDISI -> OTOMATIS "Alive" & DIKUNCI
        txtKondisi.setText("Alive"); 
        txtKondisi.setDisable(true); 
        
        // 3. PENYAKIT -> OTOMATIS "Sehat" & DIKUNCI
        txtPenyakit.setText("Sehat"); 
        txtPenyakit.setDisable(true);
        
        // Reset Prompt Text Berat
        txtBerat.setPromptText("Masukkan Berat (Kg)");

        // Tampilkan Popup
        if (displayAddHewan != null) {
            displayAddHewan.setVisible(true);
            tvHewan.setDisable(true); 
        }
        
        // Atur tombol Simpan/Update
        if (btnSimpanTambah != null) btnSimpanTambah.setVisible(true); 
        if (btnUpdate != null) btnUpdate.setVisible(false);
    }

    @FXML
    private void btnPanenProduk(ActionEvent event) {
        Hewan selected = tvHewan.getSelectionModel().getSelectedItem();
    
    if (selected == null) {
        showFancyAlert("WARNING", "Peringatan", "Pilih hewan dulu!");
        return;
    }

    // 1. AMBIL DATA MASTER DARI DATABASE (Cek null biar gak query terus)
    // Pastikan produkDAO sudah di-new di method initialize() ya!
    if (listMasterKatalog == null || listMasterKatalog.isEmpty()) {
        if (produkDAO == null) produkDAO = new ProdukDAO(); // Jaga-jaga kalau lupa init
        listMasterKatalog = produkDAO.getKatalogList();
    }

    // Reset Form
    txtKuantitas.clear();
    txtDateProduk.setValue(LocalDate.now());
    chcTipe.getItems().clear(); // Bersihkan pilihan lama

    // 2. LOGIKA FILTER (Sesuai Gender & Jenis Hewan)
    String jenis = selected.getJenis();      // "Sapi" atau "Ayam"
    String kelamin = selected.getKelamin();  // "Jantan" atau "Betina"

    for (KatalogProduk item : listMasterKatalog) {
        String namaProduk = item.getNamaProduk().toLowerCase();

        // --- FILTER UNTUK SAPI ---
        if (jenis.equalsIgnoreCase("Sapi") && namaProduk.contains("sapi")) {
            if (kelamin.equalsIgnoreCase("Jantan")) {
                // Sapi Jantan = Cuma bisa Daging
                if (namaProduk.contains("daging")) {
                    chcTipe.getItems().add(item);
                }
            } else {
                // Sapi Betina = Bisa Daging & Susu (Semua produk sapi masuk)
                chcTipe.getItems().add(item);
            }
        } 
        // --- FILTER UNTUK AYAM ---
        else if (jenis.equalsIgnoreCase("Ayam") && (namaProduk.contains("ayam") || namaProduk.contains("telur"))) {
            if (kelamin.equalsIgnoreCase("Jantan")) {
                // Ayam Jantan = Cuma bisa Daging
                if (namaProduk.contains("daging")) {
                    chcTipe.getItems().add(item);
                }
            } else {
                // Ayam Betina = Bisa Daging & Telur
                chcTipe.getItems().add(item);
            }
        }
    }

    // Validasi Jika Kosong
    if (chcTipe.getItems().isEmpty()) {
        showFancyAlert("ERROR", "Data Kosong", "Pastikan tabel katalog_produk di database sudah diisi (Susu, Daging, dll)!");
        return;
    }

    // Pilih item pertama otomatis
    chcTipe.getSelectionModel().selectFirst();

    // Tampilkan Popup
    displayPanenProduk.setVisible(true);
    tvHewan.setDisable(true);
    }

    @FXML
    private void handleSubmitProduk(ActionEvent event) {
       Hewan selectedHewan = tvHewan.getSelectionModel().getSelectedItem();
    
    // Cek Hewan
    if (selectedHewan == null) {
        showFancyAlert("WARNING", "Pilih Hewan", "Pilih dulu hewan di tabel!");
        return;
    }

    try {
        // 1. AMBIL INPUTAN
        String kuantitasStr = txtKuantitas.getText();
        KatalogProduk produkTerpilih = chcTipe.getValue(); 
        
        // 2. VALIDASI FORM
        if (kuantitasStr.isEmpty() || txtDateProduk.getValue() == null || produkTerpilih == null) {
            showFancyAlert("WARNING", "Data Kurang", "Mohon lengkapi semua form!");
            return;
        }

        // 3. SIAPKAN DATA
        int idHewan = selectedHewan.getId();
        int idKatalog = produkTerpilih.getId();
        String tanggal = txtDateProduk.getValue().toString();
        double qty = Double.parseDouble(kuantitasStr);

        // 4. BUAT OBJEK PRODUK
        Produk panenBaru = new Produk(idKatalog, idHewan, tanggal, qty, "Pending");

        // 5. SIMPAN KE DATABASE
        if (produkDAO.addProduk(panenBaru)) {
            
            // --- LOGIKA HEWAN MATI (SAPI/AYAM POTONG) ---
            // Jika produk mengandung kata "daging", hewan dianggap mati
            if (produkTerpilih.getNamaProduk().toLowerCase().contains("daging")) {
                // Update status di Database jadi "Mati"
                hewanDAO.updateKondisi(idHewan, "Mati");
            }
            
            showFancyAlert("SUCCESS", "Berhasil", "Panen " + produkTerpilih.getNamaProduk() + " tercatat!");

            // --- INI KUNCINYA AGAR REALTIME & HILANG DARI TABEL ---
            // Kita load ulang data dari database.
            // Method ini otomatis membuang hewan mati dari tabel & menghitung ulang total label.
            loadDataFromDatabase(); 
            
            // Tutup Popup & Bersihkan
            displayPanenProduk.setVisible(false);
            txtKuantitas.clear();
            tvHewan.setDisable(false);
            
        } else {
            showFancyAlert("ERROR", "Gagal", "Terjadi kesalahan database.");
        }

    } catch (NumberFormatException e) {
        showFancyAlert("ERROR", "Input Salah", "Kuantitas harus berupa angka!");
    } catch (Exception e) {
        e.printStackTrace();
    }
    }


    @FXML
    private void handleActionEdit(ActionEvent event) {
        Hewan selected = tvHewan.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                // Update data berat & usia
                selected.setBerat(Double.parseDouble(txtBerat.getText()));
                selected.setUsia(Integer.parseInt(txtUsia.getText()));
                
                // Ambil penyakit dari inputan (karena pas edit boleh diubah)
                selected.setPenyakit(txtPenyakit.getText());

                // Validasi Logic Berat
                if (!isBeratLogis(selected.getJenis(), selected.getUsia(), selected.getBerat())) {
                     showFancyAlert("WARNING", "Data Tidak Logis", "Cek kembali berat dan usia.");
                     return;
                }

                hewanDAO.update(selected); 
                showFancyAlert("SUCCESS","Sukses", "Data Berhasil Diupdate!");
                
            } catch (Exception e) {
                showFancyAlert("ERROR","Gagal", "Gagal update: " + e.getMessage());
            } finally {
                loadDataFromDatabase();
                clearForm();
                
                // Reset Disable Status (PENTING BIAR PAS TAMBAH LAGI GAK ERROR)
                chcJenis.setDisable(false);
                chcKelamin.setDisable(false);
                txtKondisi.setDisable(false); 
                txtPenyakit.setDisable(false);
                
                if (displayAddHewan != null) displayAddHewan.setVisible(false);
                tvHewan.setDisable(false); 
            }
        }
    }

    @FXML
    private void handleBackAddHewan(ActionEvent event) {
        if (displayAddHewan != null) {
            displayAddHewan.setVisible(false);
        }
        clearForm();
        
        // Reset disable agar normal kembali
        chcJenis.setDisable(false);
        chcKelamin.setDisable(false);
        txtKondisi.setDisable(false);
        
        tvHewan.setDisable(false);
    }

    @FXML
    private void handleBackPanen(ActionEvent event) {
        if (displayPanenProduk != null) {
            displayPanenProduk.setVisible(false);
        }
        tvHewan.setDisable(false);
    }

    @FXML
    private void handleActionTambahHewan(ActionEvent event) {
        try {
            // Ambil input user
            String jenis = chcJenis.getValue();
            String kelamin = chcKelamin.getValue();
            String beratStr = txtBerat.getText();
            String usiaStr = txtUsia.getText();
            
            // --- INI PERUBAHANNYA ---
            // Kita HARDCODE (Tulis Mati) biar datanya pasti bener
            String kondisi = "Alive"; 
            String penyakit = "Sehat"; 
            // ------------------------

            // Validasi input kosong
            if (beratStr.isEmpty() || usiaStr.isEmpty()) { 
                showFancyAlert("WARNING","Error", "Mohon isi Berat dan Usia!");
                return;
            }

            double berat = Double.parseDouble(beratStr);
            int usia = Integer.parseInt(usiaStr);

            // Validasi Logika Berat (Pakai method helper yang tadi)
            if (!isBeratLogis(jenis, usia, berat)) {
                showFancyAlert("WARNING", "Data Tidak Logis", 
                    "Berat " + berat + " kg tidak wajar untuk " + jenis + " usia " + usia + " bulan."
                );
                return;
            }

            // Simpan ke Database
            Hewan hewanBaru = new Hewan(jenis, kelamin, berat, usia, kondisi, penyakit);
            hewanDAO.add(hewanBaru);
            
            // Reset & Tutup
            loadDataFromDatabase();
            clearForm();
            if (displayAddHewan != null) displayAddHewan.setVisible(false);
            tvHewan.setDisable(false); 
            
            showFancyAlert("SUCCESS","Sukses", "Data Hewan (" + kondisi + " & " + penyakit + ") Berhasil Ditambahkan!");

        } catch (NumberFormatException e) {
            showFancyAlert("ERROR","Error", "Berat dan Usia harus berupa angka!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleActionLogout(ActionEvent event) {
         try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btnLogout.getScene().getWindow();
            
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

    @FXML
    private void handleActionToProdukPeternak(ActionEvent event) {
        try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btnProduk.getScene().getWindow();
            
            // 2. Cari file FXML tujuan
            java.io.File file = new java.io.File("src/main/java/Views/peternakPanen.fxml");
            
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

    @FXML
    private void handleActionToProdukKonsumsi(ActionEvent event) {
        try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btnKonsum.getScene().getWindow();
            
            // 2. Cari file FXML tujuan
            java.io.File file = new java.io.File("src/main/java/Views/peternakKonsumsi.fxml");
            
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
    
    @FXML
    private void btnHewan(ActionEvent event) {
        loadDataFromDatabase();
    }
    
    // Method Validasi Berat berdasarkan Usia
    private boolean isBeratLogis(String jenis, int usia, double berat) {
        double minBerat = 0;
        double maxBerat = 0;

        // --- LOGIKA UNTUK SAPI ---
        if (jenis.equalsIgnoreCase("Sapi")) {
            // Asumsi Sapi: Lahir ~30kg, Dewasa bisa 400-800kg
            // Rumus estimasi kasar: 
            // Minimal: 30kg + (10kg per bulan)
            // Maksimal: 60kg + (50kg per bulan)
            
            minBerat = 30 + (usia * 10); 
            maxBerat = 100 + (usia * 60); 
            
            // Cap Maksimal Sapi Limosin misal 1.2 Ton
            if (maxBerat > 1200) maxBerat = 1200; 

        } 
        // --- LOGIKA UNTUK AYAM ---
        else if (jenis.equalsIgnoreCase("Ayam")) {
            // Asumsi Ayam: Lahir ~0.04kg, Dewasa ~2-4kg
            // Minimal: 0.05kg + (0.1kg per bulan)
            // Maksimal: 0.2kg + (1kg per bulan)
            
            minBerat = 0.05 + (usia * 0.1);
            maxBerat = 0.5 + (usia * 1.5);
            
            // Cap Maksimal Ayam Broiler misal 5-6kg
            if (maxBerat > 6) maxBerat = 6;
        }

        // Cek apakah berat inputan ada di dalam rentang?
        if (berat < minBerat || berat > maxBerat) {
            System.out.println("Validasi Gagal: " + jenis + " Usia " + usia + " bulan.");
            System.out.println("Seharusnya berat antara " + minBerat + "kg - " + maxBerat + "kg.");
            return false; // TIDAK LOGIS
        }
        
        return true; // LOGIS
    }
    
    private void updatePromptBerat() {
        String usiaStr = txtUsia.getText();
        String jenis = chcJenis.getValue();

        // Kalau usia kosong atau jenis belum pilih, reset prompt
        if (usiaStr.isEmpty() || !usiaStr.matches("\\d+") || jenis == null) {
            txtBerat.setPromptText("Masukkan Berat (Kg)");
            return;
        }

        int usia = Integer.parseInt(usiaStr);
        double minBerat = 0;
        double maxBerat = 0;

        // --- RUMUS LOGIKA (Sama dengan validasi) ---
        if (jenis.equalsIgnoreCase("Sapi")) {
            // Rumus Sapi
            minBerat = 30 + (usia * 10);
            maxBerat = 100 + (usia * 60);
            if (maxBerat > 1200) maxBerat = 1200;
        } else {
            // Rumus Ayam
            minBerat = 0.05 + (usia * 0.1);
            maxBerat = 0.5 + (usia * 1.5);
            if (maxBerat > 6) maxBerat = 6;
        }

        // --- UBAH PROMPT TEXT ---
        // Contoh hasil: "Rentang Wajar: 40.0 - 160.0 kg"
        txtBerat.setPromptText("Wajar: " + String.format("%.1f", minBerat) + " - " + String.format("%.1f", maxBerat) + " kg");
    }

    private void showFancyAlert(String type, String title, String message) {
    // 1. Setup Stage Tanpa Bingkai
    Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL); // Blokir window belakang
    stage.initStyle(StageStyle.TRANSPARENT); // Transparan (GOKILNYA DISINI)
    
    // 2. Tentukan Warna & Ikon berdasarkan Tipe
    String colorCode = "#11998e"; // Default Hijau (Success)
    String iconSymbol = "✔";
    if (type.equalsIgnoreCase("ERROR")) {
        colorCode = "#ff5f6d"; // Merah
        iconSymbol = "X";
    } else if (type.equalsIgnoreCase("WARNING")) {
        colorCode = "#ffc371"; // Kuning/Oranye
        iconSymbol = "!";
    }

    // 3. Buat Layout Ikon (Lingkaran)
    Circle iconCircle = new Circle(35, Color.web(colorCode));
    Text iconText = new Text(iconSymbol);
    iconText.setStyle("-fx-font-size: 30px; -fx-fill: white; -fx-font-weight: bold;");
    StackPane iconPane = new StackPane(iconCircle, iconText);
    iconPane.setTranslateY(-40); // Geser ke atas biar nongol (Floating Effect)

    // 4. Buat Label Judul & Pesan
    Label lblTitle = new Label(title);
    lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");
    
    Label lblMessage = new Label(message);
    lblMessage.setStyle("-fx-font-size: 14px; -fx-text-fill: #666; -fx-wrap-text: true; -fx-text-alignment: center;");
    
    // 5. Buat Tombol OK
    Button btnOk = new Button("OKE, MENGERTI");
    btnOk.setStyle(
        "-fx-background-color: " + colorCode + ";" +
        "-fx-text-fill: white;" +
        "-fx-font-weight: bold;" +
        "-fx-font-size: 14px;" +
        "-fx-background-radius: 20;" +
        "-fx-padding: 10 30;" +
        "-fx-cursor: hand;"
    );
    // Efek Hover Tombol
    String finalColor = colorCode;
    btnOk.setOnMouseEntered(e -> btnOk.setStyle("-fx-background-color: " + finalColor + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 10 30; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);"));
    btnOk.setOnMouseExited(e -> btnOk.setStyle("-fx-background-color: " + finalColor + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 10 30; -fx-cursor: hand;"));
    btnOk.setOnAction(e -> stage.close());

    // 6. Susun Layout Kartu (Card)
    VBox cardLayout = new VBox(15, iconPane, lblTitle, lblMessage, btnOk);
    cardLayout.setAlignment(Pos.CENTER);
    cardLayout.setStyle(
        "-fx-background-color: white;" +
        "-fx-background-radius: 20;" +
        "-fx-padding: 30 20 20 20;" +
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 20, 0, 0, 10);"
    );
    cardLayout.setMaxWidth(350);
    cardLayout.setMinHeight(200);

    // 7. Bungkus dalam Root Transparan agar shadow tidak terpotong
    StackPane root = new StackPane(cardLayout);
    root.setStyle("-fx-background-color: transparent; -fx-padding: 20;"); // Padding buat bayangan

    // 8. Tampilkan Scene
    Scene scene = new Scene(root);
    scene.setFill(Color.TRANSPARENT); // Background Scene Transparan
    stage.setScene(scene);
    
    // 9. ANIMASI MUNCUL (Pop Up Scale)
    ScaleTransition st = new ScaleTransition(Duration.millis(250), cardLayout);
    st.setFromX(0.5); st.setFromY(0.5);
    st.setToX(1.0); st.setToY(1.0);
    st.play();

    stage.showAndWait();
}
    
    
    
}
