
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BooleanFunctionHasher {

    // Assign a unique character code to each variable and its negation
    private static final byte[] VAR_CODES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".getBytes(StandardCharsets.US_ASCII);

    // Compute the FNV-1a hash of a byte array
    private static int fnvHash(byte[] data) {
        final int FNV_OFFSET = 0x811c9dc5;
        final int FNV_PRIME = 0x01000193;
        int hash = FNV_OFFSET;
        for (byte b : data) {
            hash ^= b;
            hash *= FNV_PRIME;
        }
        return hash;
    }

    // Hash a Boolean function string in the format described above
    public static int hash(String function) {
        // Convert the function to a canonical form by sorting the subformulas and the entire function alphabetically
        String[] subformulas = function.split("\\+");
        Arrays.sort(subformulas);
        function = String.join("+", subformulas);
        char[] chars = function.toCharArray();
        Arrays.sort(chars);
        function = new String(chars);

        // Replace each variable and its negation with a unique character code
        byte[] bytes = function.getBytes(StandardCharsets.US_ASCII);
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            if (b >= 'A' && b <= 'Z') {
                bytes[i] = VAR_CODES[b - 'A'];
            } else if (b >= 'a' && b <= 'z') {
                bytes[i] = VAR_CODES[b - 'a' + 26];
            }
        }

        // Compute the hash value of the byte array using the FNV-1a hash function
        return fnvHash(bytes);
    }
}
