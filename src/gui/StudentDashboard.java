
package gui;

import exception.UnauthorizedException;
import model.Complaint;
import model.User;
import service.AuthService;
import service.ComplaintService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentDashboard extends JFrame {

    private User student;
    private ComplaintService complaintService;
    private JTable table;
    private DefaultTableModel tableModel;

    public StudentDashboard(User student) {
        this.student = student;

        try {
            complaintService = new ComplaintService("data/complaints.txt");
        } catch (Exception exception) {
            complaintService = new ComplaintService();
        }

        setTitle("Student Dashboard - " + student.getName());
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ---------------- TOP PANEL ----------------
        JButton create = new JButton("Create Complaint");
        JButton refresh = new JButton("Refresh");
        JButton logout = new JButton("Logout");

        JPanel top = new JPanel();
        top.add(create);
        top.add(refresh);
        top.add(logout);

        add(top, BorderLayout.NORTH);

        // ---------------- TABLE ----------------
        String[] columns = {
                "Complaint ID",
                "Title",
                "Category",
                "Status",
                "Description"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        // Make table easier to read
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(300);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // ---------------- BUTTON ACTIONS ----------------

        create.addActionListener(e -> {
            new CreateComplaintFrame(
                    this,
                    complaintService,
                    student
            ).setVisible(true);
        });

        refresh.addActionListener(e -> refresh());

        logout.addActionListener(e -> {
            new AuthService("data/users.txt").logout(student);
            dispose();
            new LoginFrame().setVisible(true);
        });

        // Load complaints when dashboard opens
        refresh();
    }

    /**
     * Loads the student's complaints into the table.
     */
    public void refresh() {

        // Remove old rows first
        tableModel.setRowCount(0);

        try {

            for (Complaint c :
                    complaintService.getStudentComplaints(student.getUserId())) {

                tableModel.addRow(new Object[]{
                        c.getComplaintId(),
                        c.getTitle(),
                        c.getCategory(),
                        c.getStatus(),
                        c.getDescription()
                });
            }

        } catch (UnauthorizedException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
