package fr.simplon.devweb2019.vincent.javaprojectbooks;

public class Word implements Comparable<Word> {

    private String word;
    private int count;

    public Word(String word, int count){
        this.word = word;
        this.count = count;
    }

    /**
     * Tri par comptage décroissant
     * @param o
     * @return
     */
    @Override
    public int compareTo(Word o) {
        if(this.count < o.count)
            return 1;
        else if(this.count > o.count)
            return -1;
        else
            return 0;
    }

    @Override
    /**
     * Teste l'égalité de l'objet sur son mot
     */
    public boolean equals(Object other){
        if(other != null && (other instanceof Word)){
            Word otherWord = (Word) other;
            return this.word.compareTo(otherWord.getWord()) == 0;
        }
        return false;
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
