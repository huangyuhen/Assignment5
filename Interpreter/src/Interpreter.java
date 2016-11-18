import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.regex.Matcher;
import java.io.FileReader;


class LineObject{
	Type type;
	int lineNumber;
	public LineObject(Type type, int lineNumber){
		this.type = type;
		this.lineNumber = lineNumber;
	}
}

enum Type
{
	PROGRAM, END, LEFTCOMMENT, RIGHTCOMMENT, LEFTBRAC, RIGHTBRAC, LOOP
}

enum ErrorType
{
	SEMICOLON, PROGRAM, END, LEFTCOMMENT, RIGHTCOMMENT, LEFTBRAC, RIGHTBRAC, LOOP
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
	private ArrayList<Integer> programLine = new ArrayList<>();
	private Deque<LineObject> stack = new ArrayDeque();
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
					break;
				}
				stack.pop();
			}
			if (checkIncomment){
				if (!ignoredLine.contains(lineCounter))
					ignoredLine.add(lineCounter);
			}
		}
		if (!stack.isEmpty()) {
			while (!stack.isEmpty()) {
				LineObject item = stack.pop();
				if (item.type == Type.LEFTCOMMENT)
					errorList.add(new Error(ErrorType.LEFTCOMMENT, item.lineNumber));
				else if (item.type == Type.RIGHTCOMMENT)
					errorList.add(new Error(ErrorType.RIGHTCOMMENT, item.lineNumber));
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
				if (!programLine.contains(lineCounter))
					programLine.add(lineCounter);
				stack.add(new LineObject(Type.PROGRAM,lineCounter));
			}
			if (!ignoredLine.contains(lineCounter) && codes[i].trim().equals("end")){
				checkProgram = false;
				if (!ignoredLine.contains(lineCounter))
					ignoredLine.add(lineCounter);
				if (stack.isEmpty() || stack.peek().type != Type.PROGRAM) {
					errorList.add(new Error(ErrorType.PROGRAM, lineCounter));
					break;
				}
				stack.pop();
			}
			if (checkProgram){
				if (!programLine.contains(lineCounter))
					programLine.add(lineCounter);
			}
		}
		if (!stack.isEmpty()) {
			while (!stack.isEmpty()) {
				LineObject item = stack.pop();
				if (item.type == Type.PROGRAM)
					errorList.add(new Error(ErrorType.PROGRAM, item.lineNumber));
				else if (item.type == Type.END)
					errorList.add(new Error(ErrorType.END, item.lineNumber));
			}
		}
	}

	private void bracCheck(String[] codes) {
		int lineCounter = 0;
		for (int i = 0; i < codes.length; i++) {
			lineCounter++;
			String[] lineToken = codes[i].trim().toLowerCase().split(" ");
			if (!ignoredLine.contains(lineCounter) && lineToken[0].equals("{")){
				if (stack.peek().type != Type.LOOP)
					errorList.add(new Error(ErrorType.LOOP, lineCounter));
				stack.add(new LineObject(Type.LEFTBRAC, lineCounter));
			}
			if (!ignoredLine.contains(lineCounter) && lineToken[0].equals("loop")){
				if (lineToken.length == 3 && lineToken[2].equals("{")) {
					if (!ignoredLine.contains(lineCounter))
						ignoredLine.add(lineCounter);
					stack.add(new LineObject(Type.LOOP, lineCounter));
					stack.add(new LineObject(Type.LEFTBRAC, lineCounter));
				}
				else{
					errorList.add(new Error(ErrorType.LEFTBRAC, lineCounter));
				}
			}
			if (!ignoredLine.contains(lineCounter) && lineToken[0].equals("}")){
				if (!ignoredLine.contains(lineCounter))
					ignoredLine.add(lineCounter);
				if (stack.isEmpty() || stack.peek().type != Type.LEFTBRAC) {
					errorList.add(new Error(ErrorType.RIGHTBRAC, lineCounter));
					break;
				}
				stack.pop();
				if (stack.peek().type == Type.LOOP)
					stack.pop();
			}
		}
		if (!stack.isEmpty()) {
			while (!stack.isEmpty()) {
				LineObject item = stack.pop();
				if (item.type == Type.LEFTBRAC)
					errorList.add(new Error(ErrorType.LEFTBRAC, item.lineNumber));
				else if (item.type == Type.RIGHTBRAC)
					errorList.add(new Error(ErrorType.RIGHTBRAC, item.lineNumber));
			}
		}
	}
	/*private void errorCheck(String[] codes) {
		Deque<LineObject> stack = new ArrayDeque();
		ArrayList<Integer> ignoredLine = new ArrayList<>();
		int lineCounter = 0;
		boolean checkIncomment = false;
		for (String s : codes) {
			lineCounter++;
			System.out.println(lineCounter);
			char[] charArray = s.trim().toCharArray();
			for (char c : charArray) {
				if (checkIncomment && !ignoredLine.contains(lineCounter)) {
					ignoredLine.add(lineCounter);
				}
				if (c == '(' && !checkIncomment) {
					stack.push(new LineObject(c, lineCounter));
					if (!ignoredLine.contains(lineCounter))
						ignoredLine.add(lineCounter);
					continue;
				}
				if (c == '[') {
					stack.push(new LineObject(c, lineCounter));
					if (!ignoredLine.contains(lineCounter))
						ignoredLine.add(lineCounter);
					checkIncomment = true;
					continue;
				}
				if (c == '{' && !checkIncomment) {
					stack.push(new LineObject(c, lineCounter));
					if (!ignoredLine.contains(lineCounter))
						ignoredLine.add(lineCounter);
					continue;
				}
				if (c == ')' && !checkIncomment) {
					if (stack.isEmpty() || stack.peek().character != '(')
						errorList.add(new Error(ErrorType.END, lineCounter));
					if (!ignoredLine.contains(lineCounter))
						ignoredLine.add(lineCounter);
					if (stack.isEmpty())
						continue;
					stack.pop();
					continue;

				}
				if (c == ']') {
					if (stack.isEmpty() || stack.peek().character != '[')
						errorList.add(new Error(ErrorType.RIGHTCOMMENT, lineCounter));
					if (!ignoredLine.contains(lineCounter))
						ignoredLine.add(lineCounter);
					checkIncomment = false;
					if (stack.isEmpty())
						continue;
					stack.pop();
					continue;
				}
				if (c == '}' && !checkIncomment) {
					if (stack.isEmpty() || stack.peek().character != '{')
						errorList.add(new Error(ErrorType.RIGHTBRAC, lineCounter));
					if (!ignoredLine.contains(lineCounter))
						ignoredLine.add(lineCounter);
					if (stack.isEmpty())
						continue;
					stack.pop();
					continue;
				}
			}
			if (!ignoredLine.contains(lineCounter)) {
				if (charArray[charArray.length - 1] != ';')
					errorList.add(new Error(ErrorType.SEMICOLON, lineCounter));
			}
		}
		if (!stack.isEmpty()) {
			while (!stack.isEmpty()) {
				LineObject item = stack.pop();
				if (item.character == ';')
					errorList.add(new Error(ErrorType.SEMICOLON, item.lineNumber));
				else if (item.character == '(')
					errorList.add(new Error(ErrorType.PROGRAM, item.lineNumber));
				else if (item.character == ')')
					errorList.add(new Error(ErrorType.END, item.lineNumber));
				else if (item.character == '{')
					errorList.add(new Error(ErrorType.LEFTBRAC, item.lineNumber));
				else if (item.character == '}')
					errorList.add(new Error(ErrorType.RIGHTBRAC, item.lineNumber));
				else if (item.character == '[')
					errorList.add(new Error(ErrorType.LEFTCOMMENT, item.lineNumber));
				else if (item.character == ']')
					errorList.add(new Error(ErrorType.RIGHTCOMMENT, item.lineNumber));
			}
		}
		Collections.sort(errorList, new Comparator<Error>() {
			@Override
			public int compare(Error o1, Error o2) {
				return o1.lineNumber - o2.lineNumber;
			}
		});
	}*/

	private String filePath(){
		Scanner reader = new Scanner(System.in);
		System.out.println("Please enter the full file path with extension name");
		String filePath = reader.nextLine();
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
				sb.append(line);
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
		i.programCheck(i.contentsPreProcessing(i.readFile(i.filePath())));
		for (Error e: i.errorList){
			System.out.println("Error Type is:" + e.errorType + " and the line number is: " + e.lineNumber);
		}
	}
}