package DataAccessObject;


import Models.KeranjangItem;
import java.sql.*;
import java.util.List;
import utility.DatabaseConnection;
import utility.Session;

public class PenjualanDAO {

    // Method Transaksi: Simpan Struk, Simpan Detail, & Potong Stok
    public boolean simpanTransaksi(List<KeranjangItem> keranjang, double total, double bayar, double kembalian) {
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
            String sqlJual = "INSERT INTO penjualan (total_harga, uang_dibayar, kembalian, id_karyawan) VALUES (?, ?, ?, ?)";
            psJual = conn.prepareStatement(sqlJual, Statement.RETURN_GENERATED_KEYS);
            psJual.setDouble(1, total);
            psJual.setDouble(2, bayar);
            psJual.setDouble(3, kembalian);
            psJual.setString(4, Session.getLoggedInKaryawan().getId());
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
}