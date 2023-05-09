import java.util.*;

public class Tester {
    private static final String upperCaseAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private TruthTable truthTable;
    private String boolFunction; // DNF representation of a boolean function


    public Tester() {}

    public static char solve(String boolFunction,String inputs) {
        String alphabet = Utility.extractUniqueLetters(boolFunction);
        boolFunction = BinaryDecisionDiagram.rewriteNegations(boolFunction);

        HashMap<Character, Integer> boolMap = new HashMap<>();
        if (alphabet.length() != inputs.length()) {
            throw new IllegalArgumentException("Argument \"inputs\" should be the same length as number " +
                    "of unique variables in the argument \"boolFunction\".");
        }

        for (int i = 0; i < alphabet.length(); i++) {
            boolMap.put(alphabet.charAt(i), Integer.parseInt(Character.toString(inputs.charAt(i))));
        }

        String[] arrBoolFunction = boolFunction.split("\\+");
        int result = 0;
        int partialResult;
        int operator;
        char upper, lower;
        for (String s: arrBoolFunction) {
            partialResult = 1;
            for (int i = 0; i < s.length(); i++) {
                upper = Character.toUpperCase(s.charAt(i));
                lower = Character.toLowerCase(s.charAt(i));
                operator = boolMap.get(upper);

                if (s.charAt(i) == lower) {
                    partialResult *= revert(operator);
                } else {
                    partialResult *= operator;
                }

                if (partialResult == 0) {
                    break;
                }
            }
            result += partialResult;
            if (result > 0) {
                return '1';
            }
        }


        return '0';
    }
    private static int revert(int operator) {
        if (operator != 0 && operator != 1) {
            throw new IllegalArgumentException("The argument \"operator\" must be either of value 1 or 0.");
        }

        return (operator == 1) ? 0 : 1;
    }
    private void generateRandomBoolFunction(int alphabetLength) {
        ArrayList<String> alphabet = generateAlphabet(alphabetLength);
        Collections.sort(alphabet);

        truthTable = new TruthTable(alphabetLength);
        StringBuilder boolFunction = new StringBuilder();

        /*
        System.out.println(alphabet);
        for (int i = 0; i <alphabetLength; i++) {
            System.out.println(Arrays.toString(truthTable.getVariableValues().get(i)));
        }
        System.out.println("=====================\n"+Arrays.toString(truthTable.getResults()));
         */

        ArrayList<char[]> variableValues = truthTable.getVariableValues();
        char[] results = truthTable.getResults();
        for (int i = 0; i < results.length; i++) {
            if (results[i] == '1') {
                for (int j = 0; j < variableValues.size(); j++) {
                    if (variableValues.get(j)[i] == '1') {
                        boolFunction.append(alphabet.get(j));
                    } else {
                        boolFunction.append("!").append(alphabet.get(j));
                    }
                }
                boolFunction.append("+");
            }
        }

        int lastPlusIndex = boolFunction.lastIndexOf("+");
        if (lastPlusIndex > -1) {
            boolFunction.deleteCharAt(lastPlusIndex);
        }

        this.boolFunction = boolFunction.toString();
    }
    private ArrayList<String> generateAlphabet(int length) {
        String[] alphabet = new String[length];
        Arrays.fill(alphabet, "-");

        int i = 0;
        while(Utility.contains(alphabet, '-')) {
            int index = Utility.generateInt(0,upperCaseAlphabet.length());
            if (Utility.contains(alphabet, upperCaseAlphabet.charAt(index))) {
                continue;
            }
            alphabet[i] = Character.toString(upperCaseAlphabet.charAt(index));
            i++;
        }

        ArrayList<String> finalAlphabet = new ArrayList<>();
        Collections.addAll(finalAlphabet, alphabet);

        return finalAlphabet;
    }
    public void testBB(String boolFunction, String inputs, boolean print) throws BDDNotInitializedException {
        testBDD(boolFunction, Utility.extractUniqueLetters(boolFunction), inputs, print);
    }
    public void testBDD(String boolFunction, String order,String inputs, boolean print) throws BDDNotInitializedException {
        BinaryDecisionDiagram bdd = new BinaryDecisionDiagram();
        BinaryDecisionDiagram bestOrderBDD;
        long start;
        long stop;

        start = System.nanoTime();
        bdd.createBDD(boolFunction, order);
        bestOrderBDD = BinaryDecisionDiagram.createBDDWithBestOrder(boolFunction);
        stop = System.nanoTime();

        if (print) {
            bdd.printNicely(false);
            System.out.println();
            bestOrderBDD.printNicely(false);
        }

        System.out.println("\nNumber of nodes before reduction: " + bdd.getAllNodes());
        System.out.println("Number of nodes after reduction: " + bdd.getNumberOfNodes());
        System.out.println("Reduction ratio: " + bdd.getReductionRatio() + "%");
        System.out.println("Reduction duration: " + (stop-start) + " nanoseconds = " + ((stop-start)/1_000_000.0) + " milliseconds = " + ((stop-start)/1_000_000_000.0) + " seconds");

        System.out.println("\n ---- Best order:");

        System.out.println("Best order: " + bestOrderBDD.getOrder());
        System.out.println("Number of nodes before reduction: " + bestOrderBDD.getAllNodes());
        System.out.println("Number of nodes after reduction: " + bestOrderBDD.getNumberOfNodes());
        System.out.println("Reduction ratio: " + bestOrderBDD.getReductionRatio() + "%");
        System.out.println("Reduction duration: " + (stop-start) + " nanoseconds = " + ((stop-start)/1_000_000.0) + " milliseconds = " + ((stop-start)/1_000_000_000.0) + " seconds");

        System.out.println("Ordinary BDD_use: " + bdd.useBDD(inputs));
        System.out.println("Best order BDD_use: " + bestOrderBDD.useBDD(inputs));
    }

