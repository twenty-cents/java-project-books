import java.util.HashMap;

public class Book implements Comparable<Book>{

    private String bookName = "";
    private String absoluteFilename = "";
    private String preprocessedBookAbsoluteFilename = "";
    private HashMap<String, Integer> mapMots;
    private String uniqueMots;
    private long wordsCount = 0;
    private long linesCount = 0;

    /**
     * Constructeur
     * @param bookName
     */
    public Book(String bookName, String absoluteFilename, String preprocessedBookAbsoluteFilename, long linesCount, long wordsCount){
        this.bookName = bookName;
        this.absoluteFilename = absoluteFilename;
        this.preprocessedBookAbsoluteFilename = preprocessedBookAbsoluteFilename;
        this.linesCount = linesCount;
        this.wordsCount = wordsCount;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAbsoluteFilename() {
        return absoluteFilename;
    }

    public void setAbsoluteFilename(String absoluteFilename) {
        this.absoluteFilename = absoluteFilename;
    }

    public String getPreprocessedBookAbsoluteFilename() {
        return preprocessedBookAbsoluteFilename;
    }

    public void setPreprocessedBookAbsoluteFilename(String preprocessedBookAbsoluteFilename) {
        this.preprocessedBookAbsoluteFilename = preprocessedBookAbsoluteFilename;
    }

    public HashMap<String, Integer> getMapMots() {
        return mapMots;
    }

    public void setMapMots(HashMap<String, Integer> mapMots) {
        this.mapMots = mapMots;
    }

    public long getWordsCount() {
        return wordsCount;
    }

    public void setWordsCount(long wordsCount) {
        this.wordsCount = wordsCount;
    }

    public long getLinesCount() {
        return linesCount;
    }

    public void setLinesCount(long linesCount) {
        this.linesCount = linesCount;
    }

    public String getUniqueMots() {
        return uniqueMots;
    }

    public void setUniqueMots(String uniqueMots) {
        this.uniqueMots = uniqueMots;
    }

    /**
     * Tri par défaut sur le chemin absolu du nom de fichier
     * @param book
     * @return
     */
    @Override
    public int compareTo(Book book) {
        return this.absoluteFilename.compareTo(book.getAbsoluteFilename());
    }
}
