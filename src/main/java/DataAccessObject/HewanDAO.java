package DataAccessObject;

import utility.DatabaseConnection;

import Models.Hewan;
import Models.Karyawan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.Session;

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
            Karyawan karyawan = Session.getLoggedInKaryawan();
             if (karyawan == null) {
        System.err.println("Gagal menambah Hewan: belum ada karyawan yang login.");
        return;
    }
            String sql = "INSERT INTO hewan (jenis, berat, usia_bulan, kelamin, pemilik, kondisi, penyakit) VALUES (?, ?, ?, ?,?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, hewan.getJenis());
                pstmt.setDouble(2, hewan.getBerat());
                pstmt.setInt(3, hewan.getUsia());
                pstmt.setString(4, hewan.getKelamin());
                pstmt.setInt(5, karyawan.getId());
                pstmt.setString(6, hewan.getKondisi());
                pstmt.setString(7, hewan.getPenyakit());

                pstmt.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Gagal menambah Hewan: " + e.getMessage());
            }
        }

//                public List<Hewan> getAll() {
//                    List<Hewan> karyawanList = new ArrayList<>();
//                     Karyawan karyawan = Session.getLoggedInKaryawan();
//                     if(karyawan == null){
//                         System.out.println("Belum ada karyawan yang login");
//                         return karyawanList;
//                     }
//
//                     String sql = "SELECT * FROM hewan WHERE pemilik = ?";
//
//                    try (Connection conn = DatabaseConnection.getConnection();
//                         PreparedStatement stmt = conn.prepareStatement(sql);
//                      ) {
//                        stmt.setString(1,karyawan.getId());
//
//                        ResultSet rs = stmt.executeQuery();
//                        
//                        while (rs.next()) {
//                            int id = rs.getInt("id");
//                            String jenis = rs.getString("jenis");
//                            double berat = rs.getDouble("berat");
//                            int usia = rs.getInt("usia_bulan");
//                            String kelamin = rs.getString("kelamin");
//                            String pemilik = rs.getString("pemilik");
//                            String kondisi = rs.getString("kondisi");
//                            String penyakit = rs.getString("penyakit");
//
//                            karyawanList.add(new Hewan(id,jenis, kelamin, berat, usia, kondisi, pemilik, penyakit));
//                        }
//                    } catch (SQLException e) {
//                        System.err.println("Gagal mengambil data Hewan: " + e.getMessage());
//                    }
//
//                    return karyawanList;
//                }

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
                
                int rows = pstmt.executeUpdate();
            System.out.println("Rows updated: " + rows + " | id = " + hewan.getId());

            } catch (SQLException e) {
                System.err.println("Gagal mengupdate Hewan: " + e.getMessage());
            }
        }

    public void delete(int id) {
        String sql = "DELETE FROM hewan WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Gagal menghapus Hewan: " + e.getMessage());
        }
    }
    
    public void updateKondisi(int idHewan,String kondisiBaru) {
            String sql = "UPDATE hewan SET kondisi = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, kondisiBaru);
                pstmt.setInt(2, idHewan);
                
                int rows = pstmt.executeUpdate();
            System.out.println("Rows updated: " + rows + " | id = " + idHewan);

            } catch (SQLException e) {
                System.err.println("Gagal mengupdate Hewan: " + e.getMessage());
            }
        }
    
    public List<Hewan> getAll() {
    List<Hewan> list = new ArrayList<>();

    String sql = "SELECT * FROM hewan WHERE kondisi <> 'Death'";  
    // atau kalau mau strict: kondisi = 'Alive'
    // String sql = "SELECT * FROM hewan WHERE kondisi = 'Alive'";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            Hewan h = new Hewan();
            h.setId(rs.getInt("id"));
            h.setJenis(rs.getString("jenis"));
            h.setBerat(rs.getDouble("berat"));
            h.setUsia(rs.getInt("usia_bulan"));
            h.setKelamin(rs.getString("kelamin"));
            h.setKondisi(rs.getString("kondisi"));
            h.setPenyakit(rs.getString("penyakit"));
            // tambahkan field lain sesuai class Hewan kamu

            list.add(h);
        }

    } catch (SQLException e) {
        System.err.println("Gagal mengambil hewan: " + e.getMessage());
    }

    return list;
}
    
    public String getJenisById(int id) {
        String jenis = "";
        String sql = "SELECT jenis FROM hewan WHERE id = ?";
        
        try (java.sql.Connection conn = utility.DatabaseConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            java.sql.ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                jenis = rs.getString("jenis");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jenis;
    }

    // [METHOD BARU YANG SUDAH DIPERBAIKI]
    public List<Hewan> getHewanSakit(String idPemilik) {
        List<Hewan> list = new ArrayList<>();
        
        // PERBAIKAN: Tambahkan "AND pemilik = ?" di ujung query
        String sql = "SELECT * FROM hewan WHERE " +
                     "(LOWER(kondisi) LIKE '%sakit%' " + 
                     "OR (penyakit IS NOT NULL AND LOWER(penyakit) NOT IN ('sehat', '-', '', ' '))) " +
                     "AND LOWER(kondisi) NOT IN ('mati', 'death', 'dead') " +
                     "AND pemilik = ?"; // <--- FILTER PEMILIK

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idPemilik); // Masukkan ID User yang login

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Hewan(
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error ambil hewan sakit: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    
    // =================================================================
    // KHUSUS MANAGER: AMBIL SEMUA HEWAN (Tanpa Filter Pemilik)
    // =================================================================
    public List<Hewan> getAllForManager() {
    List<Hewan> list = new ArrayList<>();

    // --- PERBAIKAN PENTING DI SINI (SQL JOIN) ---
    // Kita gabungkan tabel 'hewan' (h) dengan 'karyawan' (k)
    // Supaya bisa dapat namanya, bukan cuma ID-nya.
    
    // PERHATIKAN: Ganti 'h.id_peternak' sesuai nama kolom asing di tabel hewanmu (bisa id_karyawan, user_id, dll)
    String sql = "SELECT h.*, k.nama AS nama_peternak_asli " + 
                 "FROM hewan h " +
                 "LEFT JOIN karyawan k ON h.pemilik = k.id " + 
                 "WHERE h.kondisi NOT IN ('Mati', 'Death')";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            Hewan h = new Hewan();
            
            // Set Data Hewan Standard
            h.setId(rs.getInt("id"));
            h.setJenis(rs.getString("jenis"));
            h.setBerat(rs.getDouble("berat"));
            h.setUsia(rs.getInt("usia_bulan")); 
            h.setKelamin(rs.getString("kelamin"));
            h.setKondisi(rs.getString("kondisi"));
            h.setPenyakit(rs.getString("penyakit"));
            
            // --- AMBIL HASIL JOIN ---
            // Ambil dari alias 'nama_peternak_asli' yang kita buat di SQL atas
            String nama = rs.getString("nama_peternak_asli");
            
            // Cek jika null (misal karyawannya sudah dihapus)
            if (nama == null) {
                h.setPemilik(Integer.parseInt("Tidak Diketahui"));
            } else {
                h.setPemilik(Integer.parseInt(nama));
            }

            list.add(h);
        }

    } catch (SQLException e) {
        System.err.println("Manager Error - Gagal JOIN hewan & pemilik: " + e.getMessage());
        e.printStackTrace(); // Biar kelihatan detail errornya kalau ada
    }

    return list;
}
    
    // Method baru: Ambil hewan KHUSUS milik ID tertentu
    public List<Hewan> getByPeternak(String idPeternak) {
        List<Hewan> list = new ArrayList<>();
        
        // Filter WHERE pemilik = ?
        // Pastikan kolom di DB namanya 'pemilik' (bukan id_karyawan)
        String sql = "SELECT * FROM hewan WHERE pemilik = ? AND kondisi <> 'Death'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, idPeternak); // Masukkan ID Peternak yang login
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Hewan h = new Hewan();
                    h.setId(rs.getInt("id"));
                    h.setJenis(rs.getString("jenis"));
                    h.setBerat(rs.getDouble("berat"));
                    h.setUsia(rs.getInt("usia_bulan")); // Sesuai DB kamu
                    h.setKelamin(rs.getString("kelamin"));
                    h.setPemilik(rs.getInt("pemilik"));
                    h.setKondisi(rs.getString("kondisi"));
                    h.setPenyakit(rs.getString("penyakit"));
                    list.add(h);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}