package  macnss.service;

import macnss.db.DatabaseConnection;
import macnss.dao.UserDAOImpl;
import macnss.service.AuthenticationService;
import macnss.model.Admin;
import macnss.model.Agent;
import macnss.model.Patient;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class MaCNSSService {
    private static MaCNSSService instance;
    private final AuthenticationService authService;
    private final macnss.service.AgentService
            AgentService;
    private final macnss.service.PatientService PatientService;
    private final macnss.service.AdminService AdminService;
    private final FileService FileService;
    private final Connection connection;

    private MaCNSSService() throws SQLException {
        connection = DatabaseConnection.getInstance().getConnection();
        authService = new AuthenticationService(new UserDAOImpl(connection));
        AgentService = new AgentService(connection);
        PatientService = new PatientService(connection);
        AdminService = new AdminService(connection);
        FileService = new FileService(connection);
    }

    public static MaCNSSService getInstance() throws SQLException {
        if (instance == null) {
            instance = new MaCNSSService();
        }
        return instance;
    }
    public void closeConnection() {
        try{
            this.connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("************************************************************");
        System.out.println("                  Welcome to MaCNSS                         ");
        System.out.println("                                                            ");
        System.out.println("  Please select your role:                                  ");
        System.out.println("  1. Admin                                                  ");
        System.out.println("  2. Agent                                                  ");
        System.out.println("  3. Patient                                                ");
        System.out.println("                                                            ");
        System.out.println("************************************************************");
        System.out.print("Enter your choice: ");

        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    Admin admin = authService.adminAuth(scanner);
                    if (admin != null){
                        AdminService.displayMenu(admin);
                    }
                }
                case 2 -> {
                    Agent agent = authService.agentAuth(scanner);
                    if (agent != null){
                        AgentService.showMenu(agent);
                    }
                }
                case 3 -> {
                    Patient patient = authService.patientAuth(scanner);
                    if (patient != null){
                        PatientService.showMenu(patient,FileService);
                    }
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } else {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine();
        }
        scanner.close();
        closeConnection();
    }
}