package ObjectTypes;

import EnumerationTypes.Type;

public class LineObject{
    public Type type;
    public int lineNumber;
    public LineObject(Type type, int lineNumber){
        this.type = type;
        this.lineNumber = lineNumber;
    }
}