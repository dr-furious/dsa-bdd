package com.project.bdd;

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

    public long hashSpecial() {
        return Arrays.toString(this.boolFunction).hashCode();
    }

    @Override
    public String toString() {
        if (this.zeroChild != null && (this.zeroChild.getValue() == '0' || this.zeroChild.getValue() == '1') &&
                this.oneChild != null && (this.oneChild.getValue() == '1' || this.oneChild.getValue() == '0')) {
            return "{" +
                    "V:" + this.value + "|" +
                    "Ref:'" + this.hashCode() + "'|" +
                    "0:" + this.zeroChild.getValue() + "|" +
                    "1:" + this.oneChild.getValue() +
                    "}"
            ;
        }
        if (this.zeroChild != null && (this.zeroChild.getValue() == '0' || this.zeroChild.getValue() == '1')) {
            return "{" +
                    "V:" + this.value + "|" +
                    "Ref:'" + this.hashCode() + "'|" +
                    "0:" + this.zeroChild.getValue() + "|" +
                    "1:" + ((this.oneChild == null) ? "null" : Arrays.toString(this.oneChild.getBoolFunction())) +
                    "}"
            ;
        }
        if (this.oneChild != null && (this.oneChild.getValue() == '1' || this.oneChild.getValue() == '0')) {
            return "{" +
                    "V:" + this.value + "|" +
                    "Ref:'" + this.hashCode() + "'|" +
                    "0:" + ((this.zeroChild == null) ? "null" : Arrays.toString(this.zeroChild.getBoolFunction())) + "|" +
                    "1:" + this.oneChild.getValue() +
                    "}"
            ;
        }
        return "{" +
                "V:" + this.value + "|" +
                "Ref:'" + this.hashCode() + "'|" +
                "0:" + ((this.zeroChild == null) ? "null" : Arrays.toString(this.zeroChild.getBoolFunction())) + "|" +
                "1:" + ((this.oneChild == null) ? "null" : Arrays.toString(this.oneChild.getBoolFunction())) +
                "}"
        ;
    }

    public String toStringFull() {
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != Node.class) {
            return false;
        }
        Node node = (Node) obj;
        return node.hashSpecial() == this.hashSpecial() && isEqual(node);
     }

     public boolean isEqual(Node node) {
        if (node == null) {
            return false;
        }
        return Arrays.toString(node.getBoolFunction()).equals(Arrays.toString(this.getBoolFunction()));
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

}
