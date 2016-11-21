import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class VSA {
    public static String readFile() {
        System.out.println("Please enter the full file path with extension name");
        Scanner reader = new Scanner(System.in);
        String path = reader.nextLine();
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

    public static void main(String[] args){
        SyntaxCheckADT i = new SyntaxCheckADT();
        String filePath = readFile();
        i.contentsPreProcessing(filePath);
        i.syntaxCheck();
        i.programLineProcess();
        Execution a = new Execution(i.programLine);
        a.executeLines();
    }
}
