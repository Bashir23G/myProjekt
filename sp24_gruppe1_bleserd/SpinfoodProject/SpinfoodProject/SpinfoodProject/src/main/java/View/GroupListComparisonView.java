package View;

import Model.Group;
import Model.DataHandler;
import Model.Pair;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GroupListComparisonView {
    private JPanel comparisonPanel;
    private JTable groupTable1;
    private JTable groupTable2;
    private JScrollPane groupScrollPane1;
    private JScrollPane groupScrollPane2;
    private CustomTableModel groupTableModel1;
    private CustomTableModel groupTableModel2;
    private JButton closeButton; // Close button to hide the panel

    public GroupListComparisonView() {
        comparisonPanel = new JPanel(new BorderLayout());

        // Initialize tables with empty data
        String[] groupColumnNames = {"Group ID", "Participants"};

        groupTableModel1 = new CustomTableModel(new Object[][]{}, groupColumnNames);
        groupTable1 = new JTable(groupTableModel1);
        groupScrollPane1 = new JScrollPane(groupTable1);

        groupTableModel2 = new CustomTableModel(new Object[][]{}, groupColumnNames);
        groupTable2 = new JTable(groupTableModel2);
        groupScrollPane2 = new JScrollPane(groupTable2);

        JPanel tablePanel = new JPanel(new GridLayout(1, 2));
        tablePanel.add(groupScrollPane1);
        tablePanel.add(groupScrollPane2);

        comparisonPanel.add(tablePanel, BorderLayout.CENTER);

        // Create close button and add action listener
        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comparisonPanel.setVisible(false); // Hide the comparisonPanel
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        comparisonPanel.add(buttonPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        List<Group> groups1 = DataHandler.getInstance().getGroupList().getAllGroups();
        List<Group> groups2 = new ArrayList<>();

        // Divide groups1 into two parts
        int halfSize = groups1.size() / 2;
        List<Group> firstHalf = groups1.subList(0, halfSize);
        List<Group> secondHalf = groups1.subList(halfSize, groups1.size());

        // Load data from firstHalf into groupTable1
        loadTableData(groupTableModel1, firstHalf);

        // Load data from secondHalf into groupTable2
        loadTableData(groupTableModel2, secondHalf);
    }

    private void loadTableData(DefaultTableModel model, List<Group> groups) {
        String[] groupColumnNames = {"Group ID", "Participants"};
        Object[][] groupData = new Object[groups.size()][2];

        for (int i = 0; i < groups.size(); i++) {
            Group g = groups.get(i);
            StringBuilder participants = new StringBuilder();
            for (Pair p : g.getPairs()) {
                participants.append(p.getParticipant_1().getName()).append(" & ").append(p.getParticipant_2().getName()).append(", ");
            }
            if (participants.length() > 0) {
                participants.setLength(participants.length() - 2); // Remove the last comma and space
            }
            groupData[i][0] = g.getId();
            groupData[i][1] = participants.toString();
        }

        model.setDataVector(groupData, groupColumnNames);
    }

    public JPanel getComparisonPanel() {
        return comparisonPanel;
    }

    // Custom Table Model class
    private class CustomTableModel extends DefaultTableModel {
        public CustomTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Disable cell editing
        }
    }
}
