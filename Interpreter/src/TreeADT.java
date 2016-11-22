/*****************************************************************************************************
 * File: TreeADT.java
 * Course: Data Structures and Algorithms for Engineers
 * Project: A5
 * Author: Evil Genius
 * Versions:
 *	1.0 November 2016
 *
 * Description: This program builds a tree using preorder traversal and execute the calculation by
 *              traversing the tree using an inorder traversal.
 *
 * Parameters: String[] array
 *
 * Internal Functions: float caculateTreeUtil(TreeNode node);
                       Object caculateTree(TreeNode node);
                       TreeNode constructTreeUtil(String pre[], Index preIndex, int low, int high, int size);
                       TreeNode constructTree(String pre[], int size);
                       boolean isFloat(String str);
                       boolean isInteger(String str).
 *****************************************************************************************************/

import java.text.*;

public class TreeADT {

    //Initiate class Index to and use it as a global variable.
    private Index index = new Index();

    /*********************************************************************
     * Function Name:    caculateTreeUtil
     * Description:      This function execute the calculation by traversing
     *                   the tree using an inorder traversal.
     * Input Parameters: TreeNode node
     * Output:           Float number
     ***********************************************************************/

    public float caculateTreeUtil(TreeNode node)
    {
        //Initiate a float to be used
        float quotient=0;
        if (node.data.equals("*"))
            return caculateTreeUtil(node.left) * caculateTreeUtil(node.right);

        else if (node.data.equals("/"))
        {
            if (node.right.data.equals("0")) {
                System.out.println("Divisor cannot be zero.");
                System.exit(0);
            }
            quotient = caculateTreeUtil(node.left) / caculateTreeUtil(node.right);
            return quotient;
        }
        else if (node.data.equals("+"))
            return caculateTreeUtil(node.left) + caculateTreeUtil(node.right);
        else if (node.data.equals("-"))
            return caculateTreeUtil(node.left) - caculateTreeUtil(node.right);
        else return Float.parseFloat(node.data);
    }

    public Object caculateTree(TreeNode node)
    {
        float caculationResult=caculateTreeUtil(node);
        int toInt=0;
        if((int)caculationResult==caculationResult)
        {
            Float newCaculation=new Float(caculationResult);
            toInt=newCaculation.intValue();
            //System.out.println(toInt);
            return toInt;
        }
        else
        {
            DecimalFormat formatedFloat = new DecimalFormat("#.##");
            //System.out.println(formatedFloat.format(caculationResult));
            return Float.parseFloat(formatedFloat.format(caculationResult));
        }
    }

    private TreeNode constructTreeUtil(String pre[], Index preIndex, int low, int high, int size) {

        if (preIndex.index >= size || low > high) {
            return null;
        }

        TreeNode root = new TreeNode(pre[preIndex.index]);
        preIndex.index = preIndex.index + 1;

        if (low == high) {
            return root;
        }

        // Search for the first element which is NON-operator
        int i;
        for(i=low;i<=high;++i)
        {
            if(isInteger(pre[i]) || isFloat(pre[i]))
                break;
        }

        root.left = constructTreeUtil(pre, preIndex, preIndex.index, i, size);
        root.right = constructTreeUtil(pre, preIndex, i+1, high+1, size);

        return root;
    }

    private boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public TreeNode constructTree(String pre[], int size) {
        return constructTreeUtil(pre, index , 0, size - 1, size);
    }

    void printInorder(TreeNode node) {
        if (node == null) {
            return;
        }
        printInorder(node.left);
        System.out.print(node.data + " ");
        printInorder(node.right);
    }

}

class Index
{
    int index = 0;
}

class TreeNode
{
    String data;
    TreeNode left;
    TreeNode right;

    public TreeNode(String str) {
        this.data = str;
        this.left = null;
        this.right = null;
    }
}



