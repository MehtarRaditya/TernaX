/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAccessObject;

import Models.Hewan;
import Models.Karyawan;
import Models.PemakaianKonsumsi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utility.DatabaseConnection;
import utility.Session;

/**
 *
 * @author Muham
 */
public class PemakaianKonsumsiDAO {
    public void beriPakan(PemakaianKonsumsi pk) {
        String insertSql = "INSERT INTO pemakaian_konsumsi " +
                "(id_hewan, id_konsumsi, id_karyawan, kuantitas, tanggal_waktu) " +
                "VALUES (?, ?, ?, ?, NOW())";

        String updateSql = "UPDATE konsumsi SET stok = stok - ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // START TRANSACTION

            try (PreparedStatement ps1 = conn.prepareStatement(insertSql);
                 PreparedStatement ps2 = conn.prepareStatement(updateSql)) {

                // INSERT histori
                ps1.setInt(1, pk.getIdHewan());
                ps1.setInt(2, pk.getIdKonsumsi());
                ps1.setInt(3, pk.getIdKaryawan());
                ps1.setInt(4, pk.getKuantitas());
                ps1.executeUpdate();

                // UPDATE stok
                ps2.setInt(1, pk.getKuantitas());
                ps2.setInt(2, pk.getIdKonsumsi());
                ps2.executeUpdate();

                conn.commit(); // COMMIT
            }

        } catch (SQLException e) {
            System.err.println("Gagal beri pakan: " + e.getMessage());
        }
    }
}
