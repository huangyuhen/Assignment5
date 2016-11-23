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
 * 					checkLineTokens(ProgramLineObject lineObject)
 * 					checkStatementValid(ProgramLineObject lineObject)
 * 					checkStatementValid_print(ProgramLineObject lineObject)
 * 					checkStatementValid_assign(ProgramLineObject lineObject)
 * 					checkLoopStartValid(ProgramLineObject lineObject)
 * 					executeStatementLine(ProgramLineObject executingLine)
 * 					executeStatementLine_assignWithOperation(ProgramLineObject executingLine)
 * 					checkStringValidation(ProgramLineObject executingLine)
 * 					executeStatementLine_print(ProgramLineObject executingLine)
 * 					executeStatementLine_assign(ProgramLineObject executingLine)
 * 					executeLoopLine(ProgramLineObject executingLine)
 * 					isIntFloat(String str)
 *
 *****************************************************************************************************/


import EnumerationTypes.*;
import ObjectTypes.Error;
import ObjectTypes.ProgramLineObject;
import ObjectTypes.Value;

import java.util.*;

public class Execution{
	// Hash Table which store all the declared Values
	final static HashMap<String, Value> map = new HashMap<>();

	// List of ProgramLineObject to be validated and executed
	ArrayList<ProgramLineObject> programLineObjects;

	/*********************************************************************
	 * Constructor:  Execution(ArrayList<ProgramLineObject> list)
	 * Description: This constructor will read ArrayList<ProgramLineObject>.
	 * Input Parameters: ArrayList<ProgramLineObject>
	 * Output: void
	 ***********************************************************************/
	public Execution(ArrayList<ProgramLineObject> list){
		programLineObjects = list;
	}

	/*********************************************************************
	 * Function Name:  executeLines()
	 * Description: This method will validate and execute ProgramLineObject line by line.
	 * Input Parameters: null
	 * Output: void
	 ***********************************************************************/
	public void executeLines(){
		// declare executionLineObjects by check syntax error first
		List<ProgramLineObject> executionLineObjects = scanProgramLineToExecutionLine(programLineObjects);

		//current executionIndex (line number)
		int executionIndex = 0;
		// all the execution lines are in the executionLineObjects
		// execute the lines one by one
		// if the current executionIndex is not in the end of the execution lines
		while (executionIndex < programLineObjects.size()) {
			// get the current line to execute
			ProgramLineObject executingLine = executionLineObjects.get(executionIndex);
			switch (executingLine.type) {
				//if current execution line is in type of STATEMENT
				case STATEMENT:
					executeStatementLine(executingLine);
					// after this line got executed, increased the executionIndex
					executionIndex++;
					break;
				//if current execution line is in type of start of the LOOP
				case LOOPSTART:
					executionIndex = executeLoopLine(executingLine);
					break;
				//if current execution line is in type of end of the LOOP
				case LOOPEND:
					executionIndex = executeLoopLine(executingLine);
					break;
				default:
			}

		}
	}

	/*********************************************************************
	 * Function Name:  scanProgramLineToExecutionLine(ArrayList<ProgramLineObject> programLineObjects)
	 * Description: This method will validate list ProgramLineObject line by line.
	 * 				And return the list of ProgramLineObject as execution line.
	 * Input Parameters: ArrayList<ProgramLineObject> programLineObjects
	 * Output: List<ProgramLineObject>
	 ***********************************************************************/
	public static List<ProgramLineObject> scanProgramLineToExecutionLine(
			ArrayList<ProgramLineObject> programLineObjects) {
		// declare a List of ProgramLineObject storing ProgramLineObject to be executed
		List<ProgramLineObject> executionLineObjectsList = new ArrayList<>();
		// iterate the list
		for (ProgramLineObject lineObject : programLineObjects) {
			//check current ProgramLineObject syntax error
			checkLineTokens(lineObject);
			//if no error, add into the ProgramLineObject list to be executed
			executionLineObjectsList.add(lineObject);
		}
		return executionLineObjectsList;
	}

