public class WordUsed implements Comparable<WordUsed> {

    private String word = "";
    private long usedCount = 0;
    private float usedPercent = 0;

    public WordUsed(String word, long usedCount, float usedPercent){
        this.word = word;
        this.usedCount = usedCount;
        this.usedPercent = usedPercent;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(long usedCount) {
        this.usedCount = usedCount;
    }

    public float getUsedPercent() {
        return usedPercent;
    }

    public void setUsedPercent(float usedPercent) {
        this.usedPercent = usedPercent;
    }

    /**
     * Tri par pourcentage décroissant
     * @param wordUsed
     * @return
     */
    @Override
    public int compareTo(WordUsed wordUsed) {
        int res = 0;
        if(wordUsed.getUsedPercent() > this.usedPercent)
            res = 1;
        if(wordUsed.getUsedPercent() < this.usedPercent)
            res = -1;
        return res;
    }
}
