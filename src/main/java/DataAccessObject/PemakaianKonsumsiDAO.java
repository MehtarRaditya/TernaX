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
    
    public List<String> getRiwayatTerbaru() {
        List<String> list = new ArrayList<>();
        
        // PERBAIKAN:
        // 1. JOIN ke tabel 'konsumsi' (Bukan detail_produk)
        // 2. Ambil kolom 'nama_konsumsi' & 'tipe'
        // 3. TANPA LIMIT (Biar notif nambah terus)
        
        String sql = "SELECT h.jenis, k.nama_konsumsi, k.tipe, pk.kuantitas, pk.tanggal_waktu " +
                     "FROM pemakaian_konsumsi pk " +
                     "JOIN hewan h ON pk.id_hewan = h.id " +
                     "JOIN konsumsi k ON pk.id_konsumsi = k.id " + // <--- INI YANG BENAR
                     "ORDER BY pk.tanggal_waktu DESC"; 

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String jenisHewan = rs.getString("jenis");
                String namaPakan = rs.getString("nama_konsumsi"); // Kolom dari tabel konsumsi
                String tipe = rs.getString("tipe");
                
                // --- LOGIKA SATUAN PINTAR ---
                // Karena tabel konsumsi gak punya kolom 'satuan', kita tebak dari tipenya
                String satuan = "Kg";
                if (tipe != null && (tipe.toLowerCase().contains("vitamin") || tipe.toLowerCase().contains("obat"))) {
                    satuan = "Pcs"; // Atau Butir/Botol
                }
                
                // Format Angka (Hapus .0 di belakang)
                double qtyRaw = rs.getDouble("kuantitas");
                String qty = (qtyRaw % 1 == 0) ? String.format("%.0f", qtyRaw) : String.valueOf(qtyRaw);
                
                // Rangkai Pesan
                String pesan = "✅ Memberi " + qty + " " + satuan + " " + namaPakan + 
                               "\n     ke " + jenisHewan + " (" + rs.getTimestamp("tanggal_waktu") + ")";
                
                list.add(pesan);
            }

        } catch (SQLException e) {
            System.err.println("Gagal ambil riwayat: " + e.getMessage());
        }
        return list;
    }
    
    public List<PemakaianKonsumsi> getAllForManager() {
    List<PemakaianKonsumsi> list = new ArrayList<>();
    
    // PERUBAHAN ADA DI SINI:
    // 1. Kita gunakan JOIN (Inner Join) ke tabel hewan untuk memastikan datanya ada.
    // 2. Kita tambahkan WHERE untuk memfilter kondisi.
    
    String sql = "SELECT pk.id, pk.id_hewan, pk.id_konsumsi, pk.id_karyawan, pk.kuantitas, pk.tanggal_waktu, " +
                 "h.jenis, dp.nama_produk " +
                 "FROM pemakaian_konsumsi pk " +
                 "JOIN hewan h ON pk.id_hewan = h.id " + // Pakai JOIN biasa biar hewan yg sudah dihapus ID-nya tidak ikut
                 "LEFT JOIN detail_produk dp ON pk.id_konsumsi = dp.id " +
                 // FILTER HANYA YANG HIDUP:
                 "WHERE h.kondisi NOT IN ('Mati', 'Death') " + 
                 "ORDER BY pk.tanggal_waktu DESC";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            PemakaianKonsumsi pk = new PemakaianKonsumsi();
            
            // 1. Set Data Utama
            // pk.setId(rs.getInt("id")); 
            pk.setIdHewan(rs.getInt("id_hewan"));
            pk.setIdKonsumsi(rs.getInt("id_konsumsi"));
            pk.setIdKaryawan(rs.getInt("id_karyawan"));
            
            // Catatan: Jika di Model kamu sudah ubah jadi Double, gunakan rs.getDouble
            pk.setKuantitas((int) rs.getDouble("kuantitas")); 
            
            if (rs.getTimestamp("tanggal_waktu") != null) {
                pk.setTanggal(rs.getTimestamp("tanggal_waktu").toString());
            }

            // 2. Set Data Tambahan
            pk.setNamaKonsumsi(rs.getString("nama_produk")); 
            
            // Opsional: Jika ingin memastikan jenis hewan ikut terbaca
            // pk.setJenisHewan(rs.getString("jenis")); 

            list.add(pk);
        }

    } catch (SQLException e) {
        System.err.println("Gagal load pemakaian hewan hidup: " + e.getMessage());
        e.printStackTrace(); // Penting untuk debugging
    }
    
    return list;
}
    
    // Method Baru: Ambil riwayat KHUSUS milik Karyawan tertentu
    public List<String> getRiwayatByPeternak(String idKaryawan) {
        List<String> list = new ArrayList<>();
        
        // Filter WHERE pk.id_karyawan = ?
        String sql = "SELECT h.jenis, k.nama_konsumsi, k.tipe, pk.kuantitas, pk.tanggal_waktu " +
                     "FROM pemakaian_konsumsi pk " +
                     "JOIN hewan h ON pk.id_hewan = h.id " +
                     "JOIN konsumsi k ON pk.id_konsumsi = k.id " + 
                     "WHERE pk.id_karyawan = ? " +  // <--- FILTER PENTING
                     "ORDER BY pk.tanggal_waktu DESC"; 

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idKaryawan); // Set ID User

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String jenisHewan = rs.getString("jenis");
                    String namaPakan = rs.getString("nama_konsumsi");
                    String tipe = rs.getString("tipe");
                    
                    String satuan = "Kg";
                    if (tipe != null && (tipe.toLowerCase().contains("vitamin") || tipe.toLowerCase().contains("obat"))) {
                        satuan = "Pcs"; 
                    }
                    
                    double qtyRaw = rs.getDouble("kuantitas");
                    String qty = (qtyRaw % 1 == 0) ? String.format("%.0f", qtyRaw) : String.valueOf(qtyRaw);
                    
                    String pesan = "✅ Memberi " + qty + " " + satuan + " " + namaPakan + 
                                   "\n     ke " + jenisHewan + " (" + rs.getTimestamp("tanggal_waktu") + ")";
                    
                    list.add(pesan);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal ambil riwayat user: " + e.getMessage());
        }
        return list;
    }
}
