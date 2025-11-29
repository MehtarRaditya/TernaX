package DataAccessObject;

import Models.Karyawan;
import utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static utility.DatabaseConnection.closeConnection;

public class KaryawanDAO {
    public Karyawan checkLogin(String akun, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Karyawan karyawan = null;

        try{
            //manggil methode getconn dari util
            conn = DatabaseConnection.getConnection();
            if (conn == null){
                return null;
            }
            String sql = "SELECT * FROM karyawan WHERE akun = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, akun);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                karyawan = new Karyawan(
                        rs.getString("id"),
                        rs.getString("nama"),
                        rs.getString("role"),
                        rs.getString("tanggal_rekrut"),
                        rs.getInt("gaji"),
                        rs.getString("akun"),
                        rs.getString("password")
                );
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return karyawan;
    }

    public int getJumlahKaryawan() {
        String sql = "SELECT COUNT(*) FROM karyawan";
        int jumlah = 0;

        // Gunakan try-with-resources untuk menutup koneksi secara otomatis
        try (Connection connn = DatabaseConnection.getConnection();
             PreparedStatement pstmttmt = connn.prepareStatement(sql);
             ResultSet rs = pstmttmt.executeQuery()) {

            if (rs.next()) {
                jumlah = rs.getInt(1); // Ambil hasil dari kolom pertama
            }

        } catch (SQLException e) {
            System.err.println("Error saat mengambil jumlah karyawan: " + e.getMessage());
            // Dalam aplikasi nyata, kamu mungkin ingin melempar custom exception
        }

        return jumlah;
    }

    public void addKaryawan(Karyawan karyawan) throws SQLException {
        String sql = "INSERT INTO karyawan (nama, akun, password, role, gaji, tanggal_rekrut) VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            if (conn == null){
                return;
            }

            pstmt.setString(1, karyawan.getName());
            pstmt.setString(2, karyawan.getAkun());
            pstmt.setString(3, karyawan.getPassword());
            pstmt.setString(4, karyawan.getRole());
            pstmt.setInt(5, karyawan.getGaji());
            pstmt.setString(6, karyawan.getTanggalDirekrut());
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