	/*********************************************************************
	 * Function Name:  checkLineTokens(ProgramLineObject lineObject)
	 * Description: This method will validate ProgramLineObject.
	 * 				And return void if no error.
	 * 			    And exit the program if there's an error.
	 * Input Parameters: ProgramLineObject lineObject
	 * Output: void
	 ***********************************************************************/
	private static void checkLineTokens(ProgramLineObject lineObject) {
		switch (lineObject.type) {
			//if current execution line is in type of STATEMENT
		case STATEMENT:
			checkStatementValid(lineObject);
			break;
			//if current execution line is in type of start of the LOOP
		case LOOPSTART:
			checkLoopStartValid(lineObject);
			break;
			//if current execution line is in type of end of the LOOP
		case LOOPEND:
		default:
			break;
		}
	}


	/*********************************************************************
	 * Function Name:  checkStatementValid(ProgramLineObject lineObject)
	 * Description: This method will validate list ProgramLineObject.
	 * 				And return void if no error.
	 * 			    And exit the program if there's an error.
	 * Input Parameters: ProgramLineObject lineObject
	 * Output: void
	 ***********************************************************************/
	private static void checkStatementValid(ProgramLineObject lineObject) {
		// get first token by white space, for scan PRINT(CR) statement
		String[] preTokens = lineObject.contents.split("\\s+");
		String firstToken = preTokens[0].toLowerCase();
		//if this statement is in type of print
		if (firstToken.equals("print") || firstToken.equals("printcr"))
			checkStatementValid_print(lineObject);
		//if this statement is in type of assignment
		else
			checkStatementValid_assign(lineObject);
	}

	/*********************************************************************
	 * Function Name:  checkStatementValid_print(ProgramLineObject lineObject)
	 * Description: This method will validate ProgramLineObject.
	 * 				And return void if no error.
	 * 			    And exit the program if there's an error.
	 * Input Parameters: ProgramLineObject lineObject
	 * Output: void
	 ***********************************************************************/
	private static void checkStatementValid_print(ProgramLineObject lineObject) {
		//declare temp values store Line valid or not, errorType and errorLine number
		boolean validExecutionLine = true;
		ErrorType errorType = null;
		int errorLine = 0;

		//trim the String remove the ";"
		String trimString = lineObject.contents.replace(";", "");

		//count the number of Quotes (")
		int numberOfQuotes = 0;
		for (int i = 0; i < trimString.length(); i++) {
			if (trimString.charAt(i) == '"')
				numberOfQuotes++;
		}

		//if there is more than one " e.g. print "
		if(numberOfQuotes!=0){
			//print " "
			if(numberOfQuotes==2){
				//get the first and second (") index
				int firstQuoteIndex = trimString.indexOf("\"");
				int endOfFirstToken = trimString.indexOf(" ");

				//print a"x"
				if(firstQuoteIndex!=(endOfFirstToken+1)){
					errorType = ErrorType.STATEMENT_ERROR;
					errorLine = lineObject.lineNumber;
					validExecutionLine = false;
				}
			}
			//if number of (") not equal to 2, e.g. print """
			else{
				errorType = ErrorType.STATEMENT_ERROR;
				errorLine = lineObject.lineNumber;
				validExecutionLine = false;
			}
		}
		//if there is no quote (")
		//e.g.
		//print cb
		//printcr c d
		//print 1
		else{
			// trim the String with white space
			String[] preTokens = trimString.split("\\s+");
			char c = preTokens[1].charAt(0);

			//if the string is more than one token
			//printcr c d
			if(preTokens.length!=2){
				errorType = ErrorType.STATEMENT_ERROR;
				errorLine = lineObject.lineNumber;
				validExecutionLine = false;
			}
			//if the string is NOT a single character
			//print cb
			else if(preTokens[1].length()!=1){
				errorType = ErrorType.STATEMENT_ERROR;
				errorLine = lineObject.lineNumber;
				validExecutionLine = false;
			}
			//if the string is not a letter
			//print 1
			else if (!Character.isLetter(c)){
				errorType = ErrorType.STATEMENT_ERROR;
				errorLine = lineObject.lineNumber;
				validExecutionLine = false;
			}
		}
		//if it is an error above, exit the system and print the error
		if (validExecutionLine == false) {
			System.out.println("Error Type is:" + errorType);
			System.out.println("The code are " + "\"" + lineObject.contents+ "\"" );
			System.out.println("The line number is: " + errorLine);
			System.exit(0);
		}
	}

