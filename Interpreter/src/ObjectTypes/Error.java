/*****************************************************************************************************
 * File: Error.java
 * Course: Data Structures and Algorithms for Engineers
 * Project: A5
 * Author: Evil Genius
 *         (Yuheng Huang, Tianbing Leng, Xin Huang)
 * Versions:
 *	1.0 November 2016
 *
 * Description: This Error class include information related to an error that will be reported to
 *              user.
 *
 * Error:
 *              errorType: Error tyoe
 *              lineNumber: Actual line number in input file
 *              contents: The code in program line
 *
 *****************************************************************************************************/

package ObjectTypes;

import EnumerationTypes.ErrorType;

public class Error{

    //errorType: Error tyoe
    public ErrorType errorType;

    //lineNumber: Actual line number in input file
    public int lineNumber;

    //contents: The code in program line
    public String contents;

    //Constructure for error object
    public Error(ErrorType errorType, int lineNumber, String contents){
        this.errorType = errorType;
        this.lineNumber = lineNumber;
        this.contents = contents;
    }
}