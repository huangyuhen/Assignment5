/*****************************************************************************************************
 * File: SyntaxCheckADT.java
 * Course: Data Structures and Algorithms for Engineers
 * Project: A5
 * Author: Evil Genius
 *         (Yuheng Huang, Tianbing Leng, Xin Huang)
 * Versions:
 *	1.0 November 2016
 *
 * Description: This program checks all the syntax error for the input codes from user
 *				errors will be printed on the terminal line for user to revide the code.
 *
 *
 *
 * Internal Functions: String[] contentsPreProcessing(String contents);
                       void commentCheck(String[] codes);
 					   void programCheck(String[] codes);
   				       void bracCheck(String[] codes);
 				       void semiCheck(String[] codes);
 				       void outSideStatementCheck(String[] codes);
   					   void programLineProcess();
   Public Functions:   Boolean syntaxCheck();
 *****************************************************************************************************/


/* Import section */
import EnumerationTypes.ErrorType;
import EnumerationTypes.ProgramLineType;
import EnumerationTypes.Type;
import ObjectTypes.Error;
import ObjectTypes.LineObject;
import ObjectTypes.LoopObject;
import ObjectTypes.ProgramLineObject;
import java.lang.*;
import java.util.*;


public class SyntaxCheckADT {

	//Global place holder for input codes
	private String[] code = null;

	//Global place holder for errors in the code
	private ArrayList<Error> errorList = new ArrayList<>();

	//Global place holder for the code line which should not contain semi-column
	private ArrayList<Integer> ignoredLine = new ArrayList<>();

	//Global place holder for the code line which should have our interpretor to execute
	public ArrayList<ProgramLineObject> programLine = new ArrayList<>();

	//Global place holder for parentheses check
	private Stack<LineObject> stack = new Stack<>();


	/*********************************************************************
	 * Function Name:    contentsPreProcessing
	 * Description:      This function divides the input code from users' input
	 * 					 file to a string array.
	 * Input Parameters: String input
	 * Output:           A string array
	 ***********************************************************************/
	public String[] contentsPreProcessing(String contents){
		code = contents.split("\n");
		return code;
	}


	/*********************************************************************
	 * Function Name:    commentCheck
	 * Description:      This function checks all comment in the code which
	 * 					 mark all comment line as ignored line so that the interpreter
	 * 					 will not execute the commented line.
	 * Input Parameters: String array
	 * Post Condition:   If the error has been found in the code, error list
	 * 					 will be changed.
	 ***********************************************************************/
	private void commentCheck(String[] codes) {
		int lineCounter = 0;

		//Place holder for error checking
		boolean checkIncomment = false;
		for (int i = 0; i < codes.length; i++) {
			lineCounter++;

			//If /* is found in the start of the line, this means we are reading a comment
			if (codes[i].trim().startsWith("/*") && !checkIncomment) {

				//If the line has not yet added to global place holder, add to the list
				if (!ignoredLine.contains(lineCounter))
					ignoredLine.add(lineCounter);
				checkIncomment = true;
				//Adding this comment line for parentheses check
				stack.add(new LineObject(Type.LEFTCOMMENT,lineCounter));
			}
			//If */ is found in the end of the line, this means we are reading a comment and it has ended
			if (codes[i].trim().endsWith("*/") && checkIncomment) {
				//If the line has not yet added to global place holder, add to the list
				if (!ignoredLine.contains(lineCounter))
					ignoredLine.add(lineCounter);
				checkIncomment = false;

				//If the item on top stack is not left brackets, that means the users code is wrong
				if (stack.isEmpty() || stack.peek().type != Type.LEFTCOMMENT) {

					//Adding error to errorList
					errorList.add(new Error(ErrorType.RIGHTCOMMENT, lineCounter, code[i]));
					return;
				}

				//Found a pair matching with right brackets, pop the last item out from stack
				stack.pop();
			}

			//If the line is empty, we add the line to ignored list
			if (checkIncomment || codes[i].trim().length() == 0){
				if (!ignoredLine.contains(lineCounter))
					ignoredLine.add(lineCounter);
			}
		}

		//If there is any remaining item in the stack, this means the program code are not closed property
		if (!stack.isEmpty()) {

			//Adding all errors to error list, one at a time until the stack is empty.
			while (!stack.isEmpty()) {
				LineObject item = stack.pop();
				if (item.type == Type.LEFTCOMMENT) {
					errorList.add(new Error(ErrorType.LEFTCOMMENT, item.lineNumber, code[item.lineNumber - 1]));
					return;
				}
				else if (item.type == Type.RIGHTCOMMENT) {
					errorList.add(new Error(ErrorType.RIGHTCOMMENT, item.lineNumber, code[item.lineNumber - 1]));
					return;
				}
			}
		}
	}


