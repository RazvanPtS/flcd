import com.sun.source.tree.Tree;
import st.STKey;
import st.SymbolTable;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Analyzer {

    private final List<String> separators = new ArrayList<String>(Arrays.asList("[", "]", "{", "}", "(", ")", ":", ";", " "));
    private List<String> reservedKW = new ArrayList<String>(Arrays.asList("array", "char", "const", "do", "else", "if",
            "int", "of", "application", "read", "write", "for", "while", "then", "var", "double", "boolean", "string"));
    private final List<String> operators = new ArrayList<>(Arrays.asList("+", "-", "*", "/", ":=", "<", "<=", "=", ">=", "!="));
    private final Pattern matchIdentifier = Pattern.compile("\\b[a-zA-z][a-zA-Z0-9_]*\\b");
    private final String toSplit = "(?<=[+\\-*/(:=)<(<=)=(>=)(!=)\\[\\]{}\\(\\):; ])|" +
            "(?=[+\\-*/(:=)<(<=)=(>=)(!=)\\[\\]{}\\(\\):; ])";
    private final Pattern stringConstant = Pattern.compile("\"[a-zA-Z0-9]*\"");
    private final Pattern integerConstant = Pattern.compile("0|[\\+-]?[1-9]+[0-9]*");
    private final Pattern doubleConstant = Pattern.compile("0|[\\+-]?[1-9]+[0-9]*(\\.[0-9]+)?");
    public boolean runAnalyzer(String fileName) throws IOException {
        File file = new File(fileName);
        Scanner sc = new Scanner(file);
        StringBuilder builder = new StringBuilder();
        StringBuilder rawInput = new StringBuilder();
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            rawInput.append(line);
            line = line.replace(" ", "");
            builder.append(line.trim());
        }
        String oneLine = builder.toString();
        oneLine = oneLine.replace(" ", "");
        List<String> split = Arrays.stream(builder.toString().split(toSplit)).collect(Collectors.toList());
        for(int i=0; i<split.size(); i++){
            if(split.get(i).equals(":")){
                if(split.get(i+1).equals("=")){
                    split.remove(i);
                    split.remove(i);
                    split.add(i, ":=");
                    i--;
                }
            }
            else if(split.get(i).equals("=") && i>0){
                if(split.get(i-1).equals("!") || split.get(i-1).equals("<") || split.get(i-1).equals(">")){
                    String before = split.get(i-1);
                    split.remove(i-1);
                    split.remove(i-1);
                    split.add(i-1, before+"=");
                    i--;
                }
            }
            else if(split.get(i).equals("+") && i>0){
                if(split.get(i-1).equals(":=")){
                    split.remove(i);
                    i--;
                }
            }
            else if(split.get(i).equals("-") && i>0){
                if(split.get(i-1).equals(":=")){
                    split.remove(i);
                    split.add(i,split.get(i).replace(split.get(i), "-"+split.get(i)));
                    split.remove(i+1);
                    i--;
                }
            }
        }
        SymbolTable symbolTable = new SymbolTable();
        Map<STKey, String> pif = new LinkedHashMap<>();
        for(String word : split){
            if(separators.contains(word)) {
                pif.put(new STKey(0,0), word);
                continue;
            }
            if(reservedKW.contains(word)) {
                pif.put(new STKey(0,0), word);
                continue;
            }
            if(operators.contains(word)) {
                pif.put(new STKey(0,0), word);
                continue;
            }
            if(Pattern.matches(stringConstant.pattern(), word)){
                pif.put(new STKey(0,0), "constant=> "+word);
                continue;
            }
            if(Pattern.matches(doubleConstant.pattern(), word)){
                pif.put(new STKey(0,0), "constant=> "+word);
                continue;
            }
            if(Pattern.matches(integerConstant.pattern(), word)){
                pif.put(new STKey(0,0), "constant=> "+word);
                continue;
            }
            if(Pattern.matches(matchIdentifier.pattern(), word)) {
                STKey index = symbolTable.add(word);
                pif.put(index, word);
                continue;
            }
            String before = rawInput.toString().substring(0, rawInput.toString().indexOf(word));
            Long line = Arrays.stream(before.split("\\t")).filter(w->{return !w.equals("")&&w!=null;}).count();
            if(before.length() == 0) {
                line = 1L;
            }
            else if(before.substring(before.length()-1).equals("\t")){
                line++;
            }
            System.out.println("Lexical error at line "+line+"\n" + word +" lexically incorrect");
            return false;
        }
        BufferedWriter pbw = new BufferedWriter(new FileWriter("PIF.out"));
        StringBuilder st = new StringBuilder();
        st.append("STKey - wrapper for hashing value and internal list index\n");
        pif.forEach((k,v)->{
            st.append(k).append("='").append(v).append("'\n");
        });
        pbw.write(st.toString());
        pbw.close();
        BufferedWriter sbw = new BufferedWriter(new FileWriter("ST.out"));
        sbw.write("""
                Symbol table - implemented as a hash table composed of a HashMap with the key
                equal to the hashing value of the identifier and the value consisting of a list
                made up by all the identifiers that have the same hashing value;
                In ST.out only the index of the HashMap(the hashing value) is shown
                (hashvalue) => identifier
                """);
        sbw.write(symbolTable.toString());
        sbw.close();
        return true;
    }


}
