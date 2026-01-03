package utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NotifikasiService {
    
    // INI ADALAH "DATABASE SEMENTARA" KITA (Disimpan di RAM)
    // public static artinya satu list ini dibagi ke semua halaman
    public static final List<Pesan> listPesan = new ArrayList<>();

    // Class kecil untuk bentuk pesannya
    public static class Pesan {
        String pengirim;
        String penerima; // Bisa ID atau Nama
        String isi;
        String waktu;

        public Pesan(String pengirim, String penerima, String isi) {
            this.pengirim = pengirim;
            this.penerima = penerima;
            this.isi = isi;
            this.waktu = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM HH:mm"));
        }

        public String getDisplayMessage() {
            return "[" + waktu + "] Dari " + pengirim + ": " + isi;
        }
    }

    // Method untuk Manager Kirim Pesan
    public static void kirimPesan(String pengirim, String penerima, String isi) {
        listPesan.add(new Pesan(pengirim, penerima, isi));
    }

    // Method untuk Peternak Ambil Pesan (Filter berdasarkan namanya)
    public static List<String> getPesanUntuk(String namaPenerima) {
        List<String> hasil = new ArrayList<>();
        for (Pesan p : listPesan) {
            // Cek apakah pesan ini untuk si penerima (Case Insensitive)
            if (p.penerima.equalsIgnoreCase(namaPenerima)) {
                hasil.add(p.getDisplayMessage());
            }
        }
        return hasil;
    }
}