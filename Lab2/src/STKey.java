public class STKey {
    private int STindex;
    private int hashListIndex;

    public STKey(int STindex, int hashListIndex) {
        this.STindex = STindex;
        this.hashListIndex = hashListIndex;
    }

    public int getSTindex() {
        return STindex;
    }

    public void setSTindex(int STindex) {
        this.STindex = STindex;
    }

    public int getHashListIndex() {
        return hashListIndex;
    }

    public void setHashListIndex(int hashListIndex) {
        this.hashListIndex = hashListIndex;
    }

    @Override
    public String toString(){
        return "STKey[" + this.STindex + "," +this.hashListIndex + "]";
    }
}
