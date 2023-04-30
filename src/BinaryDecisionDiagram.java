import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
        numberOfNodes = 0;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public double getReductionRatio() {
        return reductionRatio;
    }

    public char useBDD(String inputs) throws BDDNotInitializedException {
        if (root == null) {
            throw new BDDNotInitializedException("BDD was not Initialized");
        }
        HashMap<Character, Integer> booleanMap = new HashMap<>();
        for (int i = 0; i < inputs.length(); i++) {
            booleanMap.put(upperCaseAlphabet.charAt(i), Integer.parseInt(Character.toString(inputs.charAt(i))));
        }

        Node root = this.root;
        while (root != oneNode && root != zeroNode) {
            if (booleanMap.get(root.getValue()) == 0) {
                root = root.getZeroChild();
            } else if (booleanMap.get(root.getValue()) == 1) {
                root = root.getOneChild();
            }
        }

        return root.getValue();
    }

    public static BinaryDecisionDiagram createBDDWithBestOrder(String boolFunction) {
        String order = Utility.extractUniqueLetters(boolFunction);

        BinaryDecisionDiagram bestOrderBDD;
        BinaryDecisionDiagram currentBDD = new BinaryDecisionDiagram();

        currentBDD.createBDD(boolFunction, order);
        bestOrderBDD = currentBDD;

        System.out.println("Number of nodes: " + currentBDD.countNodes());
        for (int i = 0; i < order.length()-1; i++) {
            order = Utility.shuffle(order);
            currentBDD = new BinaryDecisionDiagram();
            currentBDD.createBDD(boolFunction, order);
            System.out.println("Number of nodes: " + currentBDD.countNodes());

            if (bestOrderBDD.countNodes() > currentBDD.countNodes()) {
                bestOrderBDD = currentBDD;
            }
        }

        return bestOrderBDD;
    }

    public Node createBDD(String boolFunction, String order) {
        boolFunction = rewriteNegations(boolFunction);
        root = new Node(order.charAt(0),boolFunction.split("\\+"));
        this.order = order;

        createBDD(null, root, 0,boolFunction);

        if (root == oneNode || root == zeroNode) {
            numberOfNodes = 1;
        } else {
            countNodes();
        }
        calculateReductionRatio();
        return root;
    }

    private void createBDD(Node parent, Node node, int varFromOrder, String boolFunction) {
        if (Utility.contains(boolFunction, '1')) {
            if (parent == null) {
                this.root = oneNode;
                return;
            }
            if (parent.getZeroChild() == node) {
                parent.setZeroChild(oneNode);
            } else if (parent.getOneChild() == node) {
                parent.setOneChild(oneNode);
            }
            return;
        } else if (Utility.contains(boolFunction,'0')) {
            if (parent == null) {
                this.root = zeroNode;
                return;
            }
            if (parent.getZeroChild() == node) {
                parent.setZeroChild(zeroNode);
            } else if (parent.getOneChild() == node) {
                parent.setOneChild(zeroNode);
            }
            return;
        }

        // ============== S REDUCTION ==============
        Node exisitingNode = map.find(node.hash(), node);
        if ((node.equals(exisitingNode))) {
            if (parent.getZeroChild() == node) {
                parent.setZeroChild(exisitingNode);
            } else if (parent.getOneChild() == node) {
                parent.setOneChild(exisitingNode);
            }
            return;
        }

        // Check if boolean function is ready to be evaluated
        if (isReadyToEval(boolFunction)) {
            char c = Character.toUpperCase(boolFunction.charAt(0));
            if (Utility.contains(upperCaseAlphabet, c) && node.getValue() != c) {
                node.setValue(c);
            }

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

            map.put(node.hash(), node);
            return;
        }

        String falseFunction = createFalseBranchFunction(order.charAt(varFromOrder), boolFunction);
        String trueFunction = createTrueBranchFunction(order.charAt(varFromOrder), boolFunction);

        // ============== I REDUCTION ==============
        if (new StringWrapper(falseFunction).equals(new StringWrapper(trueFunction))) {
            node.setValue(order.charAt(varFromOrder+1));
            node.setBoolFunction(falseFunction.split("\\+"));
            createBDD(parent, node, varFromOrder+1, falseFunction);
            return;
        }

        Node zeroBranch = new Node(order.charAt(varFromOrder+1),falseFunction.split("\\+"));
        Node oneBranch = new Node(order.charAt(varFromOrder+1),trueFunction.split("\\+"));

        map.put(node.hash(), node);

        node.setZeroChild(zeroBranch);
        node.setOneChild(oneBranch);

        createBDD(node, zeroBranch, varFromOrder+1, falseFunction);
        createBDD(node, oneBranch, varFromOrder+1, trueFunction);
    }

    private String createTrueBranchFunction(char var, String boolFunction) {
        // True branch rules:
        // a -> 0
        // A -> 1

        ArrayList<String> newBFunction = new ArrayList<>();
        String[] function = boolFunction.split("\\+");

        for (String s : function) {
            s = Utility.removeDuplicates(s);
            if (!Utility.contains(s, var) && !Utility.contains(s, Character.toLowerCase(var))) {
                newBFunction.add(s);
            } else if (!Utility.contains(s, Character.toLowerCase(var)) && Utility.contains(s, var)) {
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

        newBFunction = Utility.removeDuplicates(newBFunction);

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

    private String createFalseBranchFunction(char var, String boolFunction) {
        // False branch rules:
        // a -> 1
        // A -> 0

        ArrayList<String> newBFunction = new ArrayList<>();
        String[] function = Utility.removeDuplicates(boolFunction.split("\\+"));

        for (String s : function) {
            s = Utility.removeDuplicates(s);
            if (!Utility.contains(s, var) && !Utility.contains(s, Character.toLowerCase(var))) {
                newBFunction.add(s);
            } else if (Utility.contains(s, Character.toLowerCase(var)) && !Utility.contains(s, var)) {
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

        newBFunction = Utility.removeDuplicates(newBFunction);

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

    private boolean evaluate(String boolFunction, boolean substituteForVar) {
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

    private boolean isReadyToEval(String boolFunction) {
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

    private String rewriteNegations(String boolFunction) {
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
    private void printLevel(int level) {
        printLevel(this.root, level);
    }

    public double getAllNodes() {
        return Math.pow(2, order.length()+1)-1;
    }

    private double calculateReductionRatio() {
        return this.reductionRatio = 100*(1-numberOfNodes / getAllNodes());
    }

    private int countNodes() {
        return (numberOfNodes = map.getSize() + 2);
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
            printLevel(i);
            System.out.println();
        }
    }

    public void printNicely(boolean memPrint) {
        printNicely(root, memPrint);
    }

    private void printNicely(Node root, boolean memPrint) {
        List<List<String>> lines = new ArrayList<>();
        List<Node> level = new ArrayList<>();
        List<Node> next = new ArrayList<>();

        level.add(root);
        int nn = 1;
        int widest = 0;

        while (nn != 0) {
            List<String> line = new ArrayList<>();
            nn = 0;

            for (Node n : level) {
                if (n == null) {
                    line.add(null);
                    next.add(null);
                    next.add(null);
                } else {
                    String aa;
                    if (memPrint) {
                        aa = "<" + n.getValue() + "> " + n.hashCode();
                    } else {
                        aa = "<" + n.getValue() + "> " + Arrays.toString(n.getBoolFunction()) + " (" + n.hashCode() + ")";
                    }
                    line.add(aa);
                    if (aa.length() > widest) widest = aa.length();

                    next.add(n.getZeroChild());
                    next.add(n.getOneChild());

                    if (n.getZeroChild() != null) nn++;
                    if (n.getOneChild() != null) nn++;
                }
            }

            if (widest % 2 == 1) widest++;

            lines.add(line);

            List<Node> tmp = level;
            level = next;
            next = tmp;
            next.clear();
        }

        int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
        for (int i = 0; i < lines.size(); i++) {
            List<String> line = lines.get(i);
            int hpw = (int) Math.floor(perpiece / 2f) - 1;

            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {
                    // split node
                    char c = ' ';
                    if (j % 2 == 1) {
                        if (line.get(j - 1) != null) {
                            c = (line.get(j) != null) ? '┴' : '┘';
                        } else {
                            if (line.get(j) != null) c = '└';
                        }
                    }
                    System.out.print(c);

                    // lines and spaces
                    if (line.get(j) == null) {
                        for (int k = 0; k < perpiece - 1; k++) {
                            System.out.print(" ");
                        }
                    } else {

                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? " " : "─");
                        }
                        System.out.print(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? "─" : " ");
                        }
                    }
                }
                System.out.println();
            }

            // print line of nodes
            for (int j = 0; j < line.size(); j++) {
                String f = line.get(j);
                if (f == null) f = "";
                int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
                int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

                for (int k = 0; k < gap1; k++) {
                    System.out.print(" ");
                }
                System.out.print(f);
                for (int k = 0; k < gap2; k++) {
                    System.out.print(" ");
                }
            }
            System.out.println();

            perpiece /= 2;
        }
    }

}
