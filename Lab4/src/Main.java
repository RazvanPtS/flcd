import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        FA fa = readFa();
        Scanner input = new Scanner(System.in);
        System.out.println("1-All states\n2-alphabet\n3-transitions\n4-initial states\n5-final states\n6-Input DFA\n0-exit");
        String in = input.nextLine();
        while(!in.equals("0")){
            switch (in){
                case "1":
                    printAllStates(fa);
                    break;
                case "2":
                    printAlphabet(fa);
                    break;
                case "3":
                    printTransitions(fa);
                    break;
                case "4":
                    printInitialStates(fa);
                    break;
                case "5":
                    printFinalStates(fa);
                    break;
                case "6":
                    readDFA(fa);
                    break;
                case "0":
                    return;
            }
            System.out.println("1-All states\n2-alphabet\n3-transitions\n4-initial states\n5-final states\n6-Input DFA\n0-exit");
            in = input.nextLine();
        }
    }

    private static void readDFA(FA fa) {
        Scanner s = new Scanner(System.in);
        String dfa = s.nextLine();
        int charIndex = 0;
        boolean accepted = false;
        for(int i=0; i<fa.getInitialStates().size();i++){
            if(parseDFA(fa, charIndex, fa.getInitialStates().get(i), dfa)){
                accepted = true;
                break;
            }
        }
        System.out.println(accepted ? "Accepted" : "Not accepted");
    }

    private static boolean parseDFA(FA fa, int charIndex, String initState, String dfa) {
        if(charIndex == dfa.length())
            return fa.getFinalStates().contains(initState);
        char start = dfa.charAt(charIndex);
        if(!fa.getAlph().contains(String.valueOf(start)))
            return false;

        List<String> nextStates = fa.getFunctions().stream().filter(st->
            st.getInitState().equals(initState) && st.getTransformation().equals(String.valueOf(start))
        ).map(Transition::getFinalState).collect(Collectors.toList());
        if(nextStates.isEmpty()){
            return false;
        }
        boolean res = false;
        for (String nextState : nextStates) {
            res = res || parseDFA(fa, charIndex+1, nextState, dfa);
        }
        return res;
    }

    private static void printAllStates(FA fa) {
        fa.getAllStates().forEach(System.out::println);
    }

    private static void printFinalStates(FA fa) {
        System.out.println(fa.getFinalStates().toString());
    }

    private static void printTransitions(FA fa) {
        fa.getFunctions().forEach(System.out::println);
    }

    private static void printAlphabet(FA fa) {
        System.out.println(fa.getAlph().toString());
    }

    private static void printInitialStates(FA fa) {
        System.out.println(fa.getInitialStates().toString());
    }

    private static FA readFa() throws FileNotFoundException {
        FA fa;
        File file = new File("FA.in");
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
        Pattern function = Pattern.compile("\\((.*?),(.*?)\\)->([^,]+?)");
        List<Transition> transitions = new ArrayList<>();
        Matcher m = function.matcher(elements.get(2));
        while(m.find()) {
            Transition t = new Transition(m.group(1), m.group(2), m.group(3));
            transitions.add(t);
        }
        fa = new FA(Arrays.stream(elements.get(0).split(",")).toList(),
                Arrays.stream(elements.get(1).split(",")).toList(),transitions,
                Arrays.stream(elements.get(3).split(",")).toList(),
                Arrays.stream(elements.get(4).split(",")).toList());
        return fa;
    }
}
