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
            //Hewan hewan = new Hewan();
             if (karyawan == null) {
        System.err.println("Gagal menambah Hewan: belum ada karyawan yang login.");
        return;
    }
            String sql = "INSERT INTO produk (jenis,kuantitas,kualitas,hewan,tanggal_diperoleh) VALUES (?, ?, ?, ?,?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, produk.getTipe());
                pstmt.setDouble(2, produk.getKuantitas());
                pstmt.setString(3, produk.getKualitas());
                pstmt.setInt(4, produk.getIdHewan());
                pstmt.setString(5, produk.getTanggalDiperoleh());
                
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
                         PreparedStatement stmt = conn.prepareStatement(sql);
                      ) {
                        

                        ResultSet rs = stmt.executeQuery();
                        
                        while (rs.next()) {
                            String id = rs.getString("id");
                            String jenis = rs.getString("jenis");
                            double jumlah = rs.getDouble("kuantitas");
                            String kualitas = rs.getString("kualitas");
                            int hewan_id = rs.getInt("hewan");
                            String tanggalDiperoleh = rs.getString("tanggal_diperoleh");
                            //String tanggalDiperoleh = rs.getString("tanggal");
                            //int idHewan = rs.getInt("id_hewan");
                            //String tipe = rs.getString("tipe");
                            //double kuantitas = rs.getDouble("kuantitas");
                            //String kualitas = rs.getString("status_kelayakan");
                            

                            Produk p = new Produk(id, tanggalDiperoleh, jenis, jumlah, kualitas);
            p.setIdHewan(hewan_id);                    // 👉 SET KE OBJEK

            produkList.add(p);
                        }
                    } catch (SQLException e) {
                        System.err.println("Gagal mengambil data Produk: " + e.getMessage());
                    }

                    return produkList;
                }
}