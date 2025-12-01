/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAccessObject;

import Models.Karyawan;
import Models.Pembelian;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utility.DatabaseConnection;
import utility.Session;

/**
 *
 * @author Muham
 */
public class PembelianDAO {
    public void addKonsumsi(Pembelian Tk) {
            Karyawan karyawan = Session.getLoggedInKaryawan();
             if (karyawan == null) {
        System.err.println("Gagal menambah Hewan: belum ada karyawan yang login.");
        return;
    }
            String sql = "INSERT INTO transaksi_konsumsi (karyawan_id, konsumsi_id, tanggal_dibeli,tanggal_keluar, kuantitas) VALUES (?, ?, ?, ?,?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, Integer.parseInt(karyawan.getId()));
                pstmt.setInt(2, Tk.getIdKonsumsi());
                pstmt.setString(3, Tk.getTanggalDibeli());
                pstmt.setString(4, Tk.getTanggalKeluar());
                pstmt.setInt(5, Tk.getKuantitas());
                

                pstmt.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Gagal menambah Konsumsi: " + e.getMessage());
            }
        }
    public void deleteById(int idTransaksi) {
    String sql = "DELETE FROM transaksi_konsumsi WHERE id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, idTransaksi);
        pstmt.executeUpdate();

    } catch (SQLException e) {
        System.err.println("Gagal menghapus transaksi konsumsi: " + e.getMessage());
    }
}
}
