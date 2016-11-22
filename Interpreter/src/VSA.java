import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class VSA {
    public static String readFile(String path) {
        File file = new File(path);
        if (!file.exists()){
            System.out.println("Please enter a existing file.");
            System.exit(0);
        }

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

    public static void executionOfProgram(String filePath){
        SyntaxCheckADT syntaxCheckADT = new SyntaxCheckADT();
        String fileContents = readFile(filePath);
        syntaxCheckADT.contentsPreProcessing(fileContents);
        boolean syntaxCheck = syntaxCheckADT.syntaxCheck();
        if (syntaxCheck) {
            Execution a = new Execution(syntaxCheckADT.programLine);
            a.executeLines();
        }
        else
            System.exit(0);
    }

    public static String argumentCheck(String[] args){
        if (args.length > 2) {
            System.out.println("Too many arguments");
            System.exit(0);
        }
        else if (args.length < 1) {
            System.out.println("Too few arguments");
            System.exit(0);
        }
        else
            return args[0];
        return null;
    }
    public static void main(String[] args){
        String filePath = argumentCheck(args);
        if (filePath != null)
            executionOfProgram(filePath);
    }
}
