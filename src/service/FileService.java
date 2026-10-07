package service;

import exception.InvalidComplaintException;
import model.Complaint;
import model.ComplaintStatus;
import model.HostelComplaint;
import model.InfrastructureComplaint;
import model.TechnicalComplaint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/** Stores complaints in a simple pipe-separated text file. */
public class FileService {
    private String filePath;

    public FileService(String filePath) {
        this.filePath = filePath;
    }

    public void saveComplaints(ArrayList<Complaint> complaints) throws IOException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Complaint complaint : complaints) {
                writer.write(escape(complaint.getComplaintId()) + "|"
                        + escape(complaint.getStudentId()) + "|"
                        + escape(complaint.getTitle()) + "|"
                        + escape(complaint.getDescription()) + "|"
                        + complaint.getCategory() + "|"
                        + complaint.getStatus());
                writer.newLine();
            }
        }
    }

    public ArrayList<Complaint> loadComplaints() throws IOException {
        ArrayList<Complaint> complaints = new ArrayList<Complaint>();
        File file = new File(filePath);
        if (!file.exists()) {
            return complaints;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|", -1);
                if (data.length != 6) {
                    continue;
                }
                try {
                    Complaint complaint = createComplaint(data);
                    complaint.updateStatus(ComplaintStatus.valueOf(data[5]));
                    complaints.add(complaint);
                } catch (InvalidComplaintException | IllegalArgumentException ignored) {
                    // Ignore malformed records and continue loading valid records.
                }
            }
        }
        return complaints;
    }

    private Complaint createComplaint(String[] data) throws InvalidComplaintException {
        String category = data[4].toUpperCase();
        if (category.equals("TECHNICAL")) {
            return new TechnicalComplaint(data[0], data[1], data[2], data[3]);
        }
        if (category.equals("HOSTEL")) {
            return new HostelComplaint(data[0], data[1], data[2], data[3]);
        }
        return new InfrastructureComplaint(data[0], data[1], data[2], data[3]);
    }

    public void deleteComplaint(String complaintId) throws IOException {
        ArrayList<Complaint> complaints = loadComplaints();
        for (int i = complaints.size() - 1; i >= 0; i--) {
            if (complaints.get(i).getComplaintId().equalsIgnoreCase(complaintId)) {
                complaints.remove(i);
            }
        }
        saveComplaints(complaints);
    }

    private String escape(String value) {
        return value.replace("|", "/").replace("\n", " ").replace("\r", " ");
    }
}
