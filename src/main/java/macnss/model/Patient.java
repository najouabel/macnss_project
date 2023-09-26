package macnss.model;

public class Patient extends User{
    private int matricule;

    public Patient(  int id, String name, String email, String password, int matricule) {
        super(  id,   name, email, password);
        this.matricule = matricule;
    }


    public void setMatricule(int matricule) {
        this.matricule = matricule;
    }
    public int getMatricule() {
        return this.matricule;
    }

}
