package DataAccessObject;

import Models.Karyawan;
import utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    // 2. ADD (Tambah)
    public boolean addKaryawan(Karyawan k) {
        // PERBAIKAN: Tambahkan kolom 'id' lagi ke dalam SQL
        String sql = "INSERT INTO karyawan (id, nama, akun, password, role, gaji, tanggal_rekrut) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Masukkan ID di urutan pertama
            ps.setString(1, k.getId()); 
            
            // Geser data lainnya ke urutan berikutnya
            ps.setString(2, k.getName());
            ps.setString(3, k.getAkun());
            ps.setString(4, k.getPassword());
            ps.setString(5, k.getRole());
            ps.setInt(6, k.getGaji());
            ps.setString(7, k.getTanggalDirekrut());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 1. GET ALL
    public List<Karyawan> getAll() {
        List<Karyawan> list = new ArrayList<>();
        // Pastikan nama kolom database sesuai (misal: tanggal_rekrut di DB)
        String sql = "SELECT * FROM karyawan"; 

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Karyawan k = new Karyawan();
                k.setId(String.valueOf(rs.getInt("id")));
                k.setName(rs.getString("nama"));
                k.setAkun(rs.getString("akun"));         // Sesuai Model
                k.setPassword(rs.getString("password"));
                k.setRole(rs.getString("role"));
                k.setGaji(rs.getInt("gaji"));            // Sesuai Model (int)
                k.setTanggalDirekrut(rs.getString("tanggal_rekrut")); // Sesuai Model
                
                list.add(k);
            }
        } catch (SQLException e) {
            System.err.println("Gagal load karyawan: " + e.getMessage());
        }
        return list;
    }

    public boolean updateKaryawan(Karyawan k) {
        String sql = "UPDATE karyawan SET nama=?, akun=?, password=?, role=?, gaji=?, tanggal_rekrut=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, k.getName());
            ps.setString(2, k.getAkun());
            ps.setString(3, k.getPassword());
            ps.setString(4, k.getRole());
            ps.setDouble(5, k.getGaji());
            ps.setString(6, k.getTanggalDirekrut());
            ps.setInt(7, Integer.parseInt(k.getId()));

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteKaryawan(String id) {
        String sql = "DELETE FROM karyawan WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id)); // Asumsi ID di DB tipe Integer
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int countKaryawan() {
        try (Connection conn = DatabaseConnection.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM karyawan")) {
            if(rs.next()) return rs.getInt(1);
        } catch (Exception e) {}
        return 0;
    }


    }
