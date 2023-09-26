package macnss.dao;
import macnss.model.Agent;
import macnss.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {
    protected Connection connection; // Change access level to protected

    public UserDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Patient authenticatePatient(int matricule, String password) {
        String query = "SELECT * FROM patients WHERE matricule = ? AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, matricule);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");

                return new Patient(id, name, email, password, matricule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Authentication failed
    }

    @Override
    public Agent authenticateAgent(String email, String password) {
        String query = "SELECT * FROM agents WHERE email = ? AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                return new Agent(id, name, email, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Authentication failed
    }
    @Override
    public boolean addAgent(Agent agent) {
        String sql = "INSERT INTO agents (name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, agent.getName());
            preparedStatement.setString(2, agent.getEmail());
            preparedStatement.setString(3, agent.getPassword());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean addPatient(Patient patient) {
        String sql = "INSERT INTO patients (name, email, password, matricule) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, patient.getName());
            preparedStatement.setString(2, patient.getEmail());
            preparedStatement.setString(3, patient.getPassword());
            preparedStatement.setInt(4, patient.getMatricule());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}

