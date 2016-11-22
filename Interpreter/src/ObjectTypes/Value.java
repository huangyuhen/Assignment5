/*****************************************************************************************************
 * File: Value.java
 * Course: Data Structures and Algorithms for Engineers
 * Project: A5
 * Author: Evil Genius
 * Versions:
 *	1.0 November 2016
 *
 * Description: This class Value defines the values in supported Integer and Float type and provides
 * methods for other class to access and modify the object.
 *
 * Parameters: int iValue;
 *             float fValue;
 *             ValueType type;
 *
 * Constructor:
 *                      Value(String input)
 *
 * Internal Functions:
 *                      void setValue(String input)
 *                      String getValue()
 *                      void printValue()
 *
 *****************************************************************************************************/

package ObjectTypes;
import EnumerationTypes.ValueType;

public class Value {
    public int iValue;//store as int type if value is in int type
    public float fValue;//store as float type if value is in float type
    public ValueType type;//store Value type (IntegerType/FloatType)

    /*********************************************************************
     * Constructor:  Value(String input)
     * Description: This constructor will read String as an input to declare.
     * Input Parameters: String input
     * Output: void
     ***********************************************************************/
    public Value(String input) {
        //if the input is a float
        if (input.contains(".")) {
            //convert and define the input String into float type value
            this.fValue = Float.parseFloat(input);
            this.type = ValueType.FloatType;
        }
        //if the input is an integer
        else {
            //convert and define the input String into int type value
            this.iValue = Integer.parseInt(input);
            this.type = ValueType.IntegerType;
        }
    }

    /*********************************************************************
     * Function Name:  setValue(String input)
     * Description: This method will set/overwrite the Value object.
     * Input Parameters: String input
     * Output: void
     ***********************************************************************/
    public void setValue(String input) {
        //if the input is a float
        if (input.contains(".")) {
            //convert and define the input String into float type value
            this.fValue = Float.parseFloat(input);
            this.type = ValueType.FloatType;
        }
        //if the input is an integer
        else {
            //convert and define the input String into int type value
            this.iValue = Integer.parseInt(input);
            this.type = ValueType.IntegerType;
        }
    }

    /*********************************************************************
     * Function Name:  getValue(String input)
     * Description: This method get Value object.
     * Input Parameters: String input
     * Output: String
     ***********************************************************************/
    public String getValue() {
        String valueString ="";//define a empty String type for return
        //if the Value is in type integer
        if (this.type == ValueType.IntegerType)
            valueString=Integer.toString(this.iValue);
        else //if the Value is in type float
            valueString=Float.toString(this.fValue);
        return valueString;
    }

    /*********************************************************************
     * Function Name:  printValue(String input)
     * Description: This method print Value object.
     * Input Parameters: null
     * Output: void
     ***********************************************************************/
    public void printValue() {
        //if the Value is in type integer
        if (this.type == ValueType.IntegerType)
            System.out.print(this.iValue);
        else //if the Value is in type float
            System.out.print(this.fValue);
    }

}