	/*********************************************************************
	 * Function Name:    programCheck
	 * Description:      This function checks all program statement and end statement
	 *                   in the code which mark all program line as ignored line so that the interpreter
	 * 					 will not execute the commented line.
	 * Input Parameters: String array
	 * Post Condition:   If the error has been found in the code, error list
	 * 					 will be changed.
	 ***********************************************************************/
	private void programCheck(String[] codes) {
		int lineCounter = 0;

		//Place holder for error checking
		boolean checkProgram = false;
		for (int i = 0; i < codes.length; i++) {
			lineCounter++;
			//If the line is not in the ignored line list and the line starts with program
			//Then we know we are dealing with beginning of the program
			if (!ignoredLine.contains(lineCounter) && codes[i].trim().equals("program")){
				checkProgram = true;

				//If the line has not yet added to global place holder, add to the list
				if (!ignoredLine.contains(lineCounter))
					ignoredLine.add(lineCounter);

				//Adding to stack for validation check
				stack.add(new LineObject(Type.PROGRAM,lineCounter));
				continue;
			}
			//If we are dealing with end of the program, and the line is not in ignored list, we need to process it
			else if (!ignoredLine.contains(lineCounter) && codes[i].trim().equals("end")){
				checkProgram = false;
				//If the line has not yet added to global place holder, add to the list
				if (!ignoredLine.contains(lineCounter))
					ignoredLine.add(lineCounter);

				//If the item on top stack is not start of the program, that means the users code is wrong
				if (stack.isEmpty() || stack.peek().type != Type.PROGRAM) {
					errorList.add(new Error(ErrorType.PROGRAM, lineCounter, codes[i]));
					return;
				}
				stack.pop();
			}
			if (checkProgram){
				if (!programLine.contains(lineCounter) && !ignoredLine.contains(lineCounter)) {
					if (codes[i].trim().endsWith(";"))
						programLine.add(new ProgramLineObject(ProgramLineType.STATEMENT, lineCounter, codes[i].trim(), null));
					else if (codes[i].trim().endsWith("}"))
						programLine.add(new ProgramLineObject(ProgramLineType.LOOPEND, lineCounter, codes[i].trim(), null));
					else if (codes[i].trim().endsWith("{"))
						programLine.add(new ProgramLineObject(ProgramLineType.LOOPSTART, lineCounter, codes[i].trim(), null));
				}
			}
		}

		//If there is any remaining item in the stack, this means the program code are not closed property
		if (!stack.isEmpty()) {

			//Adding all errors to error list, one at a time until the stack is empty.
			while (!stack.isEmpty()) {
				LineObject item = stack.pop();
				if (item.type == Type.PROGRAM) {
					errorList.add(new Error(ErrorType.PROGRAM, item.lineNumber, code[item.lineNumber - 1]));
					return;
				}
				else if (item.type == Type.END) {
					errorList.add(new Error(ErrorType.END, item.lineNumber, code[item.lineNumber - 1]));
					return;
				}
			}
		}
	}


