package DataAccessObject;

import Models.Kandang;
import utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KandangDAO {
    public void addKandang(Kandang kandang) {
        String sql = "INSERT INTO kandang (jenis, luas, kapasitas, terisi, tanggal_dibangun, tanggal_terakhir_dibersihkan) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, kandang.getJenis());
            pstmt.setInt(2, kandang.getLuas());
            pstmt.setInt(3, kandang.getKapasitas());
            pstmt.setInt(4, kandang.getTerisi());
            pstmt.setString(5, kandang.getTanggalDibangun());
            pstmt.setString(6, kandang.getTanggalPerawatanTerakhir());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
