package students.frame;

import java.sql.SQLException;
import java.util.Vector;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import students.logic.Group;
import students.logic.ManagementSystem;
import students.logic.Student;

public class StudentsFrame extends JFrame implements ActionListener, ListSelectionListener, ChangeListener {

    private static final String MOVE_GROUP = "moveGroup";
    private static final String CLEAR_GROUP = "clearGroup";
    private static final String INSERT_STUDENT = "insertStudent";
    private static final String UPDATE_STUDENT = "updateStudent";
    private static final String DELETE_STUDENT = "deleteStudent";
    private static final String ALL_STUDENTS = "allStudents";
    private ManagementSystem ms = null;
    private JList grpList;
    private JTable stdList;
    private JSpinner spYear;

    public StudentsFrame() throws Exception {
        // Setting up a layout for all the client form part
        getContentPane().setLayout(new BorderLayout());

        // Creating menu bar
        JMenuBar menuBar = new JMenuBar();
        // Creating drop-down menu
        JMenu menu = new JMenu("Reports");
        // Creating item in drop-down
        JMenuItem menuItem = new JMenuItem("All students");
        menuItem.setName(ALL_STUDENTS);
        menuItem.addActionListener(this);
        // Inserting item to the drop-down
        menu.add(menuItem);
        // Inserting drop-down menu to the menu bar
        menuBar.add(menu);
        // Setting menu for form
        setJMenuBar(menuBar);

        // Creating the upper panel, for year input
        JPanel top = new JPanel();
        // Installing the layout for it
        top.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Explanatory text for it
        top.add(new JLabel("Study year"));
        // spin-field
        // 1. Behavior model - only numbers
        // 2. Inserting it
        SpinnerModel sm = new SpinnerNumberModel(2020, 1900, 2100, 1);
        spYear = new JSpinner(sm);
        spYear.addChangeListener(this);
        top.add(spYear);

        // Create the bottom panel with layout
        JPanel bot = new JPanel();
        bot.setLayout(new BorderLayout());

        // Create left panel for showing the list of groups
        GroupPanel left = new GroupPanel();
        left.setLayout(new BorderLayout());
        left.setBorder(new BevelBorder(BevelBorder.LOWERED));

        // Connecting to the DB and creating object ManagementSystem
        ms = ManagementSystem.getInstance();
        // Getting groups list
        Vector<Group> gr = new Vector<Group>(ms.getGroups());
        // Creating the name
        left.add(new JLabel("Groups:"), BorderLayout.NORTH);
        // Making visual list and inserting it to the scrollable panel,
        // that we'll put on the panel "left"
        grpList = new JList(gr);
        grpList.addListSelectionListener(this);
        // Selecting the first group
        grpList.setSelectedIndex(0);
        left.add(new JScrollPane(grpList), BorderLayout.CENTER);

        // Creating button for the groups
        JButton btnMoveGroup = new JButton("Move");
        btnMoveGroup.setName(MOVE_GROUP);
        btnMoveGroup.addActionListener(this);
        JButton btnClearGroup = new JButton("Clear");
        btnClearGroup.setName(CLEAR_GROUP);
        btnClearGroup.addActionListener(this);

        // Creating panel for buttons and placing it in the bottom
        JPanel pnlButtonGroup = new JPanel();
        pnlButtonGroup.setLayout(new GridLayout(1, 2));
        pnlButtonGroup.add(btnMoveGroup);
        pnlButtonGroup.add(btnClearGroup);
        left.add(pnlButtonGroup, BorderLayout.SOUTH);

        // Creating right panel for students list
        JPanel right = new JPanel();
        // Layout
        right.setLayout(new BorderLayout());
        right.setBorder(new BevelBorder(BevelBorder.LOWERED));

        // Naming
        right.add(new JLabel("Students:"), BorderLayout.NORTH);
        // Creating table
        stdList = new JTable(1, 3);
        right.add(new JScrollPane(stdList), BorderLayout.CENTER);
        // Buttons for the students
        JButton btnAddStudent = new JButton("Add");
        btnAddStudent.setName(INSERT_STUDENT);
        btnAddStudent.addActionListener( this);
        JButton btnUpdateStudent = new JButton("Change");
        btnUpdateStudent.setName(UPDATE_STUDENT);
        btnUpdateStudent.addActionListener(this);
        JButton btnDeleteStudent = new JButton("Delete");
        btnDeleteStudent.setName(DELETE_STUDENT);
        btnDeleteStudent.addActionListener(this);

        // Panel for the buttons and placing it bottom
        JPanel pnlButtonsStudents = new JPanel();
        pnlButtonsStudents.setLayout(new GridLayout(1, 3));
        pnlButtonsStudents.add(btnAddStudent);
        pnlButtonsStudents.add(btnUpdateStudent);
        pnlButtonsStudents.add(btnDeleteStudent);
        right.add(pnlButtonsStudents, BorderLayout.SOUTH);

        // Inserting panels with the students and groups list to the bottom panel
        bot.add(left, BorderLayout.WEST);
        bot.add(right, BorderLayout.CENTER);

        // Inserting upper and bottom panels into the form
        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(bot, BorderLayout.CENTER);

        // Declaring the bounds of the form
        setBounds(100, 100, 700, 500);
    }

