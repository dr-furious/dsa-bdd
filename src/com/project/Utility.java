package com.project;

import jdk.jshell.execution.Util;

import java.util.*;

public class Utility {

    public static int generateInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Argument \"min\" must be lower than argument \"max\".");
        }

        return (int) (Math.random()*(max-min)+min);
    }
    public static String shift(String s) {
        return s.substring(s.length()-1) + s.substring(0,s.length()-1);
    }

    public static String shuffle(String s) {
        ArrayList<Character> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i<s.length(); i++) {
            list.add(s.charAt(i));
        }

        Collections.shuffle(list);

        for (char c : list) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static String extractUniqueLetters(String s) {
        if (s==null) {
            return null;
        }
        s = s.toUpperCase();
        StringBuilder sb = new StringBuilder();
        ArrayList<Character> list = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!list.contains(c) && (c >= 'A' && c <= 'Z')) {
                list.add(c);
            }
        }

        Collections.sort(list);
        for (Character character : list) {
            sb.append(character);
        }

        return sb.toString();
    }

    public static String sortBinaryFunction(String binaryFunction) {
        String[] parts = binaryFunction.split("\\+");
        StringBuilder sortedBinaryFunction = new StringBuilder();
        for (String part : parts) {
            char[] chars = part.toCharArray();
            Arrays.sort(chars);
            String sortedPart = new String(chars).replaceAll("(.)\\1+", "$1");
            sortedBinaryFunction.append(sortedPart);
            sortedBinaryFunction.append("+");
        }
        sortedBinaryFunction.deleteCharAt(sortedBinaryFunction.length() - 1);

        Set<String> oneOfAKind = new HashSet<>(Arrays.asList(sortedBinaryFunction.toString().split("\\+")));
        ArrayList<String> oneOfAKindList = new ArrayList<>(oneOfAKind);
        Collections.sort(oneOfAKindList);

        return oneOfAKindList.toString()
                .replaceAll(", ", "+")
                .replace("[", "")
                .replace("]", "");
    }

    public static boolean contains(String s, char c) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                return true;
            }
        }
        return false;
    }
    public static boolean contains(String[] s, char c) {
        for (String value : s) {
            if (value.equals(Character.toString(c))) {
                return true;
            }
        }
        return false;
    }
}