	/*********************************************************************
	 * Function Name:    bracCheck
	 * Description:      This function checks all bracket statement
	 *                   in the code which mark all program line as ignored line so that the interpreter
	 * 					 will not execute the commented line.
	 * Input Parameters: String array
	 * Post Condition:   If the error has been found in the code, error list
	 * 					 will be changed.
	 ***********************************************************************/
	private void bracCheck(String[] codes) {
		int lineCounter = 0;
		for (int i = 0; i < codes.length; i++) {
			lineCounter++;
			String[] lineToken = codes[i].trim().toLowerCase().split(" ");

			//If the line starts with loop, we need to take special care here
			if (!ignoredLine.contains(lineCounter) && lineToken[0].equals("loop")){
				ignoredLine.add(lineCounter);

				//The only permitted loop format is "loop X {"
				if (lineToken.length == 3 && lineToken[2].equals("{")) {

					//If the line has not yet added to global place holder, add to the list
					if (!ignoredLine.contains(lineCounter))
						ignoredLine.add(lineCounter);

					//We push parentheses onto stack
					stack.add(new LineObject(Type.LOOP, lineCounter));
					stack.add(new LineObject(Type.LEFTBRAC, lineCounter));

				}

				//If the format of the loop line is not we are expecting, we added to error list.
				else {
					errorList.add(new Error(ErrorType.LOOP, lineCounter, codes[i]));
					return;
				}
			}

			//If we are dealing with end of the loop, we need to check parentheses
			if (!ignoredLine.contains(lineCounter) && lineToken[0].equals("}")){

				//If the line has not yet added to global place holder, add to the list
				if (!ignoredLine.contains(lineCounter))
					ignoredLine.add(lineCounter);

				//If the last item on stack is not we are looking for, add line to error list
				if (stack.isEmpty() || stack.peek().type != Type.LEFTBRAC) {
					errorList.add(new Error(ErrorType.RIGHTBRAC, lineCounter, codes[i]));
					return;
				}
				stack.pop();
				if (stack.peek().type == Type.LOOP)
					stack.pop();
			}
		}

		//If there is any remaining item in the stack, this means the program code are not closed property
		if (!stack.isEmpty()) {

			//Adding all errors to error list, one at a time until the stack is empty.
			while (!stack.isEmpty()) {
				LineObject item = stack.pop();
				if (item.type == Type.LEFTBRAC) {
					errorList.add(new Error(ErrorType.LEFTBRAC, item.lineNumber, code[item.lineNumber - 1]));
					return;
				}
				else if (item.type == Type.RIGHTBRAC) {
					errorList.add(new Error(ErrorType.RIGHTBRAC, item.lineNumber, code[item.lineNumber - 1]));
					return;
				}
			}
		}
	}

	/*********************************************************************
	 * Function Name:    semiCheck
	 * Description:      This function checks all statement with semi-column
	 *                   in the code so that the interpreter
	 * 					 will not execute the commented line.
	 * Input Parameters: String array
	 * Post Condition:   If the error has been found in the code, error list
	 * 					 will be changed.
	 ***********************************************************************/
	private void semiCheck(String[] codes) {
		int lineCounter = 0;
		for (int i = 0; i < codes.length; i++) {
			lineCounter++;
			String line = codes[i].trim().toLowerCase();

			//If we are dealing with a program line and it is not end with semi-column
			//We add that to error list
			if (!ignoredLine.contains(lineCounter) && !line.endsWith(";") && line.length() != 0) {
				errorList.add(new Error(ErrorType.SEMICOLON, lineCounter, codes[i]));
				return;
			}
		}
	}

	/*********************************************************************
	 * Function Name:    outSideStatementCheck
	 * Description:      This function checks all statement that is out side of
	 * 				     the program statement
	 * Input Parameters: String array
	 * Post Condition:   If the error has been found in the code, error list
	 * 					 will be changed.
	 ***********************************************************************/
	private void outSideStatementCheck(String[] codes) {
		int lineCounter = 0;
		ArrayList<Integer> programLinesExtracted = new ArrayList<>();
		for (ProgramLineObject line : programLine){
			programLinesExtracted.add(line.lineNumber);
		}
		for (int i = 0; i < codes.length; i++) {
			lineCounter++;

			//If the line is not ignored line and the line is not inside program
			//We add it to error list
			if (!ignoredLine.contains(lineCounter) && !programLinesExtracted.contains(lineCounter)) {
				errorList.add(new Error(ErrorType.EXTRASTATEMENT, lineCounter, codes[i]));
				return;
			}
		}
	}


