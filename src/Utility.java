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
}
