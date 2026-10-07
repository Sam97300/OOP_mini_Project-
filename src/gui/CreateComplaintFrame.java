package gui;

import exception.InvalidComplaintException;
import model.User;
import service.ComplaintService;

import javax.swing.*;
import java.awt.*;

public class CreateComplaintFrame extends JFrame {
    public CreateComplaintFrame(StudentDashboard parent, ComplaintService service, User student) {
        setTitle("Create Complaint"); setSize(450, 330); setLocationRelativeTo(parent);
        JPanel panel = new JPanel(new GridLayout(5, 2, 8, 8)); panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JTextField title = new JTextField(); JTextArea description = new JTextArea();
        JComboBox<String> category = new JComboBox<>(new String[]{"Wi-Fi", "Classroom", "Hostel", "Lab", "Infrastructure"});
        panel.add(new JLabel("Title:")); panel.add(title); panel.add(new JLabel("Category:")); panel.add(category);
        panel.add(new JLabel("Description:")); panel.add(new JScrollPane(description));
        JButton save = new JButton("Save Complaint"); panel.add(new JLabel()); panel.add(save); add(panel);
        save.addActionListener(e -> { try {
            service.createComplaint(student.getUserId(), title.getText(), description.getText(), (String) category.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Complaint created successfully."); parent.refresh(); dispose();
        } catch (InvalidComplaintException exception) { JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); } });
    }
}
