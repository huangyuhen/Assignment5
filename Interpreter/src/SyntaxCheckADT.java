import EnumerationTypes.ErrorType;
import EnumerationTypes.ProgramLineType;
import EnumerationTypes.Type;
import ObjectTypes.Error;
import ObjectTypes.LineObject;
import ObjectTypes.LoopObject;
import ObjectTypes.ProgramLineObject;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.io.FileReader;


public class SyntaxCheckADT {
	private String[] code = null;
	private ArrayList<Error> errorList = new ArrayList<>();
	private ArrayList<Integer> ignoredLine = new ArrayList<>();
	public ArrayList<ProgramLineObject> programLine = new ArrayList<>();
	private Stack<LineObject> stack = new Stack<>();

	public String[] contentsPreProcessing(String contents){
		code = contents.split("\n");
		return code;
	}

	private void commentCheck(String[] codes) {
		int lineCounter = 0;
		boolean checkIncomment = false;
		for (int i = 0; i < codes.length; i++) {
			lineCounter++;
			if (codes[i].trim().startsWith("/*") && !checkIncomment) {
				if (!ignoredLine.contains(lineCounter))
					ignoredLine.add(lineCounter);
				checkIncomment = true;
				stack.add(new LineObject(Type.LEFTCOMMENT,lineCounter));
			}
			if (codes[i].trim().endsWith("*/") && checkIncomment) {
				if (!ignoredLine.contains(lineCounter))
					ignoredLine.add(lineCounter);
				checkIncomment = false;
				if (stack.isEmpty() || stack.peek().type != Type.LEFTCOMMENT) {
					errorList.add(new Error(ErrorType.RIGHTCOMMENT, lineCounter, code[i]));
					return;
				}
				stack.pop();
			}
			if (checkIncomment || codes[i].trim().length() == 0){
				if (!ignoredLine.contains(lineCounter))
					ignoredLine.add(lineCounter);
			}
		}
		if (!stack.isEmpty()) {
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

	private void programCheck(String[] codes) {
		int lineCounter = 0;
		boolean checkProgram = false;
		for (int i = 0; i < codes.length; i++) {
			lineCounter++;
			if (!ignoredLine.contains(lineCounter) && codes[i].trim().equals("program")){
				checkProgram = true;
				if (!ignoredLine.contains(lineCounter))
					ignoredLine.add(lineCounter);
				stack.add(new LineObject(Type.PROGRAM,lineCounter));
				continue;
			}
			else if (!ignoredLine.contains(lineCounter) && codes[i].trim().equals("end")){
				checkProgram = false;
				if (!ignoredLine.contains(lineCounter))
					ignoredLine.add(lineCounter);
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
		if (!stack.isEmpty()) {
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

	private void bracCheck(String[] codes) {
		int lineCounter = 0;
		for (int i = 0; i < codes.length; i++) {
			lineCounter++;
			String[] lineToken = codes[i].trim().toLowerCase().split(" ");

			if (!ignoredLine.contains(lineCounter) && lineToken[0].equals("loop")){
				ignoredLine.add(lineCounter);
				if (lineToken.length == 3 && lineToken[2].equals("{")) {
					if (!ignoredLine.contains(lineCounter))
						ignoredLine.add(lineCounter);
					stack.add(new LineObject(Type.LOOP, lineCounter));
					stack.add(new LineObject(Type.LEFTBRAC, lineCounter));

				}
				else if (lineToken.length == 2 && lineToken[1].matches("\\d+")) {
					ignoredLine.add(lineCounter);
					if (!ignoredLine.contains(lineCounter))
						ignoredLine.add(lineCounter);
					stack.add(new LineObject(Type.LOOP, lineCounter));

				}
				else {
					errorList.add(new Error(ErrorType.LOOP, lineCounter, codes[i]));
					return;
				}
			}
			if (!ignoredLine.contains(lineCounter) && lineToken[0].equals("}")){
				if (!ignoredLine.contains(lineCounter))
					ignoredLine.add(lineCounter);
				if (stack.isEmpty() || stack.peek().type != Type.LEFTBRAC) {
					errorList.add(new Error(ErrorType.RIGHTBRAC, lineCounter, codes[i]));
					return;
				}
				stack.pop();
				if (stack.peek().type == Type.LOOP)
					stack.pop();
			}
		}
		if (!stack.isEmpty()) {
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
	private void semiCheck(String[] codes) {
		int lineCounter = 0;
		for (int i = 0; i < codes.length; i++) {
			lineCounter++;
			String line = codes[i].trim().toLowerCase();
			if (!ignoredLine.contains(lineCounter) && !line.endsWith(";") && line.length() != 0) {
				errorList.add(new Error(ErrorType.SEMICOLON, lineCounter, codes[i]));
				return;
			}
		}
	}

	private void outSideStatementCheck(String[] codes) {
		int lineCounter = 0;
		ArrayList<Integer> programLinesExtracted = new ArrayList<>();
		for (ProgramLineObject line : programLine){
			programLinesExtracted.add(line.lineNumber);
		}
		for (int i = 0; i < codes.length; i++) {
			lineCounter++;
			String line = codes[i].trim().toLowerCase();
			if (!ignoredLine.contains(lineCounter) && !programLinesExtracted.contains(lineCounter)) {
				errorList.add(new Error(ErrorType.EXTRASTATEMENT, lineCounter, codes[i]));
				return;
			}
		}
	}

	public void programLineProcess() {
		int lineCounter = 0;
		ArrayList<ProgramLineObject> loopLines = new ArrayList<>();
		for (int i = 0; i < programLine.size(); i++){
			if (programLine.get(i).type == ProgramLineType.LOOPSTART){
				String[] extractedLoopLine = programLine.get(i).contents.split(" ");
				ArrayList<Integer> previousLoops = new ArrayList<>();
				//previousLoops.add("1");
				programLine.get(i).loopObject = new LoopObject(i, -1, extractedLoopLine[1], extractedLoopLine[1], previousLoops);
				loopLines.add(programLine.get(i));
			}
			else if (programLine.get(i).type == ProgramLineType.LOOPEND){
				if (!loopLines.isEmpty()){
					loopLines.get(loopLines.size() - 1).loopObject.endIndex = i;
					programLine.get(i).loopObject = new LoopObject(loopLines.get(loopLines.size() - 1).loopObject.startIndex, i, null, null, null);
					for (int j = loopLines.size() - 2; j >= 0; j--){
						String[] extractedInfo = loopLines.get(j).contents.split(" ");
						loopLines.get(loopLines.size() - 1).loopObject.previousLoops.add(loopLines.get(j).loopObject.startIndex);
					}
					loopLines.remove(loopLines.size() - 1);
				}
			}
		}
	}

	public Boolean syntaxCheck(){
		commentCheck(code);
		if (!errorList.isEmpty()){
			for (Error e: errorList){
				System.out.println("Error Type is:" + e.errorType);
				System.out.println("The code are " + "\"" + e.contents+ "\"" );
				System.out.println("The line number is: " + e.lineNumber);
			}
			return false;
		}
		programCheck(code);
		if (!errorList.isEmpty()){
			for (Error e: errorList){
				System.out.println("Error Type is:" + e.errorType);
				System.out.println("The code are " + "\"" + e.contents+ "\"" );
				System.out.println("The line number is: " + e.lineNumber);
			}
			return false;
		}
		bracCheck(code);
		if (!errorList.isEmpty()){
			for (Error e: errorList){
				System.out.println("Error Type is:" + e.errorType);
				System.out.println("The code are " + "\"" + e.contents+ "\"" );
				System.out.println("The line number is: " + e.lineNumber);
			}
			return false;
		}
		semiCheck(code);
		if (!errorList.isEmpty()){
			for (Error e: errorList){
				System.out.println("Error Type is:" + e.errorType);
				System.out.println("The code are " + "\"" + e.contents+ "\"" );
				System.out.println("The line number is: " + e.lineNumber);
			}
			return false;
		}
		outSideStatementCheck(code);
		if (!errorList.isEmpty()){
			for (Error e: errorList){
				System.out.println("Error Type is:" + e.errorType);
				System.out.println("The code are " + "\"" + e.contents+ "\"" );
				System.out.println("The line number is: " + e.lineNumber);
			}
			return false;
		}
		programLineProcess();
		return true;
	}
}