    public void testBDD(int numberOfVariables, boolean print, int iterations) throws BDDNotInitializedException {
        if (iterations < 1) {
            return;
        }
        BinaryDecisionDiagram bdd;
        BinaryDecisionDiagram bestOrderBDD;

        long start;
        long stop;

        double createTime = 0;
        double bestOrderCreateTime = 0;
        double useTime = 0;
        double useBestOrderTime = 0;

        double bddUseSuccessRatio = 0;
        double bestOrderBDDUseSuccessRatio = 0;

        double bddNumberOfNodesAfterReduction = 0;
        double bestOrderBDDNumberOfNodesAfterReduction = 0;

        int bestOrderNodesCount = (int) Math.pow(2,(numberOfVariables+1))-1;
        String bestOrder = "";
        for (int i = 0; i < iterations; i++) {
            bdd = new BinaryDecisionDiagram();
            generateRandomBoolFunction(numberOfVariables);

            start = System.nanoTime();
            bdd.createBDD(this.boolFunction, Utility.extractUniqueLetters(this.boolFunction));
            stop = System.nanoTime();
            bddNumberOfNodesAfterReduction += bdd.getNumberOfNodes();
            createTime += stop-start;

            start = System.nanoTime();
            bestOrderBDD = BinaryDecisionDiagram.createBDDWithBestOrder(this.boolFunction);
            stop = System.nanoTime();
            int currentBestOrderNumberOfNodes = bestOrderBDD.getNumberOfNodes();
            bestOrderBDDNumberOfNodesAfterReduction += currentBestOrderNumberOfNodes;
            if (currentBestOrderNumberOfNodes < bestOrderNodesCount) {
                bestOrderNodesCount = currentBestOrderNumberOfNodes;
                bestOrder = bestOrderBDD.getOrder();
            }
            bestOrderCreateTime += stop-start;

            start = System.nanoTime();
            bddUseSuccessRatio += testBDDUse(bdd);
            stop = System.nanoTime();
            useTime += stop-start;

            start = System.nanoTime();
            bestOrderBDDUseSuccessRatio += testBDDUse(bestOrderBDD);
            stop = System.nanoTime();
            useBestOrderTime += stop-start;

            System.out.println("Iteration " + (i+1) + ". done.");
        }

        System.out.println("" +
                "BINARY DECISION DIAGRAMS TESTING\n" +
                "-----------------------------------------\n" +
                "> Tested Boolean Function:\n> " + boolFunction + "\n" +
                "> Number of Variables:\n> " + numberOfVariables + "\n" +
                "> Iterations:\n> " + iterations + "\n" +
                "-----------------------------------------\n\n" +
                "RESULTS:\n" +
                "-----------------------------------------\n" +
                "> Binary Decision Diagram\n" +
                "> Order: " + Utility.extractUniqueLetters(this.boolFunction) + "\n" +
                "> Average Number of Nodes Before Reduction: " + ( (int) Math.pow(2,(numberOfVariables+1))-1 ) + "\n" +
                "> Average Number of Nodes After Reduction: " + (bddNumberOfNodesAfterReduction/iterations) + "\n" +
                "> Average Reduction Ratio: " + (((Math.pow(2,(numberOfVariables+1))-1)-bddNumberOfNodesAfterReduction/iterations)/(Math.pow(2,(numberOfVariables+1))-1)*100) +"%\n" +
                "> Average Reduction Duration: " + formatTime(createTime/iterations) + "\n" +
                "> Average Testing Duration: " + formatTime(useTime/iterations) + "\n" +
                "> Average Test Success Ratio: " + ((bddUseSuccessRatio/iterations)*100) + "%\n" +
                "\n" +
                "> Best Order Binary Decision Diagram\n" +
                "> Order: " + bestOrder + "\n" +
                "> Average Number of Nodes Before Reduction: " + ( (int) Math.pow(2,(numberOfVariables+1))-1 ) + "\n" +
                "> Average Number of Nodes After Reduction: " + (bestOrderBDDNumberOfNodesAfterReduction/iterations) + "\n" +
                "> Average Reduction Ratio: " + (((Math.pow(2,(numberOfVariables+1))-1)-bestOrderBDDNumberOfNodesAfterReduction/iterations)/(Math.pow(2,(numberOfVariables+1))-1)*100) +"%\n" +
                "> Average Reduction Duration: " + formatTime(bestOrderCreateTime/iterations) + "\n" +
                "> Average Testing Duration: " + formatTime(useBestOrderTime/iterations) + "\n" +
                "> Average Test Success Ratio: " + ((bestOrderBDDUseSuccessRatio/iterations)*100) + "%\n" +
                "-----------------------------------------\n"
        );
        System.out.println("END");
        if (((bestOrderBDDUseSuccessRatio/iterations)*100) < 100) {
            System.exit(10);
        }
    }

    public double testBDDUse(BinaryDecisionDiagram bdd) throws BDDNotInitializedException {
        if (bdd.getRoot() == null) {
            throw new IllegalArgumentException("BDD has yet not been initialized. Suggestion: call createBDD first.");
        }

        double successfulTest = 0;
        ArrayList<char[]> variableValues = truthTable.getVariableValues();
        int numberOfTest = variableValues.get(0).length;

        char[] results = truthTable.getResults();
        for (int i = 0; i < numberOfTest; i++) {
            String inputs = truthTable.getVariableValues(i);
            char result = results[i];
            if (bdd.useBDD(inputs) == result) {
                successfulTest++;
            } else {
                System.out.println(Arrays.toString(bdd.getRoot().getBoolFunction()) + " | " + bdd.getOrder() + " | " + inputs + " | " + result);

            }
        }

        return successfulTest/numberOfTest;
    }
    private String formatTime(double nanoseconds) {
        return nanoseconds + " nanoseconds = " +
                (nanoseconds/1_000_000.0) + " milliseconds = " +
                (nanoseconds/1_000_000_000.0) + " seconds";
    }
}
