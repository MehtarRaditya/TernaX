package DataAccessObject;

import Models.Karyawan;
import Models.Konsumsi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import utility.DatabaseConnection;
import utility.Session;

public class KonsumsiDAO {
    
    
    public int addKonsumsi(Konsumsi k) {
            Karyawan karyawan = Session.getLoggedInKaryawan();
             if (karyawan == null) {
        System.err.println("Gagal menambah Hewan: belum ada karyawan yang login.");
        return -1;
    }
            String sql = "INSERT INTO konsumsi (nama, tipe, pemasok_konsum) VALUES (?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, k.getName());
                pstmt.setString(2, k.getTipe());
                pstmt.setInt(3, Integer.parseInt(karyawan.getId()));

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
    
    public List<Konsumsi> getAll() {
    List<Konsumsi> list = new ArrayList<>();

    String sql = "SELECT * FROM konsumsi";  

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            Konsumsi k = new Konsumsi();
            k.setId(rs.getInt("id"));
            k.setName(rs.getString("nama_konsumsi"));
            k.setTipe(rs.getString("tipe"));
            k.setStok(rs.getInt("stok"));
            // tambahkan field lain sesuai class Hewan kamu

            list.add(k);
        }

    } catch (SQLException e) {
        System.err.println("Gagal mengambil data konsumsi: " + e.getMessage());
    }

    return list;
}
    
    public void UpdateStok(int idKonsumsi, int kuantitas) throws SQLException{
         String sql = "UPDATE konsumsi SET stok = stok + ? WHERE id = ?";
          try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, kuantitas);
            pstmt.setInt(2, idKonsumsi);
            pstmt.executeUpdate();

        }catch(SQLException e){
              System.err.println("gagal update"+ e.getMessage());
        }
    }
}