	/*********************************************************************
	 * Function Name:  checkStatementValid_assign(ProgramLineObject lineObject)
	 * Description: This method will validate ProgramLineObject.
	 * 				And return void if no error.
	 * 			    And exit the program if there's an error.
	 * Input Parameters: ProgramLineObject lineObject
	 * Output: void
	 ***********************************************************************/
	private static void checkStatementValid_assign(ProgramLineObject lineObject) {
		//declare temp values store Line valid or not, errorType and errorLine number
		boolean validExecutionLine = true;
		ErrorType errorType = null;
		int errorLine = 0;

		//trim the string by removing the white space
		String trimString = lineObject.contents.replace(" ", "");

		// count number of "="
		int numberOfEqual = 0;
		for (int i = 0; i < trimString.length(); i++) {
			if (trimString.charAt(i) == '=')
				numberOfEqual++;
		}

		//if more than one "="
		if (numberOfEqual != 1) {// a==3
			errorType = ErrorType.STATEMENT_ERROR;
			errorLine = lineObject.lineNumber;
			validExecutionLine = false;
		}
		// only one "="
		else {
			//tokenlize the string by "=" or ";"
			String[] tokens = trimString.split("[=;]");
			//if not a valid assignment form
			// =3 or a= or =
			if (tokens.length != 2) {
				errorType = ErrorType.STATEMENT_INVALID_ASSIGNMENT_ERROR;
				errorLine = lineObject.lineNumber;
				validExecutionLine = false;
			}
			// check both tokens
			else {
				// check first token
				if (tokens[0].length() != 1) {// aa=1
					errorType = ErrorType.STATEMENT_INVALID_ASSIGNMENT_ERROR;
					errorLine = lineObject.lineNumber;
					validExecutionLine = false;
				}
				// 1=1
				else {
					char c = tokens[0].charAt(0);
					if (!Character.isLetter(c)) {
						errorType = ErrorType.STATEMENT_INVALID_ASSIGNMENT_ERROR;
						errorLine = lineObject.lineNumber;
						validExecutionLine = false;
					}
				}
				// check second token
				// length is 1
				// a=?
				if (tokens[1].length() == 1) {
					char c = tokens[1].charAt(0);
					if ((!Character.isDigit(c)) && (!Character.isLetter(c))) {
						errorType = ErrorType.STATEMENT_INVALID_ASSIGNMENT_ERROR;
						errorLine = lineObject.lineNumber;
						validExecutionLine = false;
					}
				}
				// length is >1
				// a=ab
				else {
					//check comma to go to tree validation.
					if (tokens[1].contains(","))
						return;
					//otherwise, report error
					if (!isIntFloat(tokens[1])) {
						errorType = ErrorType.STATEMENT_INVALID_ASSIGNMENT_ERROR;
						errorLine = lineObject.lineNumber;
						validExecutionLine = false;
					}
				}
			}
		}
		//if it is an error above, exit the system and print the error
		if (validExecutionLine == false) {
			System.out.println("Error Type is:" + errorType);
			System.out.println("The code are " + "\"" + lineObject.contents+ "\"" );
			System.out.println("The line number is: " + errorLine);
			System.exit(0);
		}
	}

