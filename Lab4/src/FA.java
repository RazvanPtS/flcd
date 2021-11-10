import java.util.ArrayList;
import java.util.List;

public class FA {
    public List<String> getAllStates() {
        return allStates;
    }

    public void setAllStates(List<String> allStates) {
        this.allStates = allStates;
    }

    private List<String> allStates = new ArrayList<>();
    private List<String> initialStates = new ArrayList<>();
    private List<String> alph = new ArrayList<>();
    private List<Transition> functions = new ArrayList<>();
    private List<String> finalStates = new ArrayList<>();

    public FA(List<String> allStates, List<String> alph, List<Transition> functions,List<String> initialStates, List<String> finalStates) {
        this.allStates = allStates;
        this.initialStates = initialStates;
        this.alph = alph;
        this.functions = functions;
        this.finalStates = finalStates;
    }

    public List<String> getInitialStates() {
        return initialStates;
    }

    public void setInitialStates(List<String> initialStates) {
        this.initialStates = initialStates;
    }

    public List<String> getAlph() {
        return alph;
    }

    public void setAlph(List<String> alph) {
        this.alph = alph;
    }

    public List<Transition> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Transition> functions) {
        this.functions = functions;
    }

    public List<String> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(List<String> finalStates) {
        this.finalStates = finalStates;
    }
}
