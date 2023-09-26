package macnss.model;

public class Agent extends User {
    private String verificationCode;

    public Agent(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    // Getter for verificationCode
    public String getVerificationCode() {
        return verificationCode;
    }

    // Setter for verificationCode
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}