	/*********************************************************************
	 * Function Name:  checkLoopStartValid(ProgramLineObject lineObject)
	 * Description: This method will validate ProgramLineObject.
	 * 				And return void if no error.
	 * 			    And exit the program if there's an error.
	 * Input Parameters: ProgramLineObject lineObject
	 * Output: void
	 ***********************************************************************/
	private static void checkLoopStartValid(ProgramLineObject lineObject) {
		//declare temp values store Line valid or not, errorType and errorLine number
		boolean validExecutionLine = true;
		ErrorType errorType = null;
		int errorLine = 0;

		String[] tokenString = lineObject.contents.split(" ");

		//The only permitted loop statement is: loop X {
		if (tokenString.length != 3) {
			errorType = ErrorType.LOOP;
			errorLine = lineObject.lineNumber;
			validExecutionLine = false;
		} else {

			//If the second token in loop is number
			if (tokenString[1].matches("-?\\d+(\\.\\d+)?")) {

				//Float number are not permitted for number of loops
				if (tokenString[1].contains(".")) {
					errorType = ErrorType.FLOAT_NUMBER;
					errorLine = lineObject.lineNumber;
					validExecutionLine = false;
				}
			} else {

				//If the variable name is more than one character, this is not permitted
				if (tokenString[1].length() != 1) {
					errorType = ErrorType.VARIABLE_NAME;
					errorLine = lineObject.lineNumber;
					validExecutionLine = false;
				} else {

					//If the varibale name is not between letter a-z, this is not permitted
					if (!tokenString[1].matches(".*[a-z].*")) {
						errorType = ErrorType.INVALID_CHAR;
						errorLine = lineObject.lineNumber;
						validExecutionLine = false;
					}
				}
			}
		}
		//if it is an error above, exit the system and print the error
		if (!validExecutionLine) {
			System.out.println("Error Type is:" + errorType);
			System.out.println("The code are " + "\"" + lineObject.contents+ "\"" );
			System.out.println("The line number is: " + errorLine);
			System.exit(0);
		}

	}

	/*********************************************************************
	 * Function Name:  executeStatementLine(ProgramLineObject lineObject)
	 * Description: This method will execute ProgramLineObject.
	 * 				And return void if no error.
	 * Input Parameters: ProgramLineObject lineObject
	 * Output: void
	 ***********************************************************************/
	private static void executeStatementLine(ProgramLineObject executingLine) {
		// get first token by white space, for scan PRINT statement
		String[] preTokens = executingLine.contents.split("\\s+");
		String firstToken = preTokens[0].toLowerCase();
		//if this is statement for PRINT(CR)
		if (firstToken.equals("print") || firstToken.equals("printcr"))
			executeStatementLine_print(executingLine);
		//if this is statement for TREE operation
		else if(executingLine.contents.contains(",")){
			checkStringValidation(executingLine);
			executeStatementLine_assignWithOperation(executingLine);
		}
		//if this is statement for common assignment
		else
			executeStatementLine_assign(executingLine);
	}

