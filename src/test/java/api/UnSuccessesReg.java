package api;

public class UnSuccessesReg {
    String error;

    public UnSuccessesReg(String error) {
        this.error = error;
    }

    public UnSuccessesReg() {
    }

    public String getError() {
        return error;
    }
}
