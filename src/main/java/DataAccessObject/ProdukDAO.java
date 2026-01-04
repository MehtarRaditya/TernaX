package DataAccessObject;

import Models.Hewan;
import Models.Karyawan;
import Models.KatalogProduk;
import Models.Produk;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.DatabaseConnection;
import utility.Session;

public class ProdukDAO {
    public int getJumlahProdukByJenis(String jenis) {
        String sql = "SELECT COUNT(*) FROM detail_produk WHERE jenis = ?";
        int jumlah = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Gunakan PreparedStatement untuk mencegah SQL Injection
            pstmt.setString(1, jenis);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    jumlah = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error saat mengambil jumlah ternak (" + jenis + "): " + e.getMessage());
        }

        return jumlah;
    }

    public boolean addProduk(Produk detail_produk) {
        Karyawan karyawan = Session.getLoggedInKaryawan();
        if (karyawan == null) {
            System.err.println("Gagal menambah: belum ada karyawan login.");
            return false;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        // CUMA INSERT KE TABEL TRANSAKSI (PRODUK)
        // Status awal 'Pending'. Stok Gudang JANGAN disentuh dulu!
        String sqlInsert = "INSERT INTO detail_produk (id_produk, id_hewan, tanggal_diperoleh, kuantitas, status_kelayakan) VALUES (?, ?, ?, ?, ?)";

        try {
            conn = DatabaseConnection.getConnection();

            // Masukkan Data Panen
            pstmt = conn.prepareStatement(sqlInsert);
            pstmt.setInt(1, detail_produk.getIdKatalog());
            pstmt.setInt(2, detail_produk.getIdHewan());
            pstmt.setString(3, detail_produk.getTanggalDiperoleh());
            pstmt.setDouble(4, detail_produk.getKuantitas());
            pstmt.setString(5, "Pending");

            // Eksekusi Simpan
            int rowsInserted = pstmt.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Gagal Input Panen: " + e.getMessage());
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    public double getStokById(Connection conn, String detail_produkId) throws SQLException {
        String sql = "SELECT kuantitas FROM detail_produk WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setId(ps, 1, detail_produkId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("kuantitas");
                }
            }
        }

        throw new SQLException("Produk tidak ditemukan (id=" + detail_produkId + ")");
    }

    public void kurangiStok(Connection conn, String detail_produkId, double qty) throws SQLException {
        String sql = """
            UPDATE detail_produk
            SET kuantitas = kuantitas - ?
            WHERE id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, qty);
            setId(ps, 2, detail_produkId);
            ps.executeUpdate();
        }
    }

    private void setId(PreparedStatement ps, int index, String id) throws SQLException {
        if (id != null && id.matches("\\d+")) {
            ps.setInt(index, Integer.parseInt(id));
        } else {
            ps.setString(index, id);
        }
    }

    // 1. AMBIL LIST MENU (Buat Isi ComboBox)
    public List<KatalogProduk> getKatalogList() {
        List<KatalogProduk> list = new ArrayList<>();
        // Sesuaikan nama kolom dengan tabel katalog_produk kamu
        String sql = "SELECT id, nama_produk, satuan, harga_satuan,total_stok FROM produk";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(new KatalogProduk(
                        rs.getInt("id"),
                        rs.getString("nama_produk"),
                        rs.getString("satuan"),
                        rs.getDouble("harga_satuan"),
                        rs.getDouble("total_stok")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. AMBIL RIWAYAT (Pakai JOIN biar nama detail_produk muncul)
    public List<Produk> getAllRiwayat() {
        List<Produk> list = new ArrayList<>();

        String sql = "SELECT t.*, k.nama_produk, k.satuan " +
                "FROM detail_produk t " +
                "JOIN produk k ON t.id = k.id " +
                "ORDER BY t.tanggal_diperoleh DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Pakai Constructor TransaksiPanen yang LENGKAP
                list.add(new Produk(
                        rs.getInt("id"),
                        rs.getInt("id_produk"),
                        rs.getInt("id_hewan"),
                        rs.getString("tanggal_diperoleh"),
                        rs.getDouble("kuantitas"),
                        rs.getString("status_kelayakan"),
                        rs.getString("nama_produk"), // <--- Masuk Sini
                        rs.getString("satuan")       // <--- Masuk Sini
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    // Method untuk mengambil semua riwayat panen + Nama Produknya
    public List<Produk> getAll() {
        List<Produk> list = new ArrayList<>();

        // SQL JOIN YANG BENAR:
        // t = tabel transaksi (namanya 'produk' di database kamu)
        // k = tabel katalog (namanya 'detail_produk' di database kamu)
        String sql = "SELECT t.id, t.id_produk, t.id_hewan, t.tanggal_diperoleh, t.kuantitas, t.status_kelayakan, " +
                "k.nama_produk, k.satuan " +
                "FROM detail_produk t " +
                "JOIN produk k ON t.id_produk = k.id " + // <--- INI KUNCI JOINNYA
                "ORDER BY t.tanggal_diperoleh DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Pastikan urutan parameter sesuai dengan Constructor Produk di Models/Produk.java
                list.add(new Produk(
                        rs.getInt("id"),                  // ID Transaksi (Primary Key)
                        rs.getInt("id_produk"),           // ID Katalog (Foreign Key)
                        rs.getInt("id_hewan"),            // ID Hewan
                        rs.getString("tanggal_diperoleh"),// Tanggal
                        rs.getDouble("kuantitas"),        // Jumlah
                        rs.getString("status_kelayakan"), // Status
                        rs.getString("nama_produk"),      // <--- NAMA (Dari JOIN)
                        rs.getString("satuan")            // <--- SATUAN (Dari JOIN)
                ));
            }
        } catch (SQLException e) {
            System.err.println("Gagal load data: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // [BARU] Ambil hanya barang yang statusnya 'Pending' untuk QC
    public List<Produk> getPendingList() {
        List<Produk> list = new ArrayList<>();
        String sql = "SELECT t.id, t.id_produk, t.id_hewan, t.tanggal_diperoleh, t.kuantitas, t.status_kelayakan, " +
                "k.nama_produk, k.satuan " +
                "FROM detail_produk t " +
                "JOIN produk k ON t.id_produk = k.id " +
                "WHERE t.status_kelayakan = 'Pending' " +  // Filter Pending
                "ORDER BY t.tanggal_diperoleh ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Produk(
                        rs.getInt("id"),
                        rs.getInt("id_produk"),
                        rs.getInt("id_hewan"),
                        rs.getString("tanggal_diperoleh"),
                        rs.getDouble("kuantitas"),
                        rs.getString("status_kelayakan"),
                        rs.getString("nama_produk"),
                        rs.getString("satuan")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Update Status QC + Logika Tambah Stok jika Layak
    public boolean updateStatusQC(int idTransaksi, String statusBaru) {
        // 1. Ambil Karyawan Logistik yang sedang Login
        Karyawan karyawanLogistik = Session.getLoggedInKaryawan();
        if (karyawanLogistik == null) {
            System.err.println("Gagal Update QC: Tidak ada user logistik login.");
            return false;
        }

        Connection conn = null;
        PreparedStatement psUpdateStatus = null;
        PreparedStatement psGetInfo = null;
        PreparedStatement psUpdateStok = null;
        ResultSet rs = null;

        // Query 1: Update Status & Pemeriksa
        String sqlStatus = "UPDATE detail_produk SET status_kelayakan = ?, pemeriksa = ? WHERE id = ?";

        // Query 2: Ambil Info Produk (ID Katalog & Kuantitas) untuk update stok
        String sqlGetInfo = "SELECT id_produk, kuantitas FROM detail_produk WHERE id = ?";

        // Query 3: Tambah Stok ke Gudang Master
        String sqlStok = "UPDATE produk SET total_stok = total_stok + ? WHERE id = ?";

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Mulai Transaksi

            // --- LANGKAH A: Update Status & Pemeriksa ---
            psUpdateStatus = conn.prepareStatement(sqlStatus);
            psUpdateStatus.setString(1, statusBaru);
            psUpdateStatus.setInt(2, Integer.parseInt(karyawanLogistik.getId()));
            psUpdateStatus.setInt(3, idTransaksi);

            int affected = psUpdateStatus.executeUpdate();
            if (affected == 0) {
                conn.rollback();
                return false;
            }

            // --- LANGKAH B: Jika Status "Layak", Tambah Stok ---
            if (statusBaru.equalsIgnoreCase("Layak")) {

                // Cari tahu dulu: Produk apa ini? Berapa banyak?
                psGetInfo = conn.prepareStatement(sqlGetInfo);
                psGetInfo.setInt(1, idTransaksi);
                rs = psGetInfo.executeQuery();

                if (rs.next()) {
                    int idKatalog = rs.getInt("id_produk");
                    double qtyPanen = rs.getDouble("kuantitas");

                    // Tambahkan ke tabel produk (Gudang)
                    psUpdateStok = conn.prepareStatement(sqlStok);
                    psUpdateStok.setDouble(1, qtyPanen);
                    psUpdateStok.setInt(2, idKatalog);
                    psUpdateStok.executeUpdate();
                }
            }

            conn.commit(); // Simpan Permanen
            return true;

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            System.err.println("Gagal QC & Update Stok: " + e.getMessage());
            return false;
        } finally {
            // Tutup Resource dengan Rapi
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (psUpdateStatus != null) psUpdateStatus.close(); } catch (Exception e) {}
            try { if (psGetInfo != null) psGetInfo.close(); } catch (Exception e) {}
            try { if (psUpdateStok != null) psUpdateStok.close(); } catch (Exception e) {}
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (Exception e) {}
        }
    }

    public List<KatalogProduk> getProdukSiapJual() {
        List<KatalogProduk> list = new ArrayList<>();

        // Query Sederhana & Benar: Ambil langsung dari tabel master gudang
        // Filter: Hanya tampilkan jika stok > 0 (Opsional, biar menu gak penuh barang kosong)
        String sql = "SELECT id, nama_produk, satuan, harga_satuan, total_stok " +
                "FROM produk " +
                "WHERE total_stok > 0 " +
                "ORDER BY nama_produk ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(new KatalogProduk(
                        rs.getInt("id"),
                        rs.getString("nama_produk"),
                        rs.getString("satuan"),
                        rs.getDouble("harga_satuan"),
                        rs.getDouble("total_stok") // <--- Ini stok REAL (71.0, 50.0, dll)
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error load katalog kasir: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Produk> getAllForManager() {
        List<Produk> list = new ArrayList<>();

        // PERBAIKAN SQL SESUAI STRUKTUR DB KAMU:
        // 1. Kolom foreign key di tabel detail_produk namanya 'pemeriksa'
        // 2. Kita JOIN ke tabel karyawan berdasarkan 't.pemeriksa = kar.id'
        String sql = "SELECT t.id, t.id_produk, t.id_hewan, t.tanggal_diperoleh, t.kuantitas, t.status_kelayakan, "
                + "k.nama_produk, k.satuan, "
                + "kar.nama AS nama_pemeriksa "
                + // Ambil nama dari tabel karyawan
                "FROM detail_produk t "
                + "JOIN produk k ON t.id_produk = k.id "
                + "LEFT JOIN karyawan kar ON t.pemeriksa = kar.id "
                + // <--- PERBAIKAN DI SINI (t.pemeriksa)
                "ORDER BY t.tanggal_diperoleh DESC";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Masukkan data dasar ke Constructor
                Produk p = new Produk(
                        rs.getInt("id"),
                        rs.getInt("id_produk"),
                        rs.getInt("id_hewan"),
                        rs.getString("tanggal_diperoleh"),
                        rs.getDouble("kuantitas"),
                        rs.getString("status_kelayakan"),
                        rs.getString("nama_produk"),
                        rs.getString("satuan")
                );

                // --- LOGIKA MENGISI NAMA PEMERIKSA ---
                // 1. Ambil nama dari query SQL (pastikan di SQL-nya sudah: kar.nama AS nama_pemeriksa)
                String namaOrang = rs.getString("nama_pemeriksa");

                // 2. Bikin Objek Karyawan Sementara
                Karyawan kDummy = new Karyawan();

                // 3. Masukkan nama ke objek tersebut
                if (namaOrang != null) {
                    kDummy.setName(namaOrang); // Pakai method setName() dari Karyawan.java kamu
                } else {
                    kDummy.setName("Tidak Diketahui");
                }

                // 4. Masukkan Objek Karyawan ke Produk
                p.setIdKaryawan(kDummy); // Aman, karena tipe datanya sama-sama Karyawan

                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Gagal load data panen: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // --- [KHUSUS MANAGER] AMBIL STOK REAL-TIME DARI GUDANG (detail_produk) ---
    public double getTotalStokRealTime(String keyword) {
        double total = 0;
        // Kita cari di produk karena ini stok yang 'Ready'
        String sql = "SELECT SUM(total_stok) FROM produk WHERE nama_produk LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%"); // Cari yang mengandung kata kunci (misal: "Susu")

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal hitung stok real-time (" + keyword + "): " + e.getMessage());
        }
        return total;
    }

    // --- [KHUSUS MANAGER] HITUNG JUMLAH ITEM BERDASARKAN KUALITAS (produk) ---
    public int getJumlahStatusQC(String status, boolean isEqual) {
        int count = 0;
        String sql;

        if (isEqual) {
            // Hitung yang statusnya SAMA DENGAN "Layak"
            sql = "SELECT COUNT(*) FROM detail_produk WHERE status_kelayakan = ?";
        } else {
            // Hitung yang statusnya BUKAN "Layak" (Pending, Buang, dll)
            sql = "SELECT COUNT(*) FROM detail_produk WHERE status_kelayakan != ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal hitung status QC: " + e.getMessage());
        }
        return count;
    }

    // Method Baru: Ambil riwayat panen KHUSUS milik Peternak tertentu
    // (Caranya: JOIN ke tabel hewan -> cek kolom 'pemilik' di tabel hewan)
    public List<Produk> getAllRiwayatByPeternak(String idPeternak) {
        List<Produk> list = new ArrayList<>();

        // SQL Filter: Ambil detail_produk, JOIN ke produk (buat nama), JOIN ke hewan (buat cek pemilik)
        String sql = "SELECT p.*, k.nama_produk, k.satuan " +
                "FROM detail_produk p " +
                "JOIN produk k ON p.id_produk = k.id " +
                "JOIN hewan h ON p.id_hewan = h.id " +
                "WHERE h.pemilik = ? " + // <--- FILTER PEMILIK HEWAN
                "ORDER BY p.tanggal_diperoleh DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idPeternak); // Set ID User Login

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Produk(
                            rs.getInt("id"),
                            rs.getInt("id_produk"),
                            rs.getInt("id_hewan"),
                            rs.getString("tanggal_diperoleh"),
                            rs.getDouble("kuantitas"),
                            rs.getString("status_kelayakan"),
                            rs.getString("nama_produk"),
                            rs.getString("satuan")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal ambil riwayat panen user: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}