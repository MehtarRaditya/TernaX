package DataAccessObject;

import Models.Hewan;
import Models.Karyawan;
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
    
    public void addProduk(Produk produk) {
        Karyawan karyawan = Session.getLoggedInKaryawan();
        Hewan hewan = new Hewan();
        if (karyawan == null) {
            System.err.println("Gagal menambah Hewan: belum ada karyawan yang login.");
            return;
        }
        String sql = "INSERT INTO produk (tanggal, id_hewan, tipe, kuantitas, status_kelayakan) VALUES (?, ?, ?, ?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, produk.getTanggalDiperoleh());
            pstmt.setInt(2, hewan.getId());
            pstmt.setString(3, produk.getTipe());
            pstmt.setDouble(4, produk.getKuantitas())   ;
            pstmt.setString(5, produk.getKualitas());
                
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal menambah produk: " + e.getMessage());
        }
    }
    
    public List<Produk> getAll() {
        List<Produk> produkList = new ArrayList<>();
        Karyawan karyawan = Session.getLoggedInKaryawan();
        if(karyawan == null){
            System.out.println("Belum ada karyawan yang login");
            return produkList;
        }

        String sql = "SELECT * FROM produk";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String tanggalDiperoleh = rs.getString("tanggal");
                int idHewan = rs.getInt("id_hewan");
                String tipe = rs.getString("tipe");
                double kuantitas = rs.getDouble("kuantitas");
                String kualitas = rs.getString("status_kelayakan");
                Produk p = new Produk(id, tanggalDiperoleh, tipe, kuantitas, kualitas);
                p.setIdHewan(idHewan);                    // 👉 SET KE OBJEK
                produkList.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data Produk: " + e.getMessage());
        }
        return produkList;
    }
}