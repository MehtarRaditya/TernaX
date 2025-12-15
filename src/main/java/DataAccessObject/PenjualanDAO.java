package DataAccessObject;

import Models.Karyawan;
import Models.PenjualanDetail;
import java.sql.*;
import javafx.collections.ObservableList;
import utility.DatabaseConnection;
import utility.Session;

public class PenjualanDAO {

    private final ProdukDAO produkDAO = new ProdukDAO();

    public void checkout(ObservableList<PenjualanDetail> details) throws SQLException {
        if (details == null || details.isEmpty()) {
            throw new SQLException("Keranjang kosong!");
        }

        Karyawan karyawan = Session.getLoggedInKaryawan();
        if (karyawan == null) {
            throw new SQLException("Belum ada karyawan yang login!");
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // ✅ 1) Insert penjualan + ambil ID (PAKAI CONNECTION YANG SAMA)
            int penjualanId = insertPenjualan(conn, karyawan.getId());

            // ✅ 2) Insert detail + kurangi stok
            for (PenjualanDetail d : details) {
                String produkId = d.getProduk().getId();
                double qty = d.getQty();

                if (qty <= 0) {
                    throw new SQLException("Qty harus > 0");
                }

                double stokDb = produkDAO.getStokById(conn, produkId);
                if (qty > stokDb) {
                    throw new SQLException("Stok tidak cukup untuk produk id=" + produkId +
                            " (stok=" + stokDb + ", diminta=" + qty + ")");
                }

                insertDetail(conn, penjualanId, produkId, qty);
                produkDAO.kurangiStok(conn, produkId, qty);
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    // ✅ Insert header penjualan dan return id_penjualan
    private int insertPenjualan(Connection conn, String karyawanId) throws SQLException {
        String sql = "INSERT INTO penjualan (karyawan_penjual) VALUES (?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // id karyawan kamu String, kita handle aman
            if (karyawanId != null && karyawanId.matches("\\d+")) {
                ps.setInt(1, Integer.parseInt(karyawanId));
            } else {
                ps.setString(1, karyawanId);
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        throw new SQLException("Gagal ambil id penjualan");
    }

    private void insertDetail(Connection conn, int penjualanId, String produkId, double qty) throws SQLException {
        String sql = "INSERT INTO detail_penjualan (penjualan_id, produk_id, kuantitas) VALUES (?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, penjualanId);

            // produk_id String/INT aman
            if (produkId != null && produkId.matches("\\d+")) {
                ps.setInt(2, Integer.parseInt(produkId));
            } else {
                ps.setString(2, produkId);
            }

            ps.setDouble(3, qty);
            ps.executeUpdate();
        }
    }
}

//package DataAccessObject;
//
//import DataAccessObject.ProdukDAO;
//import Models.Karyawan;
//import Models.PenjualanDetail;
//import java.sql.*;
//import java.util.List;
//import javafx.collections.ObservableList;
//import utility.DatabaseConnection;
//import utility.Session;
//
//public class PenjualanDAO {
//
//    private final ProdukDAO produkDAO = new ProdukDAO();
//    public void checkout(ObservableList<PenjualanDetail> details) throws SQLException {
//    if (details == null || details.isEmpty()) {
//        throw new SQLException("Keranjang kosong!");
//    }
//
//    // ✅ Ambil karyawan yang login dari Session
//    Karyawan karyawan = Session.getLoggedInKaryawan();
//    int karyawanId = Integer.parseInt(karyawan.getId());
//    if (karyawan == null) {
//        throw new SQLException("Belum ada karyawan yang login!");
//    }
//
//    Connection conn = null;
//    try {
//        conn = DatabaseConnection.getConnection();
//        conn.setAutoCommit(false);
//
////        int penjualanId = insertPenjualan(conn, karyawanId);
//
//        for (PenjualanDetail d : details) {
//            String produkId = d.getProduk().getId();
//            double qty = d.getQty();
//
//            if (qty <= 0) {
//                throw new SQLException("Qty harus > 0");
//            }
//
//            // ✅ validasi stok real dari DB
//            double stokDb = produkDAO.getStokById(conn, produkId);
//            if (qty > stokDb) {
//                throw new SQLException("Stok tidak cukup untuk produk id=" + produkId
//                        + " (stok=" + stokDb + ", diminta=" + qty + ")");
//            }
//
//            insertDetail(conn, penjualanId, produkId, qty);
//            produkDAO.kurangiStok(conn, produkId, qty);
//        }
//
//        conn.commit();
//    } catch (SQLException e) {
//        if (conn != null) conn.rollback();
//        throw e;
//    } finally {
//        if (conn != null) {
//            conn.setAutoCommit(true);
//            conn.close();
//        }
//    }
//}
//
//
//    public int InsertPenjualan() throws SQLException {
//    Karyawan karyawan = Session.getLoggedInKaryawan();
//    if (karyawan == null) {
//        throw new SQLException("Belum login");
//    }
//
//    String sql = "INSERT INTO penjualan (karyawan_id) VALUES (?)";
//
//    try (Connection conn = DatabaseConnection.getConnection();
//         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//
//        ps.setString(1, karyawan.getId());
//        ps.executeUpdate();
//
//        ResultSet rs = ps.getGeneratedKeys();
//        if (rs.next()) {
//            return rs.getInt(1); // id penjualan
//        }
//    }
//
//    throw new SQLException("Gagal insert penjualan");
//}
//
//
//    private void insertDetail(Connection conn, int penjualanId, String produkId, double qty) throws SQLException {
//        String sql = "INSERT INTO detail_penjualan (penjualan_id, produk_id, qty) VALUES (?,?,?)";
//        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, penjualanId);
//
//            // produk_id bisa INT atau VARCHAR, kita handle
//            if (produkId != null && produkId.matches("\\d+")) {
//                ps.setInt(2, Integer.parseInt(produkId));
//            } else {
//                ps.setString(2, produkId);
//            }
//
//            ps.setDouble(3, qty);
//            ps.executeUpdate();
//        }
//    }
//}
