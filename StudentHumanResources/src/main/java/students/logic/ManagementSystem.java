package students.logic;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ManagementSystem {

    private static Connection conn;
    private static ManagementSystem instance;

    private static final String url = "jdbc:mariadb://localhost:3306/students";
    private static final String user = "root";
    private static final String password = "1";

    // Closed constructor
    private ManagementSystem() throws Exception {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new Exception(e);
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    // getInstance method checks if static variable is initialized (if not, it will initialize) and returns it
    public static synchronized ManagementSystem getInstance() throws Exception {
        if (instance == null) {
            instance = new ManagementSystem();
        }
        return instance;
    }

    public List<Group> getGroups() throws SQLException {
        List<Group> groups = new ArrayList<Group>();

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT group_id, " +
                "group_name, curator, speciality FROM groups")) {
            while (rs.next()) {
                Group gr = new Group();
                gr.setGroupId(rs.getInt(1));
                gr.setGroupName(rs.getString(2));
                gr.setCurator(rs.getString(3));
                gr.setSpeciality(rs.getString(4));

                groups.add(gr);
            }
        }
        return groups;
    }

    // Get the list of all students
    public Collection<Student> getAllStudents() throws SQLException {
        Collection<Student> students = new ArrayList<Student>();

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(
                "SELECT student_id, first_name, surname, " +
                        "sex, date_of_birth, group_id, education_year FROM students " +
                        "ORDER BY first_name, surname")) {
            while (rs.next()) {
                Student st = new Student(rs);
                students.add(st);
            }
        }
        return students;
    }

    // Get the list of students for the group
    public Collection<Student> getStudentsFromGroup(Group group, int year) throws SQLException {
        Collection<Student> students = new ArrayList<Student>();

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT student_id, first_name, surname, " +
                        "sex, date_of_birth, group_id, education_year FROM students " +
                        "ORDER BY first_name, surname"); ResultSet rs = stmt.executeQuery()) {
            //stmt.setInt(0, group.getGroupId());
            //stmt.setInt(1, year);
            while (rs.next()) {
                Student st = new Student(rs);
                students.add(st);
            }
        }

        return students;
    }

    // Moving students from one group to another
    public void moveStudentsToGroup(Group oldGroup, int oldYear, Group newGroup, int newYear) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "UPDATE students SET group_id=?, education_year=? " +
                        "WHERE group_id=? AND education_year=?")) {
            stmt.setInt(1, newGroup.getGroupId());
            stmt.setInt(2, newYear);
            stmt.setInt(3, oldGroup.getGroupId());
            stmt.setInt(4, oldYear);
            stmt.execute();
        }
    }

    // Delete all the students from the groups
    public void removeStudentsFromGroup(Group group, int year) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM students WHERE group_id=? AND education_year=?")) {
            stmt.setInt(1, group.getGroupId());
            stmt.setInt(2, year);
            stmt.execute();
        }
    }

    // Add a student
    public void insertStudent(Student student) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO students " +
                        "(first_name, surname, sex, date_of_birth, group_id, education_year) " +
                        "VALUES (?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getSurName());
            stmt.setString(3, new String(new char[]{student.getSex()}));
            stmt.setDate(4, new Date(student.getDateOfBirth().getTime()));
            stmt.setInt(5, student.getGroupId());
            stmt.setInt(6, student.getEducationYear());
            stmt.execute();
        }
    }

    // Update student's info
    public void updateStudent(Student student) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "UPDATE students SET " +
                        "first_name=?, surname=?, sex=?, " +
                        "date_of_birth=?, group_id=?, education_year=? " +
                        "WHERE student_id=?")) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getSurName());
            stmt.setString(3, new String(new char[]{student.getSex()}));
            stmt.setDate(4, new Date(student.getDateOfBirth().getTime()));
            stmt.setInt(5, student.getGroupId());
            stmt.setInt(6, student.getEducationYear());
            stmt.setInt(7, student.getStudentId());
            stmt.execute();
        }
    }

    // Delete student
    public void deleteStudent(Student student) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM students WHERE student_id=?")) {
            stmt.setInt(1, student.getStudentId());
            stmt.execute();
        }
    }
}
