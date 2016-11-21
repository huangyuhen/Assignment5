package ObjectTypes;

import EnumerationTypes.ValueType;

public class Value {
    public int iValue;
    public float fValue;
    public ValueType type;

    public Value(String input) {
        if (input.contains(".")) {
            this.fValue = Float.parseFloat(input);
            this.type = ValueType.FloatType;
        } else {
            this.iValue = Integer.parseInt(input);
            this.type = ValueType.IntegerType;
        }
    }

    // override the whole Value
    public void setValue(String input) {
        if (input.contains(".")) {
            this.fValue = Float.parseFloat(input);
            this.type = ValueType.FloatType;
        } else {
            this.iValue = Integer.parseInt(input);
            this.type = ValueType.IntegerType;
        }
    }

    public String getValue() {
        String valueString ="";
        if (this.type == ValueType.IntegerType)
            valueString=Integer.toString(this.iValue);
        else
            valueString=Float.toString(this.fValue);
        return valueString;
    }

    public void printValue() {
        if (this.type == ValueType.IntegerType)
            System.out.print(this.iValue);
        else
            System.out.print(this.fValue);
    }

}
