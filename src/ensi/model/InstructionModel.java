package ensi.model;

import java.io.Serializable;

public class InstructionModel implements Serializable {

    public Instruction instruction;

    public String data;

    public InstructionModel(Instruction instruction){
        this.instruction = instruction;
    }

    public InstructionModel(Instruction instruction, String data){
        this.instruction = instruction;
        this.data = data;
    }

}
