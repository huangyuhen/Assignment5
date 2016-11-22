/*****************************************************************************************************
* File: ConvertString.java
* Course: Data Structures and Algorithms for Engineers
* Project: A5
* Author: Evil Genius
* Versions:
*	1.0 November 2016
*
* Description: This program converts a String to a String Array, and handles errors which are identified from the given String. 
*
* Parameters: String input
*
* Internal Functions: String[] StringToArray(String input);
                      String[] revertToArray(String input);
                      boolean ifCorrectOrder(String []array);
                      boolean ifCorrectOperatorsAndNumbers(String []array);
                      boolean ifOperational(String []array);
                      boolean isFloat(String str);
                      boolean isInteger(String str).
*****************************************************************************************************/


import java.util.Iterator;
import java.util.*;

public class ConvertString{

    /*********************************************************************
     * Function Name:    ConvertString
     * Description:      This function checks if the converted array is valid.
     *                   If valid, the string array is returned to the caller,
     *                   otherwise, null is returned.
     * Input Parameters: String input
     * Output:           A string array
     ***********************************************************************/

    public static String[] StringToArray(String input)
    {
        //if the input string is empty  or null, null is returned to the caller.
        if(input==null || input.isEmpty())
        {
            return null;
        }
        //As long as the passed string is not null or empty, we convert it to a string array.
        String []convertedArray=revertToArray(input);
        //If the converted string array is null, that is, there is no notation of comma within the array, the array is invalid,
        //thus we return null to the caller.
        if(convertedArray==null)
        {
            return null;
        }
        //If there are some other operator types and number types other than +,-,*,/,Integer,Float exist in the array, the array is invalid,
        //thus we return null to the caller.
        else if(!ifCorrectOperatorsAndNumbers(convertedArray))
        {
            return null;
        }
        //If the number of operators is not equal to the number of numbers minus 1, we consider that this is not operational,
        //thus we return null to the caller.
        else if(!ifOperational(convertedArray))
        {
            return null;
        }
        //If the operators are not all come before numbers in the array, for example,{"+","5","-","6","8"}, the array is invalid,
        //thus we return null to the caller.
        else if(!ifCorrectOrder(convertedArray))
        {
            System.out.println("Error: To use prefix notation, operators must come before numbers.");
            return null;
        }
        //If none of above exceptions are found, we return the string array to the caller.
        else
            return convertedArray;
    }
    /*********************************************************************
     * Function Name:    revertToArray
     * Description:      This function converts a string to a string array.
     * Input Parameters: String input
     * Output:           A string array
     ***********************************************************************/

    private static String[] revertToArray(String input)
    {
            //As the first step, the input string is split by comma, then we get a string array.
            if(!input.contains(","))
                return null;
            String[] array = input.split(",");
            //Trim each string in the string array, thus space within each string is removed.
            for (int i = 0; i < array.length; i++) {
                array[i] = array[i].trim();
            }
            //Remove any space in the string array.
            List<String> list = new ArrayList<String>(Arrays.asList(array));

            list.removeAll(Collections.singleton(""));
            array = list.toArray(new String[list.size()]);
            return array;
    }

    /*********************************************************************
     * Function Name:    ifCorrectOrder
     * Description:      This function determines if the string array is in
     *                   correct order. That is, operators come first, then
     *                   numbers.
     * Input Parameters: String []array
     * Output:           boolean
     ***********************************************************************/

    private static boolean ifCorrectOrder(String []array)
    {
        List<String> newList=new ArrayList<String>();
        //We create a list to add all operators pull from the string array, then add all numbers pull from the string array to the list.
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
        //We convert the list to a string array to compare this new array with the passed one.
        String []newArray=newList.toArray(new String[newList.size()]);
        //If the two arrays are exactly the same, we assume the order is correct for the passed array, then return true.
        //Otherwise, we return false.
        if(Arrays.equals(array,newArray))
            return true;
        else
        return false;
    }

    /*********************************************************************
     * Function Name:    ifCorrectOperatorsAndNumbers
     * Description:      This function determines if the string array contains
     *                   expected operator types and number types.
     * Input Parameters: String []array
     * Output:           boolean
     ***********************************************************************/

    private static boolean ifCorrectOperatorsAndNumbers(String []array)
    {
        //We iterate the string array to see if there are only four types of operators and two types of numbers.
        //If yes, we return true, otherwise, we return false.
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
