package DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utility.DatabaseConnection;

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
}

