public class StringWrapper {
    private String s;

    public StringWrapper(String s) {
        this.s = s;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        String str = obj.toString();

        return Utility.hash(str) == Utility.hash(this.toString()) &&
                Utility.hash2(str) == Utility.hash2(this.toString());
    }

    @Override
    public String toString() {
        return this.s;
    }
}
