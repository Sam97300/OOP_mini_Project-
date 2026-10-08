package gui;

import exception.UnauthorizedException;
import model.Complaint;
import model.ComplaintStatus;
import model.User;
import service.AuthService;
import service.ComplaintService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AdminDashboard extends JFrame {
    private ComplaintService service;
    private JTable table;
    private JTextField search = new JTextField(18);
    private ArrayList<Complaint> displayed = new ArrayList<Complaint>();

    public AdminDashboard(User admin) {
        try {
            service = new ComplaintService("data/complaints.txt");
        } catch (Exception e) {
            service = new ComplaintService();
        }
        setTitle("Admin Dashboard - " + admin.getName());
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        JButton find = new JButton("Search");
        JButton all = new JButton("All");
        JButton view = new JButton("View");
        JButton update = new JButton("Update Status");
        JButton delete = new JButton("Delete");
        JButton logout = new JButton("Logout");
        top.add(new JLabel("Search:"));
        top.add(search);
        top.add(find);
        top.add(all);
        top.add(view);
        top.add(update);
        top.add(delete);
        top.add(logout);
        add(top, BorderLayout.NORTH);
        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        refresh(service.getAllComplaints());
        find.addActionListener(e -> refresh(service.searchComplaints(search.getText())));
        all.addActionListener(e -> refresh(service.getAllComplaints()));
        view.addActionListener(e -> selectedDetails());
        update.addActionListener(e -> updateStatus());
        delete.addActionListener(e -> deleteSelected());
        logout.addActionListener(e -> {
            new AuthService("data/users.txt").logout(admin);
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private void refresh(ArrayList<Complaint> complaints) {
        displayed = complaints;
        String[] columns = { "ID", "Student", "Title", "Category", "Status" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (Complaint c : complaints)
            model.addRow(new Object[] { c.getComplaintId(), c.getStudentId(), c.getTitle(), c.getCategory(),
                    c.getStatus() });
        table.setModel(model);
    }

    private Complaint selected() {
        int row = table.getSelectedRow();
        return row < 0 ? null : displayed.get(row);
    }

    private void selectedDetails() {
        Complaint c = selected();
        if (c == null) {
            message("Select a complaint.");
            return;
        }
        new ComplaintDetailsFrame(c).setVisible(true);
    }

    private void updateStatus() {
        Complaint c = selected();
        if (c == null) {
            message("Select a complaint.");
            return;
        }
        ComplaintStatus status = (ComplaintStatus) JOptionPane.showInputDialog(this, "Select status:", "Update Status",
                JOptionPane.QUESTION_MESSAGE, null, ComplaintStatus.values(), c.getStatus());
        if (status != null)
            try {
                service.updateStatus(c.getComplaintId(), status);
                refresh(service.getAllComplaints());
            } catch (UnauthorizedException e) {
                message(e.getMessage());
            }
    }

    private void deleteSelected() {
        Complaint c = selected();
        if (c == null) {
            message("Select a complaint.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete " + c.getComplaintId() + "?", "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            try {
                service.deleteComplaint(c.getComplaintId());
                refresh(service.getAllComplaints());
            } catch (UnauthorizedException e) {
                message(e.getMessage());
            }
    }

    private void message(String text) {
        JOptionPane.showMessageDialog(this, text);
    }
}
