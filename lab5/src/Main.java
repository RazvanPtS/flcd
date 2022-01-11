import java.io.File;
import java.io.FileNotFoundException;
import java.lang.management.MemoryPoolMXBean;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static String epsilon = "ε";
    private static String $ = "$";

    public static void main(String[] args) throws FileNotFoundException {
        Grammar grammar = readGrammar();
        Scanner input = new Scanner(System.in);
        System.out.println("1-Non-terminals\n2-Terminals\n3-productions\n4-initial states\n5-Productions for non-terminal\n6-CFG check\n7-FIRST\n8-FOLLOW\n9-Parsing table\n10-Parse sequence\n0-exit");
        String in = input.nextLine();
        while(!in.equals("0")){
            switch (in){
                case "1":
                    printNonTerminals(grammar);
                    break;
                case "2":
                    printTerminals(grammar);
                    break;
                case "3":
                    printTransitions(grammar);
                    break;
                case "4":
                    printInitialStates(grammar);
                    break;
                case "5":
                    printProductionsForTerminal(grammar);
                    break;
                case "6":
                    System.out.println(checkCFG(grammar) ? "CFG" : "Non-cfg");
                    break;
                case "7":
                    getFirst(grammar);
                    break;
                case "8":
                    getFollow(grammar);
                    break;
                case "9":
                    constructParsingTable(grammar);
                    break;
                case "10":
                    parseSequence(grammar);
                    break;
                case "0":
                    return;
            }
            System.out.println("1-Non-terminals\n2-Terminals\n3-productions\n4-initial states\n5-Productions for non-terminal\n6-CFG check\n7-FIRST\n8-FOLLOW\n9-Parsing table\n10-Parse sequence\n0-exit");
            in = input.nextLine();
        }
    }

    private static void parseSequence(Grammar grammar) {
        if(grammar.getParsingTable().get(new Pair<String, String>("$","$"))==null){
            constructParsingTable(grammar);
        }
        String alphaString = "a*(a+a)";
        List<String> alpha=new ArrayList<>(Stream.of(alphaString.split("\\.?+")).map(String::valueOf).collect(Collectors.toList())), beta;
        alpha.add("$");
        beta = new ArrayList<>(Arrays.stream(grammar.getInitialStates().get(0).split("\\.", 1)).toList());
        beta.add("$");
        List<Integer> pi = new ArrayList<>();
        HashMap<Pair<String, String>, Pair<String, Integer>> parsingTable = grammar.getParsingTable();
        boolean go = true;
        StringBuilder result = new StringBuilder();
        while(go){
            String headBeta = beta.get(0);
            String headAlpha = alpha.get(0);
            while(headBeta.equals(epsilon)){
                beta.remove(0);
                headBeta = beta.get(0);
            }
            if(headAlpha.equals("$") && !headBeta.equals("$")){
                headAlpha = epsilon;
            }
            Pair<String, String> headPair = new Pair<>(headBeta, headAlpha);
            // if there exists a production from beta to alpha
            if(parsingTable.get(headPair).second!=-1){
                beta.remove(0);
                Pair<String, Integer> val = parsingTable.get(headPair);
                beta.addAll(0, Stream.of(val.first.split("\\.?+")).map(String::valueOf).collect(Collectors.toList()));
                pi.add(val.second);
            } else if(parsingTable.get(headPair).first.equals("pop")){
                alpha.remove(0);
                beta.remove(0);
            }else if(parsingTable.get(headPair).first.equals("acc")){
                go = false;
                result.append("acc");
            }else{
                go = false;
                result.append("err");
            }
        }
        System.out.println(pi);
        System.out.println(result);
    }

    private static void constructParsingTable(Grammar g){
        if(g.getFollow().isEmpty()){
            getFollow(g);
        }
        HashMap<Pair<String, String>, Pair<String, Integer>> pT = new HashMap<>();
        List<Production> productions = g.getProductions();
        for(int i=0; i<productions.size(); i++){
            String lhs = productions.get(i).getInitState();
            String rhs = productions.get(i).getFinalState();
            String first = String.valueOf(rhs.charAt(0));
            int finalI = i;
            g.getFirst().get(first).forEach(fV->{
                    if(!fV.equals(epsilon)) {
                        if (!pT.containsKey(new Pair<>(lhs, fV)))
                            pT.put(new Pair<>(lhs, fV), new Pair<>(rhs, finalI + 1));
                    }else{
                        g.getFollow().get(lhs).forEach(flV->{
                            pT.put(new Pair<>(lhs, flV), new Pair<>(rhs, finalI+1));
                        });
                    }
            });
        }
        g.getTerminals().forEach(t->{
            pT.put(new Pair<>(t,t), new Pair<>("pop", -1));
        });
        pT.put(new Pair<>($,$), new Pair<>("acc", -1));
        System.out.println(("Parsing table ///////"));
        pT.forEach((k,v)->{
            System.out.printf("(%s, %s)=(%s, %s)%n", k.first, k.second, v.first, v.second);
        });
        g.setParsingTable(pT);
    }

    private static ArrayList<String> findAllTerminals(Grammar g, String nonT){
        ArrayList<String> result = new ArrayList<>();
        List<Production> productions = g.getProductions().stream().
                filter(p->p.getInitState().equals(nonT)).collect(Collectors.toList());
        productions.forEach(p->{
            String firstSymbol = String.valueOf(p.getFinalState().charAt(0));
            if(g.getTerminals().contains(firstSymbol)){
                result.add(firstSymbol);
            }
            else if(g.getNonTerminals().contains(firstSymbol)){
                result.addAll(findAllTerminals(g, firstSymbol));
            }
            else if(epsilon.equals(firstSymbol)){
                result.add(epsilon);
            }
        });
        return result;
    }

    private static void getFirst(Grammar grammar) {
        HashMap<String, ArrayList<String>> first = new HashMap<>();
        List<String> terminals = grammar.getTerminals();
        terminals.forEach(terminal->
            first.put(terminal, new ArrayList(List.of(terminal))));
        grammar.getNonTerminals().forEach(nonT->
            first.put(nonT, findAllTerminals(grammar, nonT)));
        first.forEach((k,v)->{
            List<String> unique = v.stream().distinct().collect(Collectors.toList());
            v.clear();
            v.addAll(unique);
        });
        first.put(epsilon, new ArrayList<>(List.of(epsilon)));
        grammar.setFirst(first);
        System.out.println("First() ///////");
        first.forEach((k,v)->System.out.println(k+"->"+v));
    }

    private static void getFollow(Grammar grammar){
        HashMap<String, ArrayList<String>> follow = new HashMap<>();
        if(grammar.getFirst().isEmpty())
            getFirst(grammar);
        List<String> nonTerminals = grammar.getNonTerminals();
        List<Production> productions = grammar.getProductions();
        HashMap<String, ArrayList<String>> first = grammar.getFirst();
        nonTerminals.forEach(nonT->{
            productions.forEach(pr->{
                int start = 0;
                int nonTIndex = pr.getFinalState().indexOf(nonT, start);
                while(nonTIndex != -1){
                    if(nonTIndex==pr.getFinalState().length()-1){
                        if(!follow.containsKey(nonT)){
                            follow.put(nonT, follow.get(pr.getInitState()));
                        }
                        else{
                            follow.get(nonT).addAll(follow.get(pr.getInitState()));
                        }
                    }else{
                        ArrayList<String> toAdd = first.get(String.valueOf(pr.getFinalState().charAt(nonTIndex + 1)));
                        if(toAdd.contains(epsilon)){
                            toAdd.addAll(follow.get(pr.getInitState()));
                            if(!follow.containsKey(nonT)){
                                follow.put(nonT, toAdd);
                            }
                            else{
                                follow.get(nonT).addAll(toAdd);
                            }
                        }else{
                            if(!follow.containsKey(nonT)){
                                follow.put(nonT, toAdd);
                            }
                            else{
                                follow.get(nonT).addAll(toAdd);
                            }
                        }
                    }
                    start = nonTIndex+1;
                    nonTIndex = pr.getFinalState().indexOf(nonT, start);
                }
            });
        });
        grammar.getNonTerminals().forEach(nT->{
            if(!follow.containsKey(nT)){
                follow.put(nT, new ArrayList<>(List.of()));
            }
        });
        follow.forEach((k,v)->{
            List<String> unique = v.stream().distinct().collect(Collectors.toList());
            v.clear();
            v.addAll(unique);
            if(k.equals(grammar.getInitialStates().get(0))){
                v.add(epsilon);
            }
        });
        grammar.setFollow(follow);
        System.out.println("Follow ///////");
        follow.forEach((k, v)->System.out.println(k+"->"+v));
    }

    private static boolean checkCFG(Grammar grammar) {
        List<Production> productions = grammar.getProductions();
        List<String> terminals = grammar.getTerminals();
        for(Production p : productions){
            for(String terminal : terminals){
                if(p.getInitState().contains(terminal)){
                    return false;
                }
            }
        }
        return true;
    }

    private static void printNonTerminals(Grammar grammar) {
        grammar.getNonTerminals().forEach(System.out::println);
    }

    private static void printProductionsForTerminal(Grammar grammar){
        Scanner input = new Scanner(System.in);
        String in = input.nextLine();
        grammar.getProductions().stream().filter(prod->prod.getInitState().equals(in)).forEach(System.out::println);
    }

    private static void printTransitions(Grammar grammar) {
        grammar.getProductions().forEach(System.out::println);
    }

    private static void printTerminals(Grammar grammar) {
        System.out.println(grammar.getTerminals().toString());
    }

    private static void printInitialStates(Grammar grammar) {
        System.out.println(grammar.getInitialStates().toString());
    }

    private static Grammar readGrammar() throws FileNotFoundException {
        Grammar grammar;
        File file = new File("Grammar.txt");
        Scanner s = new Scanner(file);
        StringBuilder faS = new StringBuilder();
        while(s.hasNextLine()){
            faS.append(s.nextLine());
        }
        Pattern groups = Pattern.compile(".*?\\{(.*?)}");
        Matcher matcher = groups.matcher(faS);
        List<String> elements = new ArrayList<>();
        while(matcher.find()){
            elements.add(matcher.group(1));
        }
        Pattern function = Pattern.compile("([^, ]+)->([^, ]+)");
        List<Production> productions = new ArrayList<>();
        Matcher m = function.matcher(elements.get(2));
        while(m.find()) {
            Production t = new Production(m.group(1), m.group(2));
            productions.add(t);
        }
        grammar = new Grammar(Arrays.stream(elements.get(0).split(",")).toList(),
                Arrays.stream(elements.get(1).split(",")).toList(),productions,
                Arrays.stream(elements.get(3).split(",")).toList());
        return grammar;
    }
}
