package e_was.backend.DTO;

public class ResetPassRequest {
    private String email;
    private String newPass;
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNewPass() {
        return newPass;
    }
    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    
}
