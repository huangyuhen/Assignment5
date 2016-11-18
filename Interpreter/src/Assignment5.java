package VSAInterpreter;

import java.util.*;

public class Assignment5 {
	public static void main(String[] args) {
		HashMap<Character, Value> map = new HashMap<>();
//		//
//		Value a = new Value("1");
//		Value b = new Value("2.0");
//		Value c = new Value("-03.4");
//		Value d = new Value("499");
//		Value e = new Value("0");
//
//		Value f = c;
//		Value g = new Value("6");
//		g.setValue("0.3");

		// a = "5"
		// key small case Charcter

		// a = 3
		// b = a

		map.put('a', new Value("1"));
		map.put('b', new Value("2.0"));
		map.put('c', new Value("-03.4"));
		map.put('d', new Value("499"));
		map.put('e', new Value("0"));
		map.put('f', map.get('c'));
		map.put('g', new Value("6"));
		map.get('g').setValue("0.3");

//		List<Value> list = new ArrayList<Value>();
//		list.add(a);
//		list.add(b);
//		list.add(c);
//		list.add(d);
//		list.add(e);
//		list.add(f);
//		list.add(g);

		for (char ch : map.keySet()) {
			map.get(ch).getValue();
		}
	}
}

class Value {
	int iValue;
	float fValue;
	ValueType type;

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

	public void getValue() {
		if (this.type == ValueType.IntegerType)
			System.out.println(this.iValue);
		else
			System.out.println(this.fValue);
	}
}

enum ValueType {
	IntegerType, FloatType
}