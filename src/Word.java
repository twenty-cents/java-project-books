public class Word implements Comparable<Word> {

    private String word;
    private int count;

    public Word(String word, int count){
        this.word = word;
        this.count = count;
    }

    @Override
    public int compareTo(Word o) {
        if(this.count > o.count)
            return -1;
        else if(this.count < o.count)
            return 1;
        else
            return 0;
    }

    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof Word)
        {
            sameSame = this.word == ((Word) object).word;
        }

        return sameSame;
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
