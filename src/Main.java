import exception.InvalidComplaintException;
import exception.InvalidLoginException;
import exception.UnauthorizedException;
import interfaces.Authenticatable;
import model.Admin;
import model.Complaint;
import model.ComplaintStatus;
import model.HostelComplaint;
import model.InfrastructureComplaint;
import model.Student;
import model.TechnicalComplaint;
import model.User;
import service.AuthService;
import service.ComplaintService;
import gui.LoginFrame;

public class Main {

    public static void main(String[] args) {
        System.out.println("SECURE CAMPUS COMPLAINT & ISSUE TRACKER");
        System.out.println("========================================");

        demonstrateUsers();
        demonstrateAuthentication();
        demonstrateComplaintService();
        demonstrateComplaints();
        demonstrateExceptionHandling();
        System.out.println("\nTo start the Swing application, run: java -cp out Main gui");
        if (args.length > 0 && args[0].equalsIgnoreCase("gui")) {
            javax.swing.SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }

    private static void demonstrateAuthentication() {
        System.out.println("\nAUTHENTICATION");

        AuthService authService = new AuthService("data/users.txt");

        try {
            User student = authService.login(
                    "aarav@campus.edu",
                    "student123"
            );

            System.out.println("Student login successful: "
                    + student.getName() + " (" + student.getRole() + ")");
            authService.logout(student);

            User admin = authService.login(
                    "admin@campus.edu",
                    "admin123"
            );

            System.out.println("Admin login successful: "
                    + admin.getName() + " (" + admin.getRole() + ")");
            authService.logout(admin);
        } catch (InvalidLoginException exception) {
            System.out.println("Login error: " + exception.getMessage());
        }

        try {
            authService.login("aarav@campus.edu", "wrongPassword");
        } catch (InvalidLoginException exception) {
            System.out.println("Failed login handled: " + exception.getMessage());
        }
    }

    private static void demonstrateUsers() {
        System.out.println("\nUSER HIERARCHY");

        // Parent class references point to different child class objects.
        User user1 = new Student("ST101", "Aarav", "aarav@campus.edu", "student123");
        User user2 = new Admin("AD001", "Dr. Mehta", "admin@campus.edu", "admin123");

        System.out.println(user1.getName() + " has role: " + user1.getRole());
        System.out.println(user2.getName() + " has role: " + user2.getRole());
        System.out.println("Student password accepted: " + user1.authenticate("student123"));

        // An interface reference can also refer to a Student object.
        Authenticatable authenticatableUser = new Student(
                "ST102", "Diya", "diya@campus.edu", "diya123");
        System.out.println("Interface authentication result: "
                + authenticatableUser.authenticate("diya123"));
    }

    private static void demonstrateComplaints() {
        System.out.println("\nCOMPLAINT HIERARCHY");

        try {
            Complaint technicalComplaint = new TechnicalComplaint(
                    "C001", "ST101", "Wi-Fi not working", "Internet is unavailable");
            Complaint infrastructureComplaint = new InfrastructureComplaint(
                    "C002", "ST101", "Broken classroom fan", "The fan is not working");
            Complaint hostelComplaint = new HostelComplaint(
                    "C003", "ST102", "Water problem", "There is no water in the hostel");

            Complaint[] complaints = {
                    technicalComplaint,
                    infrastructureComplaint,
                    hostelComplaint
            };

            for (Complaint complaint : complaints) {
                System.out.println(complaint);
            }

            technicalComplaint.updateStatus(ComplaintStatus.IN_PROGRESS);
            System.out.println("\nAfter status update:");
            System.out.println(technicalComplaint);

            technicalComplaint.updateStatus(ComplaintStatus.RESOLVED);
            System.out.println("Is technical complaint resolved? "
                    + technicalComplaint.isResolved());
        } catch (InvalidComplaintException exception) {
            System.out.println("Complaint error: " + exception.getMessage());
        }
    }

    private static void demonstrateComplaintService() {
        System.out.println("\nCOMPLAINT SERVICE");

        ComplaintService complaintService = new ComplaintService();

        try {
            Complaint wiFiComplaint = complaintService.createComplaint(
                    "ST101",
                    "Wi-Fi not working",
                    "Internet is unavailable in the library",
                    "Wi-Fi"
            );

            Complaint hostelComplaint = complaintService.createComplaint(
                    "ST102",
                    "Water problem",
                    "There is no water in the hostel",
                    "Hostel"
            );

            Complaint labComplaint = complaintService.createComplaint(
                    "ST101",
                    "Broken lab chair",
                    "A chair is broken in the computer lab",
                    "Lab"
            );

            System.out.println("Created complaints:");
            printComplaints(complaintService.getAllComplaints());

            System.out.println("\nComplaints belonging to ST101:");
            printComplaints(complaintService.getStudentComplaints("ST101"));

            System.out.println("\nSearch results for 'HOSTEL':");
            printComplaints(complaintService.searchComplaints("HOSTEL"));

            complaintService.updateStatus(
                    wiFiComplaint.getComplaintId(),
                    ComplaintStatus.IN_PROGRESS
            );
            System.out.println("\nAfter updating "
                    + wiFiComplaint.getComplaintId() + ":");
            System.out.println(wiFiComplaint);

            // This variable shows that category selection created a subclass.
            System.out.println("Created category for lab complaint: "
                    + labComplaint.getCategory());
            System.out.println("Created category for hostel complaint: "
                    + hostelComplaint.getCategory());
        } catch (InvalidComplaintException | UnauthorizedException exception) {
            System.out.println("Complaint service error: " + exception.getMessage());
        }
    }

    private static void printComplaints(java.util.ArrayList<Complaint> complaints) {
        for (Complaint complaint : complaints) {
            System.out.println(complaint);
        }
    }

    private static void demonstrateExceptionHandling() {
        System.out.println("\nEXCEPTION HANDLING");

        try {
            new HostelComplaint("C004", "", "Empty student ID",
                    "This should produce a validation error");
        } catch (InvalidComplaintException exception) {
            System.out.println("Handled invalid complaint: " + exception.getMessage());
        }
    }
}
