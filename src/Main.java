import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws BDDNotInitializedException {
        Tester tester = new Tester();
        //tester.testBDD("A!B!C!D+C!A+A!C!B+B!D+A!C+B!A!C!D+C!D!A!B+!ACB!D+A", "CBAD", "1000", true);


        int id = 16;
        for (int i = 16; i <= 30; i++) {
            tester.testBDD(id, false,100, id*100+1);
            System.out.println("========================================= " + (id*100+1) + ". done");
            id++;
        }

    }
}