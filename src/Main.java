import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws BDDNotInitializedException {
        Tester tester = new Tester();


        BinaryDecisionDiagram testBDD = new BinaryDecisionDiagram();
        //tester.testBDD("ABCE!D+H!JGIF+LNK!M+P!OQR", "EJDFHPIBAQNOKRCLMG", "000000000000000000", false);


        for (int i = 1; i <= 2; i++) {
            tester.testBDD(13, false,5, i);
            System.out.println("========================================= " + i + ". done");
        }


        /*String s = "";
        try {
             s = Files.readString(Paths.get("./src/input.txt"));
            System.out.println(s);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }


        // tester.testBDD(s,"DGHJNOPQSTUWZ", "0100000000000", false);
        testBDD.createBDD(s,"DGHJNOPQSTUWZ");*/
    }
}