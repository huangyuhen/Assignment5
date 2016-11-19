package VSAInterpreter;

import java.util.*;

public class Assignment5 {
	public static void main(String[] args) {
		String command1 = "a = 3 ;";
		String command2 = "a=b;";
		String command3 = "loop x {";
		String command4 = "}";

		// String lineNum Type
		// Initialize, waiting Huang to passing the whole arraylist
		ArrayList<ProgramLineObject> programLineObjects = new ArrayList<>();
		programLineObjects.add(new ProgramLineObject(ProgramLineType.STATEMENT, 1, command1));
		programLineObjects.add(new ProgramLineObject(ProgramLineType.STATEMENT, 2, command2));
		programLineObjects.add(new ProgramLineObject(ProgramLineType.LOOPSTART, 3, command3));
		programLineObjects.add(new ProgramLineObject(ProgramLineType.LOOPEND, 4, command4));

		// scanProgramLine
		List<ProgramLineObject> executionLineObjects = scanProgramLineToExecutionLine(programLineObjects);
		

		for (ProgramLineObject plo : executionLineObjects) {
			System.out.print(plo.contents);
			System.out.print(plo.lineNumber);
			System.out.println(plo.type);
		}

		HashMap<Character, Value> map = new HashMap<>();

		map.put('a', new Value(".1"));
		// map.put('b', new Value("2.0"));
		// map.put('c', new Value("-03.4"));
		// map.put('d', new Value("499"));
		// map.put('e', new Value("0"));
		// map.put('f', map.get('c'));
		// map.put('g', new Value("6"));
		// map.get('g').setValue("0.3");

		// List<Value> list = new ArrayList<Value>();
		// list.add(a);
		// list.add(b);
		// list.add(c);
		// list.add(d);
		// list.add(e);
		// list.add(f);
		// list.add(g);

		for (char ch : map.keySet()) {
			map.get(ch).getValue();
		}
	}

	private static List<ProgramLineObject> scanProgramLineToExecutionLine(ArrayList<ProgramLineObject> programLineObjects) {

		List<ProgramLineObject> executionLineObjectsList = new ArrayList<>();
		for (ProgramLineObject lineObject : programLineObjects) {
			checkLineTokens(lineObject);
			executionLineObjectsList.add(lineObject);
		}
		return executionLineObjectsList;
	}

	// return VOID if correct
	// only stop and exit when error
	private static void checkLineTokens(ProgramLineObject lineObject) {
		switch (lineObject.type) {
		case STATEMENT:
			checkStatementValid(lineObject);
		case LOOPSTART:
			checkLoopStartValid(lineObject);
		case LOOPEND:
		default:
		}
	}

	// return VOID if correct
	// only stop and exit when error
	private static void checkStatementValid(ProgramLineObject lineObject) {
		boolean validExecutionLine = true;
		ErrorType errorType = null;
		int errorLine = 0;

//		System.out.print(lineObject.contents + " ");
//		System.out.print(lineObject.lineNumber + " ");
//		System.out.println(lineObject.type);
//
		String trimString = lineObject.contents.replace(" ", "");
//		System.out.println("After token:  " + trimString);

		// count number of "="
		int numberOfEqual = 0;
		for (int i = 0; i < trimString.length(); i++) {
			if (trimString.charAt(i) == '=')
				numberOfEqual++;
		}

		if (numberOfEqual != 1) {// a==3
			errorType = ErrorType.STATEMENT_EQUALSIGN_ERROR;
			errorLine = lineObject.lineNumber;
			validExecutionLine = false;
		} else {
			String[] tokens = trimString.split("[=;]");
			if (tokens.length != 2) {// =3 or a= or =
				errorType = ErrorType.STATEMENT_INVALID_ASSIGNMENT_ERROR;
				errorLine = lineObject.lineNumber;
				validExecutionLine = false;
			} else {// check both tokens
				// check first token
				if (tokens[0].length() != 1) {// aa=1
					errorType = ErrorType.STATEMENT_INVALID_ASSIGNMENT_ERROR;
					errorLine = lineObject.lineNumber;
					validExecutionLine = false;
				} else {// 1=1
					char c = tokens[0].charAt(0);
					if (!Character.isLetter(c)) {
						errorType = ErrorType.STATEMENT_INVALID_ASSIGNMENT_ERROR;
						errorLine = lineObject.lineNumber;
						validExecutionLine = false;
					}
				}
				// check second token
				// length is 1
				if (tokens[1].length() == 1) {// a=?
					char c = tokens[1].charAt(0);
					if ((!Character.isDigit(c)) && (!Character.isLetter(c))) {
						errorType = ErrorType.STATEMENT_INVALID_ASSIGNMENT_ERROR;
						errorLine = lineObject.lineNumber;
						validExecutionLine = false;
					}
				}
				// length is >1
				else {//a=ab
					if (!isIntFloat(tokens[1])) {
						errorType = ErrorType.STATEMENT_INVALID_ASSIGNMENT_ERROR;
						errorLine = lineObject.lineNumber;
						validExecutionLine = false;
					}
				}
			}
		}
		if (validExecutionLine == false) {
			System.out.print(errorType);
			System.out.println(" line:" + errorLine);
			System.exit(0);
		}
	}

	private static void checkLoopStartValid(ProgramLineObject lineObject) {

	}

	private static boolean isIntFloat(String str) {
		boolean status = true;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (Character.isDigit(c) || c == '.') {
				// do nothing
			} else {
				status = false;
				break;
			}
		}
		return status;
	}

}

class ProgramLineObject {
	ProgramLineType type;
	int lineNumber;
	String contents;

	public ProgramLineObject(ProgramLineType type, int lineNumber, String contents) {
		this.type = type;
		this.lineNumber = lineNumber;
		this.contents = contents;
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

enum ProgramLineType {
	LOOPSTART, LOOPEND, STATEMENT
}

enum Type {
	PROGRAM, END, LEFTCOMMENT, RIGHTCOMMENT, LEFTBRAC, RIGHTBRAC, LOOP
}

enum ErrorType {
	SEMICOLON, PROGRAM, END, LEFTCOMMENT, RIGHTCOMMENT, LEFTBRAC, RIGHTBRAC, LOOP, EXTRASTATEMENT, STATEMENT_EQUALSIGN_ERROR, STATEMENT_INVALID_ASSIGNMENT_ERROR
}
