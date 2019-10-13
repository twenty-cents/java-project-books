import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookStatistics {

    private static final String PREPROCESS_DIRECTORY = "./preprocess/";
    private static ArrayList<String> books;
    private static HashMap<String, String> preprocessedBooks;
    static {
        books = new ArrayList<String>();
        preprocessedBooks = new HashMap<String, String>();
        }

    public BookStatistics(){
        // Nettoyage du répertoire de prétraitement
        cleanPreprocessDirectory("");
    }

    public ArrayList<String> getBooks(){
        return books;
    }

    /**
     * Contrôle si les fichiers reçus existent bien dans le système
     * @param books : Tableau des fichiers à contrôler
     * @return      : Liste des fichiers inexistants dans le système
     */
    public List<String> checkFiles(String[] books){
        // Liste des livres indisponibles
        List<String> unavailableBooks = new ArrayList<String>();

        // Contrôle si les fichiers en entrée existent
        for(int i=0; i < books.length; i++){
            if(new File(books[i]).exists() == false)
                unavailableBooks.add(books[i]);
        }

        return unavailableBooks;
    }

    public void addBook(String book) throws BookStatisticsException {
        // Chemin absolu
        File f = new File(book);
        book = f.getAbsolutePath();

        // Prétraitement - Extraction par mots
        String preprocessedBook = PREPROCESS_DIRECTORY + f.getName();
        convertBookToWords(book, preprocessedBook);
        preprocessedBooks.put(book, preprocessedBook);

        // Ajout du livre dans la bibliothèque
        books.add(book);

        // Tri des livres par ordre alphabétique
        Collections.sort(this.books);
    }

    public void removeBook(String f){
        books.remove(f);
        preprocessedBooks.remove(f);
        // Tri des livres par ordre alphabétique
        Collections.sort(this.books);
    }

    /**
     * Récupère la liste des livres disponibles à la racine du répertoire reçu ainsi
     * que dans tous ses sous-répertoires
     * @param bookDirectory
     * @return
     */
    public List<File> getAvailableBooks(String bookDirectory){
        // Valeur par défaut
        if (bookDirectory.compareTo("") == 0)
            bookDirectory = "./books/";

        // Récupération de la liste des livres disponibles par
        // appel récursif de la méthode parseBookDir(), qui renvoie la
        // liste des livre d'un répertoire donné.
        List<File> availablesBooks = new ArrayList<File>();
        availablesBooks = parseBookDirs(bookDirectory, availablesBooks);

        // Tri de la liste des livres disponibles par ordre alphabéthique
        Collections.sort(availablesBooks);

        return availablesBooks;
    }

    /**
     * Liste les livres disponibles pour un répertoire donné
     * Nota : cette méthode est récursive et s'auto appelle
     * pour chaque sous-répertoire trouvé.
     *
     * @param bookDirectory
     * @return
     */
    private List<File> parseBookDirs(String bookDirectory, List<File> files) {
        // Création de l'objet File pour le répertoire reçu
        File directory = new File(bookDirectory);

        // Récupération de tous les fichiers du répertoire
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {
                // Si le fichier n'est pas un répertoire, on considère qu'il s'agit d'un livre
                if (file.isFile()) {
                    // Le fichier ne doit pas être déjà dans la liste des livres
                    if(books.contains(file.getAbsolutePath()) == false)
                        files.add(file);
                }
                // S'il s'agit d'un répertoire, appel récursif de la méthode pour en récupérer le contenu
                else if (file.isDirectory()) {
                    parseBookDirs(file.getAbsolutePath(), files);
                }
            }

        return files;
    }

    /**
     * Extrait le contenu d'un livre au format un mot par ligne
     *
     * @param in
     * @param out
     * @throws BookStatisticsException
     */
    public void convertBookToWords(String in, String out) throws BookStatisticsException {
        Pattern p = Pattern.compile("\\w+", Pattern.UNICODE_CHARACTER_CLASS);
        try (Scanner sc = new Scanner(new File(in));
             PrintStream fileOut = new PrintStream(new FileOutputStream(out))) {
            for (int i = 0; sc.hasNextLine(); ++i) {
                for (Matcher m1 = p.matcher(sc.nextLine()); m1.find(); ) {
                    fileOut.println(m1.group());
                }
            }
        } catch (FileNotFoundException e) {
            throw new BookStatisticsException("Le prétraitement du fichier '" + in + "' a échoué!", e);
        }
    }

    public static <Records> void convertBookToWordsV2(String in, String out) throws FileNotFoundException {
        Pattern p = Pattern.compile("\\w+", Pattern.UNICODE_CHARACTER_CLASS);
        Set<String> mots = new HashSet<String>();
        HashMap<String, Integer> mapMots = new HashMap<String, Integer>();

        NavigableMap<String, Integer> treemap = new TreeMap<String, Integer>(
                Collections.reverseOrder());

        SortedSet<Map.Entry<String, Integer>> sortedset = new TreeSet<Map.Entry<String, Integer>>(
                new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> e1,
                                       Map.Entry<String, Integer> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                });

        try (Scanner sc = new Scanner(new File(in));
             PrintStream fileOut = new PrintStream(new FileOutputStream(out))) {
            for (int i = 0; sc.hasNextLine(); ++i) {
                for (Matcher m1 = p.matcher(sc.nextLine()); m1.find(); ) {
                    if (mapMots.containsKey(m1.group())) {
                        mapMots.put(m1.group(), mapMots.get(m1.group() + 1));
                        int v = treemap.get(m1.group());
                        v++;
                        treemap.put(m1.group(), v);
                        //treemap.put(m1.group(), treemap.get(m1.group() + 1));
                    } else {
                        mapMots.put(m1.group(), 1);
                        treemap.put(m1.group(), 1);
                    }
                    if (mots.contains(m1.group()) == false) {
                        mots.add(m1.group());
                        fileOut.println(m1.group());
                    }
                }
            }
        }

        sortedset.addAll(treemap.entrySet());

        //mapMots = MapUtil.sortMap(mapMots);
        //System.out.println(sortedset);
        //Set keys = sortedset.keySet();   // It will return you all the keys in Map in the form of the Set

        int j = 0;
        for (Iterator i = sortedset.iterator(); i.hasNext() && j < 50; ) {

            Map.Entry<String, Integer> e = (Map.Entry<String, Integer>) i.next();
            System.out.println(e.getKey() + " : " + e.getValue());
            //int value =  0;//treemap.get(key);
            //Records value = (Records) treemap.get(key); // Here is an Individual Record in your HashMap
            //System.out.println(key + " : " + value);
            //System.out.println(key.toString() + " : " + value.toString());
            j++;
        }
    }


    /**
     * Supprime les éventuels fichiers de mots issus du lancement précédents dans le
     * le répertoire de prétraitement
     *
     * @param preprocessDirectory
     * @return
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
     * Retourne le nombre de lignes du fichier reçu (1)
     *
     * @param f
     * @return
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
     * Retourne le nombre de mots du fichier (2)
     *
     * @param f
     * @return
     */
    public int countBookWords(String f) {
        return countBookLines(preprocessedBooks.get(f));
    }

    public Map<String, Integer> getWordsFrequency(String f, int scope) throws FileNotFoundException {
        TreeMap<String, Integer> treemap = new TreeMap<String, Integer>();
        Map<String, Integer> res = new HashMap<String, Integer>();

        ArrayList<Word> words = new ArrayList();

        try (Scanner sc = new Scanner(new File(preprocessedBooks.get(f)))) {
            for (int i = 0; sc.hasNextLine(); ++i) {
                String s = sc.nextLine();
                if(words.contains(s)) {
                    int v = words.indexOf(s);
                    Word w = words.get(v);
                    w.setCount(w.getCount() + 1);
                    words.remove(v);
                    words.add(w);
                } else {
                    Word w = new Word(s, 1);
                    words.add(w);
                }
            }
        }

        // Création du dictionnaire trié par valeur décroissante
        SortedSet<Map.Entry<String, Integer>> sortedset = new TreeSet<Map.Entry<String, Integer>>(
                new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> e1,
                                       Map.Entry<String, Integer> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                });

