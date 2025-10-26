import Models.Ayam;

public class Main {
    public static void main(String[] args) {
        Ayam ayam = new Ayam(1, "Ayam", "Betina", 10, 2, "Diternak", 5);

        System.out.println(ayam.getId());
        System.out.println(ayam.getJenis());
    }
}