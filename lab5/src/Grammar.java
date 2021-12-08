import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Grammar {
    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    public void setNonTerminals(List<String> allStates) {
        this.nonTerminals = allStates;
    }

    private List<String> nonTerminals = new ArrayList<>();
    private List<String> initialStates = new ArrayList<>();
    private List<String> terminals = new ArrayList<>();
    private List<Production> productions = new ArrayList<>();
    private HashMap<String, ArrayList<String>> first = new HashMap<>();
    private HashMap<String, ArrayList<String>> follow = new HashMap<>();
    private HashMap<Pair<String, String>, Pair<String, Integer>> parsingTable = new HashMap<>();

    public Grammar(List<String> allStates, List<String> alph, List<Production> functions, List<String> initialStates) {
        this.nonTerminals = allStates;
        this.initialStates = initialStates;
        this.terminals = alph;
        this.productions = functions;
    }

    public HashMap<String, ArrayList<String>> getFirst(){
        return this.first;
    }

    public HashMap<String, ArrayList<String>> getFollow(){
        return this.follow;
    }

    public void setFirst(HashMap<String, ArrayList<String>> res){
        this.first = res;
    }

    public void setFollow(HashMap<String, ArrayList<String>> res){
        this.follow = res;
    }
    public List<String> getInitialStates() {
        return nonTerminals;
    }

    public void setInitialStates(List<String> initialStates) {
        this.nonTerminals = initialStates;
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public void setTerminals(List<String> alph) {
        this.terminals = alph;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> functions) {
        this.productions = functions;
    }

    public HashMap<Pair<String, String>, Pair<String, Integer>> getParsingTable() {
        return parsingTable;
    }

    public void setParsingTable(HashMap<Pair<String, String>, Pair<String, Integer>> parsingTable) {
        this.parsingTable = parsingTable;
    }
}
