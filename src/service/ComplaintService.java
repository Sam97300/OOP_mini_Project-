package service;

import exception.InvalidComplaintException;
import exception.UnauthorizedException;
import model.Complaint;
import model.ComplaintStatus;
import model.HostelComplaint;
import model.InfrastructureComplaint;
import model.TechnicalComplaint;

import java.util.ArrayList;
import java.io.IOException;

/**
 * Contains the main operations related to complaints.
 */
public class ComplaintService {
    private ArrayList<Complaint> complaints;
    private int nextComplaintNumber;
    private FileService fileService;

    public ComplaintService() {
        complaints = new ArrayList<Complaint>();
        nextComplaintNumber = 1;
    }

    public ComplaintService(String complaintsFilePath) throws IOException {
        this.fileService = new FileService(complaintsFilePath);
        this.complaints = fileService.loadComplaints();
        nextComplaintNumber = findNextNumber();
    }

    /**
     * Creates the correct complaint subclass from the selected category.
     */
    public Complaint createComplaint(String studentId, String title,
                                     String description, String category)
            throws InvalidComplaintException {
        if (category == null || category.trim().isEmpty()) {
            throw new InvalidComplaintException("Complaint category cannot be empty.");
        }

        String complaintId = generateComplaintId();
        String selectedCategory = category.trim().toUpperCase();
        Complaint complaint;

        if (selectedCategory.equals("WI-FI")
                || selectedCategory.equals("TECHNICAL")) {
            complaint = new TechnicalComplaint(
                    complaintId, studentId, title, description);
        } else if (selectedCategory.equals("HOSTEL")) {
            complaint = new HostelComplaint(
                    complaintId, studentId, title, description);
        } else if (selectedCategory.equals("CLASSROOM")
                || selectedCategory.equals("LAB")
                || selectedCategory.equals("INFRASTRUCTURE")) {
            complaint = new InfrastructureComplaint(
                    complaintId, studentId, title, description);
        } else {
            throw new InvalidComplaintException("Invalid complaint category.");
        }

        complaints.add(complaint);
        save();
        return complaint;
    }

    private int findNextNumber() {
        int largest = 0;
        for (Complaint complaint : complaints) {
            try {
                largest = Math.max(largest, Integer.parseInt(
                        complaint.getComplaintId().substring(1)));
            } catch (NumberFormatException | StringIndexOutOfBoundsException ignored) { }
        }
        return largest + 1;
    }

    public void save() throws InvalidComplaintException {
        if (fileService == null) return;
        try {
            fileService.saveComplaints(complaints);
        } catch (IOException exception) {
            throw new InvalidComplaintException("Could not save complaints: " + exception.getMessage());
        }
    }

    private String generateComplaintId() {
        String complaintId = String.format("C%03d", nextComplaintNumber);
        nextComplaintNumber++;
        return complaintId;
    }

    public ArrayList<Complaint> getAllComplaints() {
        return new ArrayList<Complaint>(complaints);
    }

    /**
     * Returns only complaints created by the given student.
     */
    public ArrayList<Complaint> getStudentComplaints(String studentId)
            throws UnauthorizedException {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new UnauthorizedException("Student ID is required.");
        }

        ArrayList<Complaint> studentComplaints = new ArrayList<Complaint>();

        for (Complaint complaint : complaints) {
            if (complaint.getStudentId().equalsIgnoreCase(studentId.trim())) {
                studentComplaints.add(complaint);
            }
        }

        return studentComplaints;
    }

    /**
     * Searches by complaint ID, student ID, title, category, or status.
     */
    public ArrayList<Complaint> searchComplaints(String searchText) {
        ArrayList<Complaint> matchingComplaints = new ArrayList<Complaint>();

        if (searchText == null || searchText.trim().isEmpty()) {
            return matchingComplaints;
        }

        String text = searchText.trim().toLowerCase();

        for (Complaint complaint : complaints) {
            if (complaint.getComplaintId().toLowerCase().contains(text)
                    || complaint.getStudentId().toLowerCase().contains(text)
                    || complaint.getTitle().toLowerCase().contains(text)
                    || complaint.getCategory().toLowerCase().contains(text)
                    || complaint.getStatus().toString().toLowerCase().contains(text)) {
                matchingComplaints.add(complaint);
            }
        }

        return matchingComplaints;
    }

    public void updateStatus(String complaintId, ComplaintStatus status)
            throws UnauthorizedException {
        Complaint complaint = findComplaint(complaintId);

        if (complaint == null) {
            throw new UnauthorizedException("Complaint was not found.");
        }

        complaint.updateStatus(status);
        try {
            if (fileService != null) fileService.saveComplaints(complaints);
        } catch (IOException exception) {
            throw new UnauthorizedException("Could not save status: " + exception.getMessage());
        }
    }

    public void deleteComplaint(String complaintId) throws UnauthorizedException {
        Complaint complaint = findComplaint(complaintId);
        if (complaint == null) throw new UnauthorizedException("Complaint was not found.");
        complaints.remove(complaint);
        try {
            if (fileService != null) fileService.saveComplaints(complaints);
        } catch (IOException exception) {
            throw new UnauthorizedException("Could not delete complaint: " + exception.getMessage());
        }
    }

    private Complaint findComplaint(String complaintId) {
        if (complaintId == null) {
            return null;
        }

        for (Complaint complaint : complaints) {
            if (complaint.getComplaintId().equalsIgnoreCase(complaintId.trim())) {
                return complaint;
            }
        }

        return null;
    }
}
