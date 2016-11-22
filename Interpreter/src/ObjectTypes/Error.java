package ObjectTypes;

import EnumerationTypes.ErrorType;

public class Error{
    public ErrorType errorType;
    public int lineNumber;
    public String contents;
    public Error(ErrorType errorType, int lineNumber, String contents){
        this.errorType = errorType;
        this.lineNumber = lineNumber;
        this.contents = contents;
    }
}