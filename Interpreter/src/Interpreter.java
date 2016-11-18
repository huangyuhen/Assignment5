import java.io.*;
import java.lang.*;
import java.util.*;
import java.io.FileReader;


class LineObject{
	Type type;
	int lineNumber;
	public LineObject(Type type, int lineNumber){
		this.type = type;
		this.lineNumber = lineNumber;
	}
}

class ProgramLineObject{
	ProgramLineType type;
	int lineNumber;
	String contents;
	public ProgramLineObject(ProgramLineType type, int lineNumber, String contents){
		this.type = type;
		this.lineNumber = lineNumber;
		this.contents = contents;
	}
}

enum ProgramLineType
{
	LOOPSTART, LOOPEND, STATEMENT
}

enum Type
{
	PROGRAM, END, LEFTCOMMENT, RIGHTCOMMENT, LEFTBRAC, RIGHTBRAC, LOOP
}

enum ErrorType
{
	SEMICOLON, PROGRAM, END, LEFTCOMMENT, RIGHTCOMMENT, LEFTBRAC, RIGHTBRAC, LOOP, EXTRASTATEMENT
}

class Error{
	ErrorType errorType;
	int lineNumber;
	public Error(ErrorType errorType, int lineNumber){
		this.errorType = errorType;
		this.lineNumber = lineNumber;
	}
}

