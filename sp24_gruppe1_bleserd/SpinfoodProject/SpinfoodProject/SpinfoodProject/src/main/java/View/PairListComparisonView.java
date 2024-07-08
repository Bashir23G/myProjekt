package View;

import Model.Pair;
import Model.DataHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class PairListComparisonView {
    private JPanel comparisonPanel;
    private JTable pairTable1;
    private JTable pairTable2;
    private JScrollPane pairScrollPane1;
    private JScrollPane pairScrollPane2;
    private CustomTableModel pairTableModel1;
    private CustomTableModel pairTableModel2;
    private JButton closeButton; // Close button to hide the panel

    public PairListComparisonView() {
        comparisonPanel = new JPanel(new BorderLayout());

        // Initialize tables with empty data
        String[] pairColumnNames = {"Pair ID", "Participant 1", "Participant 2", "Kitchen Location", "Distance to Party"};

        pairTableModel1 = new CustomTableModel(new Object[][]{}, pairColumnNames);
        pairTable1 = new JTable(pairTableModel1);
        pairScrollPane1 = new JScrollPane(pairTable1);

        pairTableModel2 = new CustomTableModel(new Object[][]{}, pairColumnNames);
        pairTable2 = new JTable(pairTableModel2);
        pairScrollPane2 = new JScrollPane(pairTable2);

        JPanel tablePanel = new JPanel(new GridLayout(1, 2));
        tablePanel.add(pairScrollPane1);
        tablePanel.add(pairScrollPane2);

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
        List<Pair> pairs1 = DataHandler.getInstance().getPairList().getPairList();
        List<Pair> pairs2 = new ArrayList<>();

        // Divide pairs1 into two parts
        int halfSize = pairs1.size() / 2;
        List<Pair> firstHalf = pairs1.subList(0, halfSize);
        List<Pair> secondHalf = pairs1.subList(halfSize, pairs1.size());

        // Load data from firstHalf into pairTable1
        loadTableData(pairTableModel1, firstHalf);

        // Load data from secondHalf into pairTable2
        loadTableData(pairTableModel2, secondHalf);
    }

    private void loadTableData(DefaultTableModel model, List<Pair> pairs) {
        String[] pairColumnNames = {"Pair ID", "Participant 1", "Participant 2", "Kitchen Location", "Distance to Party"};
        Object[][] pairData = new Object[pairs.size()][5];

        for (int i = 0; i < pairs.size(); i++) {
            Pair p = pairs.get(i);
            pairData[i][0] = p.getPairId();
            pairData[i][1] = (p.getParticipant_1() != null) ? p.getParticipant_1().getName() : "None";
            pairData[i][2] = (p.getParticipant_2() != null) ? p.getParticipant_2().getName() : "None";
            pairData[i][3] = (p.getKitchenLocation() != null) ? p.getKitchenLocation().toString() : "None";
            pairData[i][4] = p.getDistanceToPartyLocation();
        }

        model.setDataVector(pairData, pairColumnNames);
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
