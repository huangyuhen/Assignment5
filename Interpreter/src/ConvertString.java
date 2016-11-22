import java.util.Iterator;
import java.util.*;
/**
 * Created by rachel on 11/18/16.
 */
public class ConvertString{


    //Main function called by caller to convert a String to a String Array.
    public static String[] StringToArray(String input)
    {
        if(input==null || input.isEmpty())
        {
            System.out.println("Error: The String is NULL.");
            return null;
        }
        String []convertedArray=revertToArray(input);
        if(convertedArray==null) {
            System.out.println("Error: The String must be split by ','.");
            return null;
        }
        else if(!ifCorrectOperatorsAndNumbers(convertedArray)) {
            System.out.println("Error: Only +, -, *, /, Integer and Float are available.");
            return null;
        }
        else if(!ifOperational(convertedArray)) {
            System.out.println("Error: To operate calculations, the number of operators must be equal to the number of numbers minus 1.");
            return null;
        }
        else if(!ifCorrectOrder(convertedArray)) {
            System.out.println("Error: To use prefix notation, operators must come before numbers.");
            return null;
        }
        else
            return convertedArray;
    }

    private static String[] revertToArray(String input)
    {
            if(!input.contains(","))
                return null;
            String[] array = input.split(",");
            for (int i = 0; i < array.length; i++) {
                array[i] = array[i].trim();
            }
            List<String> list = new ArrayList<String>(Arrays.asList(array));

            list.removeAll(Collections.singleton(""));
            array = list.toArray(new String[list.size()]);
            return array;
    }
    private static boolean ifCorrectOrder(String []array)
    {
        List<String> newList=new ArrayList<String>();
        for(String s:array)
        {
            if(!isFloat(s)&& !isInteger(s))
                newList.add(s);
        }
        for(String st:array)
        {
            if(isFloat(st) || isInteger(st))
                newList.add(st);
        }
        String []newArray=newList.toArray(new String[newList.size()]);
        if(Arrays.equals(array,newArray))
            return true;
        else
        return false;
    }

    private static boolean ifCorrectOperatorsAndNumbers(String []array)
    {
        for(String s:array)
        {
            if (!s.equals("+") && !s.equals("-") && !s.equals("*") && !s.equals("/") && !isFloat(s) && !isInteger(s))
            {
                return false;
            }
        }
        return true;
    }

    private static boolean ifOperational(String []array)
    {
        int count=0;
        int length=array.length;
        if(ifCorrectOperatorsAndNumbers(array))
        {
            for (int i = 0; i < length; i++) {
                if (!isFloat(array[i]) && !isInteger(array[i]))
                    count++;
            }
            if (count == length - count - 1)
                return true;
            return false;
        }
        return false;
    }

    private static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

   /* public static void main(String[] args)
    {
        ConvertString check=new ConvertString();
        String input= "*, 3, -";
        String []array=check.revertToArray(input);
        for(String s:array)
        System.out.print(s);
        boolean order=check.ifCorrectOrder(array);
        System.out.println("If correct order:"+order);
        boolean re=check.ifCorrectOperatorsAndNumbers(array);
        System.out.println("If correct operator and number:"+re);
        boolean operational=check.ifOperational(array);
        System.out.println("If operational:"+operational);

    }*/
}
