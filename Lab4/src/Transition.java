public class Transition {

    private String initState;
    private String transformation;
    private String finalState;

    public Transition(String initState, String transformation, String finalState) {
        this.initState = initState;
        this.transformation = transformation;
        this.finalState = finalState;
    }

    public String getInitState() {
        return initState;
    }

    public void setInitState(String initState) {
        this.initState = initState;
    }

    public String getTransformation() {
        return transformation;
    }

    public void setTransformation(String transformation) {
        this.transformation = transformation;
    }

    public String getFinalState() {
        return finalState;
    }

    public void setFinalState(String finalState) {
        this.finalState = finalState;
    }

    @Override
    public String toString(){
        return "("+this.initState+", "+this.transformation+") -> "+this.finalState;
    }
}
