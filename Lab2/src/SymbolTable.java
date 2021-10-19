import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SymbolTable{

    private Map<Integer, HashNode> nodes;

    public SymbolTable(){
        this.nodes = new HashMap<>();
    }

    private int getHash(String identifier){
        return identifier.hashCode();
    }

    public STKey add(String identifier){
        int hash = this.getHash(identifier);
        if(!this.nodes.containsKey(hash)){
            this.nodes.put(hash, new HashNode(new ArrayList<>()));
        }
        return new STKey(hash, this.nodes.get(hash).add(identifier));
    }

    public STKey remove(String identifier){
        int hash = this.getHash(identifier);
        if(this.nodes.containsKey(hash)){
            int pos = this.nodes.get(hash).remove(identifier);
            if(pos != -1)
                return new STKey(hash, pos);
        }
        return new STKey(-1, -1);
    }

    public STKey get(String identifier){
        int hash = this.getHash(identifier);
        if(this.nodes.containsKey(hash)){
            int pos = this.nodes.get(hash).get(identifier);
            if(pos != -1)
                return new STKey(hash, pos);
        }
        return new STKey(-1, -1);
    }

}
