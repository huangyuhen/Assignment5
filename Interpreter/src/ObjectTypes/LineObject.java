/*****************************************************************************************************
 * File: LineObject.java
 * Course: Data Structures and Algorithms for Engineers
 * Project: A5
 * Author: Evil Genius
 * Versions:
 *	1.0 November 2016
 *
 * Description: This LineObject class include information related to a line.
 *
 * LineObject:
 *              type: type of the line object
 *              lineNumber: line number in actual file
 *
 *****************************************************************************************************/
package ObjectTypes;

import EnumerationTypes.Type;

public class LineObject{
    //type: type of the line object
    public Type type;

    //lineNumber: line number in actual file
    public int lineNumber;

    //Constructure
    public LineObject(Type type, int lineNumber){
        this.type = type;
        this.lineNumber = lineNumber;
    }
}