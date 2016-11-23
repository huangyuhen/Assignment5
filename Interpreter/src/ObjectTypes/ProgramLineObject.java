/*****************************************************************************************************
 * File: ProgramLineObject.java
 * Course: Data Structures and Algorithms for Engineers
 * Project: A5
 * Author: Evil Genius
 *         (Yuheng Huang, Tianbing Leng, Xin Huang)
 * Versions:
 *	1.0 November 2016
 *
 * Description: This LoopObject class include information related to loop inside the VSA formatted
 *              file.
 *
 * Error:
 *              type: type of the program line
 *              lineNumber: line number in actual text file
 *              contents: the actual program line contents
 *              loopObject: If it is an loop line, a loop object will be assigned
 *****************************************************************************************************/
package ObjectTypes;

import EnumerationTypes.ProgramLineType;
import ObjectTypes.LoopObject;

public class ProgramLineObject{

    //type: type of the program line
    public ProgramLineType type;

    //lineNumber: line number in actual text file
    public int lineNumber;

    //contents: the actual program line contents
    public String contents;

    //loopObject: If it is an loop line, a loop object will be assigned
    public LoopObject loopObject;

    //Actual constructure
    public ProgramLineObject(ProgramLineType type, int lineNumber, String contents, LoopObject loopObject){
        this.type = type;
        this.lineNumber = lineNumber;
        this.contents = contents;
        this.loopObject = loopObject;
    }
}