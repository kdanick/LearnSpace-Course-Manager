import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LecturersPage extends JPanel {
    public LecturersPage() {
        setBackground(Color.WHITE); // White background
        setLayout(new BorderLayout());


        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1));
        topPanel.setBackground(Color.WHITE);

        JLabel greetingLabel = new JLabel("Welcome Back!", SwingConstants.CENTER);
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 28));
        greetingLabel.setForeground(Color.BLACK);

        JLabel subGreetingLabel = new JLabel("Begin building your educational skills today.", SwingConstants.CENTER);
        subGreetingLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subGreetingLabel.setForeground(Color.BLACK);

        topPanel.add(greetingLabel);
        topPanel.add(subGreetingLabel);
        add(topPanel, BorderLayout.NORTH);


        String[] columnNames = {"ID", "Name", "Email", "Courses", "Grades"};
        Object[][] studentData = {
                {101, "Alice Johnson", "alice@example.com", "Mathematics, Physics", "A, B+"},
                {102, "Bob Smith", "bob@example.com", "Computer Science, English", "A-, B"},
                {103, "Charlie Brown", "charlie@example.com", "History, Mathematics", "B+, A"}
        };

        JTable table = new JTable(new DefaultTableModel(studentData, columnNames));
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setGridColor(Color.BLACK);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton addButton = new JButton("Add Student");
        JButton editButton = new JButton("Edit Student");
        JButton deleteButton = new JButton("Delete Student");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        JLabel label2 = new JLabel("List of Enrolled Students", SwingConstants.CENTER);
        label2.setFont(new Font("Arial", Font.BOLD, 18));
        label2.setForeground(Color.BLACK); // Black text
        add(label2, BorderLayout.SOUTH);
    }
}