public class Interpreter {
	private String[] code = null;
	private ArrayList<Error> errorList = new ArrayList<>();
	private ArrayList<Integer> ignoredLine = new ArrayList<>();
	private ArrayList<ProgramLineObject> programLine = new ArrayList<>();
	private Stack<LineObject> stack = new Stack<>();
	private String[] contentsPreProcessing(String contents){
		code = contents.split("\n");
		/*replaceAll("program","(").replaceAll("end",")")
				.replaceAll("/", Matcher.quoteReplacement("[")).replaceAll("\\*","#")
				.replaceAll("#\\[","]").replaceAll("\\[#","[").split("\n");*/
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
					errorList.add(new Error(ErrorType.RIGHTCOMMENT, lineCounter));
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
					errorList.add(new Error(ErrorType.LEFTCOMMENT, item.lineNumber));
					return;
				}
				else if (item.type == Type.RIGHTCOMMENT) {
					errorList.add(new Error(ErrorType.RIGHTCOMMENT, item.lineNumber));
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
					errorList.add(new Error(ErrorType.PROGRAM, lineCounter));
					return;
				}
				stack.pop();
			}
			if (checkProgram){
				if (!programLine.contains(lineCounter) && !ignoredLine.contains(lineCounter)) {
					if (codes[i].trim().endsWith(";"))
						programLine.add(new ProgramLineObject(ProgramLineType.STATEMENT, lineCounter, codes[i].trim()));
					else if (codes[i].trim().endsWith("}"))
						programLine.add(new ProgramLineObject(ProgramLineType.LOOPEND, lineCounter, codes[i].trim()));
					else if (codes[i].trim().endsWith("{"))
						programLine.add(new ProgramLineObject(ProgramLineType.LOOPSTART, lineCounter, codes[i].trim()));
				}
			}
		}
		if (!stack.isEmpty()) {
			while (!stack.isEmpty()) {
				LineObject item = stack.pop();
				if (item.type == Type.PROGRAM) {
					errorList.add(new Error(ErrorType.PROGRAM, item.lineNumber));
					return;
				}
				else if (item.type == Type.END) {
					errorList.add(new Error(ErrorType.END, item.lineNumber));
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
//			if (!ignoredLine.contains(lineCounter) && lineToken[0].equals("{")){
//				ignoredLine.add(lineCounter);
//				if (stack.isEmpty() || stack.peek().type != Type.LOOP) {
//					errorList.add(new Error(ErrorType.LOOP, lineCounter));
//					return;
//				}
//				stack.add(new LineObject(Type.LEFTBRAC, lineCounter));
//			}
			if (!ignoredLine.contains(lineCounter) && lineToken[0].equals("loop")){
				ignoredLine.add(lineCounter);
				if (lineToken.length == 3 && lineToken[2].equals("{")) {
					if (!ignoredLine.contains(lineCounter))
						ignoredLine.add(lineCounter);
					stack.add(new LineObject(Type.LOOP, lineCounter));
					stack.add(new LineObject(Type.LEFTBRAC, lineCounter));
					//programLine.remove(programLine.indexOf(lineCounter));
				}
				else if (lineToken.length == 2 && lineToken[1].matches("\\d+")) {
					ignoredLine.add(lineCounter);
					if (!ignoredLine.contains(lineCounter))
						ignoredLine.add(lineCounter);
					stack.add(new LineObject(Type.LOOP, lineCounter));
					//programLine.remove(programLine.indexOf(lineCounter));
					//ignoredLine.add(lineCounter);
				}
				else {
					errorList.add(new Error(ErrorType.LOOP, lineCounter));
					return;
				}
			}
			if (!ignoredLine.contains(lineCounter) && lineToken[0].equals("}")){
				if (!ignoredLine.contains(lineCounter))
					ignoredLine.add(lineCounter);
				if (stack.isEmpty() || stack.peek().type != Type.LEFTBRAC) {
					errorList.add(new Error(ErrorType.RIGHTBRAC, lineCounter));
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
					errorList.add(new Error(ErrorType.LEFTBRAC, item.lineNumber));
					return;
				}
				else if (item.type == Type.RIGHTBRAC) {
					errorList.add(new Error(ErrorType.RIGHTBRAC, item.lineNumber));
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
				errorList.add(new Error(ErrorType.SEMICOLON, lineCounter));
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
				errorList.add(new Error(ErrorType.EXTRASTATEMENT, lineCounter));
				return;
			}
		}
	}

	private void syntaxCheck(){
		commentCheck(code);
		if (!errorList.isEmpty()){
			for (Error e: errorList){
				System.out.println("Error Type is:" + e.errorType + " and the line number is: " + e.lineNumber);
			}
			return;
		}
		programCheck(code);
		if (!errorList.isEmpty()){
			for (Error e: errorList){
				System.out.println("Error Type is:" + e.errorType + " and the line number is: " + e.lineNumber);
			}
			return;
		}
		bracCheck(code);
		if (!errorList.isEmpty()){
			for (Error e: errorList){
				System.out.println("Error Type is:" + e.errorType + " and the line number is: " + e.lineNumber);
			}
			return;
		}
		semiCheck(code);
		if (!errorList.isEmpty()){
			for (Error e: errorList){
				System.out.println("Error Type is:" + e.errorType + " and the line number is: " + e.lineNumber);
			}
			return;
		}
		outSideStatementCheck(code);
		if (!errorList.isEmpty()){
			for (Error e: errorList){
				System.out.println("Error Type is:" + e.errorType + " and the line number is: " + e.lineNumber);
			}
			return;
		}
	}
	private String filePath(){
		//Scanner reader = new Scanner(System.in);
		//System.out.println("Please enter the full file path with extension name");
		String filePath = "/Users/yuhenghuang/GitHub/Assignment5/Interpreter/src/Test.txt";//reader.nextLine();
		File file = new File(filePath);
		if (!file.exists()){
			System.out.println("Please enter a existing file.");
			return filePath();
		}
		return filePath;
	}

	private String readFile(String path) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line.toLowerCase().trim());
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			br.close();
			return sb.toString();
		} catch (IOException e){
			System.out.println("Empty file name, start again");
		}
		return null;
	}
	public static void main(String[] args){
		Interpreter i = new Interpreter();
		i.contentsPreProcessing(i.readFile(i.filePath()));
		i.syntaxCheck();
		for (ProgramLineObject line: i.programLine){
			System.out.println(line.contents);
		}
	}
}