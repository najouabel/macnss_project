package macnss.dao;

import macnss.model.Document;
import macnss.model.Medicine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import macnss.db.DatabaseConnection;
import macnss.model.RefundFile;
import util.tools;

public class DocumentDAOImpl {

    private final Connection connection;

    public DocumentDAOImpl(Connection connection){
        this.connection = connection;
    }

    public Medicine createMedicineDocument() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the barcode for the medicine: ");
        Long barcode = scanner.nextLong();

        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (connection != null) {
            String sql = "SELECT nom, ppv, taux_remboursement FROM medicines WHERE code = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, barcode);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String name = resultSet.getString("nom");
                    double price = resultSet.getDouble("ppv");
                    Double percentage = tools.percentageStringToDouble(resultSet.getString("taux_remboursement"));

                    Medicine medicine = new Medicine(0,name,price,percentage,"medcine",barcode);

                    return medicine;
                } else {
                    System.out.println("medicine not found");
                    return null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            } finally {
                DatabaseConnection.closeConnection(connection);
            }
        } else {
            System.out.println("connection is null");
            return null;
        }
    }
    public void addDocument(Document document, RefundFile file){
        String sql = "INSERT INTO documents (name, refundamount, type) VALUES (?,?,?)";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
            preparedStatement.setString(1, document.getName());
            preparedStatement.setDouble(2, document.getAmountPaid());
            preparedStatement.setString(3, document.getType());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        sql = "INSERT INTO file_document (file_id, document_id) VALUES (?,?)";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, file.getId());
            preparedStatement.setInt(2, document.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}