	/*********************************************************************
	 * Function Name:    programLineProcess
	 * Description:      This function preprocess all the program line which involves
	 * 					 looping calculation.
	 * Input Parameters: None
	 * Post Condition:   If the line is loop or loop end, we will do some process to the
	 * 					 program line
	 ***********************************************************************/
	private void programLineProcess() {
		ArrayList<ProgramLineObject> loopLines = new ArrayList<>();
		for (int i = 0; i < programLine.size(); i++){

			//This is the senario which we deal with loop start
			if (programLine.get(i).type == ProgramLineType.LOOPSTART){
				String[] extractedLoopLine = programLine.get(i).contents.split(" ");
				ArrayList<Integer> previousLoops = new ArrayList<>();

				//We create a loop object to keep track of all parent loops.
				programLine.get(i).loopObject = new LoopObject(i, -1, extractedLoopLine[1], extractedLoopLine[1], previousLoops);
				loopLines.add(programLine.get(i));
			}
			else if (programLine.get(i).type == ProgramLineType.LOOPEND){
				if (!loopLines.isEmpty()){

					//We need to assign end index for the last looping object
					loopLines.get(loopLines.size() - 1).loopObject.endIndex = i;

					//If we are at the end of loop, we simply assign loop start index to the object
					programLine.get(i).loopObject = new LoopObject(loopLines.get(loopLines.size() - 1).loopObject.startIndex, i, null, null, null);

					//We add all the parent loop to list so that each loop will know how many parent loop there are
					for (int j = loopLines.size() - 2; j >= 0; j--){
						String[] extractedInfo = loopLines.get(j).contents.split(" ");
						loopLines.get(loopLines.size() - 1).loopObject.previousLoops.add(loopLines.get(j).loopObject.startIndex);
					}

					//If we are done with the current loop, we remove it from parent loop list.
					loopLines.remove(loopLines.size() - 1);
				}
			}
		}
	}

	/*********************************************************************
	 * Function Name:    syntaxCheck
	 * Description:      This function calls all syntax check functions to
	 * 				     ensure all the program line is correct
	 * Input Parameters: None
	 * Output Condition:
	 * 					Boolean: function return true if there is no syntax
	 * 							 error in the input file.
	 ***********************************************************************/
	public Boolean syntaxCheck(){

		//Calling comment check
		commentCheck(code);
		if (!errorList.isEmpty()){
			for (Error e: errorList){
				System.out.println("Error Type is:" + e.errorType);
				System.out.println("The code are " + "\"" + e.contents+ "\"" );
				System.out.println("The line number is: " + e.lineNumber);
			}
			return false;
		}

		//Calling program check
		programCheck(code);
		if (!errorList.isEmpty()){
			for (Error e: errorList){
				System.out.println("Error Type is:" + e.errorType);
				System.out.println("The code are " + "\"" + e.contents+ "\"" );
				System.out.println("The line number is: " + e.lineNumber);
			}
			return false;
		}

		//Calling bracket check
		bracCheck(code);
		if (!errorList.isEmpty()){
			for (Error e: errorList){
				System.out.println("Error Type is:" + e.errorType);
				System.out.println("The code are " + "\"" + e.contents+ "\"" );
				System.out.println("The line number is: " + e.lineNumber);
			}
			return false;
		}

		//Calling semi-column check
		semiCheck(code);
		if (!errorList.isEmpty()){
			for (Error e: errorList){
				System.out.println("Error Type is:" + e.errorType);
				System.out.println("The code are " + "\"" + e.contents+ "\"" );
				System.out.println("The line number is: " + e.lineNumber);
			}
			return false;
		}

		//To check there is outside of program statement
		outSideStatementCheck(code);
		if (!errorList.isEmpty()){
			for (Error e: errorList){
				System.out.println("Error Type is:" + e.errorType);
				System.out.println("The code are " + "\"" + e.contents+ "\"" );
				System.out.println("The line number is: " + e.lineNumber);
			}
			return false;
		}

		//Calling program line process to deal with loop statement
		programLineProcess();
		return true;
	}
}