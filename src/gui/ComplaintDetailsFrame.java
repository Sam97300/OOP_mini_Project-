package gui;

import model.Complaint;
import javax.swing.*;
import java.awt.*;

public class ComplaintDetailsFrame extends JFrame {
    public ComplaintDetailsFrame(Complaint complaint) {
        setTitle("Complaint Details"); setSize(450, 300); setLocationRelativeTo(null);
        JTextArea details = new JTextArea("ID: " + complaint.getComplaintId() + "\n"
                + "Student: " + complaint.getStudentId() + "\n"
                + "Category: " + complaint.getCategory() + "\n"
                + "Status: " + complaint.getStatus() + "\n\n"
                + "Title: " + complaint.getTitle() + "\n"
                + "Description: " + complaint.getDescription());
        details.setEditable(false); details.setLineWrap(true); details.setWrapStyleWord(true); add(new JScrollPane(details), BorderLayout.CENTER);
    }
}
