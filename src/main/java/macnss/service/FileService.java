package macnss.service;

import macnss.dao.*;
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

    private final PatientService PatientService;

    private final macnss.dao.UserDAOImpl UserDAOImpl;

    private final Connection connection;

    List<Document> documents;

    public FileService(Connection connection) {
        this.PatientService =  new PatientService(connection);
        this.refundFileDAOImpl= new RefundFileDAOImpl(connection);
        this.connection = connection;
        this.UserDAOImpl = new UserDAOImpl(connection);
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
    public void getClientFiles(int matricule) {
        Patient patient = UserDAOImpl.getUserByMatricule(matricule);
        List<RefundFile> refundfiles = refundFileDAOImpl.getFileByUser(patient);
        System.out.println("your files :");
        for (RefundFile refundfile : refundfiles) {
            System.out.println(refundfile.getId() + "- Status : " + refundfile.getStatus() + "- TotalRefund : " + refundfile.getTotalRefund());
        }
    }
    Document createDocument(String type,Scanner scanner){
        System.out.println("Enter how much paid on the "+type+" : ");
        int amount = scanner.nextInt();
        return new Document(0,type,amount,0.0,type);
    }

    public void addFile(Scanner scanner)  {
        DocumentDAOImpl documentDAO = new DocumentDAOImpl(connection);
        List<Document> selectedDocuments = new ArrayList<>();

        System.out.println("Please choose : ");
        System.out.println("1 - existing patient \n2 - new patient");
        int choice = tools.tryParse(scanner.nextLine());
        while (choice != 1 && choice != 2){
            System.out.println("Please choose : ");
            System.out.println("1 - existing patient \n 2 - new patient");
            choice = tools.tryParse(scanner.nextLine());
        }
        Patient patient = null;
        if(choice == 1){
            System.out.println("enter patient matricule : ");
            int matricule = scanner.nextInt();
            patient = UserDAOImpl.getUserByMatricule(matricule);
        }else {
            patient = PatientService.createPatient();
            if (patient != null) {
                System.out.println("Patient added with ID: " + patient.getId());
            } else {
                System.out.println("Failed to add the patient.");
                return;
            }
        }




        while (true) {
            System.out.println("Please select the type of document to add to the refund file:");
            System.out.println("1. Medicines");
            System.out.println("2. Radiography");
            System.out.println("3. Medical Examination");
            System.out.println("4. Medical Analysis");
            System.out.println("5. Finish Adding Documents");
            System.out.print("Enter your choice: ");
            scanner = new Scanner(System.in);

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    Medicine medicine = documentDAO.createMedicineDocument();
                    if (medicine != null) {
                        selectedDocuments.add(medicine);
                    }
                }
                case 2 -> {
                    Document radiography = createDocument("radio", scanner);
                    if (radiography != null) {
                        selectedDocuments.add(radiography);
                    }
                }
                case 3 -> {
                    // Create a Medical Examination document
                    Document medicalExamination = createDocument("medicalExamination", scanner);
                    if (medicalExamination != null) {
                        selectedDocuments.add(medicalExamination);
                    }
                }
                case 4 -> {
                    // Create a Medical Analysis document
                    Document medicalAnalysis = createDocument("medicalAnalysis", scanner);
                    if (medicalAnalysis != null) {
                        selectedDocuments.add(medicalAnalysis);
                    }
                }
                case 5 -> {
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
                }
                default -> System.out.println("Invalid choice. Please select a valid option.");
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
                        case 1 -> newStatus = RefundFileStatus.approved;
                        case 2 -> newStatus = RefundFileStatus.rejected;
                        case 3 -> newStatus = RefundFileStatus.pending;
                        default -> {
                            System.out.println("Invalid choice. Status not updated.");
                            return;
                        }
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
    public void editFile(Scanner scanner){
        List<RefundFile> files =refundFileDAOImpl.getAllFiles();
        for (RefundFile file : files) {
            System.out.println(file.getId() + " - " + file.getStatus().toString());
        }
        System.out.println("Please enter the file id you want to edit: ");
        int fileId = scanner.nextInt();
        RefundFile file = refundFileDAOImpl.getFileById(fileId);
        System.out.println("Please enter the new file status: ");
        System.out.println("1- pending\n2- approved\n3- rejected\n");
        int newFileStatus = scanner.nextInt();
        if(newFileStatus == 1){
            file.setStatus(RefundFileStatus.pending);
            refundFileDAOImpl.updateFile(file);
            System.out.println("Refund file status updated.");
        }else if(newFileStatus == 2){
            file.setStatus(RefundFileStatus.approved);
            refundFileDAOImpl.updateFile(file);
            System.out.println("Refund file status updated.");
        }else if(newFileStatus == 3){
            file.setStatus(RefundFileStatus.rejected);
            refundFileDAOImpl.updateFile(file);
            System.out.println("Refund file status updated.");
        }else{
            System.out.println("Invalid choice. Please select a valid option.");
        }

        if(refundFileDAOImpl.updateFile(file)){
            System.out.println("File status updated successfully.");
        }

    }
    public double calculateRefundForDocuments(List<Document> documents) {
        double totalRefund = 0.0;

        for (Document document : documents) {
            // Check if the document is a Medicine
            if (document instanceof Medicine medicine) {
                double price = medicine.getAmountPaid();
                double percentage = medicine.getPercentage();
                // Calculate the refund for this medicine
                double medicineRefund = price * percentage;

                // Add the medicine refund to the total refund
                totalRefund += medicineRefund;
            }else {
                double price = document.getAmountPaid();
                double percentage = document.getPercentage();
                // Calculate the refund for this document
                double documentRefund = price * percentage;

                // Add the document refund to the total refund
                totalRefund += documentRefund;
            }
        }
        return totalRefund;
    }
}