/*****************************************************************************************************
 * File: Execution.java
 * Course: Data Structures and Algorithms for Engineers
 * Project: A5
 * Author: Evil Genius
 * Versions:
 *	1.0 November 2016
 *
 * Description: This program will validate the program line syntax and execute line by line.
 *
 * Parameters:
 * 				HashMap<String, Value> map;
 * 				ArrayList<ProgramLineObject> programLineObjects;
 *
 * Internal Functions:
 * Execution(ArrayList<ProgramLineObject> list)
 * executeLines()
 * scanProgramLineToExecutionLine(ArrayList<ProgramLineObject> programLineObjects)
 *
 *
 *****************************************************************************************************/


import EnumerationTypes.*;
import ObjectTypes.ProgramLineObject;
import ObjectTypes.Value;

import java.util.*;

public class Execution{
	// stored all value
	final static HashMap<String, Value> map = new HashMap<>();
	ArrayList<ProgramLineObject> programLineObjects;

	public Execution(ArrayList<ProgramLineObject> list){
		programLineObjects = list;
	}

	public void executeLines(){
		List<ProgramLineObject> executionLineObjects = scanProgramLineToExecutionLine(programLineObjects);

		int executionIndex = 0;
		// all the execution lines are in the executionLineObjects
		// execute the lines one by one
		while (executionIndex < programLineObjects.size()) {
			// get the head of the line to execute
			ProgramLineObject executingLine = executionLineObjects.get(executionIndex);
			switch (executingLine.type) {
				case STATEMENT:
					executeStatementLine(executingLine);
					executionIndex++;
					break;
				case LOOPSTART:
					executionIndex = executeLoopLine(executingLine);
					break;
				case LOOPEND:
					executionIndex = executeLoopLine(executingLine);
					break;
				default:
			}
			// after this line got executed, remove the head of the line
			//executionLineObjects.remove(0);

		}
	}
	public static List<ProgramLineObject> scanProgramLineToExecutionLine(ArrayList<ProgramLineObject> programLineObjects) {

		List<ProgramLineObject> executionLineObjectsList = new ArrayList<>();
		for (ProgramLineObject lineObject : programLineObjects) {
			checkLineTokens(lineObject);
			// checkLoopStartValid(lineObject);
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
			break;
		case LOOPSTART:
			checkLoopStartValid(lineObject);
			break;
		case LOOPEND:
		default:
			break;
		}
	}


	// return VOID if correct
	// only stop and exit when error
	private static void checkStatementValid(ProgramLineObject lineObject) {
		// get first token by white space, for scan PRINT statement
		String[] preTokens = lineObject.contents.split("\\s+");
		String firstToken = preTokens[0].toLowerCase();
		if (firstToken.equals("print") || firstToken.equals("printcr"))
			checkStatementValid_print(lineObject);
		else
			checkStatementValid_assign(lineObject);
	}

	private static void checkStatementValid_print(ProgramLineObject lineObject) {
		boolean validExecutionLine = true;
		ErrorType errorType = null;
		int errorLine = 0;
		
		String trimString = lineObject.contents.replace(";", "");
		
		int numberOfQuotes = 0;
		for (int i = 0; i < trimString.length(); i++) {
			if (trimString.charAt(i) == '"')
				numberOfQuotes++;
		}
		
		//print "
		if(numberOfQuotes!=0){
			if(numberOfQuotes==2){
				int firstQuoteIndex = trimString.indexOf("\"");
				int endOfFirstToken = trimString.indexOf(" ");

				//print a"x"
				if(firstQuoteIndex!=(endOfFirstToken+1)){
					errorType = ErrorType.STATEMENT_ERROR;
					errorLine = lineObject.lineNumber;
					validExecutionLine = false;
				}
			}
			//" not =2
			else{
				errorType = ErrorType.STATEMENT_ERROR;
				errorLine = lineObject.lineNumber;
				validExecutionLine = false;
			}
		}
		//print cb
		//printcr c d
		//print 1
		else{
			String[] preTokens = trimString.split("\\s+");
			char c = preTokens[1].charAt(0);

			//printcr c d
			if(preTokens.length!=2){
				errorType = ErrorType.STATEMENT_ERROR;
				errorLine = lineObject.lineNumber;
				validExecutionLine = false;
			}
			//print cb
			else if(preTokens[1].length()!=1){
				errorType = ErrorType.STATEMENT_ERROR;
				errorLine = lineObject.lineNumber;
				validExecutionLine = false;
			}
			//print 1
			else if (!Character.isLetter(c)){
				errorType = ErrorType.STATEMENT_ERROR;
				errorLine = lineObject.lineNumber;
				validExecutionLine = false;
			}
		}
		if (validExecutionLine == false) {
			System.out.println("Error Type is:" + errorType);
			System.out.println("The code are " + "\"" + lineObject.contents+ "\"" );
			System.out.println("The line number is: " + errorLine);
			System.exit(0);
		}
		
	}

