package fr.simplon.devweb2019.vincent.javaprojectbooks;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Menu {

    // Construction de la classe de gestion des statistiques des livres
    static private BookStatistics bookStatistics;
    static{
        bookStatistics = new BookStatistics();
    }
    static private Scanner sc;
    static{
        sc = new Scanner(System.in);
    }

    // Constante scope top mots les plus fréquents
    public static final int TOP_SCOPE = 50;

    /**
     * Constructeur
     * @param books : Liste des fichiers de type livre
     */
    public Menu(String[] books){
        // Contrôle d'existance des fichiers reçus en paramètre
        // La méthode retourne la liste des fichiers introuvables dans le système
        List<String> unavailableBooks = bookStatistics.checkFiles(books);

        // Aucune anomalie relevée sur les arguments reçus (livres)
        // -> Exécution du menu général
        if(unavailableBooks.size() == 0){

            // Mise à jour de la liste des livres pris en charge
            for(int i=0; i < books.length; i++){
                addBook(books[i]);
            }

            // Pause utilisateur
            pause();

            // Pseudo Nettoyage du terminal
            clear();

            // exécution du menu général
            executeMenuMain();

            // Sortie de l'application
            exit(0);
        }
        else {
            // Affiche la liste des livres introuvables dans le système
            displayErrorParams(unavailableBooks);

            // Sortie de l'application
            exit(0);
        }
    }

    /**
     * Sortie de l'application
     * @param status
     */
    private void exit(int status){
        // Nettoyage du répertoire de prétraitements
        bookStatistics.cleanPreprocessDirectory("");

        // Sortie de l'application
        System.exit(status);
    }

    /**
     * Ajout d'un livre dans la bibliothèque
     * @param book : livre à ajouter
     */
    private void addBook(String book){

        // Message à caractère informatif
        System.out.println("Ajout dans la bibliothèque du fichier " + book + "...en cours...");
        try {
            // Ajout du livre
            bookStatistics.addBook(book);

            // Message à caractère informatif
            System.out.println("Ajout dans la bibliothèque du fichier " + book + " ...terminé.");

        } catch (BookStatisticsException e) {

            // Affichage du message d'erreur
            System.out.println(e.getMessage());
        }
    }

    /**
     * Affiche la liste des livres introuvables dans le système
     * @param unavailableBooks : Liste des livres introuvables dans le système
     */
    private void displayErrorParams(List<String> unavailableBooks){

        // // Message à caractère informatif
        addTitleOption("!!! : Anomalie dans les paramètres reçus !!!");
        System.out.println("Liste des fichiers inexistants dans le système :\n");

        // Récupération et affichage de la liste des livres introuvables dans le système
        for(String book : unavailableBooks){
            System.out.println("  - " + book);
        }

        // Mise en pause
        this.pause();
    }

    /**
     * Affiche le message d'accueil
     */
    private void displayMenuMain(){

        // Ajout du titre du menu d'accueil
        addTitleOption("Programme de statistiques de bibliothèque");
        System.out.println("Menu général :\n");

        // Ajout des options
        addOption(1, "Lister les fichiers");
        addOption(2, "Ajouter un fichier");
        addOption(3, "Supprimer un fichier");
        addOption(4, "Afficher des informations sur un livre");
        addOption(5, "Quitter le programme");
    }

    /**
     * Exécution du menu général
     */
    private void executeMenuMain(){
        boolean oneMore = true;

        while(oneMore){

            // Affiche le menu général
            displayMenuMain();

            // Sélection d'une option
            int option = selectOption(1, 5, "");

            // Nettoyage de l'affichage
            clear();

            // Débranchement vers l'option sélectionnée
            switch (option){
                case 1:
                    executeOptionListCurrentBooks();
                    break;
                case 2:
                    executeOptionAddBook();
                    break;
                case 3:
                    executeOptionRemoveBook();
                    break;
                case 4:
                    executeSubmenuBookInfo();
                    break;
                case 5:
                    oneMore = false;
                    break;
                default:
                    System.out.println("Option non prévue, veuillez contacter le support informatique.");
            }

            // Nettoyage de l'affichage
            clear();
        }
        System.out.println("Merci, au revoir!");
    }

    /**
     * Option 1 - Affiche la liste des livres présents dans la bibliothèque
     */
    private void executeOptionListCurrentBooks(){

        // Message à caractère informatif
        addTitleOption("Option 1 : Lister les fichiers");
        System.out.println("Liste des livres présents dans la bibliothèque :\n");

        // Récupération et affichage de la liste des livres
        for(Book book : bookStatistics.getBooks()){
            System.out.println("  - " + book.getBookName());
        }

        // Mise en pause
        this.pause();
    }

    /**
     * Option 2 - Ajoute un livre dans la bibliothèque
     * @return : Nom du livre ajouté (TODO : retourner l'objet fr.simplon.devweb2019.vincent.javaProjectBooks.Book serait mieux)
     */
    private String executeOptionAddBook(){

        // Message à caractère informatif
        addTitleOption("Option 2 : Ajouter un fichier");
        System.out.println("Liste des livres disponibles :\n");

        int option = -1;
        String newBook = "";

        // Récupération de la liste des livres disponibles
        List<File> books = null;
        try {
            books = bookStatistics.getAvailableBooks("./books/");
        } catch (BookStatisticsException e) {
            // Message à caractère informatif
            System.out.println(e.getMessage());
        }

        // La collection doit avoir au moins un livre
        if(books.size() > 0){

            // Affichage des livres
            for(int i = 0; i < books.size(); i++){
                addOption(i+1, books.get(i).getAbsolutePath());
                //System.out.println("  - (" + Integer.toString(i+1) + ") : " + books.get(i));
            }

            // Sélection
            option = selectOption(1, books.size(), "Veuillez saisir le numéro du fichier à ajouter");

            // Ajout du fichier sélectionné dans la collection
            newBook = books.get(option-1).getAbsolutePath();
            addBook(books.get(option-1).getAbsolutePath());

            // Message de confirmation
            System.out.println("Le livre suivant a été ajouté dans la bibliothèque : \n" + books.get(option-1).getName());

        } else {
            System.out.println("\n!!! Désolé, aucun livre à ajouter disponible !!!");
        }

        // Pause
        pause();

        return newBook;
    }

    /**
     * Option 3 - Supprime un fichier
     * Supprime un livre de la bibliothèque
     * @return : nom du fichier supprimé
     */
    private String executeOptionRemoveBook(){

        // Message à caractère informatif
        addTitleOption("Option 3 : Supprimer un fichier");
        System.out.println("Liste des livres présents dans la bibliothèque :\n");

        int option = -1;
        String removed = "";

        // Récupéraion de la liste des livres
        ArrayList<Book> books = bookStatistics.getBooks();

        // La collection doit avoir au moins un livre
        if(books.size() > 0){

            // Affichage des livres
            for(int i = 0; i < books.size(); i++){
                addOption(i+1, books.get(i).getBookName());
                //System.out.println("  - (" + Integer.toString(i+1) + ") : " + books.get(i));
            }

            // Sélection
            option = selectOption(1, books.size(), "Veuillez saisir le numéro du fichier à supprimer");

            // Suppression du livre dans la collection
            removed = books.get(option-1).getBookName();
            bookStatistics.removeBook(books.get(option-1));

            // Message de confirmation
            System.out.println("\nLe livre suivant a été supprimé de la bibliothèque : \n" + removed);

        } else {
            System.out.println("\n!!! Désolé, aucun livre à supprimer disponible !!!");
        }

        // Pause
        pause();

        return removed;
    }

    /**
     * Option 4 - Exécution du sous-menu : Afficher les informations sur un livre
     * TODO : Méthode trop longue :
     * TODO   - l'affichage des livres est commun à une autre option
     * TODO   - L'affichae du sous menu et la sélection des options pourrait être découpé en deux méthodes distinctes
     */
    private void executeSubmenuBookInfo(){

        // Affiche le titre de l'option
        addTitleOption("Option 4 : Afficher des informations sur un livre");
        System.out.println("Liste des livres présents dans la bibliothèque :\n");

        int option = -1;
        Book book = null;

        // Récupéraion de la liste des livres
        ArrayList<Book> books = bookStatistics.getBooks();

        // La collection doit avoir au moins un livre
        if(books.size() > 0){

            // Affichage des livres
            for(int i = 0; i < books.size(); i++){
                addOption(i+1, books.get(i).getBookName());
            }

            // Sélection
            option = selectOption(1, books.size(), "Veuillez saisir le numéro du livre à sélectionner");

            // Récupération du livre dans la collection
            book = books.get(option-1);

            boolean oneMore = true;

            while(oneMore){

                // Nettoyage du terminal
                clear();
                // Affiche le titre de l'option
                addTitleOption("Option 4 : Afficher des informations sur un livre");
                System.out.println("Livre sélectionné : " + book.getBookName() + "\n");

                System.out.println("Options disponibles :\n");

                // Affiche les sous-options disponibles
                addOption(1, "Affiche le nombre de lignes du fichier");
                addOption(2, "Affiche le nombre de mots du fichier");
                addOption(3, "Affiche les 50 mots les plus fréquents et leur nombre d'occurrences");
                addOption(4, "Affiche les mots qui sont présents seulement dans ce fichier et aucun des autres fichiers");
                addOption(5, "Affiche pour chacun des autres fichiers le pourcentage de mots de l'autre fichier qui sont présents dans le fichier sélectionné, par ordre décroissant de ce pourcentage.");
                addOption(6, "Affiche le nombre de mots unique du fichier\n");
                addOption(7, "Retour au menu précédent");

                // Sélectionne une option
                option = selectOption(1, 7, "");

                switch (option){
                    case 1:
                        executeOptionBookTotalRows(book);
                        break;
                    case 2:
                        executeOptionBookTotalWords(book);
                        break;
                    case 3:
                        executeOptionBookWordsFrequency(book);
                        break;
                    case 4:
                        executeOptionBookWordsUnique(book);
                        break;
                    case 5:
                        executeOptionBooksWordUseInAllBooks(book);
                        break;
                    case 6:
                        executeOptionWordsUniqueInCurrentBook(book);
                        break;
                    case 7:
                        oneMore = false;
                        break;
                    default:
                        System.out.println("Option non prévue, veuillez contacter le support informatique.");
                }

                // Pause
                if(oneMore)
                    pause();
            }

        } else {
            System.out.println("\n!!! Désolé, aucun livre à supprimer disponible !!!");
        }

    }

    /**
     * Option 4.1 - Affiche le nombre de lignes du fichier
     * @param book : Livre à analyser
     */
    private void executeOptionBookTotalRows(Book book){
        // Message à caractère informatif
        System.out.println("Le livre contient " + book.getLinesCount() + " lignes. ( ==> " + book.getBookName() + " )\n");
    }

    /**
     * Option 4.2 - Affiche le nombre de mots du fichier
     * @param book : Livre à analyser
     */
    private void executeOptionBookTotalWords(Book book){
        // Message à caractère informatif
        System.out.println("Le livre contient " + book.getWordsCount() + " mots. ( ==> " + book.getBookName() + " )\n");
    }

    /**
     * Option 4.6 - Affiche leSystem.out.println("\nListe des 50 mots les plus fréquents dans le livre courant ( ==> " + book.getBookName() + " )\n");s mots différents d'un livre
     * @param book
     */
    private void executeOptionWordsUniqueInCurrentBook(Book book){
        // Message à caractère informatif
        System.out.println("Le livre contient " + book.getUniqueWords() + " mots différents. ( ==> " + book.getBookName() + " )\n");
    }

    /**
     * Option 4.4 - Affiche les mots qui sont présents seulement dans ce fichier et aucun des autres fichiers
     * @param book
     */
    private void executeOptionBookWordsUnique(Book book){
        try {
            Set<String> uniqueWords = bookStatistics.getBookUniqueWordsInAllBooks(book);

            System.out.println("\nListe des mots uniques à ce livre : " + uniqueWords.size() + " mot(s) ( ==> " + book.getBookName() + " )\n");

            String words = "";
            for(String word : uniqueWords){
                if(words.equals(""))
                    words += word;
                else
                    words += ", " + word;
            }
            // Liste finale
            System.out.print(words);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Option 4.3 - Affiche les 50 mots les plus fréquents et leur nombre d'occurrences
     * @param book : Livre à analyser
     */
    private void executeOptionBookWordsFrequency(Book book){
        System.out.println("\nListe des 50 mots les plus fréquents dans le livre courant ( ==> " + book.getBookName() + " )\n");
        int scope = Menu.TOP_SCOPE;
        try {
            String s = "";
            // Extraction des n mots les plus utilisés
            List<Word> words = bookStatistics.getWordsFrequency(book, scope);

            // Affichage des résultats
            for(Word word : words){
                if(s.equals(""))
                    s += word.getWord() + " (" + word.getCount() + ")";
                else
                    s += ", " + word.getWord() + " (" + word.getCount() + ")";
            }

            System.out.println(s);
        } catch (FileNotFoundException e) {
            System.out.println("\n!!! Anomalie lors du traitement de comptage : " + e.getMessage());
        }

    }

    /**
     * Option 4.5 - Affiche pour chacun des autres fichiers le pourcentage de mots de l'autre fichier
     * qui sont présents dans le fichier sélectionnés, par ordre décroissant de ce pourcentage
     * @param book
     */
    private void executeOptionBooksWordUseInAllBooks(Book book){
        System.out.println("\nTaux d'utilisation des mots du livre courant ( ==> " + book.getBookName() + " ) dans les autres livres de la bibliothèque :\n");

        // Exécution de la demande par la classe business
        List<WordUsed> wordsUsed = bookStatistics.getBooksWordUseInAllBooksV2(book);

        // Boucle d'affichage des résultats
        for(WordUsed wordUsedInBook : wordsUsed){
            System.out.println(wordUsedInBook.getUsedPercent() + " %" + " ==> " + wordUsedInBook.getWord());
        }
    }

    /**
     * Saisie d'une option utilisateur
     *
     * @param min : indice option minimum sélectionnable
     * @param max : indice option maximum sélectionnable
     * @param msg : message de saisie à afficher (optionnel)
     * @return    : n° option sélectionnée
     */
    private int selectOption(int min, int max, String msg){
        int option = -1;

        // Message par défaut
        if(msg.compareTo("") == 0)
            msg = "Veuillez choisir une option";

        do{
            try {
                System.out.print("\n>>> " + msg + " : ");
                option = Integer.parseInt(sc.nextLine());
                if(option < min || option > max)
                    throw new Exception("");
            } catch (Exception e) {
                System.out.println("Saisie incorrecte, veuillez ôter vos mouffles...");
                option = -1;
            }
        }while (option == -1);

        return option;
    }

    /**
     * Affiche un titre de menu
     * @param title : titre à afficher
     */
    private void addTitleOption(String title){
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println(title);
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    /**
     * Affiche une option de menu
     * @param optNumber : indice option à afficher
     * @param optLabel  : libellé option à afficher
     */
    private void addOption(int optNumber, String optLabel){
        System.out.println("(" + optNumber + ") " + optLabel);
    }

    /**
     * Mise en pause l'application
     */
    private void pause(){
        System.out.println("\n>>> Appuyez sur <Entrée> pour continuer...");
        sc.nextLine();
    }

    /**
     * Effectue plusieurs retours chariot pour simuler un nettoyage du terminal
     *
     * NOTA : pas d'appel de commande directe dans le terminal (clear pour linux ou cls pour windows)
     * car cela obligerait à tester le système d'exploitation pour déterminer quelle commande
     * lancer, et nuirait à la portabilité du code.
     */
    private void clear(){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

}
