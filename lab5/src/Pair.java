public class Pair<T,K>{
    public T first;
    public K second;

    public Pair(T first, K second) {
        this.first = first;
        this.second = second;
    }
    @Override
    public boolean equals(Object o){
        if(o instanceof Pair){
            Pair<T, K> p = (Pair<T, K>) o;
            return p.first.equals(this.first) && p.second.equals(this.second);
        }
        return false;
    }
}
