package api;

public class SuccessesReg {
    public Integer id;
    public String token;

    public SuccessesReg(Integer id, String token) {
        this.id = id;
        this.token = token;
    }

    public SuccessesReg(String token) {
        this.token = token;
    }

    public SuccessesReg() {
    }

    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
