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

    public void addKaryawan(Karyawan karyawan) throws SQLException {
        String sql = "INSERT INTO karyawan (id, nama, akun, password, role, gaji, tanggal_rekrut) VALUES (?, ?, ?, ?, ?, ?,?)";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            if (conn == null){
                return;
            }

            pstmt.setString(1, karyawan.getId());
            pstmt.setString(2, karyawan.getName());
            pstmt.setString(3, karyawan.getAkun());
            pstmt.setString(4, karyawan.getPassword());
            pstmt.setString(5, karyawan.getRole());
            pstmt.setInt(6, karyawan.getGaji());
            pstmt.setString(7, karyawan.getTanggalDirekrut());
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Karyawan> getAll() {
    List<Karyawan> list = new ArrayList<>();

    String sql = "SELECT * FROM karyawan";  
    // atau kalau mau strict: kondisi = 'Alive'
    // String sql = "SELECT * FROM hewan WHERE kondisi = 'Alive'";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            Karyawan k = new Karyawan();
            k.setId(rs.getString("id"));
            k.setName(rs.getString("nama"));
            k.setAkun(rs.getString("akun"));
            k.setPassword(rs.getString("password"));
            k.setRole(rs.getString("role"));
            k.setGaji(rs.getInt("gaji"));
            k.setTanggalDirekrut(rs.getString("tanggal_rekrut"));
            // tambahkan field lain sesuai class Hewan kamu

            list.add(k);
        }

    } catch (SQLException e) {
        System.err.println("Gagal mengambil karyawan: " + e.getMessage());
    }

    return list;
}

    public void update(Karyawan k) {
                String sql = "UPDATE karyawan SET nama = ?, akun = ?,password = ?, role = ?, gaji = ? WHERE id = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setString(1, k.getName());
                    pstmt.setString(2, k.getAkun());
                    pstmt.setString(3, k.getPassword());
                    pstmt.setString(4, k.getRole());
                    pstmt.setInt(5,k.getGaji());
                    pstmt.setString(6,k.getId());

                    pstmt.executeUpdate();

                    int rows = pstmt.executeUpdate();
                System.out.println("Rows updated: " + rows + " | id = " + k.getId());

                } catch (SQLException e) {
                    System.err.println("Gagal mengupdate karyawan: " + e.getMessage());
                }
            }

    public void delete(String id) {
            String sql = "DELETE FROM karyawan WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, id);
                pstmt.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Gagal menghapus Karyawan: " + e.getMessage());
            }
        }


    }
