package ObjectTypes;

import EnumerationTypes.ProgramLineType;
import ObjectTypes.LoopObject;

public class ProgramLineObject{
    public ProgramLineType type;
    public int lineNumber;
    public String contents;
    public LoopObject loopObject;
    public ProgramLineObject(ProgramLineType type, int lineNumber, String contents, LoopObject loopObject){
        this.type = type;
        this.lineNumber = lineNumber;
        this.contents = contents;
        this.loopObject = loopObject;
    }
}