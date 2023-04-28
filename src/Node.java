import java.util.Arrays;

public class Node {
    private char value;
    private String[] boolFunction;

    private Node zeroChild;
    private Node oneChild;

    public Node(char value, String[] boolFunction) {
        this.value = value;
        this.boolFunction = boolFunction;
        zeroChild = null;
        oneChild = null;
    }

    public Node(char value) {
        this.value = value;
        this.boolFunction = null;
        zeroChild = null;
        oneChild = null;

    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public String[] getBoolFunction() {
        return boolFunction;
    }

    public void setBoolFunction(String[] boolFunction) {
        this.boolFunction = boolFunction;
    }

    public Node getZeroChild() {
        return zeroChild;
    }

    public void setZeroChild(Node zeroChild) {
        this.zeroChild = zeroChild;
    }

    public Node getOneChild() {
        return oneChild;
    }

    public void setOneChild(Node oneChild) {
        this.oneChild = oneChild;
    }

    // AB+!CD+AC+!B
    public void createZeroChild(char controlValue,Node oneNode,Node zeroNode) {
        String newBoolFunction = "";
        for (String s : this.boolFunction) {
            if (s.contains("" + this.value)) {
                if (s.contains("!"+this.value)) {
                    if (s.length() == 2) {
                        setZeroChild(oneNode);
                        return;
                    }
                    for (int i = 0; i < s.length(); i++) {
                        if (s.charAt(i) == '!') {
                            i++;
                            continue;
                        }
                        newBoolFunction += Character.toString(s.charAt(i));
                    }
                    newBoolFunction += "+";
                }
                continue;
            }
            newBoolFunction += s + "+";
        }

        if (newBoolFunction.length() == 0) {
            this.zeroChild = zeroNode;
            return;
        }
        this.zeroChild = new Node(controlValue, Utility.removeDuplicates(newBoolFunction.split("\\+")));
    }

    public void createOneChild(char controlValue, Node oneNode,Node zeroNode) {
        String newBoolFunction = "";
        for (String s : this.boolFunction) {
            if (s.contains("!" + this.value)) {
                if (s.length() == 2) {
                    setOneChild(zeroNode);
                }
                continue;
            }
            if (s.contains("" + this.value)) {
                if (s.length() == 1) {
                    setOneChild(oneNode);
                    return;
                }
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == this.value) {
                        continue;
                    }
                    newBoolFunction += Character.toString(s.charAt(i));
                }
                newBoolFunction += "+";
            } else {
                newBoolFunction += s + "+";
            }
        }

        if (newBoolFunction.length() == 0) {
            this.oneChild = oneNode;
            return;
        }
        this.oneChild = new Node(controlValue, Utility.removeDuplicates(newBoolFunction.split("\\+")));
    }

    @Override
    public String toString() {
        if (this.zeroChild != null && (this.zeroChild.getValue() == '0' || this.zeroChild.getValue() == '1') &&
                this.oneChild != null && (this.oneChild.getValue() == '1' || this.oneChild.getValue() == '0')) {
            return "{" +
                    "V:" + this.value + "|" +
                    "F:" + Arrays.toString(this.boolFunction) + "|" +
                    "0:" + this.zeroChild.getValue() + "|" +
                    "1:" + this.oneChild.getValue() +
                    "}"
            ;
        }
        if (this.zeroChild != null && (this.zeroChild.getValue() == '0' || this.zeroChild.getValue() == '1')) {
            return "{" +
                    "V:" + this.value + "|" +
                    "F:" + Arrays.toString(this.boolFunction) + "|" +
                    "0:" + this.zeroChild.getValue() + "|" +
                    "1:" + ((this.oneChild == null) ? "null" : Arrays.toString(this.oneChild.getBoolFunction())) +
                    "}"
            ;
        }
        if (this.oneChild != null && (this.oneChild.getValue() == '1' || this.oneChild.getValue() == '0')) {
            return "{" +
                    "V:" + this.value + "|" +
                    "F:" + Arrays.toString(this.boolFunction) + "|" +
                    "0:" + ((this.zeroChild == null) ? "null" : Arrays.toString(this.zeroChild.getBoolFunction())) + "|" +
                    "1:" + this.oneChild.getValue() +
                    "}"
            ;
        }
        return "{" +
                "V:" + this.value + "|" +
                "F:" + Arrays.toString(this.boolFunction) + "|" +
                "0:" + ((this.zeroChild == null) ? "null" : Arrays.toString(this.zeroChild.getBoolFunction())) + "|" +
                "1:" + ((this.oneChild == null) ? "null" : Arrays.toString(this.oneChild.getBoolFunction())) +
                "}"
        ;
    }

    public int computeHeight() {
        return computeHeight(this);
    }

    public static int computeHeight(Node root) {

        if (root == null) {
            return 0;
        }
        int leftHeight = computeHeight(root.getZeroChild());
        int rightHeight = computeHeight(root.getOneChild());

        return ((leftHeight > rightHeight) ? leftHeight : rightHeight) + 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != Node.class) {
            return false;
        }

        return Arrays.equals(((Node) obj).getBoolFunction(), this.boolFunction);
    }
}