    // ActionListener method
    @Override
    public void  actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Component) {
            Component c = (Component) e.getSource();
            if (c.getName().equals(MOVE_GROUP)) {
                moveGroup();
            }
            if (c.getName().equals(CLEAR_GROUP)) {
                clearGroup();
            }
            if (c.getName().equals(ALL_STUDENTS)) {
                showAllStudents();
            }
            if (c.getName().equals(INSERT_STUDENT)) {
                insertStudent();
            }
            if (c.getName().equals(UPDATE_STUDENT)) {
                updateStudent();
            }
            if (c.getName().equals(DELETE_STUDENT)) {
                deleteStudent();
            }
        }
    }

    // ListSelectionListener method
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            reloadStudents();
        }
    }

    // ChangeListener method
    @Override
    public void stateChanged(ChangeEvent e) {
        reloadStudents();
    }

    // Method for reloading students
    void reloadStudents() {
        Thread t = new Thread() {
            @Override
            public void run() {
                if (stdList != null) {
                    Group g = (Group) grpList.getSelectedValue();
                    int y = ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
                    try {
                        Collection<Student> s = ms.getStudentsFromGroup(g, y);
                        stdList.setModel(new StudentTableModel(new Vector<Student>(s)));
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    // Method for moving the group
    private void moveGroup() {
        Thread t = new Thread() {
            @Override
            public void run() {
                if (grpList.getSelectedValue() == null) {
                    return;
                }
                try {
                    Group g = (Group) grpList.getSelectedValue();
                    int y = ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
                    GroupDialog gd = new GroupDialog(y, ms.getGroups());
                    gd.setModal(true);
                    gd.setVisible(true);
                    if (gd.getResult()) {
                        ms.moveStudentsToGroup(g, y, gd.getGroup(), gd.getYear());
                        reloadStudents();
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                }
            }
        };
        t.start();
    }

    // Method for clearing group
    private void clearGroup() {
        Thread t = new Thread() {
            @Override
            public void run() {
                if (grpList.getSelectedValue() != null) {
                    if (JOptionPane.showConfirmDialog(StudentsFrame.this,
                            "Do you want to delete students from the group?",
                            "Deleting the the students",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        Group g = (Group) grpList.getSelectedValue();
                        int y = ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
                        try {
                            ms.removeStudentsFromGroup(g, y);
                            reloadStudents();
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                        }
                    }
                }
            }
        };
        t.start();
    }

    // Method for adding a student
    private void insertStudent() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    StudentDialog sd = new StudentDialog(ms.getGroups(), true, StudentsFrame.this);
                    sd.setModal(true);
                    sd.setVisible(true);
                    if (sd.getResult()) {
                        Student s = sd.getStudent();
                        ms.insertStudent(s);
                        reloadStudents();
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                }
            }
        };
        t.start();
    }

    // Method for redacting a student
    private void updateStudent() {
        Thread t = new Thread() {
            @Override
            public void run() {
                if (stdList != null) {
                    StudentTableModel stm = (StudentTableModel) stdList.getModel();
                    if (stdList.getSelectedRow() >= 0) {
                        Student s = stm.getStudent(stdList.getSelectedRow());
                        try {
                            StudentDialog sd = new StudentDialog(ms.getGroups(), false,
                                    StudentsFrame.this);
                            sd.setStudent(s);
                            sd.setModal(true);
                            sd.setVisible(true);
                            if (sd.getResult()) {
                                Student us = sd.getStudent();
                                ms.updateStudent(us);
                                reloadStudents();
                            }
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(StudentsFrame.this,
                                "You need to choose a student from the list");
                    }
                }
            }
        };
        t.start();
    }

    // Method for deleting a student
    private void deleteStudent() {
        Thread t = new Thread() {
            @Override
            public void run() {
                if (stdList != null) {
                    StudentTableModel stm = (StudentTableModel) stdList.getModel();
                    if (stdList.getSelectedRow() >= 0) {
                        if (JOptionPane.showConfirmDialog(StudentsFrame.this,
                                "Do you want to delete the student?", "Delete student",
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            Student s = stm.getStudent(stdList.getSelectedRow());
                            try {
                                ms.deleteStudent(s);
                                reloadStudents();
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                            }
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(StudentsFrame.this,
                                "You need to choose a student");
                    }
                }
            }
        };
        t.start();
    }

    // Method for showing all the students
    private void showAllStudents() {
        JOptionPane.showMessageDialog(this, "showAllStudents");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Cancelling work in case we cannot connect to the database
                    StudentsFrame sf = new StudentsFrame();
                    sf.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    sf.setVisible(true);
                    sf.reloadStudents();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Inner class overridden panel
    class GroupPanel extends JPanel {
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(250, 0);
        }
    }
}
