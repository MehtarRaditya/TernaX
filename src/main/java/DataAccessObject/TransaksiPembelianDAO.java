/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAccessObject;

import Models.Karyawan;
import Models.TransaksiPembelian;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import utility.DatabaseConnection;
import utility.Session;

/**
 *
 * @author Muham
 */
public class TransaksiPembelianDAO {
    public void add(TransaksiPembelian transaksi) {
        String sql = "INSERT INTO transaksi_pembelian (id_pembelian, id_karyawan, tanggal_pembelian) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, transaksi.getId());
            pstmt.setInt(2, transaksi.getId_karyawan());
            pstmt.setString(3, transaksi.getTanggalPembelian());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int insertAndGetId(TransaksiPembelian transaksi) {
        Karyawan karyawan = Session.getLoggedInKaryawan();
        if (karyawan == null) {
            System.err.println("Gagal simpan transaksi: belum ada karyawan login.");
            return -1;
        }

        String sql = "INSERT INTO transaksi_pembelian (tanggal_pembelian, id_karyawan) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, transaksi.getTanggalPembelian());
            pstmt.setInt(2, Integer.parseInt(karyawan.getId())); // kamu pakai getId() String

            int affected = pstmt.executeUpdate();
            if (affected == 0) return -1;

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idPembelian = rs.getInt(1);
                    transaksi.setId(idPembelian);
                    return idPembelian;
                }
            }

        } catch (SQLException e) {
            System.err.println("Gagal insert transaksi_pembelian: " + e.getMessage());
        }
        return -1;
    }
    
    // Tambahkan method ini di class TransaksiPembelianDAO
    public java.util.List<Models.RiwayatKonsumsi> getAllRiwayat() {
        java.util.List<Models.RiwayatKonsumsi> list = new java.util.ArrayList<>();
        
        // Query JOIN 3 Tabel (Transaksi + Detail + Konsumsi)
        String sql = "SELECT tp.id_pembelian, tp.tanggal_pembelian, k.nama_konsumsi, k.tipe, dp.kuantitas " +
                     "FROM detail_pembelian dp " +
                     "JOIN transaksi_pembelian tp ON dp.id_pembelian = tp.id_pembelian " +
                     "JOIN konsumsi k ON dp.id_konsumsi = k.id " +
                     "ORDER BY tp.tanggal_pembelian DESC, tp.id_pembelian DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Models.RiwayatKonsumsi(
                    rs.getInt("id_pembelian"),
                    rs.getString("tanggal_pembelian"),
                    rs.getString("nama_konsumsi"),
                    rs.getString("tipe"),
                    rs.getInt("kuantitas")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
