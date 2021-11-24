import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

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
                    //getFollow(grammar);
                    break;
                case "0":
                    return;
            }
            System.out.println("1-Non-terminals\n2-Terminals\n3-productions\n4-initial states\n5-Productions for non-terminal\n6-CFG check\n7-FIRST\n8-FOLLOW\n0-exit");
            in = input.nextLine();
        }
    }

    private static void getFirst(Grammar grammar) {
        Map<String, List<String>> first = new HashMap<>();
        List<String> terminals = grammar.getTerminals();

        //init

        terminals.forEach(terminal->
            first.put(terminal, List.of(terminal)));

        grammar.getNonTerminals().forEach(nonT->{
            List<Production> productions = grammar.getProductions().stream().filter(pr->pr.getInitState().equals(nonT)).collect(Collectors.toList());
            for(Production p : productions) {
                String finalP = p.getFinalState();
                for (int i = 0; i < finalP.length(); i++) {
                    if (terminals.contains(String.valueOf(finalP.charAt(i)))) {
                        first.put(nonT, List.of(String.valueOf(finalP.charAt(i))));
                        break;
                    }
                }
                if (!first.containsKey(nonT)) {
                    first.put(nonT, List.of());
                }
            }
        });
        first.forEach((k,v)->System.out.println(k+"->"+v));
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
        Pattern function = Pattern.compile("(.*?)->([^,]+)");
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
