import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultiMap<T, U> {
    private HashMap<T, List<U>> map;

    public MultiMap() {
        map = new HashMap<>();
    }

    public boolean put(T key, U value) {
        List<U> inMapValue = map.get(key);

        if (inMapValue == null) {
            List<U> list = new ArrayList<>();
            list.add(value);
            map.put(key, list);
            return true;
        }

        if (inMapValue.contains(value)) {
            return false;
        } else {
            inMapValue.add(value);
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
            return true;
        }

        return false;
    }

    public U find (T key, U value) {
        List<U> inMapValue = map.get(key);

        if (inMapValue == null) {
            return null;
        }

        if (inMapValue.contains(value)) {
            return value;
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
}
