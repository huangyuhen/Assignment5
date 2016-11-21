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

class LoopObject{
	int startIndex;
	int endIndex;
	String numOfExcution;
	String originalNumOfExcution;
	int numOfExcutionInNum;
	int originalNumOfExcutionInNum;
	int previousLoopInNum;
	ArrayList<Integer> previousLoops;
	public LoopObject(int startIndex, int endIndex, String numOfExcution, String originalNumOfExcution, ArrayList<Integer> previousLoops){
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.numOfExcution = numOfExcution;
		this.originalNumOfExcution = originalNumOfExcution;
		this.previousLoops = previousLoops;
		this.numOfExcutionInNum = -1;
		this.originalNumOfExcutionInNum = -1;
		this.previousLoopInNum = -1;
	}


}
class ProgramLineObject{
	ProgramLineType type;
	int lineNumber;
	String contents;
	LoopObject loopObject;
	public ProgramLineObject(ProgramLineType type, int lineNumber, String contents, LoopObject loopObject){
		this.type = type;
		this.lineNumber = lineNumber;
		this.contents = contents;
		this.loopObject = loopObject;
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

enum ValueType {
	IntegerType, FloatType
}

enum ErrorType {
	SEMICOLON, PROGRAM, END, LEFTCOMMENT, RIGHTCOMMENT, LEFTBRAC, RIGHTBRAC, LOOP, EXTRASTATEMENT, STATEMENT_ERROR, STATEMENT_INVALID_ASSIGNMENT_ERROR, UNDECLARED_ERROR, FLOAT_NUMBER, VARIABLE_NAME, INVALID_CHAR
}

class Error{
	ErrorType errorType;
	int lineNumber;
	public Error(ErrorType errorType, int lineNumber){
		this.errorType = errorType;
		this.lineNumber = lineNumber;
	}
}

public class SyntaxCheckADT {
	private String[] code = null;
	private ArrayList<Error> errorList = new ArrayList<>();
	private ArrayList<Integer> ignoredLine = new ArrayList<>();
	private ArrayList<ProgramLineObject> programLine = new ArrayList<>();
	private Stack<LineObject> stack = new Stack<>();
	private String[] contentsPreProcessing(String contents){
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

	private void programLineProcess() {
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
//		for (ProgramLineObject line : programLine){
//			if (line.type == ProgramLineType.LOOPSTART){
//				System.out.println("Start line number "  + programLine.get(line.loopObject.startIndex).lineNumber);
//				System.out.println("End line number " + programLine.get(line.loopObject.endIndex).lineNumber);
//				System.out.println("numOfExcution " + line.loopObject.numOfExcution);
//				System.out.println("originalNumOfExcution " + line.loopObject.originalNumOfExcution);
//				for (String s: line.loopObject.previousLoops){
//					System.out.println("loops " + s);
//				}
//			}
//			if (line.type == ProgramLineType.LOOPEND){
//				System.out.println(line.loopObject.startIndex);
//			}
//		}
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
		SyntaxCheckADT i = new SyntaxCheckADT();
		i.contentsPreProcessing(i.readFile(i.filePath()));
		i.syntaxCheck();
		i.programLineProcess();
		Assignment5 a = new Assignment5(i.programLine);
		a.executeLines();
	}
}