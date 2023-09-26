package macnss.dao;

import macnss.model.Medicine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import macnss.db.DatabaseConnection;
public class DocumentDAOImpl {

    public Medicine createMedicineDocument() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the barcode for the medicine: ");
        Double barcode = scanner.nextDouble();

        // Utilisez la classe DatabaseConnection existante pour obtenir une connexion à la base de données
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (connection != null) {
            // Requête pour récupérer le prix et le taux de remboursement du médicament en fonction du code-barres
            String sql = "SELECT nom, ppv, taux_remboursement FROM medicines WHERE code = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setDouble(1, barcode);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String nom = resultSet.getString("nom");
                    double price = resultSet.getDouble("ppv");
                    String tauxRemboursement = resultSet.getString("taux_remboursement");

                    // Créez et renvoyez une instance de Medicine avec les détails du code-barres, du prix et du taux de remboursement
                    Medicine medicine = new Medicine(barcode);
                    medicine.setNom(nom);
                    medicine.setPrice(price);
                    medicine.setPercentage(tauxRemboursement);

                    return medicine;
                } else {
                    System.out.println("Médicament non trouvé pour le code-barres donné.");
                    return null; // Gérez en conséquence, par exemple, renvoyez null ou déclenchez une exception
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérez l'exception de la requête de base de données
                return null;
            } finally {
                // Fermez la connexion à la base de données une fois terminé
                DatabaseConnection.closeConnection(connection);
            }
        } else {
            System.out.println("Échec de l'établissement d'une connexion à la base de données.");
            return null; // Gérez l'échec de la connexion à la base de données
        }
    }


}
