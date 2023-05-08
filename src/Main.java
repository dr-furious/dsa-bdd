import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws BDDNotInitializedException {
        Tester tester = new Tester();


        BinaryDecisionDiagram testBDD = new BinaryDecisionDiagram();
        tester.testBDD("ABCD+!ABCD", "ABCD", "0000", true);

        //tester.testBDD(13, false,1);
    }
}