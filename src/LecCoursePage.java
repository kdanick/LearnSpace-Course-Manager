import DatabaseManager.Db_connect;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class LecCoursePage extends JPanel {
    private JComboBox<String> courseDropdown;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private int lecturerId;

    public LecCoursePage(int lecturerId) {
        this.lecturerId = lecturerId;
        setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("My Courses", SwingConstants.CENTER);
        titlePanel.add(titleLabel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(titlePanel, gbc);

        // Instructions Section
        JPanel instructionPanel = new JPanel();
        JLabel instructionLabel = new JLabel("Select a course to view enrolled students:");
        instructionPanel.add(instructionLabel);

        gbc.gridx = 1;
        topPanel.add(instructionPanel, gbc);

        // Dropdown Section
        JPanel dropdownPanel = new JPanel();
        courseDropdown = new JComboBox<>();
        courseDropdown.setPreferredSize(new Dimension(250, 25));
        fetchCourses();
        courseDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchStudentsForCourse((String) courseDropdown.getSelectedItem());
            }
        });
        dropdownPanel.add(courseDropdown);

        gbc.gridx = 2;
        topPanel.add(dropdownPanel, gbc);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        studentTable = new JTable(tableModel);
        studentTable.setPreferredScrollableViewportSize(new Dimension(400, 150));
        add(new JScrollPane(studentTable), BorderLayout.CENTER);
    }

    private void fetchCourses() {
        try (Connection conn = Db_connect.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT course_name FROM courses WHERE lecturer_id = ?")) {
            stmt.setInt(1, lecturerId);
            ResultSet rs = stmt.executeQuery();
            courseDropdown.addItem("Select Course");
            while (rs.next()) {
                courseDropdown.addItem(rs.getString("course_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchStudentsForCourse(String courseName) {
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{"baseClasses.Student ID", "Name", "Email", "Grade", "Score"});

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT s.student_id, s.name, s.email, g.grade, g.score " +
                             "FROM students s " +
                             "JOIN enrollments e ON s.student_id = e.student_id " +
                             "JOIN courses c ON e.course_id = c.course_id " +
                             "LEFT JOIN grades g ON g.student_id = s.student_id AND g.course_id = c.course_id " +
                             "WHERE c.course_name = ?")) {
            stmt.setString(1, courseName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("student_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("email"));
                row.add(rs.getString("grade") != null ? rs.getString("grade") : "N/A");
                row.add(rs.getString("score") != null ? rs.getString("score") : "N/A");
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
