package macnss.dao;
import macnss.model.Agent;
import macnss.model.Patient;
import macnss.model.User;

import java.util.List;

public interface UserDAO {

    public Patient authenticatePatient(int matricule, String password);
    public Agent authenticateAgent(String email, String password);
    public boolean addPatient(Patient patient);

    public boolean addAgent(Agent agent);

}