	/*********************************************************************
	 * Function Name:  executeStatementLine_assignWithOperation(ProgramLineObject lineObject)
	 * Description: This method will execute ProgramLineObject.
	 * 				And return void if no error.
	 * Input Parameters: ProgramLineObject lineObject
	 * Output: void
	 ***********************************************************************/
	private static void executeStatementLine_assignWithOperation(ProgramLineObject executingLine) {

		//preprocess the execution line contents
		String commandLine = executingLine.contents;
		String trimString = commandLine.replace(" ", "");
		String[] tokens = trimString.split("[=;]");
		String[] calculationTokens = tokens[1].split(",");
		StringBuilder sb = new StringBuilder();
		for (int i = 0;i<calculationTokens.length - 1;i++){

			//If we are dealing with character
			if (!calculationTokens[i].matches("-?\\d+(\\.\\d+)?")) {

				//If we are dealing with logic operater, we continue
				if (calculationTokens[i].equals("+") || calculationTokens[i].equals("-") ||
						calculationTokens[i].equals("*") || calculationTokens[i].equals("/")) {
					sb.append(calculationTokens[i]).append(",");
					continue;
				}
				else{
					//If we are dealing with a Integer type, we get variable from map
					if (map.get(calculationTokens[i]).type == ValueType.IntegerType){
						sb.append(map.get(calculationTokens[i]).iValue).append(",");
					}
					else{

						//Else we get float value from map.
						sb.append(map.get(calculationTokens[i]).fValue).append(",");
					}
				}
			}
			else{
				sb.append(calculationTokens[i]).append(",");
			}
		}
		int lastIndex = calculationTokens.length - 1;

		//This block is only for handling the last charater in the operation string
		if (!calculationTokens[lastIndex].matches("-?\\d+(\\.\\d+)?")) {
			//If we are dealing with logic operater, we continue
			if (calculationTokens[lastIndex].equals("+") || calculationTokens[lastIndex].equals("-") ||
					calculationTokens[lastIndex].equals("*") || calculationTokens[lastIndex].equals("/")) {
				sb.append(calculationTokens[lastIndex]);
			} else {
				//If we are dealing with a Integer type, we get variable from map
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


		String[] convertedVersion = ConvertString.StringToArray(sb.toString());
		//If there is no contents on the other side of = operation, we exit the program
		if (convertedVersion == null){
			System.exit(0);
		}
		else {
			TreeADT newTree = new TreeADT();
			//If the calculation is valid, we construct a tree for the calculation method
			TreeNode root = newTree.constructTree(convertedVersion, convertedVersion.length);

			//Now we have a result  from calculation method so that we can store value to our hash map
			Object result = newTree.caculateTree(root);

			// if TreeCalculation got devision is 0
			if(result==null){
				System.out.println("Error Type is:" + ErrorType.STATEMENT_ERROR);
				System.out.println("The code are " + "\"" + executingLine.contents+ "\"" );
				System.out.println("The line number is: " + executingLine.lineNumber);
				System.exit(0);
			}

			if (!map.containsKey(tokens[0])) {
				map.put(tokens[0], new Value(result.toString()));
			}
			else
				map.get(tokens[0]).setValue(result.toString());
		}
	}

	/*********************************************************************
	 * Function Name:  checkStringValidation(ProgramLineObject lineObject)
	 * Description: This method will validate ProgramLineObject for TREE operation.
	 * 				And return void if no error.
	 * Input Parameters: ProgramLineObject lineObject
	 * Output: void
	 ***********************************************************************/
	private static void checkStringValidation(ProgramLineObject executingLine){

		//program contents partioning
		boolean validExecutionLine = true;
		ErrorType errorType = null;
		int errorLine = 0;
		String trimString = executingLine.contents.replace(" ", "");
		String[] tokens = trimString.split("[=;]");
		String[] calculationTokens = tokens[1].split(",");
		for (int i = 0; i < calculationTokens.length;i++) {

			//If we are not dealing with number, we need to filter out error input
			if (!calculationTokens[i].matches("-?\\d+(\\.\\d+)?")) {

				//If the token is more than 1 character, we have a invalid execution line
				if (calculationTokens[i].length() != 1){
					errorType = ErrorType.VARIABLE_NAME;
					errorLine = executingLine.lineNumber;
					validExecutionLine = false;
					break;
				}
				else{

					//If it is logic operater, the let it go through
					if (calculationTokens[i].equals("+") || calculationTokens[i].equals("-")
							|| calculationTokens[i].equals("*") || calculationTokens[i].equals("/")){
						continue;
					}

					//If the variable is not in the map, we are encountering a undefined variable error
					if (map.get(calculationTokens[i]) == null){
						errorType = ErrorType.UNDECLARED_ERROR;
						errorLine = executingLine.lineNumber;
						validExecutionLine = false;
						break;
					}
				}
			}
		}

		//Print out error information if there is any error.
		if (!validExecutionLine) {
			System.out.println("Error Type is:" + errorType);
			System.out.println("The code are " + "\"" + executingLine.contents+ "\"" );
			System.out.println("The line number is: " + errorLine);
			System.exit(0);
		}
	}

	/*********************************************************************
	 * Function Name:  executeStatementLine_print(ProgramLineObject lineObject)
	 * Description: This method will execute ProgramLineObject for PRINT operation.
	 * 				And exit the program when error.
	 * 				And return void if no error.
	 * Input Parameters: ProgramLineObject lineObject
	 * Output: void
	 ***********************************************************************/
	private static void executeStatementLine_print(ProgramLineObject executingLine) {
		//Remove ";" from the string
		String trimString = executingLine.contents.replace(";", "");

		//get the tokens to be print
		String[] preTokens = trimString.split("\\s+");
		String firstToken = preTokens[0].toLowerCase();

		//get the first/second token index
		int firstQuote = trimString.indexOf("\"");
		int secondQuote = trimString.indexOf("\"", trimString.indexOf("\"")+1);

		//print "x";
		if(firstQuote>0){
			//if PRINT
			if(firstToken.equals("print"))
				System.out.print(trimString.substring(firstQuote+1, secondQuote));
			//if PRINTCR
			else
				System.out.println(trimString.substring(firstQuote+1, secondQuote));
		}
		//print a;
		else{
			//get the variable
			String secondToken = preTokens[1].toLowerCase();

			//if a not declared
			if (!map.containsKey(secondToken)) {
				System.out.println("Error Type is:" + ErrorType.UNDECLARED_ERROR);
				System.out.println("The code are " + "\"" + executingLine.contents+ "\"" );
				System.out.println("The line number is: " + executingLine.lineNumber);
				System.exit(0);
			}
			else{
				//if PRINT
				if(firstToken.equals("print"))
					map.get(secondToken).printValue();
				//if PRINTCR
				else{
					map.get(secondToken).printValue();
					System.out.println();
				}
			}
		}
	}

	/*********************************************************************
	 * Function Name:  executeStatementLine_assign(ProgramLineObject lineObject)
	 * Description: This method will execute ProgramLineObject for ASSIGNMENT operation.
	 * 				And exit the program when error.
	 * 				And return void if no error.
	 * Input Parameters: ProgramLineObject lineObject
	 * Output: void
	 ***********************************************************************/
	private static void executeStatementLine_assign(ProgramLineObject executingLine) {
		//trim and split the line contents by = and ;
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
				// a not declared before, just assign to it
				if (!map.containsKey(leftValue))
					map.put(leftValue, map.get(rightValue));
				// a declared before
				else
					map.get(leftValue).setValue(map.get(rightValue).getValue());
			}
		}
		// a = 3;
		else {
			// a not declared before, just assign to it
			if (!map.containsKey(leftValue))
				map.put(leftValue, new Value(rightValue));
			// a declared before
			else
				map.get(leftValue).setValue(map.get(rightValue).getValue());
		}
	}

