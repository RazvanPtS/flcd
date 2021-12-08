import java.io.File;
import java.io.FileNotFoundException;
import java.lang.management.MemoryPoolMXBean;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    private static String epsilon = "ε";
    private static String $ = "$";

    public static void main(String[] args) throws FileNotFoundException {
        Grammar grammar = readGrammar();
        Scanner input = new Scanner(System.in);
        System.out.println("1-Non-terminals\n2-Terminals\n3-productions\n4-initial states\n5-Productions for non-terminal\n6-CFG check\n7-FIRST\n8-FOLLOW\n0-exit");
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
                case "9":
                    constructParsingTable(grammar);
                    break;
                case "0":
                    return;
            }
            System.out.println("1-Non-terminals\n2-Terminals\n3-productions\n4-initial states\n5-Productions for non-terminal\n6-CFG check\n7-FIRST\n8-FOLLOW\n0-exit");
            in = input.nextLine();
        }
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
            if(first.equals(epsilon))
                continue;
            g.getFirst().get(first).forEach(fV->{
                    if(!fV.equals(epsilon))
                        if(!pT.containsKey(new Pair<>(lhs, fV)))
                            pT.put(new Pair<>(lhs, fV), new Pair<>(first, finalI+1));
            });
            if(g.getFirst().get(first).contains(epsilon)){
                int finalI1 = i;
                g.getFollow().get(lhs).forEach(fV->{
                    pT.put(new Pair<>(lhs, fV), new Pair<>(first, finalI1+1));
                });
            }
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
        nonTerminals.forEach(nonT->{
            productions.forEach(pr->{
                int start = 0;
                int nonTIndex = pr.getFinalState().indexOf(nonT, start);
                while(nonTIndex != -1 && nonTIndex < pr.getFinalState().length()-1){
                    if(!follow.containsKey(nonT)){
                        follow.put(nonT, grammar.getFirst().get(String.valueOf(pr.getFinalState().charAt(nonTIndex+1))));
                    }
                    else{
                        follow.get(nonT).addAll(grammar.getFirst().get(String.valueOf(pr.getFinalState().charAt(nonTIndex+1))));
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
