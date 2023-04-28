import java.util.zip.ZipError;

public class BinaryDecisionDiagram {
    private static final String upperCaseAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String lowerCaseAlphabet = upperCaseAlphabet.toLowerCase();

    private Node root;
    private int numberOfNodes;
    private String order;
    private double reductionRatio;

    private MultiMap<Integer, Node> map;

    private final Node zeroNode = new Node('0');
    private final Node oneNode = new Node('1');

    public BinaryDecisionDiagram() {
        map = new MultiMap<>();
    }

    public Node createBDD(String boolFunction, String order) {
        boolFunction = rewriteNegations(boolFunction);
        root = new Node(order.charAt(0),boolFunction.split("\\+"));
        this.order = order;

        return this.root;
    }

    private void createBDD(Node node, int varFromOrder, String boolFunction, String order) {
        // If bool function contains only one var,
        // determine the value of the node's children and return


    }

    private String createFunctionFromZeroVar(char var, String boolFunction) {
        // Zero branch rules:
        // a -> 1
        // A -> 0

        StringBuilder newBoolFunction = new StringBuilder();
        String[] function = boolFunction.split("\\+");

        for (int i = 0; i < function.length; i++) {

        }

        return newBoolFunction.toString();
    }

    public boolean contains(String s, char c) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                return true;
            }
        }
        return false;
    }

    public boolean evaluate(String boolFunction) {
        char check = boolFunction.charAt(0);
        char lowerCheck = Character.toLowerCase(check);

        String[] function = boolFunction.split("\\+");
        int result = 0;

        for (String s : function) {
            int midResult = 1;
            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(j) == '1') {
                    return true;
                }
                if (s.charAt(j) == lowerCheck) {
                    midResult = 0;
                    break;
                }
            }
            result += midResult;
        }

        return result > 0;
    }

    public boolean isReadyToEval(String boolFunction) {
        for (int i = 0; i < boolFunction.length(); i++) {
            if (boolFunction.charAt(i) == '1') {
                return true;
            }
        }
        boolean checkFlag = false;
        for (int i = 0; i < upperCaseAlphabet.length(); i++) {
            for (int j = 0; j < boolFunction.length(); j++) {
                if (boolFunction.charAt(j) == upperCaseAlphabet.charAt(i) ||
                boolFunction.charAt(j) == lowerCaseAlphabet.charAt(i)) {
                    checkFlag = true;
                } else if (boolFunction.charAt(j) == '+') {

                } else {
                    checkFlag = false;
                    break;
                }
            }
            if (checkFlag) {
                return true;
            }
        }

        return false;
    }

    public String rewriteNegations(String boolFunction) {
        for (int i = 0; i < upperCaseAlphabet.length(); i++) {
            boolFunction = boolFunction.replaceAll("!" + upperCaseAlphabet.charAt(i), "" + lowerCaseAlphabet.charAt(i));
        }

        return boolFunction;
    }

    // Prints given level of the tree from specified node
    private void printLevel(Node root, int level) {
        if (root == null) {
            return;
        }

        if (level == 1) {
            System.out.println(root);
        } else if (level > 1) {
            printLevel(root.getZeroChild(), level-1);
            printLevel(root.getOneChild(), level-1);
        }
    }

    // Prints given level of the tree from the root
    public void printLevel(int level) {
        printLevel(this.root, level);
    }

    // Prints the tree
    public void print() {
        if (this.root == null) {
            System.out.println("<null>");
            return;
        }
        int treeHeight = this.root.computeHeight();

        for (int i = 1; i < treeHeight+1; i++) {
            System.out.print("Level " + (i-1) + ": ");
            printLevel(root, i);
            System.out.println();
        }
    }


}
