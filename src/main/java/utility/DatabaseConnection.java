package utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ternax";
    private static final String USER = "YOUR-USERNAME";
    private static final String PASS = "YOUR-PASSWORD";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void closeConnection(Connection connection){
        if(connection != null){
            try{
                connection.close();
                System.out.println("Koneksi ditutup");
            }catch(SQLException e){
                System.out.println("gagal tutup");
                e.printStackTrace();
            }
        }
    }
}
