/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;
import Models.Karyawan;

/**
 *
 * @author Muham
 */
public class Session {
    private static Karyawan loggedInKaryawan;

    public static void setLoggedInKaryawan(Karyawan peternak) {
        loggedInKaryawan = peternak;
    }

    public static Karyawan getLoggedInKaryawan() {
        return loggedInKaryawan;
    }
    
    public static void clear() {
        loggedInKaryawan = null;
    }
}
