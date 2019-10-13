import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // Contrôle d'existence des fichiers reçus
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));

        // Appel du menu applicatif
        Menu menu = new Menu(args);

        //System.out.println("Hello world");
        //System.out.print("\n\n\n\n\n\n\n\n\n\n");
        //System.out.println(BookStatistics.countBookLines("./books/Spinoza/Spinoza-Ethique.txt"));
        //System.out.println(BookStatistics.countBookLines("./books/mots.txt"));
        //System.out.println(BookStatistics.countBookLines("./books/mots2.txt"));
/**
        try {
            BookStatistics.convertBookToWordsV2("./books/Spinoza/Spinoza-Ethique.txt", "./books/mots2.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
*/
    }

}
