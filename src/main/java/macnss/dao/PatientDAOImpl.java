package macnss.dao;

import macnss.db.DatabaseConnection;
import macnss.model.Patient;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PatientDAOImpl extends UserDAOImpl {
    public PatientDAOImpl(Connection connection) {
        super(connection);
    }

    // Add a new patient to the database
    public boolean addPatientToDatabase(Patient patient) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (connection != null) {
        String sql = "INSERT INTO patients (name, email, password, matricule) VALUES (?, ?, ?, ?)";
         try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, patient.getName());
            preparedStatement.setString(2, patient.getEmail());
            preparedStatement.setString(3, patient.getPassword());
            preparedStatement.setInt(4, patient.getMatricule());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    patient.setId(userId);
                }

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print the exception for debugging
        }
}
        return false;

    }


    public Patient createPatient() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter patient name: ");
        String name = scanner.nextLine();

        System.out.println("Enter patient email: ");
        String email = scanner.nextLine();

        System.out.println("Enter patient password: ");
        String password = scanner.nextLine();

        System.out.println("Enter patient matricule: ");
        int matricule = scanner.nextInt();

        return new Patient(6, name, email, password, matricule);
    }
}
