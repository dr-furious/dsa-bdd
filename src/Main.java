import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //BinaryDecisionDiagram bdd = new BinaryDecisionDiagram();
        //bdd.createBDD("ABC+!AB+AB!C+CD+A+A!D", "ABCD");
        //bdd.print();

        String[] s = {"A","A!D", "ABC","A", "!AB","A!D", "AB!C", "ABC","CD", "A", "A!D"};
        String[] s2 = {"A","A!D", "ABC","A", "AB!C", "ABC","CD", "A", "A!D"};
        Node node = new Node('A', s);
        Node node2 = new Node('B', s2);
        BinaryDecisionDiagram bdd = new BinaryDecisionDiagram();
        // System.out.println(Arrays.toString(Utility.removeDuplicates(s)));

        MultiMap<Integer, Node> map = new MultiMap<>();
        System.out.println(map.put(1, node));
        System.out.println(map.put(1, node2));
        System.out.println(map.put(3, node2));
        System.out.println(map.put(3, node2));
        System.out.println(map.put(234, node2));
        System.out.println(map.put(234, node));

        System.out.println(map);

        System.out.println(map.delete(1, node));
        System.out.println(map.delete(1, node));
        System.out.println(map.delete(234, node));

        System.out.println(map);

        String s1 = "A!D + A!BC + A + !AB + A!D + !A!B!C + ABC + CD + A + A!D";
        //System.out.println(bdd.rewriteNegations(s1));
    }
}