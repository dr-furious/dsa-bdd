package com.project.tester;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.project.bdd.BDDNotInitializedException;
import com.project.bdd.BinaryDecisionDiagram;
import com.project.Utility;

public class Tester {
    private static final String upperCaseAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String TESTS_FILE_PATH = "./../tests/test_";
    private static final String TEST_INFO_PATH = "/info";
    private static final String TEST_RESULTS_PATH = "/results";
    private static final String TEST_BDD_VS_BO_BDD_PATH = "/bdd_vs_bo_bdd";

    private TruthTable truthTable;
    private String boolFunction; // DNF representation of a boolean function

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

    public void testBDD(int numberOfVariables, boolean print, int iterations, int testID) throws BDDNotInitializedException {
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

        double bddReductionRatio = ((Math.pow(2, (numberOfVariables + 1)) - 1) - bddNumberOfNodesAfterReduction / iterations) / (Math.pow(2, (numberOfVariables + 1)) - 1);
        double bestOrderBDDReductionRatio = ((Math.pow(2, (numberOfVariables + 1)) - 1) - bestOrderBDDNumberOfNodesAfterReduction / iterations) / (Math.pow(2, (numberOfVariables + 1)) - 1);
        String data = "" +
                "BINARY DECISION DIAGRAMS TESTING\n" +
                "-----------------------------------------\n" +
                "> Number of Variables:\n> " + numberOfVariables + "\n" +
                "> Iterations:\n> " + iterations + "\n" +
                "-----------------------------------------\n\n" +
                "RESULTS:\n" +
                "-----------------------------------------\n" +
                "> Binary Decision Diagram\n" +
                "> Average Number of Nodes Before Reduction: " + ( (int) Math.pow(2,(numberOfVariables+1))-1 ) + "\n" +
                "> Average Number of Nodes After Reduction: " + (bddNumberOfNodesAfterReduction/iterations) + "\n" +
                "> Average Reduction Ratio: " + (bddReductionRatio *100) +"%\n" +
                "> Average Reduction Duration: " + formatTime(createTime/iterations) + "\n" +
                "> Average Testing Duration: " + formatTime(useTime/iterations) + "\n" +
                "> Average Test Success Ratio: " + ((bddUseSuccessRatio/iterations)*100) + "%\n" +
                "\n" +
                "> Best Order Binary Decision Diagram\n" +
                "> Average Number of Nodes Before Reduction: " + ( (int) Math.pow(2,(numberOfVariables+1))-1 ) + "\n" +
                "> Average Number of Nodes After Reduction: " + (bestOrderBDDNumberOfNodesAfterReduction/iterations) + "\n" +
                "> Average Reduction Ratio: " + (bestOrderBDDReductionRatio *100) +"%\n" +
                "> Average Reduction Duration: " + formatTime(bestOrderCreateTime/iterations) + "\n" +
                "> Average Testing Duration: " + formatTime(useBestOrderTime/iterations) + "\n" +
                "> Average Test Success Ratio: " + ((bestOrderBDDUseSuccessRatio/iterations)*100) + "%\n" +
                "-----------------------------------------\n\n" +
                "> Average Reduction Ratio When Comparing BDD With Best-ordered BDD: " +
                (100-(bestOrderBDDReductionRatio *100)/(bddReductionRatio *100)) +
                "%\n-----------------------------------------\nEND\n";
        System.out.println(data);

        Path path = createFolders(TESTS_FILE_PATH + testID);
        String infoData = "" +
                "Number_of_Variables;"+ numberOfVariables +"\n" +
                "Number_of_Iterations;" + iterations;
        toFile(infoData, path, TEST_INFO_PATH);

        String resultsData = "" +
                "BDD_Type;Nodes_Before_Reduction;Nodes_After_Reduction;Reduction_Ratio;Reduction_Duration;Testing_Duration;Test_Success_Ratio\n" +
                "" +
                "Alphabetical_Ordered_BDD;" +
                ( (int) Math.pow(2,(numberOfVariables+1))-1 ) + ";" +
                (bddNumberOfNodesAfterReduction/iterations) + ";" +
                (bddReductionRatio *100) + ";" +
                (createTime/iterations) + ";" +
                (useTime/iterations) + ";" +
                ((bddUseSuccessRatio/iterations)*100) + "\n" +
                "" +
                "Best_Ordered_BDD;"+
                ( (int) Math.pow(2,(numberOfVariables+1))-1 ) + ";" +
                (bestOrderBDDNumberOfNodesAfterReduction/iterations) + ";" +
                (bestOrderBDDReductionRatio *100) + ";" +
                (bestOrderCreateTime/iterations) + ";" +
                (useBestOrderTime/iterations) + ";" +
                ((bestOrderBDDUseSuccessRatio/iterations)*100) + "\n";
        toFile(resultsData, path,TEST_RESULTS_PATH);

        String bddVSBOBDD = "" +
                "BDD_vs_BO_BDD\n" +
                (100-(bestOrderBDDReductionRatio *100)/(bddReductionRatio * 100));
        toFile(bddVSBOBDD, path, TEST_BDD_VS_BO_BDD_PATH);

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
            }
        }

        return successfulTest/numberOfTest;
    }
    private String formatTime(double nanoseconds) {
        return nanoseconds + " nanoseconds = " +
                (nanoseconds/1_000_000.0) + " milliseconds = " +
                (nanoseconds/1_000_000_000.0) + " seconds";
    }

    private void toFile(String data, Path path, String fileName) {
        String filePath = path.toString() + fileName + ".csv";

        File file = new File(filePath);
        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(data);
            System.out.println("Data written to the file " + filePath + ".");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file " + filePath);
            e.printStackTrace();
        } finally {
            try {
                assert bw != null;
                bw.close();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Path createFolders(String path) {
        Path filePath = Paths.get(path);
        if (!Files.exists(filePath)) {
            try {
                Files.createDirectories(filePath);
                System.out.println("Directory created: " + filePath);
            } catch (IOException e) {
                System.err.println("Failed to create directory: " + e.getMessage());
            }
        }

        return filePath;
    }
}
