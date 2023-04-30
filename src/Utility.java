import jdk.jshell.execution.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Utility {

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

    public static String[] removeDuplicates(String[] s) {
        MultiMap<Integer, StringWrapper> map = new MultiMap<>();
        StringWrapper stringWrapper;

        for (String value : s) {
            stringWrapper = new StringWrapper(value);
            map.put(Utility.hash(stringWrapper.toString()), stringWrapper);
        }

        return map.toStringArray();
    }

    public static ArrayList<String> removeDuplicates(ArrayList<String> s) {
        MultiMap<Integer, StringWrapper> map = new MultiMap<>();
        StringWrapper stringWrapper;

        for (String value : s) {
            stringWrapper = new StringWrapper(value);
            map.put(Utility.hash(stringWrapper.toString()), stringWrapper);
        }

        return map.toArrayList();
    }

    public static String removeDuplicates(String s) {
        ArrayList<Character> list = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {
            if (!list.contains(s.charAt(i))) {
                list.add(s.charAt(i));
            }
        }

        StringBuilder newS = new StringBuilder();
        for (Character character : list) {
            newS.append(character);
        }

        return newS.toString();
    }

    public static boolean contains(String s, char c) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                return true;
            }
        }
        return false;
    }
    public static int hash(String[] boolFunction) {
        int hash = 0;
        for (String s : boolFunction) {
            for (int j = 0; j < s.length(); j++) {
                hash += s.charAt(j);
            }
        }

        return hash;
    }

    public static int hash(String boolFunction) {
        int hash = 0;
        for (int j = 0; j < boolFunction.length(); j++) {
            hash += boolFunction.charAt(j);
        }

        return hash;
    }
    public static long hash2(String[] boolFunction) {
        int hash = 0;
        for (String s : boolFunction) {
            int midHash = 1;
            for (int j = 0; j < s.length(); j++) {
                midHash *= s.charAt(j);
            }
            hash += midHash % s.length();
        }

        return hash;
    }

    public static long hash2(String boolFunction) {
        int hash = 1;
        for (int j = 0; j < boolFunction.length(); j++) {
            hash *= boolFunction.charAt(j);
        }

        return hash;
    }
}
