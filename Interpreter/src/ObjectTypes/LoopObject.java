/*****************************************************************************************************
 * File: LoopObject.java
 * Course: Data Structures and Algorithms for Engineers
 * Project: A5
 * Author: Evil Genius
 * Versions:
 *	1.0 November 2016
 *
 * Description: This LoopObject class include information related to loop inside the VSA formatted
 *              file.
 *
 * Error:
 *              startIndex: start index of the loop
 *              endIndex: end index of the loop
 *              numOfExcution: number of execution in string format
 *              originalNumOfExcution: initial value of number of execution in string format
 *              numOfExcutionInNum: number of execution in integer
 *              originalNumOfExcutionInNum: original number of execution in integer
 *              previousLoopInNum: the parent loop of number of execution
 *              previousLoops: A list that contains all the parent loops.
 *****************************************************************************************************/
package ObjectTypes;

import java.util.ArrayList;

public class LoopObject{

    //startIndex: start index of the loop
    public int startIndex;

    //endIndex: end index of the loop
    public int endIndex;

    //numOfExcution: number of execution in string format
    public String numOfExcution;

    //originalNumOfExcution: initial value of number of execution in string format
    public String originalNumOfExcution;

    //numOfExcutionInNum: number of execution in integer
    public int numOfExcutionInNum;

    //originalNumOfExcutionInNum: original number of execution in integer
    public int originalNumOfExcutionInNum;

    //previousLoopInNum: the parent loop of number of execution
    public int previousLoopInNum;

    //previousLoops: A list that contains all the parent loops.
    public ArrayList<Integer> previousLoops;

    //Constructuror
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
