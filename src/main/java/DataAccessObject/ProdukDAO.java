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
        String sql = "SELECT COUNT(*) FROM produk WHERE jenis = ?";
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
    
    public boolean addProduk(Produk produk) {
            Karyawan karyawan = Session.getLoggedInKaryawan();
            //Hewan hewan = new Hewan();
             if (karyawan == null) {
        System.err.println("Gagal menambah Hewan: belum ada karyawan yang login.");
        return false;
    }
             // Definisi Resource di luar try agar bisa di-close di finally
            Connection conn = null;
            PreparedStatement pstmt = null;
            PreparedStatement psUpdate = null;
            
            String sqlInsert = "INSERT INTO produk (id_produk,id_hewan,tanggal_diperoleh,kuantitas,status_kelayakan,pemeriksa) VALUES (?, ?, ?, ?,?,?)";
            String sqlUpdate = "UPDATE detail_produk SET total_stok = total_stok + ? WHERE id = ?";
            try  {
                conn = DatabaseConnection.getConnection();
                conn.setAutoCommit(false);
                pstmt = conn.prepareStatement(sqlInsert);
                pstmt.setInt(1, produk.getIdKatalog());
                pstmt.setInt(2, produk.getIdHewan());
                pstmt.setString(3, produk.getTanggalDiperoleh());
                pstmt.setDouble(4, produk.getKuantitas());
                pstmt.setString(5, "Pending");
                pstmt.setString(6, karyawan.getId());
                pstmt.executeUpdate();
                
                int rowsInserted = pstmt.executeUpdate();
                
                // --- EKSEKUSI UPDATE STOK ---
        psUpdate = conn.prepareStatement(sqlUpdate);
        psUpdate.setDouble(1, produk.getKuantitas()); // Tambah Stok sejumlah panen
        psUpdate.setInt(2, produk.getIdKatalog());    // Cari berdasarkan ID Produk
        
        psUpdate.executeUpdate(); // Eksekusi UPDATE

        // 2. Kalau semua lancar, COMMIT (Simpan Permanen)
        conn.commit();
        
        return rowsInserted > 0;
                
            } catch (SQLException e) {
        // 3. Jika ada error, ROLLBACK (Batalkan Semua)
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        System.err.println("Gagal Transaction: " + e.getMessage());
        return false;
    } finally {
        // 4. Tutup Koneksi & Statement dengan Rapi
        try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
        try { if (psUpdate != null) psUpdate.close(); } catch (Exception e) {}
        try { 
            if (conn != null) { 
                conn.setAutoCommit(true); // Kembalikan ke default
                conn.close(); 
            } 
        } catch (Exception e) {}
    }
        }
       
    public double getStokById(Connection conn, String produkId) throws SQLException {
        String sql = "SELECT kuantitas FROM produk WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setId(ps, 1, produkId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("kuantitas");
                }
            }
        }

        throw new SQLException("Produk tidak ditemukan (id=" + produkId + ")");
    }
    
    public void kurangiStok(Connection conn, String produkId, double qty) throws SQLException {
        String sql = """
            UPDATE produk
            SET kuantitas = kuantitas - ?
            WHERE id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, qty);
            setId(ps, 2, produkId);
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
        String sql = "SELECT id, nama_produk, satuan, harga_satuan,total_stok FROM detail_produk"; 
        
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
    
    // 3. AMBIL RIWAYAT (Pakai JOIN biar nama produk muncul)
    public List<Produk> getAllRiwayat() {
        List<Produk> list = new ArrayList<>();
        
        String sql = "SELECT t.*, k.nama_produk, k.satuan " +
                     "FROM produk t " +
                     "JOIN detail_produk k ON t.id = k.id " +
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
                     "FROM produk t " +
                     "JOIN detail_produk k ON t.id_produk = k.id " + // <--- INI KUNCI JOINNYA
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
                     "FROM produk t " +
                     "JOIN detail_produk k ON t.id_produk = k.id " +
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

    // [BARU] Update Status Kelayakan
    public boolean updateStatusQC(int idTransaksi, String statusBaru) {
        String sql = "UPDATE produk SET status_kelayakan = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, statusBaru);
            ps.setInt(2, idTransaksi);
            
            int affected = ps.executeUpdate();
            return affected > 0;
            
        } catch (SQLException e) {
            System.err.println("Gagal Update Status QC: " + e.getMessage());
            return false;
        }
        
        
    }
    
    public List<KatalogProduk> getProdukSiapJual() {
        List<KatalogProduk> list = new ArrayList<>();
        
        // LOGIKA SQL:
        // 1. Gabungkan tabel 'detail_produk' (katalog) dengan 'produk' (hasil panen)
        // 2. Filter HANYA yang status_kelayakan = 'Layak'
        // 3. Jumlahkan (SUM) kuantitasnya -> jadi total_stok_layak
        // 4. Kelompokkan (GROUP BY) berdasarkan jenis produknya
        
        String sql = "SELECT k.id, k.nama_produk, k.satuan, k.harga_satuan, " +
                     "COALESCE(SUM(p.kuantitas), 0) as total_stok_layak " +
                     "FROM detail_produk k " +
                     "LEFT JOIN produk p ON k.id = p.id_produk AND p.status_kelayakan = 'Layak' " +
                     "GROUP BY k.id, k.nama_produk, k.satuan, k.harga_satuan";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Pastikan kamu punya Constructor di KatalogProduk yang urutannya:
                // (id, nama, satuan, harga, stok)
                KatalogProduk kp = new KatalogProduk(
                    rs.getInt("id"),
                    rs.getString("nama_produk"),
                    rs.getString("satuan"),
                    rs.getDouble("harga_satuan"),
                    rs.getDouble("total_stok_layak") // Ini hasil penjumlahan yang LAYAK saja
                );
                
                // Opsional: Kalau mau yang stok 0 tidak usah tampil, aktifkan if ini
                // if (kp.getStok() > 0) {
                    list.add(kp);
                // }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil produk siap jual: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}