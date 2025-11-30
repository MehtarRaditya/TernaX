package DataAccessObject;

import Models.Karyawan;
import Models.Konsumsi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import utility.DatabaseConnection;
import utility.Session;

public class KonsumsiDAO {
    
    
    public int addKonsumsi(Konsumsi k) {
            Karyawan karyawan = Session.getLoggedInKaryawan();
             if (karyawan == null) {
        System.err.println("Gagal menambah Hewan: belum ada karyawan yang login.");
        return -1;
    }
            String sql = "INSERT INTO konsumsi (nama, tipe, pemasok_konsum, tanggal_exp) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, k.getName());
                pstmt.setString(2, k.getTipe());
                pstmt.setInt(3, Integer.parseInt(karyawan.getId()));
                pstmt.setString(4, k.getTanggalExpire());

                pstmt.executeUpdate();
                
                ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

            } catch (SQLException e) {
                System.err.println("Gagal menambah Hewan: " + e.getMessage());
            }
             return -1; // gagal
        }
}
