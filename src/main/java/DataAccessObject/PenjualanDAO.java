package DataAccessObject;


import Models.KeranjangItem;
import Models.Penjualan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utility.DatabaseConnection;
import utility.Session;

public class PenjualanDAO {

    // Method Transaksi: Simpan Struk, Simpan Detail, & Potong Stok
    public boolean simpanTransaksi(List<KeranjangItem> keranjang, double total, double bayar, double kembalian, String tanggal) {
        Connection conn = null;
        PreparedStatement psJual = null;
        PreparedStatement psDetail = null;
        PreparedStatement psUpdateStok = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            
            // 1. Matikan Auto Commit (Mulai Transaksi)
            conn.setAutoCommit(false);

            // -------------------------------------------------
            // LANGKAH A: Simpan ke Tabel 'penjualan' (Header)
            // -------------------------------------------------
            String sqlJual = "INSERT INTO penjualan (total_harga, uang_dibayar, kembalian, id_karyawan, tanggal) VALUES (?, ?, ?, ?, ?)";
            
            psJual = conn.prepareStatement(sqlJual, Statement.RETURN_GENERATED_KEYS);
            psJual.setDouble(1, total);
            psJual.setDouble(2, bayar);
            psJual.setDouble(3, kembalian);
            psJual.setInt(4, Session.getLoggedInKaryawan().getId());
            
            // Masukkan tanggal yang dikirim dari Controller
            psJual.setString(5, tanggal); 

            psJual.executeUpdate();

            // Ambil ID Transaksi yang baru dibuat (Misal: Struk #101)
            int idTransaksi = 0;
            rs = psJual.getGeneratedKeys();
            if (rs.next()) {
                idTransaksi = rs.getInt(1);
            }

            // -------------------------------------------------
            // LANGKAH B: Loop Keranjang -> Simpan Detail & Potong Stok
            // -------------------------------------------------
            String sqlDetail = "INSERT INTO detail_penjualan (id_penjualan, id_katalog, harga_deal, kuantitas, subtotal) VALUES (?, ?, ?, ?, ?)";
            String sqlStok = "UPDATE detail_produk SET total_stok = total_stok - ? WHERE id = ?";
            
            psDetail = conn.prepareStatement(sqlDetail);
            psUpdateStok = conn.prepareStatement(sqlStok);

            for (KeranjangItem item : keranjang) {
                // Masukkan ke detail_penjualan
                psDetail.setInt(1, idTransaksi);
                psDetail.setInt(2, item.getIdProduk());
                psDetail.setDouble(3, item.getHarga());
                psDetail.setDouble(4, item.getQty());
                psDetail.setDouble(5, item.getSubtotal());
                psDetail.addBatch(); // Kumpulkan antrian query

                // Potong Stok di katalog_produk
                psUpdateStok.setDouble(1, item.getQty());
                psUpdateStok.setInt(2, item.getIdProduk());
                psUpdateStok.addBatch();
            }

            // Eksekusi semua batch
            psDetail.executeBatch();
            psUpdateStok.executeBatch();

            // -------------------------------------------------
            // LANGKAH C: COMMIT (Simpan Permanen)
            // -------------------------------------------------
            conn.commit();
            return true;

        } catch (Exception e) {
            // Jika ada error, BATALKAN SEMUA (Rollback)
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            // Tutup koneksi manual karena kita main transaction
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (psJual != null) psJual.close(); } catch (Exception e) {}
            try { if (psDetail != null) psDetail.close(); } catch (Exception e) {}
            try { if (psUpdateStok != null) psUpdateStok.close(); } catch (Exception e) {}
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (Exception e) {}
        }
    }
    
    // Method untuk mengambil Riwayat Penjualan (Data Gabungan untuk Tabel)
    public List<Penjualan> getAll() { 
        List<Penjualan> list = new ArrayList<>();

        // REVISI SQL SESUAI STRUKTUR DATABASE KAMU:
        // 1. Ganti 'p.tanggal_transaksi' -> 'p.tanggal'
        // 2. Hapus 'prod.kategori' (karena tidak ada) -> Ganti jadi 'prod.satuan' biar ada isinya
        String sql = "SELECT p.id, p.tanggal, " +
                     "dp.kuantitas, dp.harga_deal, dp.subtotal, " +
                     "prod.nama_produk, prod.satuan " + 
                     "FROM penjualan p " +
                     "JOIN detail_penjualan dp ON p.id = dp.id_penjualan " +
                     "JOIN detail_produk prod ON dp.id_katalog = prod.id " +
                     "ORDER BY p.tanggal DESC"; // Urutkan dari yang terbaru

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Penjualan jual = new Penjualan();

                // 1. ID Penjualan
                jual.setId(rs.getInt("id"));
                
                // 2. Tanggal (Perbaikan nama kolom)
                if (rs.getTimestamp("tanggal") != null) {
                    jual.setTanggalTransaksi(rs.getTimestamp("tanggal").toString());
                }

                // 3. Nama Produk
                jual.setNamaProduk(rs.getString("nama_produk"));

                // 4. Kategori (SOLUSI CERDAS: Kita isi pakai 'Satuan' karena kolom Kategori tidak ada)
                // Jadi nanti di tabel munculnya: "Kg", "Liter", "Ekor", dll.
                jual.setKategori(rs.getString("satuan")); 

                // 5. Kuantitas (Ubah ke Double sesuai permintaanmu sebelumnya)
                // Meskipun di DB mungkin int, kita cast ke double biar masuk ke Model Penjualan
                jual.setKuantitas((int) (double) rs.getInt("kuantitas"));

                // 6. Harga Deal
                jual.setHargaSatuan(rs.getDouble("harga_deal"));

                // 7. Subtotal
                jual.setTotalHarga(rs.getDouble("subtotal"));

                list.add(jual);
            }

        } catch (SQLException e) {
            System.err.println("Gagal load data penjualan: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }
}