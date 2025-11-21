package DataAccessObject;

import Models.Karyawan;
import utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static utility.DatabaseConnection.closeConnection;

public class KaryawanDAO {
    public Karyawan checkLogin(String akun, String password) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Karyawan karyawan = null;

        try{
            //manggil methode getcon dari util
            con = DatabaseConnection.getConnection();
            if (con == null){
                return null;
            }
            String sql = "SELECT * FROM karyawan WHERE akun = ? AND password = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, akun);
            ps.setString(2, password);
            rs = ps.executeQuery();
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
            closeConnection(con);
        }
        return karyawan;
    }
}
