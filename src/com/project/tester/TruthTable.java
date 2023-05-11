package com.project.tester;

import com.project.Utility;

import java.util.ArrayList;

public class TruthTable {
    private ArrayList<char[]> variableValues;
    private char[] results;

    public ArrayList<char[]> getVariableValues() {
        return variableValues;
    }

    public char[] getResults() {
        return results;
    }

    public TruthTable(int length) {
        generateTable(length);
    }

    public void generateTable(int length) {
        variableValues = new ArrayList<>();
        int valuesLength = (int) Math.pow(2, length);
        results = generateRandomBinaryArray(valuesLength);

        for (int i = length-1; i >= 0; i--) {
            variableValues.add(generateBinaryValues((int) Math.pow(2,i), valuesLength));
        }
    }

    public String getVariableValues(int index) {
        StringBuilder sb = new StringBuilder();

        for (char[] variableValue : variableValues) {
            sb.append(variableValue[index]);
        }

        return sb.toString();
    }
    private char[] generateBinaryValues(int binarySectionLength, int outputLength) {
        int counter = 0;
        boolean ones = false;
        char[] output = new char[outputLength];

        for (int i = 0; i < outputLength; i++) {
            if (ones) {
                output[i] = '1';
                counter++;
                if (counter == binarySectionLength) {
                    counter = 0;
                    ones = false;
                }
            } else {
                output[i] = '0';
                counter++;
                if (counter == binarySectionLength) {
                    counter = 0;
                    ones = true;
                }
            }
        }

        return output;
    }
    private char getRandomBinaryValue() {
        return (Utility.generateInt(0,2) == 1) ? '1' : '0';
    }
    private char[] generateRandomBinaryArray(int length) {
        char[] output = new char[length];
        for (int i = 0; i < length; i++) {
            output[i] = getRandomBinaryValue();
        }

        return output;
    }
}
