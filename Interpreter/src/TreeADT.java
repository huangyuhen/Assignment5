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
     *                   the tree using an inorder traversal recursively.
     * Input Parameters: TreeNode node
     * Output:           Float
    ***********************************************************************/

    public float caculateTreeUtil(TreeNode node) throws ArithmeticException
    {
        //If the operator is *, then we executing * calculation.
        if (node.data.equals("*"))
            return caculateTreeUtil(node.left) * caculateTreeUtil(node.right);
        //If the operator is /, then we executing / calculation.
        else if (node.data.equals("/"))
        {
            //If the divisor is 0, the program throw ArithmeticException.
            if (node.right.data.equals("0"))
                throw new ArithmeticException();

            //Otherwise, we executing the / calculation.
            float quotient = caculateTreeUtil(node.left) / caculateTreeUtil(node.right);
            return quotient;
        }
        //If the operator is +, then we executing + calculation.
        else if (node.data.equals("+"))
            return caculateTreeUtil(node.left) + caculateTreeUtil(node.right);
        //If the operator is -, then we executing - calculation.
        else if (node.data.equals("-"))
            return caculateTreeUtil(node.left) - caculateTreeUtil(node.right);
        //Finally, we convert the calculation to float.
        else return Float.parseFloat(node.data);
    }

    /*********************************************************************
     * Function Name:    caculateTree
     * Description:      This function calls caculateTreeUtil function to get
     *                   the float type of result, then convert it to Object type.
     * Input Parameters: TreeNode node
     * Output:           Object
     ***********************************************************************/

    public Object caculateTree(TreeNode node)
    {
        float caculationResult;
        //Call caculateTreeUtil function to get the float result.
        try
        {
            caculationResult=caculateTreeUtil(node);
        }
        //return null if an ArithmeticException is caught
        catch(ArithmeticException ex)
        {
            return null;
        }

        int toInt=0;
        //To check if the result can be an integer, if yes, then return int type.
        if((int)caculationResult==caculationResult)
        {
            Float newCaculation=new Float(caculationResult);
            toInt=newCaculation.intValue();
            return toInt;
        }
        //Otherwise, return the result in float type with format "#.##"
        else
        {
            DecimalFormat formatedFloat = new DecimalFormat("#.##");
            return Float.parseFloat(formatedFloat.format(caculationResult));
        }
    }

    /*********************************************************************
     * Function Name:    constructTreeUtil
     * Description:      This function builds a tree using preorder traversal
     *                   recursively.
     * Input Parameters: String pre[], Index preIndex, int low, int high, int size
     * Output:           TreeNode
     ***********************************************************************/

    private TreeNode constructTreeUtil(String pre[], Index preIndex, int low, int high, int size)
    {
        if (preIndex.index >= size || low > high) {
            return null;
        }
        //Create a tree node whose data is the string from the string array.
        TreeNode root = new TreeNode(pre[preIndex.index]);
        preIndex.index = preIndex.index + 1;
        //When low==high, it means no child for the current node, thus we simply return the node.
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
        //Call constructTreeUtil recursively to fill nodes with strings in the string array.
        root.left = constructTreeUtil(pre, preIndex, preIndex.index, i, size);
        root.right = constructTreeUtil(pre, preIndex, i+1, high+1, size);

        return root;
    }

    /*********************************************************************
     * Function Name:    isFloat
     * Description:      This function determines if the string can be converted
     *                   to a float number.
     * Input Parameters: String str
     * Output:           boolean
     ***********************************************************************/

    private boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*********************************************************************
     * Function Name:    isInteger
     * Description:      This function determines if the string can be converted
     *                   to an integer.
     * Input Parameters: String str
     * Output:           boolean
     ***********************************************************************/

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*********************************************************************
     * Function Name:    constructTree
     * Description:      This function calls constructTreeUtil to build up
     *                   the tree.
     * Input Parameters: String pre[], int size
     * Output:           TreeNode
     ***********************************************************************/

    public TreeNode constructTree(String pre[], int size)
    {
        return constructTreeUtil(pre, index , 0, size - 1, size);
    }
}

/*********************************************************************
 * Class Name:       Index
 * Description:      This class stores the value of index will be used
 *                   as a global variable in TreeADT class.
 ***********************************************************************/

class Index
{
    int index = 0;
}

/*********************************************************************
 * Class Name:       TreeNode
 * Description:      This class defines the elements in TreeNode class,
 *                   those elements are left node, right node, and data
 *                   of string.
 ***********************************************************************/

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



