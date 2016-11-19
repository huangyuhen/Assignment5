package VSAInterpreter;

import java.util.*;

public class Assignment5 {
	// stored all value
	final static HashMap<String, Value> map = new HashMap<>();

	public static void main(String[] args) {
		String command1 = "e = 2 ;";
		String command2 = "a = 3.1;";
		String command3 = "b =4;";
		String command4 = "c= a ;";
//		String command5 = "e = d;";
		// String command6 = "loop x {";
		// String command7 = "}";

		// String lineNum Type
		// Initialize, waiting Huang to passing the whole arraylist
		ArrayList<ProgramLineObject> programLineObjects = new ArrayList<>();
		programLineObjects.add(new ProgramLineObject(ProgramLineType.STATEMENT, 1, command1));
		programLineObjects.add(new ProgramLineObject(ProgramLineType.STATEMENT, 2, command2));
		programLineObjects.add(new ProgramLineObject(ProgramLineType.STATEMENT, 3, command3));
		programLineObjects.add(new ProgramLineObject(ProgramLineType.STATEMENT, 4, command4));
//		programLineObjects.add(new ProgramLineObject(ProgramLineType.STATEMENT, 5, command5));
//		programLineObjects.add(new ProgramLineObject(ProgramLineType.LOOPSTART, 6, command6));
//		programLineObjects.add(new ProgramLineObject(ProgramLineType.LOOPEND, 7, command7));

		// scanProgramLine
		List<ProgramLineObject> executionLineObjects = scanProgramLineToExecutionLine(programLineObjects);

		// all the execution lines are in the executionLineObjects
		// execute the lines one by one
		while (!executionLineObjects.isEmpty()) {
			// get the head of the line to execute
			ProgramLineObject executingLine = executionLineObjects.get(0);
			switch (executingLine.type) {
			case STATEMENT:
				executeStatementLine(executingLine);
			case LOOPSTART:
				executeLoopLine(executingLine);
			case LOOPEND:
			default:
			}
			// after this line got executed, remove the head of the line
			executionLineObjects.remove(0);
		}

		 for (String s : map.keySet()) {
			 System.out.print(s);
			 System.out.print("=");
			 map.get(s).getValue();
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

		// System.out.print(lineObject.contents + " ");
		// System.out.print(lineObject.lineNumber + " ");
		// System.out.println(lineObject.type);
		//
		String trimString = lineObject.contents.replace(" ", "");
		// System.out.println("After token:  " + trimString);

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
				else {// a=ab
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

	private static void executeStatementLine(ProgramLineObject executingLine) {
		String commandLine = executingLine.contents;
		String trimString = commandLine.replace(" ", "");
		String[] tokens = trimString.split("[=;]");
		// leftValue = rightValue;
		String leftValue = tokens[0];
		String rightValue = tokens[1];

		// a = b;
		if (!isIntFloat(rightValue)) {
			// (b not declared)
			if (!map.containsKey(rightValue)) {
				System.out.print(ErrorType.UNDECLARED_ERROR);
				System.out.println(" line:" + executingLine.lineNumber);
				System.exit(0);
			}
			// b declared
			else {
				// a not declared before
				if (!map.containsKey(leftValue))
					map.put(leftValue, map.get(rightValue));
				// a declared before
				else
					map.get(leftValue).setValue(rightValue);
			}
		}
		// a = 3;
		else {
			// a not declared before
			if (!map.containsKey(leftValue))
				map.put(leftValue, new Value(rightValue));
			// a declared before
			else
				map.get(leftValue).setValue(rightValue);
		}
	}

	private static void executeLoopLine(ProgramLineObject executingLine) {

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
	PROGRAM, END, LEFTCOMMENT, RIGHTCOMMENT, LEFTBRAC, RIGHTBRAC, LOOP,
}

enum ErrorType {
	SEMICOLON, PROGRAM, END, LEFTCOMMENT, RIGHTCOMMENT, LEFTBRAC, RIGHTBRAC, LOOP, EXTRASTATEMENT, STATEMENT_EQUALSIGN_ERROR, STATEMENT_INVALID_ASSIGNMENT_ERROR, UNDECLARED_ERROR
}
