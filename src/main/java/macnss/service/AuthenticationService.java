package macnss.service;

import macnss.dao.UserDAOImpl;
import macnss.model.Admin;
import macnss.model.Agent;
import macnss.model.Patient;
import util.tools;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.Scanner;

public class AuthenticationService {
    private final UserDAOImpl userDAO;

    public AuthenticationService(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }

    public Agent agentAuth(Scanner scanner) {
        System.out.println("=== User Sign-In ===");

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        while (tools.isValidEmailFormat(email)) {
            System.out.println("Invalid email format. Please enter a valid email:");
            email = new Scanner(System.in).nextLine();
        }
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        // Validate user's password
        while (tools.isValidPassword(password)) {

            System.out.println("Invalid password format. Password must be at least 8 characters long without spaces:");
            password = new Scanner(System.in).nextLine();
        }

        Agent authenticatedAgent = userDAO.authenticateAgent(email, password);

        if (authenticatedAgent != null) {
                if(sendVerificationEmail(email)){
                    System.out.println("success");

                }else{
                    System.out.println("invalid verification code");
                }
                return authenticatedAgent;
            } else {
                System.out.println("Sign-In failed. Invalid email or password.");
                return null;
            }
        }
        public boolean sendVerificationEmail(String email){
            String codeverification = EmailSimpleService.codeGenerator();
            String Sbj= "Macnss Verification code ";
            String Msg="Verification code : "+codeverification;
            LocalTime sendTime = EmailSimpleService.sendMail(Msg,Sbj,email);
            System.out.println("entre verification code sent to your email : ");
            String code;
            do{
                Scanner scanner = new Scanner(System.in);
                code = scanner.nextLine();
                if(code.equals(codeverification)){
                    return true ;
                }
                System.out.println("entre verification code : ");


            }while (codevalide(sendTime));
            return false;
        }
        public boolean codevalide(LocalTime sendTime){
            if(sendTime.until(LocalTime.now(), ChronoUnit.MINUTES)<=2)
                return true;
            return false;
        }

    public Patient patientAuth(Scanner scanner) {
        System.out.println("=== User Sign-In ===");
        System.out.print("Enter your matricule: ");
        int matricule ;
        matricule = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        // Validate user's password
        while (tools.isValidPassword(password)) {
            System.out.println("Invalid password format. Password must be at least 8 characters long without spaces:");
            password = scanner.nextLine();
        }

        Patient authenticatedPatient = userDAO.authenticatePatient(matricule, password);

        if (authenticatedPatient != null) {
            System.out.println("Sign-In successful!");
            return authenticatedPatient;
        } else {
            System.out.println("Sign-In failed. Invalid email or password.");
            return null;
        }
    }
    public Admin adminAuth(Scanner scanner) {
        System.out.println("========== admin Sign-In ==========");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        // Validate user's email
        while (tools.isValidEmailFormat(email)) {
            System.out.println("Invalid email format. Please enter a valid email:");
            email = new Scanner(System.in).nextLine();
        }
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (email.equals("admin@gmail.com") && password.equals("admin")) {
            System.out.println("Sign-In successful!");
            System.out.println("==============================");
            return new Admin(1,"admin", "admin@gmail.com", "admin");
        } else {
            System.out.println("Sign-In failed. Invalid email or password.");
            return null;
        }

    }
    public void addAgent(Scanner scanner) {
        System.out.println("=== Agent Registration ===");

        // Collect admin information
        System.out.print("Enter Agent name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Agent email: ");
        String email = scanner.nextLine();
        // Validate user's email
        while (tools.isValidEmailFormat(email)) {
            System.out.println("Invalid email format. Please enter a valid email:");
            email = new Scanner(System.in).nextLine();
        }

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // Validate user's password
        while (tools.isValidPassword(password)) {
            System.out.println("Invalid password format. Password must be at least 8 characters long without spaces:");
            password = new Scanner(System.in).nextLine();
        }

        Agent Agent = new Agent(0,name, email, password);

        if (userDAO.addAgent(Agent)) {
            System.out.println("Agent registration successful!");
        } else {
            System.out.println("Agent registration failed. Please check the input.");
        }
    }
    public void addPatient(Scanner scanner) {
        System.out.println("=== Patient Registration ===");

        // Collect admin information
        System.out.print("Enter Patient name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Patient email: ");
        String email = scanner.nextLine();
        // Validate user's email
        while (tools.isValidEmailFormat(email)) {
            System.out.println("Invalid email format. Please enter a valid email:");
            email = new Scanner(System.in).nextLine();
        }

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // Validate user's password
        while (tools.isValidPassword(password)) {
            System.out.println("Invalid password format. Password must be at least 8 characters long without spaces:");
            password = new Scanner(System.in).nextLine();
        }
        Random random = new Random();

        // Generate a random 6-digit number
        int min = 100000; // Minimum 6-digit number (100000)
        int max = 999999; // Maximum 6-digit number (999999)
        int matricule = random.nextInt(max - min + 1) + min;
        Patient patient = new Patient(0,name, email, password, matricule);
        System.out.println("patient Name :"+patient.getName()+"| email : "+patient.getEmail()+"| matricule : "+patient.getMatricule()+"| password :"+patient.getPassword());
        scanner.nextLine();
        Patient patient1 = userDAO.addPatient(patient);

        if (patient1 != null) {
            System.out.println("Patient registration successful!");
            System.out.println("patient Name :"+patient.getName()+"| email : "+patient.getEmail()+"| matricule : "+patient.getMatricule()+"| password :"+patient.getPassword());
        } else {
            System.out.println("Patient registration failed. Please check the input.");
        }
    }
}
