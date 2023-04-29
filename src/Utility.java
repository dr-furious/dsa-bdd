import java.util.ArrayList;

public class Utility {

    public static String[] removeDuplicates(String[] s) {
        ArrayList<String> list = new ArrayList<>();

        for (String value : s) {
            if (!list.contains(value)) {
                list.add(value);
            }
        }

        String[] newS = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            newS[i] = list.get(i);
        }

        return newS;
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
}
