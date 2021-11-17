public class Production {

    private String initState;
    private String finalState;

    public Production(String initState, String finalState) {
        this.initState = initState;
        this.finalState = finalState;
    }

    public String getInitState() {
        return initState;
    }

    public void setInitState(String initState) {
        this.initState = initState;
    }

    public String getFinalState() {
        return finalState;
    }

    public void setFinalState(String finalState) {
        this.finalState = finalState;
    }

    @Override
    public String toString(){
        return this.initState+" -> "+this.finalState;
    }
}