	// return VOID if correct
	// only stop and exit when error
	private static void checkStatementValid_assign(ProgramLineObject lineObject) {
		boolean validExecutionLine = true;
		ErrorType errorType = null;
		int errorLine = 0;

		String trimString = lineObject.contents.replace(" ", "");
		// System.out.println("After token:  " + trimString);

		// count number of "="
		int numberOfEqual = 0;
		for (int i = 0; i < trimString.length(); i++) {
			if (trimString.charAt(i) == '=')
				numberOfEqual++;
		}

		if (numberOfEqual != 1) {// a==3
			errorType = ErrorType.STATEMENT_ERROR;
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

					//check comma to go to tree validation.
					if (tokens[1].contains(","))
						return;
					if (!isIntFloat(tokens[1])) {
						errorType = ErrorType.STATEMENT_INVALID_ASSIGNMENT_ERROR;
						errorLine = lineObject.lineNumber;
						validExecutionLine = false;
					}
				}
			}
		}
		if (validExecutionLine == false) {
			System.out.println("Error Type is:" + errorType);
			System.out.println("The code are " + "\"" + lineObject.contents+ "\"" );
			System.out.println("The line number is: " + errorLine);
			System.exit(0);
		}
	}

	private static void checkLoopStartValid(ProgramLineObject lineObject) {
		boolean validExecutionLine = true;
		ErrorType errorType = null;
		int errorLine = 0;

		String[] tokenString = lineObject.contents.split(" ");
		if (tokenString.length != 3) {
			errorType = ErrorType.LOOP;
			errorLine = lineObject.lineNumber;
			validExecutionLine = false;
		} else {
			if (tokenString[1].matches("-?\\d+(\\.\\d+)?")) {
				if (tokenString[1].contains(".")) {
					errorType = ErrorType.FLOAT_NUMBER;
					errorLine = lineObject.lineNumber;
					validExecutionLine = false;
				}
			} else {
				if (tokenString[1].length() != 1) {
					errorType = ErrorType.VARIABLE_NAME;
					errorLine = lineObject.lineNumber;
					validExecutionLine = false;
				} else {
					if (!tokenString[1].matches(".*[a-z].*")) {
						errorType = ErrorType.INVALID_CHAR;
						errorLine = lineObject.lineNumber;
						validExecutionLine = false;
					}
				}
			}
		}
		if (!validExecutionLine) {
			System.out.println("Error Type is:" + errorType);
			System.out.println("The code are " + "\"" + lineObject.contents+ "\"" );
			System.out.println("The line number is: " + errorLine);
			System.exit(0);
		}

	}

	private static void executeStatementLine(ProgramLineObject executingLine) {
		// get first token by white space, for scan PRINT statement
		String[] preTokens = executingLine.contents.split("\\s+");
		String firstToken = preTokens[0].toLowerCase();
		if (firstToken.equals("print") || firstToken.equals("printcr"))
			executeStatementLine_print(executingLine);
		else if(executingLine.contents.contains(",")){
			checkStringValidation(executingLine);
			executeStatementLine_assignWithOperation(executingLine);
		}
		else
			executeStatementLine_assign(executingLine);
	}

	private static void executeStatementLine_assignWithOperation(ProgramLineObject executingLine) {
		String commandLine = executingLine.contents;
		String trimString = commandLine.replace(" ", "");
		String[] tokens = trimString.split("[=;]");
		String[] calculationTokens = tokens[1].split(",");
		StringBuilder sb = new StringBuilder();
		for (int i = 0;i<calculationTokens.length - 1;i++){
			if (!calculationTokens[i].matches("-?\\d+(\\.\\d+)?")) {
				if (calculationTokens[i].equals("+") || calculationTokens[i].equals("-") || calculationTokens[i].equals("*") || calculationTokens[i].equals("/")) {
					sb.append(calculationTokens[i]).append(",");
					continue;
				}
				else{
					if (map.get(calculationTokens[i]).type == ValueType.IntegerType){
						sb.append(map.get(calculationTokens[i]).iValue).append(",");
					}
					else{
						sb.append(map.get(calculationTokens[i]).fValue).append(",");
					}
				}
			}
			else{
				sb.append(calculationTokens[i]).append(",");
			}
		}
		int lastIndex = calculationTokens.length - 1;
		if (!calculationTokens[lastIndex].matches("-?\\d+(\\.\\d+)?")) {
			if (calculationTokens[lastIndex].equals("+") || calculationTokens[lastIndex].equals("-") ||
					calculationTokens[lastIndex].equals("*") || calculationTokens[lastIndex].equals("/")) {
				sb.append(calculationTokens[lastIndex]);
			} else {
				if (map.get(calculationTokens[lastIndex]).type == ValueType.IntegerType) {
					sb.append(map.get(calculationTokens[lastIndex]).iValue);
				} else {
					sb.append(map.get(calculationTokens[lastIndex]).fValue);
				}
			}
		}
		else {
			sb.append(calculationTokens[lastIndex]);
		}
		//System.out.println(sb.toString());
		String[] convertedVersion = ConvertString.StringToArray(sb.toString());
		if (convertedVersion == null){
			System.exit(0);
		}
		else {
			TreeADT newTree = new TreeADT();
			TreeNode root = newTree.constructTree(convertedVersion, convertedVersion.length);
			Object result = newTree.caculateTree(root);
			if (!map.containsKey(tokens[0])) {
				map.put(tokens[0], new Value(result.toString()));
			}
			else
				map.get(tokens[0]).setValue(result.toString());
		}
	}

	private static void checkStringValidation(ProgramLineObject executingLine){
		boolean validExecutionLine = true;
		ErrorType errorType = null;
		int errorLine = 0;
		String trimString = executingLine.contents.replace(" ", "");
		String[] tokens = trimString.split("[=;]");
		String[] calculationTokens = tokens[1].split(",");
		for (int i = 0; i < calculationTokens.length;i++) {
			//System.out.println(calculationTokens[i]);
			if (!calculationTokens[i].matches("-?\\d+(\\.\\d+)?")) {
				if (calculationTokens[i].length() != 1){
					errorType = ErrorType.VARIABLE_NAME;
					errorLine = executingLine.lineNumber;
					validExecutionLine = false;
					break;
				}
				else{
					if (calculationTokens[i].equals("+") || calculationTokens[i].equals("-") || calculationTokens[i].equals("*") || calculationTokens[i].equals("/")){
						continue;
					}
					if (map.get(calculationTokens[i]) == null){
						errorType = ErrorType.UNDECLARED_ERROR;
						errorLine = executingLine.lineNumber;
						validExecutionLine = false;
						break;
					}
				}
			}
		}
		if (!validExecutionLine) {
			System.out.println("Error Type is:" + errorType);
			System.out.println("The code are " + "\"" + executingLine.contents+ "\"" );
			System.out.println("The line number is: " + errorLine);
			System.exit(0);
		}
	}
	private static void executeStatementLine_print(ProgramLineObject executingLine) {					
		String trimString = executingLine.contents.replace(";", "");
		
		String[] preTokens = trimString.split("\\s+");
		String firstToken = preTokens[0].toLowerCase();
		
		int firstQuote = trimString.indexOf("\"");
		int secondQuote = trimString.indexOf("\"", trimString.indexOf("\"")+1);
		//print "x";
		if(firstQuote>0){
			if(firstToken.equals("print"))
				System.out.print(trimString.substring(firstQuote+1, secondQuote));
			else
				System.out.println(trimString.substring(firstQuote+1, secondQuote));
		}
		//print a;
		else{
			//a not declared
			String secondToken = preTokens[1].toLowerCase();
			
			if (!map.containsKey(secondToken)) {
				System.out.println("Error Type is:" + ErrorType.UNDECLARED_ERROR);
				System.out.println("The code are " + "\"" + executingLine.contents+ "\"" );
				System.out.println("The line number is: " + executingLine.lineNumber);
				System.exit(0);
			}
			else{
				if(firstToken.equals("print"))
					map.get(secondToken).printValue();
				else{
					map.get(secondToken).printValue();
					System.out.println();
				}
			}
		}

	}
		
		
	private static void executeStatementLine_assign(ProgramLineObject executingLine) {	
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
					map.get(leftValue).setValue(map.get(rightValue).getValue());
			}
		}
		// a = 3;
		else {
			// a not declared before
			if (!map.containsKey(leftValue))
				map.put(leftValue, new Value(rightValue));
			// a declared before
			else
				map.get(leftValue).setValue(map.get(rightValue).getValue());
		}
	}

	private int executeLoopLine(ProgramLineObject executingLine) {
		if (executingLine.type == ProgramLineType.LOOPSTART){
			String[] tokenString = executingLine.contents.split(" ");
			if (tokenString[1].matches("-?\\d+(\\.\\d+)?")){
				if (executingLine.loopObject.originalNumOfExcutionInNum == -1){
					executingLine.loopObject.originalNumOfExcutionInNum = Integer.parseInt(tokenString[1]);
					executingLine.loopObject.numOfExcutionInNum = Integer.parseInt(tokenString[1]) - 1;
					return executingLine.loopObject.startIndex + 1;
				}
				else if (executingLine.loopObject.numOfExcutionInNum > 0) {
					executingLine.loopObject.numOfExcutionInNum--;
					return executingLine.loopObject.startIndex + 1;
				}
				else{
					if (programLineObjects.get(executingLine.loopObject.startIndex).loopObject.previousLoops.size() > 0){
						if (programLineObjects.get(programLineObjects.get(executingLine.loopObject.startIndex).loopObject.previousLoops.get(0)).loopObject.numOfExcutionInNum > 0) {
							executingLine.loopObject.numOfExcutionInNum = executingLine.loopObject.originalNumOfExcutionInNum;
							return executingLine.loopObject.endIndex + 1;
						}
						else{
							programLineObjects.get(executingLine.loopObject.startIndex).loopObject.previousLoops.remove(0);
							executingLine.loopObject.numOfExcutionInNum = executingLine.loopObject.originalNumOfExcutionInNum;
							return executingLine.loopObject.endIndex + 1;
						}
					}
					return executingLine.loopObject.endIndex + 1;
				}
			}
			else{
				if (executingLine.loopObject.originalNumOfExcutionInNum == -1) {
					if (map.get(tokenString[1]).type == ValueType.IntegerType) {
						executingLine.loopObject.originalNumOfExcutionInNum = map.get(tokenString[1]).iValue;
						executingLine.loopObject.numOfExcutionInNum = map.get(tokenString[1]).iValue - 1;
						return executingLine.loopObject.startIndex + 1;
					}
				}
				else if (executingLine.loopObject.numOfExcutionInNum > 0) {
					executingLine.loopObject.numOfExcutionInNum--;
					return executingLine.loopObject.startIndex + 1;
				}
				else{
					if (programLineObjects.get(executingLine.loopObject.startIndex).loopObject.previousLoops.size() > 0){
						if (programLineObjects.get(programLineObjects.get(executingLine.loopObject.startIndex).loopObject.previousLoops.get(0)).loopObject.numOfExcutionInNum > 0) {
							executingLine.loopObject.numOfExcutionInNum = executingLine.loopObject.originalNumOfExcutionInNum;
							return executingLine.loopObject.endIndex + 1;
						}
						else{
							programLineObjects.get(executingLine.loopObject.startIndex).loopObject.previousLoops.remove(0);
							executingLine.loopObject.numOfExcutionInNum = executingLine.loopObject.originalNumOfExcutionInNum;
							return executingLine.loopObject.endIndex + 1;
						}
					}
					return executingLine.loopObject.endIndex + 1;
				}
			}
			//executingLine.loopObject.numOfExcution
		}
		else if (executingLine.type == ProgramLineType.LOOPEND){
			return executingLine.loopObject.startIndex;
		}
		return -1;
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

