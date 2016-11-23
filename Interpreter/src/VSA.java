/*****************************************************************************************************
 * File: VSA.java
 * Course: Data Structures and Algorithms for Engineers
 * Project: A5
 * Author: Evil Genius
 *         (Yuheng Huang, Tianbing Leng, Xin Huang)
 * Versions:
 *	1.0 November 2016
 *
 * Description: This java file is the main class for VSA program interpreter
 *
 *
 * How to use the class:
 *                  Open terminal in your laptop
 *                  cd to the directory that you have this java file
 *                  Type in terminal:
 *                          javac VSA.java
 *                  Type in terminal:
 *                          java VSA [file path]
 *                  Notice that the file path should be the program you will execute.
 *
 Public Functions:   String readFile();
                     void executionOfProgram(String[] args);
                     String argumentCheck(String[] args)
 *****************************************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class VSA {

    /*********************************************************************
     * Function Name:    readFile
     * Description:      This function read the input file into a buffer array
     *                   and return the entire buffer string back to caller.
     * Input Parameters: String path
     * Output:           Program codes string
     ***********************************************************************/
    public static String readFile(String path) {
        File file = new File(path);

        //If the file doesn't exists, we print the error statement to user
        if (!file.exists()){
            System.out.println("Please enter a existing file.");
            System.exit(0);
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();

            //Read single line
            String line = br.readLine();

            //Adding each line to string builder buffer
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

    /*********************************************************************
     * Function Name:    executionOfProgram
     * Description:      This function execute the program file input form user
     * Input Parameters: String path
     * Output:           On terminal comment
     ***********************************************************************/
    public static void executionOfProgram(String filePath){
        SyntaxCheckADT syntaxCheckADT = new SyntaxCheckADT();

        //Read the file to a string buffer
        String fileContents = readFile(filePath);

        //Calling contents pre processing method
        syntaxCheckADT.contentsPreProcessing(fileContents);

        //The syntax check return true if there is no error in program
        boolean syntaxCheck = syntaxCheckADT.syntaxCheck();

        //The execution will not start unless there is no syntax error
        if (syntaxCheck) {
            Execution a = new Execution(syntaxCheckADT.programLine);
            a.executeLines();
        }
        else
            System.exit(0);
    }


    /*********************************************************************
     * Function Name:    argumentCheck
     * Description:      This function gets user input from terminal commend
     * Input Parameters: String args
     * Output:           On terminal comment
     ***********************************************************************/
    public static String argumentCheck(String[] args){

        //If there are more than 1 commend from user, print the error
        if (args.length > 2) {
            System.out.println("Too many arguments in commend line, please only provide file path");
            System.exit(0);
        }
        else if (args.length < 1) {
            System.out.println("Please provide a file path");
            System.exit(0);
        }
        else
            return args[0];
        return null;
    }

    /*********************************************************************
     * Function Name:    main
     * Description:      This function is the main class for VSA interpreter
     * Input Parameters: String args
     * Output:           On terminal comment
     ***********************************************************************/
    public static void main(String[] args){
        String filePath = argumentCheck(args);

        //If the file path is passed check, then we are executing the program
        if (filePath != null)
            executionOfProgram(filePath);
        else{
            System.out.println("Invalid file path");
        }
    }
}
