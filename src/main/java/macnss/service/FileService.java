package macnss.service;

import macnss.dao.DocumentDAOImpl;
import macnss.dao.DocumentDAO;
import macnss.dao.PatientDAOImpl;
import macnss.dao.RefundFileDAOImpl;
import macnss.model.*;
import util.tools;
import macnss.Enum.RefundFileStatus;

import java.sql.Connection;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileService {

    private final RefundFileDAOImpl refundFileDAOImpl;

    private final PatientDAOImpl patientDAOImpl;

    List<Document> documents;

    private final Connection connection;



    public FileService(Connection connection) {
        this.patientDAOImpl =  new PatientDAOImpl(connection);
        this.connection = connection;
        this.refundFileDAOImpl= new RefundFileDAOImpl(connection);
    }

    public void checkClientFiles(User authenticatedUser) {
        List<RefundFile> refundfiles = refundFileDAOImpl.getFileByUser(authenticatedUser);
        System.out.println("your files :");
        System.out.println("---------------------------------------------------------");
        System.out.println("|  ID  |      DATE      |   STATUS   |   TotalRefund   |");
        System.out.println("---------------------------------------------------------");
        for (RefundFile refundfile : refundfiles) {
            System.out.printf("| %-4d | %-14s | %-10s | %-15.2f |\n",
                    refundfile.getId(),
                    refundfile.getCreationDate(),
                    refundfile.getStatus(),
                    refundfile.getTotalRefund());
        }
        System.out.println("---------------------------------------------------------");

    }

    public void addFile()  {
        DocumentDAOImpl documentDAO = new DocumentDAOImpl();
        List<Document> selectedDocuments = new ArrayList<>();

        PatientDAOImpl patientDAOImpl = new PatientDAOImpl(this.connection);
        Patient patient = patientDAOImpl.createPatient();

        if (patientDAOImpl.addPatientToDatabase(patient)) {
            System.out.println("Patient added with ID: " + patient.getId());
        } else {
            System.out.println("Failed to add the patient.");
            return;
        }


        while (true) {
            System.out.println("Please select the type of document to add to the refund file:");
            System.out.println("1. Medicines");
            System.out.println("2. Radiography");
            System.out.println("3. Medical Examination");
            System.out.println("4. Medical Analysis");
            System.out.println("5. Finish Adding Documents");
            System.out.print("Enter your choice: ");
            Scanner scanner = new Scanner(System.in);

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:

                    Medicine medicine = documentDAO.createMedicineDocument();
                    if (medicine != null){
                        Document document = medicine;
                        selectedDocuments.add(document);
                    }
                    break;
                case 5:
                    if (selectedDocuments.isEmpty()) {
                        System.out.println("No documents selected. Please add at least one document.");
                    } else {
                        this.documents = selectedDocuments;
                        System.out.println("Documents added to the refund file.");
                        System.out.println("Total documents: " + selectedDocuments.size());
                        calculateRefundForDocuments(selectedDocuments);
                        System.out.println("Total Amount: " + calculateRefundForDocuments(selectedDocuments));

                        RefundFile refundFile = new RefundFile(patient.getId(), tools.getCurrentDate(), calculateRefundForDocuments(selectedDocuments), RefundFileStatus.pending);
                        if (refundFileDAOImpl.addRefundFileToDatabase(refundFile)) {
                            System.out.println("Refund file added to the database.");
                        } else {
                            System.out.println("Failed to add the refund file to the database.");
                        }
                        return;
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }
    public void updateFileStatus() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the patient's matricule: ");
        int matricule = scanner.nextInt();
        scanner.nextLine();
        List<RefundFile> filematricule = refundFileDAOImpl.getFileBymatricule(matricule);

        System.out.println("Files for Matricule " + matricule + ":");
        System.out.println("---------------------------------------------------------");
        System.out.println("|  ID  |      DATE      |   STATUS   |   TotalRefund   |");
        System.out.println("---------------------------------------------------------");

        for (RefundFile refundFile : filematricule) {
            System.out.printf("| %-4d | %-14s | %-10s | %-15.2f |\n",
                    refundFile.getId(),
                    refundFile.getCreationDate(),
                    refundFile.getStatus(),
                    refundFile.getTotalRefund());
        }

        System.out.println("---------------------------------------------------------");

        System.out.print("Enter the ID File ");

        if (scanner.hasNextInt()) {
            int fileId = scanner.nextInt();
            scanner.nextLine();

                System.out.println("Choose the new status:");
                System.out.println("1. Approved");
                System.out.println("2. Rejected");
                System.out.println("3. Pending");
                System.out.print("Enter your choice: ");

                if (scanner.hasNextInt()) {
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    RefundFileStatus newStatus;
                    switch (choice) {
                        case 1:
                            newStatus = RefundFileStatus.approved;
                            break;
                        case 2:
                            newStatus = RefundFileStatus.rejected;
                            break;
                        case 3:
                            newStatus = RefundFileStatus.pending;
                            break;
                        default:
                            System.out.println("Invalid choice. Status not updated.");
                            return;
                    }

                    if (refundFileDAOImpl.updateFile(fileId,newStatus)) {
                        System.out.println("File status updated successfully.");
                        if(sendstatusVerificationEmail(matricule,newStatus)){
                            System.out.println("success");

                        }else{
                            System.out.println("invalid email");
                        }
                    } else {
                        System.out.println("Failed to update file status.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid choice.");
                }
            } else {
                System.out.println("File not found or does not belong to the authenticated user.");
            }

    }

    public boolean sendstatusVerificationEmail(int matricule, RefundFileStatus newStatus){
        String etat=newStatus.toString();
        String Sbj= "Macnss :  modification de votre dossier ";
        String Msg="etat de votre dossier "+etat;
        String email=refundFileDAOImpl.getEmailByMatricule(matricule);
        LocalTime emailSentTime = EmailSimpleService.sendMail(Msg,Sbj,email);

        if (emailSentTime != null) {
            return true;
        } else {
            return false;
        }

    }
    public double calculateRefundForDocuments(List<Document> documents) {
        double totalRefund = 0.0;

        for (Document document : documents) {
            // Check if the document is a Medicine
            if (document instanceof Medicine) {
                Medicine medicine = (Medicine) document;
                double price = medicine.getPrice();
                double percentage = medicine.getPercentageAsDouble();

                // Calculate the refund for this medicine
                double medicineRefund = price * (percentage / 100.0);

                // Add the medicine refund to the total refund
                totalRefund += medicineRefund;
            }
        }
        return totalRefund;
    }
}