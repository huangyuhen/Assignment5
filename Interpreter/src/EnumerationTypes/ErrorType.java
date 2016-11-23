/*****************************************************************************************************
 * File: ErrorType.java
 * Course: Data Structures and Algorithms for Engineers
 * Project: A5
 * Author: Evil Genius
 *         (Yuheng Huang, Tianbing Leng, Xin Huang)
 * Versions:
 *	1.0 November 2016
 *
 * Description: This ErrorType defines type of error during the program execution.
 *
 * ErrorType:           SEMICOLON,
 *                      PROGRAM,
 *                      END,
 *                      LEFTCOMMENT,
 *                      RIGHTCOMMENT,
 *                      LEFTBRAC,
 *                      RIGHTBRAC,
 *                      LOOP,
 *                      EXTRASTATEMENT,
 *                      STATEMENT_ERROR,
 *                      STATEMENT_INVALID_ASSIGNMENT_ERROR,
 *                      UNDECLARED_ERROR,
 *                      FLOAT_NUMBER,
 *                      VARIABLE_NAME,
 *                      INVALID_CHAR
 *
 *****************************************************************************************************/

package EnumerationTypes;

public enum ErrorType {
    SEMICOLON,
    PROGRAM,
    END,
    LEFTCOMMENT,
    RIGHTCOMMENT,
    LEFTBRAC,
    RIGHTBRAC,
    LOOP,
    EXTRASTATEMENT,
    STATEMENT_ERROR,
    STATEMENT_INVALID_ASSIGNMENT_ERROR,
    UNDECLARED_ERROR,
    FLOAT_NUMBER,
    VARIABLE_NAME,
    INVALID_CHAR
}