/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAccessObject;

import Models.Hewan;
import Models.Karyawan;
import Models.LogistikRow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.DatabaseConnection;
import utility.Session;

/**
 *
 * @author Muham
 */
public class ViewLogistikDAO {
     public List<LogistikRow> getAll() {
                    List<LogistikRow> konsumsiList = new ArrayList<>();
                     Karyawan karyawan = Session.getLoggedInKaryawan();
                     if(karyawan == null){
                         System.out.println("Belum ada karyawan yang login");
                         return konsumsiList;
                     }

                     String sql = "SELECT * FROM v_logistik ";

                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(sql);
                      ) {
//                        stmt.setString(1,karyawan.getId());

                        ResultSet rs = stmt.executeQuery();
                        
                        while (rs.next()) {
                            int idTransaksi = rs.getInt("id_transaksi");
                            int idKonsumsi = rs.getInt("id_konsumsi");
                            String nama = rs.getString("nama");
                            String tipe = rs.getString("tipe");
                            int kuantitas = rs.getInt("kuantitas");
                            String tanggalBeli = rs.getString("tanggal_beli");
                            String tanggalExp = rs.getString("tanggal_expire");
                         

                            konsumsiList.add(new LogistikRow( nama,  tipe,  kuantitas,  tanggalBeli,  tanggalExp,  idTransaksi,  idKonsumsi));
                        }
                    } catch (SQLException e) {
                        System.err.println("Gagal mengambil data Hewan: " + e.getMessage());
                    }

                    return konsumsiList;
                }
}
