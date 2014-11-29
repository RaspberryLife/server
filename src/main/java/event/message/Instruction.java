package event.message;

import java.util.List;

/**
 * Created by Peter Mösenthin.
 */
public abstract class Instruction {

    protected int instructionId;
    protected List<Integer> intParameters;
    protected List<String> stringParameters;

    public int getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(int instructionId) {
        this.instructionId = instructionId;
    }

    public List<Integer> getIntParameters() {
        return intParameters;
    }

    public void setIntParameters(List<Integer> intParameters) {
        this.intParameters = intParameters;
    }

    public List<String> getStringParameters() {
        return stringParameters;
    }

    public void setStringParameters(List<String> stringParameters) {
        this.stringParameters = stringParameters;
    }
}
