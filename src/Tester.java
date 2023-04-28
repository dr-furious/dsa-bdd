public class Tester {

    public static void main(String[] args) {
        BinaryDecisionDiagram bdd = new BinaryDecisionDiagram();

        String s1 = "A!D+A!BC+A+!AB+A!D+!A!B!C+ABC+CD+A+A!D+1";
        String s2 = "A+A+A+!A+A!+!A!B+A+CD+A+A";
        String s3 = "!A+!A+!A+!AAAAA+!A+!AA+!A+!A";
        String s4 = "!DD+D!D+!D+!D+DDD";
        String s5 = "!ZZ+Z!Z+!Z+!Z+ZZZZ!ZZZZ+1";

        System.out.println(bdd.isReadyToEval(s1 = bdd.rewriteNegations(s1)));
        System.out.println(bdd.isReadyToEval(s2 = bdd.rewriteNegations(s2)));
        System.out.println(bdd.isReadyToEval(s3 = bdd.rewriteNegations(s3)));
        System.out.println(bdd.isReadyToEval(s4 = bdd.rewriteNegations(s4)));
        System.out.println(bdd.isReadyToEval(s5 = bdd.rewriteNegations(s5)));

        System.out.println();

        System.out.println(bdd.evaluate(s1));
        System.out.println(bdd.evaluate(s3));
        System.out.println(bdd.evaluate(s4));
        System.out.println(bdd.evaluate(s5));

        System.out.println();

        System.out.println(bdd.contains(s1, 'A'));
        System.out.println(bdd.contains(s1, 'a'));
        System.out.println(bdd.contains(s1, 'Y'));
    }
}
