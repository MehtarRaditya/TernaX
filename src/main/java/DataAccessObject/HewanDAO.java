package DataAccessObject;

import utility.DatabaseConnection;

import Models.Hewan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HewanDAO {
     public int getJumlahHewanByJenis(String jenis) {
        String sql = "SELECT COUNT(*) FROM hewan WHERE jenis = ?";
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
            System.err.println("Error saat mengambil jumlah hewan (" + jenis + "): " + e.getMessage());
        }

        return jumlah;
    }
     
    public void add(Hewan hewan) {
        String sql = "INSERT INTO hewan (jenis, berat, usia_bulan, kelamin, pemilik, kondisi, penyakit) VALUES (?, ?, ?, ?,?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, hewan.getJenis());
            pstmt.setDouble(2, hewan.getBerat());
            pstmt.setInt(3, hewan.getUsia());
            pstmt.setString(4, hewan.getKelamin());
            pstmt.setString(5, hewan.getPemilik());
            pstmt.setString(6, hewan.getKondisi());
            pstmt.setString(7, hewan.getPenyakit());
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Gagal menambah Hewan: " + e.getMessage());
        }
    }

    public List<Hewan> getAll() {
        List<Hewan> karyawanList = new ArrayList<>();
        String sql = "SELECT * FROM hewan";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String jenis = rs.getString("jenis");
                double berat = rs.getDouble("berat");
                int usia = rs.getInt("usia_bulan");
                String kelamin = rs.getString("kelamin");
                String pemilik = rs.getString("pemilik");
                String kondisi = rs.getString("kondisi");
                String penyakit = rs.getString("penyakit");
                
                karyawanList.add(new Hewan(jenis, kelamin, berat, usia, kondisi, pemilik, penyakit));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data Hewan: " + e.getMessage());
        }
        
        return karyawanList;
    }

    public void update(Hewan hewan) {
        String sql = "UPDATE hewan SET berat = ?, usia_bulan = ?, kondisi = ?, penyakit = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, hewan.getBerat());
            pstmt.setInt(2, hewan.getUsia());
            pstmt.setString(3, hewan.getKondisi());
            pstmt.setString(4, hewan.getPenyakit());
            pstmt.setInt(5, hewan.getId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Gagal mengupdate Hewan: " + e.getMessage());
        }
    }

    public void delete(String id) {
        String sql = "DELETE FROM hewan WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Gagal menghapus Hewan: " + e.getMessage());
        }
    }
}