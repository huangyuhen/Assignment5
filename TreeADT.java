import java.text.*;
/**
 * Created by rachel on 11/17/16.
 */
public class TreeADT {

    /*public static void main(String[] args)
    {
        TreeADT tree = new TreeADT();
        String pre[] = new String[]{"-", "+", "*", "-", "1","2","8","3","1.0"};
        //String pre[] = new String[]{"+","-","7","8","1.456"};
        //String pre[] = new String[]{"+", "*", "+","1", "2", "3","4.5"};
        int size = pre.length;
        TreeNode root = tree.constructTree(pre, size);
        System.out.println("Inorder traversal of the constructed tree is ");
        tree.printInorder(root);
        tree.caculateTree(root);
    }*/

    float caculateTreeUtil(TreeNode node) throws ArithmeticException
    {
        if (node.data.equals("*"))
            return caculateTreeUtil(node.left) * caculateTreeUtil(node.right);

        else if (node.data.equals("/"))
        {
            float quotient=caculateTreeUtil(node.left) / caculateTreeUtil(node.right);
            if(quotient==Float.POSITIVE_INFINITY || quotient==Float.NEGATIVE_INFINITY)
                throw new ArithmeticException("Divisor cannot be 0");
            return quotient;
        }
        else if (node.data.equals("+"))
            return caculateTreeUtil(node.left) + caculateTreeUtil(node.right);
        else if (node.data.equals("-"))
            return caculateTreeUtil(node.left) - caculateTreeUtil(node.right);
        else return Float.parseFloat(node.data);
    }

    Object caculateTree(TreeNode node)
    {
        float caculationResult=caculateTreeUtil(node);
        int toInt=0;
        if((int)caculationResult==caculationResult)
        {
            Float newCaculation=new Float(caculationResult);
            toInt=newCaculation.intValue();
            System.out.println(toInt);
            return toInt;
        }
        else
        {
            DecimalFormat formatedFloat = new DecimalFormat("#.##");
            System.out.println(formatedFloat.format(caculationResult));
            return Float.parseFloat(formatedFloat.format(caculationResult));
        }
    }

    Index index = new Index();

    TreeNode constructTreeUtil(String pre[], Index preIndex, int low, int high, int size) {

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

    public boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    TreeNode constructTree(String pre[], int size) {
        return constructTreeUtil(pre, index, 0, size - 1, size);
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
        data = str;
        this.left = null;
        this.right = null;
    }
}



