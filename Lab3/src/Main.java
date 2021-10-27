import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	Analyzer analyzer = new Analyzer();
    if(analyzer.runAnalyzer("p1.txt")){
        System.out.println("Lexically correct");
    }

    }
}
