public class Tester {

    public static void main(String[] args) {
        BinaryDecisionDiagram bdd = new BinaryDecisionDiagram();

        String s1 = "A!D+A!BC+A+!AB+A!D+!A!B!C+ABC+CD+A+A!D";
        String s2 = "A+A+A+!A+A!+!A!B+A+CD+A+A";
        String s3 = "!A+!A+!A+!AAAAA+!A+!AA+!A+!A";
        String s4 = "!DD+D!D+!D+!D+DDD";
        String s5 = "!ZZ+Z!Z+!Z+!Z+ZZZZ!ZZZZ";

        System.out.println(bdd.isReadyToEval(bdd.rewriteNegations(s1)));
        System.out.println(bdd.isReadyToEval(bdd.rewriteNegations(s2)));
        System.out.println(bdd.isReadyToEval(bdd.rewriteNegations(s3)));
        System.out.println(bdd.isReadyToEval(bdd.rewriteNegations(s4)));
        System.out.println(bdd.isReadyToEval(bdd.rewriteNegations(s5)));

        System.out.println();

        System.out.println(bdd.evaluate(s3));
        System.out.println(bdd.evaluate(s4));
        System.out.println(bdd.evaluate(s5));
    }
}
