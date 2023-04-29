import java.util.ArrayList;

public class BinaryDecisionDiagram {
    private static final String upperCaseAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String lowerCaseAlphabet = upperCaseAlphabet.toLowerCase();

    private Node root;
    private int numberOfNodes;
    private String order;
    private double reductionRatio;

    private MultiMap<Integer, Node> map;

    private final Node zeroNode;
    private final Node oneNode;

    public BinaryDecisionDiagram() {
        map = new MultiMap<>();
        zeroNode = new Node('0');
        oneNode = new Node('1');
        numberOfNodes = 3;
    }

    public Node createBDD(String boolFunction, String order) {
        boolFunction = rewriteNegations(boolFunction);
        root = new Node(order.charAt(0),boolFunction.split("\\+"));
        this.order = order;

        createBDD(root, 0,boolFunction);

        return this.root;
    }

    private void createBDD(Node node, int varFromOrder, String boolFunction) {
        // If bool function contains only one var,
        // determine the value of the node's children and return

        if (isReadyToEval(boolFunction)) {
            // False branch
            if (evaluate(boolFunction, false)) {
                // decision = 1
                node.setZeroChild(oneNode);
            } else {
                // decision = 0
                node.setZeroChild(zeroNode);
            }

            // True branch
            if (evaluate(boolFunction, true)) {
                // decision = 1
                node.setOneChild(oneNode);
            } else {
                // decision = 0
                node.setOneChild(zeroNode);
            }

            return;
        }

        String falseFunction = createFalseBranchFunction(order.charAt(varFromOrder), boolFunction);
        String trueFunction = createTrueBranchFunction(order.charAt(varFromOrder), boolFunction);

        Node zeroBranch = new Node(order.charAt(varFromOrder+1),falseFunction.split("\\+"));
        Node oneBranch = new Node(order.charAt(varFromOrder+1),trueFunction.split("\\+"));

        node.setZeroChild(zeroBranch);
        node.setOneChild(oneBranch);

        createBDD(zeroBranch, varFromOrder+1, falseFunction);
        createBDD(oneBranch, varFromOrder+1, trueFunction);
    }

    public String createTrueBranchFunction(char var, String boolFunction) {
        // True branch rules:
        // a -> 0
        // A -> 1

        ArrayList<String> newBFunction = new ArrayList<>();
        String[] function = Utility.removeDuplicates(boolFunction.split("\\+"));

        for (String s : function) {
            s = Utility.removeDuplicates(s);
            if (!contains(s, var) && !contains(s, Character.toLowerCase(var))) {
                newBFunction.add(s);
            } else if (!contains(s, Character.toLowerCase(var)) && contains(s, var)) {
                if (s.length() == 1) {
                    newBFunction.add("1");
                } else {
                    StringBuilder partialFormula = new StringBuilder();
                    for (int i = 0; i < s.length(); i++) {
                        if (s.charAt(i) != var) {
                            partialFormula.append(s.charAt(i));
                        }
                    }
                    newBFunction.add(partialFormula.toString());
                }
            }
        }

        StringBuilder newBoolFunction = new StringBuilder();
        for (int i = 0; i < newBFunction.size(); i++) {
            newBoolFunction.append(newBFunction.get(i));
            if (i != newBFunction.size()-1) {
                newBoolFunction.append("+");
            }
        }

        if (newBoolFunction.toString().isBlank()) {
            return "0";
        }

        return newBoolFunction.toString();
    }

    public String createFalseBranchFunction(char var, String boolFunction) {
        // False branch rules:
        // a -> 1
        // A -> 0

        ArrayList<String> newBFunction = new ArrayList<>();
        String[] function = Utility.removeDuplicates(boolFunction.split("\\+"));

        for (String s : function) {
            s = Utility.removeDuplicates(s);
            if (!contains(s, var) && !contains(s, Character.toLowerCase(var))) {
                newBFunction.add(s);
            } else if (contains(s, Character.toLowerCase(var)) && !contains(s, var)) {
                if (s.length() == 1) {
                    newBFunction.add("1");
                } else {
                    StringBuilder partialFormula = new StringBuilder();
                    for (int i = 0; i < s.length(); i++) {
                        if (s.charAt(i) != Character.toLowerCase(var)) {
                            partialFormula.append(s.charAt(i));
                        }
                    }
                    newBFunction.add(partialFormula.toString());
                }
            }
        }

        StringBuilder newBoolFunction = new StringBuilder();
        for (int i = 0; i < newBFunction.size(); i++) {
            newBoolFunction.append(newBFunction.get(i));
            if (i != newBFunction.size()-1) {
                newBoolFunction.append("+");
            }
        }

        if (newBoolFunction.toString().isBlank()) {
            return "0";
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

    public boolean evaluate(String boolFunction, boolean substituteForVar) {
        char check = boolFunction.charAt(0);
        if (check == '0') {
            return false;
        }
        char lowerCheck = (substituteForVar) ? Character.toLowerCase(check) : Character.toUpperCase(check);

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
        if (boolFunction.charAt(0) == '0') {
            return true;
        }
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
            System.out.print(root);
            System.out.print(" ");
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
            if (i == treeHeight) {
                System.out.print("Level Results: ");
            } else {
                System.out.print("Level " + order.charAt(i-1) + ": ");
            }
            printLevel(root, i);
            System.out.println();
        }
    }

    public double calculateReductionRatio() {
        double allNodes = Math.pow(2, order.length()+1)-1;
        return 100-(this.reductionRatio = numberOfNodes / allNodes);
    }

    public int countNodes() {
        countNodes(root);

        // +2 To count in the 0 and 1 children
        return this.numberOfNodes+2;
    }

    private void countNodes(Node node){
        if (node == zeroNode || node == oneNode) {
            return;
        }

        numberOfNodes++;

        countNodes(node.getZeroChild());
        countNodes(node.getOneChild());
    }
}
