package st;

import java.util.List;

public class HashNode {

    private List<String> identifiers;

    public HashNode(List<String> elements) {
        this.identifiers = elements;
    }

    public int add(String identifier){
        if(!this.identifiers.contains(identifier))
            this.identifiers.add(identifier);
        return this.identifiers.indexOf(identifier);
    }

    public int remove(String identifier) {
        if(this.identifiers.contains(identifier)){
            int pos = this.identifiers.indexOf(identifier);
            this.identifiers.remove(identifier);
            return pos;
        }
        return -1;
    }

    public int get(String identifier){
        return this.identifiers.indexOf(identifier);
    }

    @Override
    public String toString(){
        final StringBuilder result=new StringBuilder();
        this.identifiers.forEach(id->result.append(id.toString()+"; "));
        return result.toString();
    }
}
