import java.util.zip.ZipError;

public class BinaryDecisionDiagram {
    private Node root;
    private int numberOfNodes;
    private String order;
    private double reductionRatio;
    private final Node zeroNode = new Node('0');
    private final Node oneNode = new Node('1');

    public BinaryDecisionDiagram() {}

    public Node createBDD(String boolFunction, String order) {
        root = new Node(order.charAt(0),boolFunction.split("\\+"));
        this.order = order;

        return this.root;
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
