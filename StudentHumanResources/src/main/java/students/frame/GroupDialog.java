package students.frame;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import students.logic.Group;

public class GroupDialog extends JDialog implements ActionListener {
    private static final int D_HEIGHT = 150; // height
    private static final int D_WIDTH = 200;  // width
    private JSpinner spYear;
    private JComboBox groupList;
    private JButton btnOk = new JButton("OK");
    private JButton btnCancel = new JButton("Cancel");
    private boolean result = false;

    public GroupDialog(int year, List groups) {
        setTitle("Group transfer");

        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        // First element - header for the groups chooser field
        JLabel label = new JLabel("New group:");
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        gbl.setConstraints(label, constraints);
        getContentPane().add(label);

        // Second element - groups list
        groupList = new JComboBox(new Vector(groups));
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        gbl.setConstraints(groupList, constraints);
        getContentPane().add(groupList);

        // Third element - header of the year's field
        label = new JLabel("New year:");
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        gbl.setConstraints(label, constraints);
        getContentPane().add(label);

        // Increment a year by one
        spYear = new JSpinner(new SpinnerNumberModel(year + 1, 1900, 2100, 1));
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        gbl.setConstraints(spYear, constraints);
        getContentPane().add(spYear);

        // Buttons
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.BOTH;
        btnOk.setName("OK");
        btnOk.addActionListener(this);
        gbl.setConstraints(btnOk, constraints);
        getContentPane().add(btnOk);

        btnCancel.setName("Cancel");
        btnCancel.addActionListener(this);
        gbl.setConstraints(btnCancel, constraints);
        getContentPane().add(btnCancel);

        // Setting behavior on close - not close totally
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        //Getting screen size
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        // Putting it in the middle
        setBounds(((int) dimension.getWidth() - GroupDialog.D_WIDTH) / 2, ((int) dimension.getHeight() -
                GroupDialog.D_HEIGHT) / 2, GroupDialog.D_WIDTH, GroupDialog.D_HEIGHT);
    }

    // Receive the year on the form
    public int getYear() {
        return ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
    }

    // Receive the group on form
    public Group getGroup() {
        if (groupList.getModel().getSize() > 0) {
            return (Group) groupList.getSelectedItem();
        }
        return null;
    }

    // Get the result "true" -> OK, "false" -> Cancel
    public boolean getResult() {
        return result;
    }

    // Processing the buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();
        if (src.getName().equals("OK")) {
            result = true;
        }
        if (src.getName().equals("Cancel")) {
            result = false;
        }
        setVisible(false);
    }
}
