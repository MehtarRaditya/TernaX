/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAccessObject;

import Models.DetailPembelian;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.DatabaseConnection;

/**
 *
 * @author Muham
 */
public class DetailPembelianDAO {
    public void insert(DetailPembelian detail) {
        String sql = "INSERT INTO detail_pembelian (id_pembelian, id_konsumsi, kuantitas) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, detail.getIdPembelian());
            pstmt.setInt(2, detail.getIdKonsumsi());
            pstmt.setInt(3, detail.getKuantitas());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Gagal insert detail_pembelian: " + e.getMessage());
        }
    }
    
    public List<DetailPembelian> getAll() {
    List<DetailPembelian> list = new ArrayList<>();

    String sql = "SELECT * FROM hewan WHERE kondisi <> 'Death'";  
    // atau kalau mau strict: kondisi = 'Alive'
    // String sql = "SELECT * FROM hewan WHERE kondisi = 'Alive'";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            DetailPembelian h = new DetailPembelian();
            h.setId(rs.getInt("id"));
            h.setIdKonsumsi(rs.getInt("id_konsumsi"));
            h.setIdPembelian(rs.getInt("id_pembelian"));
            h.setKuantitas(rs.getInt("kuantitas"));            // tambahkan field lain sesuai class Hewan kamu

            list.add(h);
        }

    } catch (SQLException e) {
        System.err.println("Gagal mengambil hewan: " + e.getMessage());
    }

    return list;
}
}
