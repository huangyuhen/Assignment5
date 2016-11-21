package ObjectTypes;

import java.util.ArrayList;

public class LoopObject{
    public int startIndex;
    public int endIndex;
    public String numOfExcution;
    public String originalNumOfExcution;
    public int numOfExcutionInNum;
    public int originalNumOfExcutionInNum;
    public int previousLoopInNum;
    public ArrayList<Integer> previousLoops;
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
