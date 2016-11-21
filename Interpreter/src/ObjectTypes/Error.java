package ObjectTypes;

import EnumerationTypes.ErrorType;

public class Error{
    public ErrorType errorType;
    public int lineNumber;
    public Error(ErrorType errorType, int lineNumber){
        this.errorType = errorType;
        this.lineNumber = lineNumber;
    }
}