package fr.simplon.devweb2019.vincent.javaprojectbooks;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookStatistics {

    private static final String PREPROCESS_DIRECTORY = "./preprocess/";
    private static ArrayList<Book> books;
    private static HashMap<String, String> preprocessedBooks;
    static {
        books = new ArrayList<Book>();
        preprocessedBooks = new HashMap<String, String>();
        }

    public BookStatistics(){
        // Nettoyage du répertoire de prétraitement
        cleanPreprocessDirectory("");
    }

    public ArrayList<Book> getBooks(){
        return books;
    }

    /**
     * Contrôle si les fichiers reçus existent bien dans le système
     *
     * @param filenames : Tableau des fichiers à contrôler
     * @return          : Liste des fichiers inexistants dans le système
     */
    public List<String> checkFiles(String[] filenames){

        // Liste des livres indisponibles
        List<String> unavailableBooks = new ArrayList<String>();

        // Contrôle si les fichiers en entrée existent
        for(int i=0; i < filenames.length; i++){
            if(new File(filenames[i]).exists() == false)
                unavailableBooks.add(filenames[i]);
        }

        return unavailableBooks;
    }

    /**
     * Ajout d'un livre dans la bibliothèque
     *
     * @param filename : Livre à ajouter
     * @throws BookStatisticsException
     */
    public void addBook(String filename) throws BookStatisticsException {

        // Chemin cannonical (car plus complet que le chemin absolu
        File f = new File(filename);
        String cannonicalPath;
        try {
            cannonicalPath = f.getCanonicalPath();
        } catch (IOException e) {
            throw new BookStatisticsException("", e, ErrorCodes.CANNONICAL_PATH_RESOLVE_ERROR);
        }

        // Prétraitement - Extraction par mots
        String preprocessedBook = PREPROCESS_DIRECTORY + f.getName();
        Map<String, Long> bookCounts = convertBookToWords(filename, preprocessedBook);

        // Construction d'un livre
        Book book = new Book(f.getName(), cannonicalPath, preprocessedBook, bookCounts.get("lines"), bookCounts.get("words"), bookCounts.get("uniqueWords"));

        // Ajout du livre dans la bibliothèque
        books.add(book);

        try {
            // Comptage des couples mots / utilisation
            book.setMapMots(getBookWordUses(book.getPreprocessedBookAbsoluteFilename()));

            // Récupération de la liste des mots unique du livre
            book.setlUniqueWords(getBookUniqueWords(book));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Tri des livres par ordre alphabétique
        Collections.sort(books);
    }

    /**
     * Suppression d'un livre de la bibliothèque
     *
     * @param f : Livre à supprimer
     */
    public void removeBook(Book f){

        // Suppression du livre
        books.remove(f);

        // Tri des livres par ordre alphabétique
        Collections.sort(books);
    }

    /**
     * Récupère la liste des livres disponibles à la racine du répertoire reçu ainsi
     * que dans tous ses sous-répertoires
     *
     * @param bookDirectory : Répertoire de base contenant les livres de la bibliothèque
     * @return              : liste des fichiers livre identifiés
     */
    public List<File> getAvailableBooks(String bookDirectory) throws BookStatisticsException {

        // Récupération de la liste des livres disponibles par
        // appel récursif de la méthode parseBookDir(), qui renvoie la
        // liste des livre d'un répertoire donné.
        List<File> availablesBooks = new ArrayList<File>();
        try {
            availablesBooks = parseBookDirs(bookDirectory, availablesBooks);
        } catch (IOException e) {
            throw new BookStatisticsException("", e, ErrorCodes.CANNONICAL_PATH_RESOLVE_ERROR);
        }

        // Tri de la liste des livres disponibles par ordre alphabéthique
        Collections.sort(availablesBooks);

        return availablesBooks;
    }

    /**
     * Liste les livres disponibles pour un répertoire donné
     * Nota : cette méthode est récursive et s'auto appelle
     * pour chaque sous-répertoire trouvé.
     *
     * @param bookDirectory : répertoire à analyser
     * @return              : liste des livres identifiés dans le répertoire
     */
    private List<File> parseBookDirs(String bookDirectory, List<File> files) throws IOException {
        // Création de l'objet File pour le répertoire reçu
        File directory = new File(bookDirectory);

        // Récupération de tous les fichiers du répertoire
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {

                // Si le fichier n'est pas un répertoire, on considère qu'il s'agit d'un livre
                if (file.isFile()) {

                    // Le fichier ne doit pas être déjà dans la liste des livres
                    boolean isBookExist = false;
                    for(Book book : books){
                        if(book.getAbsoluteFilename().equals(file.getCanonicalPath()) && isBookExist == false)
                            isBookExist = true;
                    }
                    if(isBookExist == false)
                        files.add(file);
                }
                // S'il s'agit d'un répertoire, appel récursif de la méthode pour en récupérer le contenu
                else if (file.isDirectory()) {
                    parseBookDirs(file.getCanonicalPath(), files);
                }
            }

        return files;
    }

    /**
     * Extrait le contenu d'un livre au format un mot par ligne
     *
     * @param in  : nom du livre à extraire
     * @param out : nom du livre de mots à créer
     * @return    : map avec :
     *              - le nombre de lignes du livre
     *              - Le nombre mots du livre
     *              - Le nombre de mots uniques du livre
     * @throws BookStatisticsException
     */
    public Map<String, Long> convertBookToWords(String in, String out) throws BookStatisticsException {
        Map<String, Long> bookCounts = new HashMap<String, Long>();
        long linesCount = 0;
        long wordsCount = 0;
        ArrayList<String> uniqueWords = new ArrayList<String>();

        Pattern p = Pattern.compile("\\w+", Pattern.UNICODE_CHARACTER_CLASS);
        try (Scanner sc = new Scanner(new File(in));
             PrintStream fileOut = new PrintStream(new FileOutputStream(out))) {
            for (int i = 0; sc.hasNextLine(); ++i) {
                linesCount++;
                for (Matcher m1 = p.matcher(sc.nextLine()); m1.find(); ) {
                    String s = m1.group().toLowerCase();
                    fileOut.println(s);

                    // Comptage des mots
                    wordsCount++;

                    // Mots uniques
                    if(uniqueWords.contains(s) == false) {
                        uniqueWords.add(s);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new BookStatisticsException("Le prétraitement du fichier '" + in + "' a échoué!", e);
        }

        // Sauvegarde des résultats dans une hashmap pour faciliter le retour
        bookCounts.put("lines", linesCount);
        bookCounts.put("words", wordsCount);
        bookCounts.put("uniqueWords", new Long(uniqueWords.size()));

        return bookCounts;
    }

    /**
     * Construction de la table de hashage mots / utilisation
     *
     * @param preprocessedBookAbsoluteFilename : nom du fichier de mots à traiter
     * @return : map mot / nombre d'utilisations
     * @throws FileNotFoundException
     */
    private HashMap<String, Integer> getBookWordUses(String preprocessedBookAbsoluteFilename) throws FileNotFoundException {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        //ArrayList<fr.simplon.devweb2019.vincent.javaProjectBooks.Word> words = new ArrayList();

        // Lecture du livre prétraité pour dénombrer l'utilisation de chaque mot
        try (Scanner sc = new Scanner(new File(preprocessedBookAbsoluteFilename))) {
            for (int i = 0; sc.hasNextLine(); ++i) {
                String s = sc.nextLine();
                if(map.containsKey(s)) {
                    int v = map.get(s);
                    v++;
                    map.put(s, v);
                } else {
                    map.put(s, 1);
                }
            }
        }

        return map;
    }

    /**
     * Supprime les éventuels fichiers de mots issus du lancement précédents dans le
     * le répertoire de prétraitement
     *
     * @param preprocessDirectory : suppression des éventuels fichiers prétraités issus du lancement précédent
     * @return : oui/non si l'action s'est bien déroulé
     */
    public boolean cleanPreprocessDirectory(String preprocessDirectory) {
        // Valeur par défaut
        if (preprocessDirectory.compareTo("") == 0)
            preprocessDirectory = "./preprocess/";

        // code retour
        boolean hasDeleted = true;

        // Suppression de tous les fichiers du répertoire
        // Nota : pas de gestion récursive pour supprimer d'éeventuels sous-répertoires (inutile par notre cas de figure)
        File[] allContents = new File(preprocessDirectory).listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                hasDeleted = file.delete();
            }
        }

        return hasDeleted;
    }

    /**
     * Retourne le nombre de lignes du fichier livre original
     *
     * @param f : chemin du livre à analyser
     * @return  : nombre de ligne du livre
     */
    public int countBookLines(String f) {
        int lines = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            while (reader.readLine() != null) lines++;
        } catch (FileNotFoundException e) {
            // Fichier non trouvé
            e.printStackTrace();
        } catch (IOException e) {
            // Problème de lecture
            e.printStackTrace();
        }

        return lines;
    }

    /**
     * Retourne le nombre de mots du fichier de mots
     *
     * @param f : nom du fichier de mots à analyser
     * @return : nombre de mots du livre
     */
    public int countBookWords(String f) {
        return countBookLines(preprocessedBooks.get(f));
    }

    /**
     * Option 4.3 - Affiche les 50 mots les plus fréquents et leur nombre d'occurrences
     *
     * @param book  : Livre à analyser
     * @param scope : Nombre de mots à analyser
     * @return      : Liste des n (scope) tuples (mot, fréquence)
     * @throws FileNotFoundException
     */
    public List<Word> getWordsFrequency(Book book, int scope) throws FileNotFoundException {
        ArrayList<Word> words = new ArrayList();
        ArrayList<Word> wordsFinal = new ArrayList();

        // Lecture du livre prétraité pour dénombrer l'utilisation de chaque mot
        for (Map.Entry<String, Integer> e : book.getMapMots().entrySet()) {
            Word word = new Word(e.getKey(), e.getValue());
            words.add(word);
        }

        // Tri de la liste des mots par utilisation décroissante
        Collections.sort(words);

        // Limitation de la liste au scope
        if(words.size() > 50){
            for(int i=0; i < 50; i++){
                wordsFinal.add(words.get(i));
            }
        }

        return wordsFinal;
    }

    /**
     * Option 4.4 - Afficher les mots qui sont présents seulement dans ce fichier et aucun des autres fichiers
     *
     * @param book : livre à analyser
     * @return     : liste des mots présents seulement dans ce fichier et aucun des autres fichiers
     */
    public Set<String> getBookUniqueWordsInAllBooks(Book book) throws FileNotFoundException {
        Set<String> uniqueWords = new TreeSet<String>();

        // Récupération de la liste des mots du livre
        try (Scanner sc = new Scanner(new File(book.getPreprocessedBookAbsoluteFilename()))) {
            for (int i = 0; sc.hasNextLine(); ++i) {
                String s = sc.nextLine();
                if(uniqueWords.contains(s) == false)
                    uniqueWords.add(s);
            }
        }

        // Recherche des mots du livres dans tous les livres de la bibliothèque
        for(Book b : books){
            // Recherche de concordance des mots des autres livres avec ceux du livre courant
            if(b.getPreprocessedBookAbsoluteFilename().compareTo(book.getPreprocessedBookAbsoluteFilename()) != 0){
                try (Scanner sc = new Scanner(new File(b.getPreprocessedBookAbsoluteFilename()))) {
                    for (int j = 0; j < uniqueWords.size(); j++) {
                        for (int i = 0; sc.hasNextLine(); ++i) {
                            String s = sc.nextLine();
                            // Si le mot existe dans un autre livre, on le retire du résultat
                            if (uniqueWords.contains(s) == true)
                                uniqueWords.remove(s);
                        }
                    }
                }
            }
        }

        return uniqueWords;
    }

    /**
     * Option 4.5 - Affiche pour chacun des autres fichiers le pourcentage de mots de l'autre fichier
     * qui sont présents dans le fichier sélectionnés, par ordre décroissant de ce pourcentage.
     *
     * @param book : livre à analyser
     * @return : map livre / % mots utilisés
     */
    public List<WordUsed> getBooksWordUseInAllBooksV2(Book book){
        List<WordUsed> lWordsUsed = new ArrayList<WordUsed>();

        // Etape 2 - Utilisation de ces mots dans les autres livres
        for(Book b : books){
            // Recherche de concordance des mots des autres livres avec ceux du livre courant
            long used = 0;
            if(b.getPreprocessedBookAbsoluteFilename().compareTo(book.getPreprocessedBookAbsoluteFilename()) != 0){
                for(String word : book.getlUniqueWords()){
                    // Si le mot existe dans un autre livre, on le retire du résultat
                    if (b.getlUniqueWords().contains(word) == true){
                        used++;
                    }

                }

                float n = used;
                float d = book.getUniqueWords();

                float res = BookStatistics.round(((n / d) * 100), 2);
                lWordsUsed.add(new WordUsed(b.getBookName(), used, res));
            }

        }

        Collections.sort(lWordsUsed);
        return lWordsUsed;
    }

    /**
     * Récupère la liste des mots uniques d'un livre
     *
     * @param book : livre à analyser
     * @return : liste des mots uniques du livre
     * @throws FileNotFoundException
     */
    public ArrayList<String> getBookUniqueWords(Book book) throws FileNotFoundException {
        ArrayList<String> uniqueWords = new ArrayList<String>();

        // Récupération de la liste des mots du livre
        try (Scanner sc = new Scanner(new File(book.getPreprocessedBookAbsoluteFilename()))) {
            for (int i = 0; sc.hasNextLine(); ++i) {
                String s = sc.nextLine();
                if(uniqueWords.contains(s) == false)
                    uniqueWords.add(s);
            }
        }

        return uniqueWords;
    }

    /**
     * Méthode utilitaire honteusement pompée sur google pour arrondir un float
     *
     * @param value : valeur à arrondir
     * @param scale : précision
     * @return      : valeur arrrondie
     */
    public static float round(float value, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++) {
            pow *= 10;
        }
        float tmp = value * pow;
        float tmpSub = tmp - (int) tmp;

        return ( (float) ( (int) (
                value >= 0
                        ? (tmpSub >= 0.5f ? tmp + 1 : tmp)
                        : (tmpSub >= -0.5f ? tmp : tmp - 1)
        ) ) ) / pow;

    }
 }