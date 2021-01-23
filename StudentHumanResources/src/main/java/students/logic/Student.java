package students.logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Student implements Comparable {
    private int studentId;
    private String firstName;
    private String surName;
    private Date dateOfBirth;
    private char sex;
    private int groupId;
    private int educationYear;

    public Student() {
    }

    public Student(ResultSet rs) throws SQLException {
        setStudentId(rs.getInt(1));
        setFirstName(rs.getString(2));
        setSurName(rs.getString(3));
        setSex(rs.getString(4).charAt(0));
        setDateOfBirth(rs.getDate(5));
        setGroupId(rs.getInt(6));
        setEducationYear(rs.getInt(7));
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getEducationYear() {
        return educationYear;
    }

    public void setEducationYear(int educationYear) {
        this.educationYear = educationYear;
    }

    @Override
    public String toString() {
        return firstName + " " + surName + ", " + DateFormat.getDateInstance(DateFormat.SHORT).format(dateOfBirth)
                + ", Group ID=" + groupId + " Year:" + educationYear;
    }

    @Override
    public int compareTo(Object obj) {
        Collator c = Collator.getInstance(new Locale("en"));
        c.setStrength(Collator.PRIMARY);
        return c.compare(this.toString(), obj.toString());
    }
}
