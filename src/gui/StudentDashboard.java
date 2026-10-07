package gui;

import exception.InvalidComplaintException;
import exception.UnauthorizedException;
import model.Complaint;
import model.User;
import service.AuthService;
import service.ComplaintService;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {
    private User student;
    private ComplaintService complaintService;
    private JTextArea complaintArea = new JTextArea();

    public StudentDashboard(User student) {
        this.student = student;
        try { complaintService = new ComplaintService("data/complaints.txt"); }
        catch (Exception exception) { complaintService = new ComplaintService(); }
        setTitle("Student Dashboard - " + student.getName());
        setSize(650, 430); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setLocationRelativeTo(null);
        JButton create = new JButton("Create Complaint");
        JButton refresh = new JButton("Refresh");
        JButton logout = new JButton("Logout");
        JPanel top = new JPanel(); top.add(create); top.add(refresh); top.add(logout); add(top, BorderLayout.NORTH);
        complaintArea.setEditable(false); add(new JScrollPane(complaintArea), BorderLayout.CENTER);
        create.addActionListener(e -> new CreateComplaintFrame(this, complaintService, student).setVisible(true));
        refresh.addActionListener(e -> refresh());
        logout.addActionListener(e -> { new AuthService("data/users.txt").logout(student); dispose(); new LoginFrame().setVisible(true); });
        refresh();
    }

    public void refresh() {
        complaintArea.setText("");
        try {
            for (Complaint c : complaintService.getStudentComplaints(student.getUserId())) complaintArea.append(c + "\n");
        } catch (UnauthorizedException exception) { complaintArea.setText(exception.getMessage()); }
    }
}
