package ensi.model;

import java.io.Serializable;
import java.util.ArrayList;

public class InstructionModel implements Serializable {

    public Instruction instruction;

    public String data;

    public ArrayList<Score> scores;

    public InstructionModel(Instruction instruction){
        this.instruction = instruction;
    }

    public InstructionModel(Instruction instruction, String data){
        this.instruction = instruction;
        this.data = data;
    }

    public InstructionModel(Instruction instruction, String data, ArrayList<Score> scores){
        this.instruction = instruction;
        this.data = data;
        this.scores = scores;
    }

}
