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

    // ===============================
    // JUMLAH PRODUK PER JENIS
    // ===============================
    public int getJumlahProdukByJenis(String jenis) {
        String sql = "SELECT COUNT(*) FROM detail_produk WHERE jenis = ?";
        int jumlah = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, jenis);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    jumlah = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error saat mengambil jumlah produk (" + jenis + "): " + e.getMessage());
        }
        return jumlah;
    }

    // ===============================
    // INSERT TRANSAKSI PRODUK
    // ===============================
    public boolean addProduk(Produk produk) {
        Karyawan karyawan = Session.getLoggedInKaryawan();
        if (karyawan == null) {
            System.err.println("Gagal menambah Produk: belum ada karyawan login.");
            return false;
        }

        Connection conn = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;

        String sqlInsert =
                "INSERT INTO detail_produk (id_produk, id_hewan, tanggal_diperoleh, kuantitas, status_kelayakan, pemeriksa) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlUpdate =
                "UPDATE produk SET total_stok = total_stok + ? WHERE id = ?";

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            psInsert = conn.prepareStatement(sqlInsert);
            psInsert.setInt(1, produk.getIdKatalog());
            psInsert.setInt(2, produk.getIdHewan());
            psInsert.setString(3, produk.getTanggalDiperoleh());
            psInsert.setDouble(4, produk.getKuantitas());
            psInsert.setString(5, "Pending");
            psInsert.setString(6, karyawan.getId());

            int rowsInserted = psInsert.executeUpdate();

            psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setDouble(1, produk.getKuantitas());
            psUpdate.setInt(2, produk.getIdKatalog());
            psUpdate.executeUpdate();

            conn.commit();
            return rowsInserted > 0;

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) {}
            System.err.println("Gagal transaksi produk: " + e.getMessage());
            return false;
        } finally {
            try { if (psInsert != null) psInsert.close(); } catch (Exception ignored) {}
            try { if (psUpdate != null) psUpdate.close(); } catch (Exception ignored) {}
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception ignored) {}
        }
    }

    // ===============================
    // STOK
    // ===============================
    public double getStokById(Connection conn, String produkId) throws SQLException {
        String sql = "SELECT total_stok FROM produk WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setId(ps, 1, produkId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("total_stok");
            }
        }
        throw new SQLException("Produk tidak ditemukan (id=" + produkId + ")");
    }

    public void kurangiStok(Connection conn, String produkId, double qty) throws SQLException {
        String sql = "UPDATE produk SET total_stok = total_stok - ? WHERE id = ?";
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

    // ===============================
    // KATALOG
    // ===============================
    public List<KatalogProduk> getKatalogList() {
        List<KatalogProduk> list = new ArrayList<>();
        String sql = "SELECT id, nama_produk, satuan, harga_satuan, total_stok FROM produk";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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

    // ===============================
    // RIWAYAT PRODUK
    // ===============================
    public List<Produk> getAll() {
        List<Produk> list = new ArrayList<>();

        String sql =
                "SELECT t.id, t.id_produk, t.id_hewan, t.tanggal_diperoleh, t.kuantitas, t.status_kelayakan, " +
                        "k.nama_produk, k.satuan " +
                        "FROM detail_produk t " +
                        "JOIN produk k ON t.id_produk = k.id " +
                        "ORDER BY t.tanggal_diperoleh DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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

    // ===============================
    // PENDING QC
    // ===============================
    public List<Produk> getPendingList() {
        List<Produk> list = new ArrayList<>();

        String sql =
                "SELECT t.id, t.id_produk, t.id_hewan, t.tanggal_diperoleh, t.kuantitas, t.status_kelayakan, " +
                        "k.nama_produk, k.satuan " +
                        "FROM detail_produk t " +
                        "JOIN produk k ON t.id_produk = k.id " +
                        "WHERE t.status_kelayakan = 'Pending' " +
                        "ORDER BY t.tanggal_diperoleh ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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

    // ===============================
    // UPDATE QC
    // ===============================
    public boolean updateStatusQC(int idTransaksi, String statusBaru) {
        String sql = "UPDATE detail_produk SET status_kelayakan = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statusBaru);
            ps.setInt(2, idTransaksi);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Gagal update QC: " + e.getMessage());
            return false;
        }
    }

    // ===============================
    // PRODUK SIAP JUAL
    // ===============================
    public List<KatalogProduk> getProdukSiapJual() {
        List<KatalogProduk> list = new ArrayList<>();

        String sql =
                "SELECT k.id, k.nama_produk, k.satuan, k.harga_satuan, " +
                        "COALESCE(SUM(t.kuantitas), 0) AS total_stok_layak " +
                        "FROM produk k " +
                        "LEFT JOIN detail_produk t ON k.id = t.id_produk AND t.status_kelayakan = 'Layak' " +
                        "GROUP BY k.id, k.nama_produk, k.satuan, k.harga_satuan";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new KatalogProduk(
                        rs.getInt("id"),
                        rs.getString("nama_produk"),
                        rs.getString("satuan"),
                        rs.getDouble("harga_satuan"),
                        rs.getDouble("total_stok_layak")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