/*
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            while (reader.readLine() != null){
                if (treemap.containsKey(m1.group())) {
                    mapMots.put(m1.group(), mapMots.get(m1.group() + 1));
                    int v = treemap.get(m1.group());
                    v++;
                    treemap.put(m1.group(), v);
                    //treemap.put(m1.group(), treemap.get(m1.group() + 1));
                } else {
                    mapMots.put(m1.group(), 1);
                    treemap.put(m1.group(), 1);
                }
            }
                ;
        } catch (FileNotFoundException e) {
            // Fichier non trouvé
            e.printStackTrace();
        } catch (IOException e) {
            // Problème de lectureif(this.count == o.count)
            e.printStackTrace();
        }
*/
        try (Scanner sc = new Scanner(new File(preprocessedBooks.get(f)))) {
            for (int i = 0; sc.hasNextLine(); ++i) {
                String s = sc.nextLine();
                if(treemap.containsKey(s)) {
                    int v = treemap.get(s);
                    v++;
                    treemap.put(s, v);
                } else {
                    treemap.put(s, 1);
                }
            }
        }

/**        Pattern p = Pattern.compile("\\w+", Pattern.UNICODE_CHARACTER_CLASS);
        try (Scanner sc = new Scanner(new File(f));
             PrintStream fileOut = new PrintStream(new FileOutputStream(preprocessedBooks.get(f)))) {
            for (int i = 0; sc.hasNextLine(); ++i) {
                for (Matcher m1 = p.matcher(sc.nextLine()); m1.find(); ) {
                    if (mapMots.containsKey(m1.group())) {
                        mapMots.put(m1.group(), mapMots.get(m1.group() + 1));
                        int v = treemap.get(m1.group());
                        v++;
                        treemap.put(m1.group(), v);
                        //treemap.put(m1.group(), treemap.get(m1.group() + 1));
                    } else {
                        mapMots.put(m1.group(), 1);
                        treemap.put(m1.group(), 1);
                    }
                    if (mots.contains(m1.group()) == false) {
                        mots.add(m1.group());
                        fileOut.println(m1.group());
                    }
                }
            }
        }
*/
        sortedset.addAll(treemap.entrySet());

        //mapMots = MapUtil.sortMap(mapMots);
        //System.out.println(sortedset);
        //Set keys = sortedset.keySet();   // It will return you all the keys in Map in the form of the Set

        int j = 0;
        for (Iterator i = sortedset.iterator(); i.hasNext() && j < scope; ) {

            Map.Entry<String, Integer> e = (Map.Entry<String, Integer>) i.next();
            res.put(e.getKey(), e.getValue());
            //System.out.println(e.getKey() + " : " + e.getValue());
            //int value =  0;//treemap.get(key);
            //Records value = (Records) treemap.get(key); // Here is an Individual Record in your HashMap
            //System.out.println(key + " : " + value);
            //System.out.println(key.toString() + " : " + value.toString());
            j++;
        }

        return res;
    }

 }