	/*********************************************************************
	 * Function Name:  executeLoopLine(ProgramLineObject lineObject)
	 * Description: This method will execute ProgramLineObject for LOOP operation.
	 * Input Parameters: ProgramLineObject lineObject
	 * Output: int
	 ***********************************************************************/
	private int executeLoopLine(ProgramLineObject executingLine) {

		//If we are dealing with loop program line
		if (executingLine.type == ProgramLineType.LOOPSTART){
			String[] tokenString = executingLine.contents.split(" ");

			//If the token is number
			if (tokenString[1].matches("-?\\d+(\\.\\d+)?")){

				//Since when we create loopObject, we didn't assign any number of loop execution to the object
				if (executingLine.loopObject.originalNumOfExcutionInNum == -1){

					//Now we parse the string to actual integer
					executingLine.loopObject.originalNumOfExcutionInNum = Integer.parseInt(tokenString[1]);

					//Decrementing the number of execution for the loop
					executingLine.loopObject.numOfExcutionInNum = Integer.parseInt(tokenString[1]) - 1;

					//We return the index of the next statement that should be exectuted in the program line list
					return executingLine.loopObject.startIndex + 1;
				}
				else if (executingLine.loopObject.numOfExcutionInNum > 0) {
					//If the number is valid, we then go ahead return the index of next execution statement
					executingLine.loopObject.numOfExcutionInNum--;
					return executingLine.loopObject.startIndex + 1;
				}
				else{

					//If there the current loop is inside of an larger loop
					if (programLineObjects.get(executingLine.loopObject.startIndex).
							loopObject.previousLoops.size() > 0){

						//As long as we have any outside loop that has not finished, we should restore the number
						//of execution of current loop
						if (programLineObjects.get(programLineObjects.get(
								executingLine.loopObject.startIndex).loopObject.
								previousLoops.get(0)).loopObject.numOfExcutionInNum > 0) {

							//restoring the number of execution for current loop
							executingLine.loopObject.numOfExcutionInNum =
									executingLine.loopObject.originalNumOfExcutionInNum;
							return executingLine.loopObject.endIndex + 1;
						}
						else{
							//We are done with the parent loop, so we should remove it from the previous loop list
							programLineObjects.get(executingLine.loopObject.startIndex).
									loopObject.previousLoops.remove(0);

							//restoring the number of execution
							executingLine.loopObject.numOfExcutionInNum =
									executingLine.loopObject.originalNumOfExcutionInNum;
							return executingLine.loopObject.endIndex + 1;
						}
					}
					return executingLine.loopObject.endIndex + 1;
				}
			}

			//This the case of dealing with variable name
			else{

				//If there is no parsing before, we need to parse the variable to number
				if (executingLine.loopObject.originalNumOfExcutionInNum == -1) {
					if (map.get(tokenString[1]) == null){
						System.out.println("Error Type is:" + ErrorType.VARIABLE_NAME);
						System.out.println("The code are " + "\"" + executingLine.contents+ "\"" );
						System.out.println("The line number is: " + executingLine.lineNumber);
						System.exit(0);
					}
					if (map.get(tokenString[1]).type == ValueType.IntegerType) {

						//Getting the variable value from the map
						executingLine.loopObject.originalNumOfExcutionInNum = map.get(tokenString[1]).iValue;

						//Decrement the number of execution
						executingLine.loopObject.numOfExcutionInNum = map.get(tokenString[1]).iValue - 1;
						return executingLine.loopObject.startIndex + 1;
					}
				}

				//If the number of execution has been parsed before, we decrement the execution times.
				else if (executingLine.loopObject.numOfExcutionInNum > 0) {
					executingLine.loopObject.numOfExcutionInNum--;
					return executingLine.loopObject.startIndex + 1;
				}
				else{

					//If there the current loop is inside of an larger loop
					if (programLineObjects.get(
							executingLine.loopObject.startIndex).loopObject.previousLoops.size() > 0){

						//As long as we have any outside loop that has not finished, we should restore the number
						//of execution of current loop
						if (programLineObjects.get(
								programLineObjects.get(
										executingLine.loopObject.startIndex).loopObject.previousLoops.
										get(0)).loopObject.numOfExcutionInNum > 0) {

							//Restoring the number of execution
							executingLine.loopObject.numOfExcutionInNum =
									executingLine.loopObject.originalNumOfExcutionInNum;
							return executingLine.loopObject.endIndex + 1;
						}
						else{
							//We are done with the parent loop, so we should remove it from the previous loop list
							programLineObjects.get(
									executingLine.loopObject.startIndex).loopObject.previousLoops.remove(0);

							//Restoring the number of execution
							executingLine.loopObject.numOfExcutionInNum =
									executingLine.loopObject.originalNumOfExcutionInNum;
							return executingLine.loopObject.endIndex + 1;
						}
					}
					return executingLine.loopObject.endIndex + 1;
				}
			}

		}

		//If it is loop end, we return the index of the start of the loop
		else if (executingLine.type == ProgramLineType.LOOPEND){
			return executingLine.loopObject.startIndex;
		}

		//In the case of unhandled, we return -1 to indicate error.
		return -1;
	}

	/*********************************************************************
	 * Function Name:  isIntFloat(String str)
	 * Description: This method will check whether this String's content is
	 * 				in type of inte
	 * Input Parameters: String str
	 * Output: boolean
	 ***********************************************************************/
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

