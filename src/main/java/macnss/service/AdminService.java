package macnss.service;
import macnss.dao.UserDAOImpl;
import macnss.service.AuthenticationService;
import macnss.model.User;

import java.sql.Connection;
import java.util.Scanner;

public class AdminService {

    private final Connection connection;
    public AdminService(Connection connection) {
        this.connection = connection;
    }

    public void displayMenu(User authenticatedUser) {
        Scanner scanner = new Scanner(System.in);


        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. Add an Agent");
            System.out.println("2. Exit");

            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> new AuthenticationService(new UserDAOImpl(connection)).addAgent(scanner);
                    case 2 -> {
                        System.out.println("Thank you for using the CNSS service. Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }


}

