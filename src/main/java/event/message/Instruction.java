package event.message;

import java.util.List;

/**
 * Created by Peter Mösenthin.
 */
public abstract class Instruction {

    protected int instructionId;
    protected List<String> parameters;

    public int getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(int instructionId) {
        this.instructionId = instructionId;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
}
