import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultiMap<T, U> {
    private HashMap<T, List<U>> map;
    private int size;

    public MultiMap() {
        map = new HashMap<>();
    }

    public boolean put(T key, U value) {
        List<U> inMapValue = map.get(key);

        if (inMapValue == null) {
            List<U> list = new ArrayList<>();
            list.add(value);
            map.put(key, list);
            size++;
            return true;
        }

        if (inMapValue.contains(value)) {
            return false;
        } else {
            inMapValue.add(value);
            size++;
        }

        return true;
    }

    public boolean delete(T key, U value) {
        List<U> inMapValue = map.get(key);

        if (inMapValue == null) {
            return false;
        }

        if (inMapValue.contains(value)) {
            inMapValue.remove(value);
            size--;
            return true;
        }

        return false;
    }

    public U find (T key, U value) {
        List<U> inMapValue = map.get(key);

        if (inMapValue == null) {
            return null;
        }

        for (U u : inMapValue) {
            if (u.equals(value)) {
                return u;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        for (T key : map.keySet()) {
            List<U> list = map.get(key);
            s
                    .append("[")
                    .append(key)
                    .append(",");
            for (U value : list) {
                s
                        .append(value.toString())
                        .append("] --> ");
            }
            s.append("NULL\n");
        }
        return s.toString();
    }

    public String[] toStringArray() {
        String[] s = new String[size];
        int index = 0;

        for (T key : map.keySet()) {
            List<U> list = map.get(key);
            for (U value : list) {
                s[index] = value.toString();
                index++;
            }
        }
        return s;
    }

    public int getSize() {
        return size;
    }
}
