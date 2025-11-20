package DataAccessObject;

public class KaryawanDAO {
<<<<<<< Updated upstream
=======
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Username: ");
        String username = input.nextLine();

        System.out.print("Password: ");
        String password = input.nextLine();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM karyawan WHERE akun = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Login BERHASIL! Selamat datang " + rs.getString("role") + ", " + rs.getString("nama"));
            } else {
                System.out.println("Login GAGAL! Username atau password salah.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        input.close();
    }
>>>>>>> Stashed changes
}
