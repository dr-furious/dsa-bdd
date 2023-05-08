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

        return Utility.hash(str.split("\\+")) == Utility.hash(this.toString().split("\\+")) &&
                Utility.hash2(str.split("\\+")) == Utility.hash2(this.toString().split("\\+")) &&
                Utility.hash3(str.split("\\+")) == Utility.hash3(this.toString().split("\\+")) &&
                Utility.hash4(str.split("\\+")) == Utility.hash4(this.toString().split("\\+"));
    }

    @Override
    public String toString() {
        return this.s;
    }
}
