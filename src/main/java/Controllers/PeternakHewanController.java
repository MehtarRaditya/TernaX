/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DataAccessObject.HewanDAO;
import DataAccessObject.ProdukDAO;
import Models.Hewan;
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
    private ChoiceBox<String> chcTipe;
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
      
        // Load data dari database saat aplikasi dimulai
        loadDataFromDatabase();
    }
    }

    private void loadDataFromDatabase() {
        List<Hewan> list = hewanDAO.getAll();
        
        // hanya tampilkan hewan yang kondisinya bkn "Mati"
        // jadi kalau tadi habis panen daging, hewannya hilang dari tabel
        List<Hewan> hewanHidup = list.stream()
                .filter(h -> !h.getKondisi().equalsIgnoreCase("Mati"))
                .collect(Collectors.toList());
                
        dataHewan = FXCollections.observableArrayList(hewanHidup);
        tvHewan.setItems(dataHewan);
        
        // Hitung total
        long ayam = list.stream().filter(h -> h.getJenis().equalsIgnoreCase("Ayam")).count();
        long sapi = list.stream().filter(h -> h.getJenis().equalsIgnoreCase("Sapi")).count();
        if(totalAyam != null) totalAyam.setText(String.valueOf(ayam));
        if(totalSapi != null) totalSapi.setText(String.valueOf(sapi));
        
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
        // Isi Form
        chcJenis.setValue(selected.getJenis());
        chcKelamin.setValue(selected.getKelamin());
        txtBerat.setText(String.valueOf(selected.getBerat()));
        txtUsia.setText(String.valueOf(selected.getUsia()));
        txtKondisi.setText(selected.getKondisi());
        txtPenyakit.setText(selected.getPenyakit());

        // Tampilkan Popup & Matikan Tabel
        if (displayAddHewan != null) {
            displayAddHewan.setVisible(true);
            tvHewan.setDisable(true);
        }
        // Atur Tombol Simpan
        if (btnSimpanTambah != null) btnSimpanTambah.setVisible(false);
        if (btnUpdate != null) btnUpdate.setVisible(true);
        
    }

    @FXML
    private void btnTambah(ActionEvent event) {
        clearForm();
        if (displayAddHewan != null) {
            displayAddHewan.setVisible(true);
            tvHewan.setDisable(true); 
        }
        if (btnSimpanTambah != null) {
        btnSimpanTambah.setVisible(true); 
    }
        if (btnUpdate != null) {
        btnUpdate.setVisible(false); 
    }
    }

    @FXML
    private void btnPanenProduk(ActionEvent event) {
        Hewan selected = tvHewan.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            showFancyAlert("WARNING","Peringatan", "Pilih hewan dulu!");
            return;
        }

        // Reset Form
        txtKuantitas.clear();
        txtDateProduk.setValue(LocalDate.now());
        chcTipe.getItems().clear();

        // logika penentuan tipe produk (Sapi/Ayam & Jantan/Betina)
        String jenis = selected.getJenis();     // "Sapi" atau "Ayam"
        String kelamin = selected.getKelamin(); // "Jantan" atau "Betina"

        if (jenis.equalsIgnoreCase("Sapi")) {
            if (kelamin.equalsIgnoreCase("Jantan")) {
                chcTipe.getItems().add("Daging");
            } else { // Betina
                chcTipe.getItems().addAll("Daging", "Susu");
            }
        } else if (jenis.equalsIgnoreCase("Ayam")) {
            if (kelamin.equalsIgnoreCase("Jantan")) {
                chcTipe.getItems().add("Daging");
            } else { // Betina
                chcTipe.getItems().addAll("Telur", "Daging");
            }
        }
        
        // Default pilih item pertama
        if (!chcTipe.getItems().isEmpty()) {
            chcTipe.setValue(chcTipe.getItems().get(0));
        }

        // Tampilkan Popup
        displayPanenProduk.setVisible(true);
        // ... (Validasi selectedHewan dll) ...
        
        // Di bagian paling bawah method ini (saat popup ditampilkan):
        displayPanenProduk.setVisible(true);
        tvHewan.setDisable(true);
    }

    @FXML
    private void handleSubmitProduk(ActionEvent event) {
       // 1. Cek Hewan Terpilih
        Hewan selectedHewan = tvHewan.getSelectionModel().getSelectedItem();
        if (selectedHewan == null) {
            System.out.println("Pilih dulu hewan di tabel!");
            return;
        }

        try {
            // Ambil Data dari Form Popup
            String kuantitasStr = txtKuantitas.getText();
            String tipe = chcTipe.getValue();
            
            // Validasi Input Kosong
            if (kuantitasStr.isEmpty() || txtDateProduk.getValue() == null || tipe == null) {
                System.out.println("Data harus lengkap (Kuantitas, Tanggal, Tipe)!");
                return;
            }

            int id_hewan = selectedHewan.getId();
            String tanggal = txtDateProduk.getValue().toString();
            double kuantitass = Double.parseDouble(kuantitasStr);
            String kualitas = "Belum diisi"; 

            // Simpan ke Database Produk
            Produk baru = new Produk(tanggal, tipe, kuantitass, kualitas, id_hewan);
            produkDAO.addProduk(baru);
            
            System.out.println("Tipe produk: " + tipe);
            showFancyAlert("SUCCESS","Sukses", "Produk berhasil ditambakan " + tipe);

            // (Update Kondisi Hewan jadi Death)
            if ("daging".equalsIgnoreCase(tipe)) {
                // Update di Database
                hewanDAO.updateKondisi(id_hewan, "Death");
               
                selectedHewan.setKondisi("Death");
            }
            
            //Refresh Tabel & Tutup Popup
            loadDataFromDatabase(); // Refresh agar hewan yg "Death" hilang
            
            // Tutup popup setelah berhasil
            if (displayPanenProduk != null) {
                displayPanenProduk.setVisible(false);
            }
            txtKuantitas.clear();
            tvHewan.setDisable(false);
            
        } catch (NumberFormatException e) {
            System.out.println("Kuantitas harus berupa angka!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
    }


    @FXML
    private void handleActionEdit(ActionEvent event) {
        Hewan selected = tvHewan.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setJenis(chcJenis.getValue());
                selected.setKelamin(chcKelamin.getValue());
                selected.setBerat(Double.parseDouble(txtBerat.getText()));
                selected.setUsia(Integer.parseInt(txtUsia.getText()));
                selected.setKondisi(txtKondisi.getText());
                selected.setPenyakit(txtPenyakit.getText());

                hewanDAO.update(selected); 
                showFancyAlert("SUCCESS","Sukses", "Data Berhasil Diupdate!");
                
            } catch (Exception e) {
                showFancyAlert("ERROR","Waduh Gagal!", "Gagal update: " + e.getMessage());
            } finally {
                loadDataFromDatabase();
                clearForm();
                if (displayAddHewan != null) displayAddHewan.setVisible(false);
                tvHewan.setDisable(false); // 
            }
        }
    }

    @FXML
    private void handleBackAddHewan(ActionEvent event) {
        if (displayAddHewan != null) {
            displayAddHewan.setVisible(false);
        }
        clearForm();
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
            String jenis = chcJenis.getValue();
            String kelamin = chcKelamin.getValue();
            String beratStr = txtBerat.getText();
            String usiaStr = txtUsia.getText();
            String kondisi = txtKondisi.getText();
            String penyakit = txtPenyakit.getText();

            // Validasi
            if (beratStr.isEmpty() || usiaStr.isEmpty() || kondisi.isEmpty()) {
                showFancyAlert("WARNING","Error", "Mohon isi Berat, Usia, dan Kondisi!");
                return;
            }

            double berat = Double.parseDouble(beratStr);
            int usia = Integer.parseInt(usiaStr);

            // Buat Objek
            Hewan hewanBaru = new Hewan(jenis, kelamin, berat, usia, kondisi, penyakit);
            
            // Simpan ke DB
            hewanDAO.add(hewanBaru);
            
            // Refresh
            loadDataFromDatabase();
            clearForm();

            if (displayAddHewan != null) {
                displayAddHewan.setVisible(false);
            }
            tvHewan.setDisable(false); 
            displayAddHewan.setVisible(false); // Tutup popup
            
            showFancyAlert("SUCCESS","Sukses", "Data Hewan Berhasil Ditambahkan!");

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
    }

    @FXML
    void handleActionToPakan(ActionEvent event) {
        try {
            // 1. Ambil Stage (Layar) dari tombol btnProduk
            javafx.stage.Stage stage = (javafx.stage.Stage) btnProduk.getScene().getWindow();

            // 2. Cari file FXML tujuan
            java.io.File file = new java.io.File("src/main/java/Views/PemberianPakanDashboard.fxml");

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
    @FXML
    void btnLogout(ActionEvent event) {

    }
}
