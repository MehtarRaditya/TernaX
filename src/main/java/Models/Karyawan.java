package Models;

public class Karyawan {
    private String id;
    private String name;
    private String role;
    private String tanggalDirekrut;
    private int gaji;
    private String akun;
    private String password;

    public Karyawan(String id, String name, String role, String tanggalDirekrut, int gaji, String akun,
            String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.tanggalDirekrut = tanggalDirekrut;
        this.gaji = gaji;
        this.akun = akun;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTanggalDirekrut() {
        return tanggalDirekrut;
    }

    public void setTanggalDirekrut(String tanggalDirekrut) {
        this.tanggalDirekrut = tanggalDirekrut;
    }

    public int getGaji() {
        return gaji;
    }

    public void setGaji(int gaji) {
        this.gaji = gaji;
    }

    public String getAkun() {
        return akun;
    }

    public void setAkun(String akun) {
        this.akun = akun;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
}
