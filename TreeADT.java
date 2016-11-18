/**
 * Created by rachel on 11/17/16.
 */
public class TreeADT {

    public static void main(String[] args)
    {
        TreeADT tree = new TreeADT();
        String pre[] = new String[]{"-", "+", "*","/", "-", "1","2","8","3","6","1.2"};
        //String pre[] = new String[]{"+", "*", "+","1", "2", "3","4.5"};
        int size = pre.length;
        TreeNode root = tree.constructTree(pre, size);
        System.out.println("Inorder traversal of the constructed tree is ");
        tree.printInorder(root);
        float result=tree.caculate(root);
        System.out.println();
        System.out.println(result);

    }

    float caculate(TreeNode node) throws ArithmeticException
    {
        if (node.data.equals("*"))
            return caculate(node.left) * caculate(node.right);

        else if (node.data.equals("/"))
        {
            float quotient=caculate(node.left) / caculate(node.right);
            if(quotient==Float.POSITIVE_INFINITY || quotient==Float.NEGATIVE_INFINITY)
                throw new ArithmeticException("Divisor cannot be 0");
            return quotient;
        }
        else if (node.data.equals("+"))
            return caculate(node.left) + caculate(node.right);
        else if (node.data.equals("-"))
            return caculate(node.left) - caculate(node.right);

        else return Float.parseFloat(node.data);
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

    boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    boolean isInteger(String str) {
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



