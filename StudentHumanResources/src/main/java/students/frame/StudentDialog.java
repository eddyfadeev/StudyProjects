package students.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import students.logic.Group;
import students.logic.ManagementSystem;
import students.logic.Student;

public class StudentDialog extends JDialog implements ActionListener {

    private static final int D_HEIGHT = 200; // window height
    private static final int D_WIDTH = 450;  // window width
    private static final int L_X = 10;       // left label's border for the field
    private static final int L_W = 100;      // border's label width for the field
    private static final int C_W = 150;      // field's width

    // Owner of our window
    private StudentsFrame owner;

    // Button pressing result
    private boolean result = false;

    // Student's parameters
    private int studentId = 0;
    private JTextField firstName = new JTextField();
    private JTextField surName = new JTextField();
    private JSpinner dateOfBirth = new JSpinner(new SpinnerDateModel(new Date(), null, null,
            Calendar.DAY_OF_MONTH));
    private ButtonGroup sex = new ButtonGroup();
    private JSpinner year = new JSpinner(new SpinnerNumberModel(2020, 1900, 2100, 1));
    private JComboBox groupList;

    // Second parameter contains sign - adding or changing student
    public StudentDialog(List<Group> groups, boolean newStudent, StudentsFrame owner) {
        this.owner = owner;

        setTitle("Edit student data");
        getContentPane().setLayout(new FlowLayout());

        groupList = new JComboBox(new Vector<Group>(groups));

        JRadioButton m = new JRadioButton("Male");
        JRadioButton w = new JRadioButton("Female");

        m.setActionCommand("M");
        w.setActionCommand("F");

        sex.add(m);
        sex.add(w);

        getContentPane().setLayout(null);

        // Surname
        JLabel label = new JLabel("Surname:", JLabel.RIGHT);
        label.setBounds(L_X, 10, L_W, 20);
        getContentPane().add(label);
        surName.setBounds(L_X + L_W + 10, 10, C_W, 20);
        getContentPane().add(surName);

        //First name
        label = new JLabel("Name:", JLabel.RIGHT);
        label.setBounds(L_X, 30, L_W, 20);
        getContentPane().add(label);
        firstName.setBounds(L_X + L_W + 10, 30, C_W, 20);
        getContentPane().add(firstName);

        // Sex
        label = new JLabel("Sex:", JLabel.RIGHT);
        label.setBounds(L_X, 70, L_W, 20);
        getContentPane().add(label);
        m.setBounds(L_X + L_W + 10, 70, C_W / 2, 20);
        getContentPane().add(m);

        w.setBounds(L_X + L_W + 10 + C_W / 2, 70, C_W / 2, 20);
        w.setSelected(true);
        getContentPane().add(w);

        // Date of birth
        label = new JLabel("Date of birth:", JLabel.RIGHT);
        label.setBounds(L_X, 90, L_W, 20);
        getContentPane().add(label);
        dateOfBirth.setBounds(L_X + L_W + 10, 90, C_W, 20);
        getContentPane().add(dateOfBirth);

        // Group
        label = new JLabel("Group:", JLabel.RIGHT);
        label.setBounds(L_X, 115, L_W, 25);
        getContentPane().add(label);
        groupList.setBounds(L_X + L_W + 10, 115, C_W, 25);
        getContentPane().add(groupList);

        // Year of study
        label = new JLabel("Year of study:", JLabel.RIGHT);
        label.setBounds(L_X, 145, L_W, 20);
        getContentPane().add(label);
        year.setBounds(L_X + L_W + 10, 145, C_W, 20);
        getContentPane().add(year);

        JButton btnOk = new JButton("OK");
        btnOk.setName("OK");
        btnOk.addActionListener(this);
        btnOk.setBounds(L_X + L_W + C_W + 10 + 50, 10, 100, 25);
        getContentPane().add(btnOk);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setName("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setBounds(L_X + L_W + C_W + 10 + 50, 40, 100, 25);
        getContentPane().add(btnCancel);

        if (newStudent) {
            JButton btnNew = new JButton("New");
            btnNew.setName("New");
            btnNew.addActionListener(this);
            btnNew.setBounds(L_X + L_W + C_W + 10 + 50, 70, 100, 25);
            getContentPane().add(btnNew);
        }

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(((int) dimension.getWidth() - StudentDialog.D_WIDTH) / 2,
                ((int) dimension.getHeight() - StudentDialog.D_HEIGHT) / 2,
                StudentDialog.D_WIDTH, StudentDialog.D_HEIGHT);
    }

    // Setting fields according to transferred data about student
    public void setStudent(Student st) {
        studentId = st.getStudentId();
        firstName.setText(st.getFirstName());
        surName.setText(st.getSurName());
        dateOfBirth.getModel().setValue(st.getDateOfBirth());
        for (Enumeration e = sex.getElements(); e.hasMoreElements();) {
            AbstractButton ab = (AbstractButton) e.nextElement();
            ab.setSelected(ab.getActionCommand().equals(new String("" + st.getSex())));
        }
        year.getModel().setValue(st.getEducationYear());
        for (int i = 0; i < groupList.getModel().getSize(); i++) {
            Group g = (Group) groupList.getModel().getElementAt(i);
            if (g.getGroupId() == st.getGroupId()) {
                groupList.setSelectedIndex(i);
                break;
            }
        }
    }

    // Return data as a new student with the appropriate fields
    public Student getStudent() {
        Student st = new Student();
        st.setStudentId(studentId);
        st.setFirstName(firstName.getText());
        st.setSurName(surName.getText());
        Date d = ((SpinnerDateModel) dateOfBirth.getModel()).getDate();
        st.setDateOfBirth(d);
        for (Enumeration e = sex.getElements(); e.hasMoreElements();) {
            AbstractButton ab = (AbstractButton) e.nextElement();
            if (ab.isSelected()) {
                st.setSex(ab.getActionCommand().charAt(0));
            }
        }
        int y = ((SpinnerNumberModel) year.getModel()).getNumber().intValue();
        st.setEducationYear(y);
        st.setGroupId(((Group) groupList.getSelectedItem()).getGroupId());
        return st;
    }
    public boolean getResult() {
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();
        if (src.getName().equals("New")) {
            result = true;
            try {
                ManagementSystem.getInstance().insertStudent(getStudent());
                owner.reloadStudents();
                firstName.setText("");
                surName.setText("");
            } catch (Exception sql_ex) {
                JOptionPane.showMessageDialog(this, sql_ex.getMessage());
            }
            return;
        }
        if (src.getName().equals("OK")) {
            result = true;
        }
        if (src.getName().equals("Cancel")) {
            result = false;
        }
        setVisible(false);
    }
}
