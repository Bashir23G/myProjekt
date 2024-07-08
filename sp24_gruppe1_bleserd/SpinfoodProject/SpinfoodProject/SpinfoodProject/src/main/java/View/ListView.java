package View;

import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListView {
    private JPanel listViewPanel;
    private JTable successorTable;
    private JTable pairTable;
    private JScrollPane successorScrollPane;
    private JScrollPane pairScrollPane;
    private JButton loadButton;
    private JButton assignButton;
    private JButton removeButton;
    private JButton createPairButton;
    private JButton undoButton;
    private JButton redoButton;
    private JButton graphButton;
    private CustomTableModel successorTableModel;
    private CustomTableModel pairTableModel;
    private UndoRedoManager undoRedoManager;
    private PersistenceManager persistenceManager;
    private JButton closeButton;
    private JLabel unpairedParticipant;
    private JButton printQualityButton; // New button
    private JComboBox<Criteria> rankingCriteriaComboBox;


    public ListView() {
        listViewPanel = new JPanel(new BorderLayout());
        rankingCriteriaComboBox = new JComboBox<>(Criteria.values());
        rankingCriteriaComboBox.addActionListener(e -> sortPairsBySelectedCriteria());
        // Initialize tables with empty data
        String[] successorColumnNames = {"ID", "Name", "Age", "Gender"};
        successorTableModel = new CustomTableModel(new Object[][]{}, successorColumnNames);
        successorTable = new JTable(successorTableModel);

        String[] pairColumnNames = {"Pair ID", "Participant 1", "Participant 2", "Kitchen Location", "Distance to Party"};
        pairTableModel = new CustomTableModel(new Object[][]{}, pairColumnNames);
        pairTable = new JTable(pairTableModel);

        successorScrollPane = new JScrollPane(successorTable);
        pairScrollPane = new JScrollPane(pairTable);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 8));
        loadButton = new JButton("Load Data");
        assignButton = new JButton("Assign");
        removeButton = new JButton("Remove");
        createPairButton = new JButton("Create Pair");
        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");
        graphButton = new JButton("Generate Graph");
        closeButton = new JButton("Close");
        printQualityButton = new JButton("Print Quality"); // New button
        JPanel criteriaPanel = new JPanel();
        criteriaPanel.add(new JLabel("Ranking Criteria:"));
        criteriaPanel.add(rankingCriteriaComboBox);

        buttonPanel.add(loadButton);
        buttonPanel.add(assignButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(createPairButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);
        buttonPanel.add(graphButton);
        buttonPanel.add(printQualityButton); // Adding the new button
        listViewPanel.add(criteriaPanel, BorderLayout.NORTH);
        buttonPanel.add(closeButton);

        // Create headings for tables
        JLabel successorHeading = new JLabel("Unpaired Participants");
        JLabel pairHeading = new JLabel("Pair Table");

        // Add headings and tables to panel
        JPanel tablePanel = new JPanel(new GridLayout(1, 2)); // 1 row, 2 columns
        JPanel successorPanel = new JPanel(new BorderLayout());
        JPanel pairPanel = new JPanel(new BorderLayout());

        successorPanel.add(successorHeading, BorderLayout.NORTH);
        successorPanel.add(successorScrollPane, BorderLayout.CENTER);

        pairPanel.add(pairHeading, BorderLayout.NORTH);
        pairPanel.add(pairScrollPane, BorderLayout.CENTER);

        tablePanel.add(successorPanel);
        tablePanel.add(pairPanel);

        listViewPanel.add(tablePanel, BorderLayout.CENTER);
        listViewPanel.add(buttonPanel, BorderLayout.SOUTH);

        undoRedoManager = new UndoRedoManager();
        persistenceManager = new PersistenceManager();

        // Load persisted data when the application starts
        persistenceManager.loadState();

        loadButton.addActionListener(e -> loadData());
        assignButton.addActionListener(e -> assignSuccessorToPair());
        removeButton.addActionListener(e -> removeFromPairList());
        createPairButton.addActionListener(e -> createPair());
        undoButton.addActionListener(e -> undoAction());
        redoButton.addActionListener(e -> redoAction());
        graphButton.addActionListener(e -> generateGraph());
        printQualityButton.addActionListener(e -> printPairListQuality()); // Action listener for the new button

        closeButton.addActionListener(e -> {
            listViewPanel.setVisible(false); // Hide the listViewPanel on close button click
        });
    }
    private void printPairListQuality() {
        // Calculate and print the quality metrics of the pair list
        PairList pairList = DataHandler.getInstance().getPairList();

        double avgAgeDifference = pairList.calculateAverageAgeDifference();
        double genderDiversity = pairList.calculateGenderDiversity();
        double preferenceDeviation = pairList.calculatePreferenceDeviation();

        String message = String.format("Pair List Quality:\n\nAverage Age Difference: %.2f\nGender Diversity: %.2f\nPreference Deviation: %.2f",
                avgAgeDifference, genderDiversity, preferenceDeviation);

        JOptionPane.showMessageDialog(null, message, "Pair List Quality", JOptionPane.INFORMATION_MESSAGE);
    }
    private void loadData() {
        // Load successor list data
        List<Participant> successors = DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList();
        String[] successorColumnNames = {"ID", "Name", "Age", "Gender"};
        Object[][] successorData = new Object[successors.size()][4];
        for (int i = 0; i < successors.size(); i++) {
            Participant p = successors.get(i);
            successorData[i][0] = p.getId();
            successorData[i][1] = p.getName();
            successorData[i][2] = p.getAge();
            successorData[i][3] = p.getGender();
        }
        successorTableModel.setDataVector(successorData, successorColumnNames);

        // Load pair list data
        List<Pair> pairs = DataHandler.getInstance().getPairList().getPairList();
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
        pairTableModel.setDataVector(pairData, pairColumnNames);
    }

    private void sortPairsBySelectedCriteria() {
        Criteria selectedCriteria = (Criteria) rankingCriteriaComboBox.getSelectedItem();
        if (selectedCriteria != null) {
            PairList pairList = DataHandler.getInstance().getPairList();
            pairList.sortByCriteria(selectedCriteria);
            loadData(); // Refresh the table with sorted data
        }
    }

    private void assignSuccessorToPair() {
        int selectedSuccessorRow = successorTable.getSelectedRow();
        int selectedPairRow = pairTable.getSelectedRow();
        if (selectedSuccessorRow != -1 && selectedPairRow != -1) {
            Participant successor = DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().get(selectedSuccessorRow);
            Pair pair = DataHandler.getInstance().getPairList().getPairList().get(selectedPairRow);

            Participant oldParticipant1 = pair.getParticipant_1();
            Participant oldParticipant2 = pair.getParticipant_2();

            if (oldParticipant1 != null && oldParticipant2 != null) {
                // Both slots are occupied, ask which one to replace
                Object[] options = {oldParticipant1.getName(), oldParticipant2.getName()};
                int choice = JOptionPane.showOptionDialog(null,
                        "Both participants are already assigned. Which one would you like to replace?",
                        "Choose Participant",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, options, options[0]);

                if (choice == 0) {
                    pair.setParticipant_1(successor);
                    DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().add(oldParticipant1);
                } else if (choice == 1) {
                    pair.setParticipant_2(successor);
                    DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().add(oldParticipant2);
                } else {
                    return; // No choice made
                }
            } else if (oldParticipant1 == null) {
                pair.setParticipant_1(successor);
            } else {
                pair.setParticipant_2(successor);
            }

            DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().remove(successor);

            // Save state for undo/redo
            Runnable undoAction = () -> {
                pair.setParticipant_1(oldParticipant1);
                pair.setParticipant_2(oldParticipant2);
                DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().add(successor);
                DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().remove(oldParticipant1);
                DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().remove(oldParticipant2);
                loadData();
            };

            Runnable redoAction = () -> {
                pair.setParticipant_1(successor);
                pair.setParticipant_2(oldParticipant2);
                DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().remove(successor);
                DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().add(oldParticipant1);
                DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().add(oldParticipant2);
                loadData();
            };

            undoRedoManager.saveState(undoAction, redoAction);

            // Persist the state
            persistenceManager.saveState();

            loadData(); // Reload data to reflect changes
        } else {
            JOptionPane.showMessageDialog(null, "Please select a successor and a pair to assign.", "Invalid Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removeFromPairList() {
        int selectedRow = pairTable.getSelectedRow();
        if (selectedRow != -1) {
            Pair pair = DataHandler.getInstance().getPairList().getPairList().get(selectedRow);

            Participant oldParticipant1 = pair.getParticipant_1();
            Participant oldParticipant2 = pair.getParticipant_2();

            // Save current state for undo
            Runnable undoAction = () -> {
                pair.setParticipant_1(oldParticipant1);
                pair.setParticipant_2(oldParticipant2);
                DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().remove(oldParticipant1);
                DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().remove(oldParticipant2);
                loadData();
            };

            // Perform the removal
            pair.setParticipant_1(null);
            pair.setParticipant_2(null);
            DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().add(oldParticipant1);
            DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().add(oldParticipant2);

            // Save new state for redo
            Runnable redoAction = () -> {
                pair.setParticipant_1(null);
                pair.setParticipant_2(null);
                DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().add(oldParticipant1);
                DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().add(oldParticipant2);
                loadData();
            };

            // Save the action for undo/redo
            undoRedoManager.saveState(undoAction, redoAction);

            // Persist the state
            persistenceManager.saveState();

            loadData(); // Reload data to reflect changes
        } else {
            JOptionPane.showMessageDialog(null, "Please select a pair to remove.", "Invalid Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void createPair() {
        int[] selectedRows = successorTable.getSelectedRows();
        if (selectedRows.length == 2) {
            Participant participant1 = DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().get(selectedRows[0]);
            Participant participant2 = DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().get(selectedRows[1]);

            Pair newPair = new Pair(participant1, participant2);

            // Save current state for undo
            Runnable undoAction = () -> {
                DataHandler.getInstance().getPairList().getPairList().remove(newPair);
                DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().add(participant1);
                DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().add(participant2);
                loadData(); // Reload data to reflect changes
            };

            // Perform the creation and removal from successor list
            DataHandler.getInstance().getPairList().getPairList().add(newPair);
            DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().remove(participant1);
            DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().remove(participant2);

            // Save new state for redo
            Runnable redoAction = () -> {
                DataHandler.getInstance().getPairList().getPairList().add(newPair);
                DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().remove(participant1);
                DataHandler.getInstance().getPairList().getSuccessorList().getParticipantList().remove(participant2);
                loadData(); // Reload data to reflect changes
            };

            // Save the action for undo/redo
            undoRedoManager.saveState(undoAction, redoAction);

            // Persist the state
            persistenceManager.saveState();

            loadData(); // Reload data to reflect changes
        } else {
            JOptionPane.showMessageDialog(null, "Please select exactly two successors to create a pair.", "Invalid Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void undoAction() {
        undoRedoManager.undo();
        loadData(); // Refresh the UI after undo
    }

    private void redoAction() {
        undoRedoManager.redo();
        loadData(); // Refresh the UI after redo
    }

    private void generateGraph() {
        int[] selectedRows = pairTable.getSelectedRows();
        if (selectedRows.length > 0) {
            List<Double> longitudes = new ArrayList<>();
            List<Double> latitudes = new ArrayList<>();

            for (int row : selectedRows) {
                Pair pair = DataHandler.getInstance().getPairList().getPairList().get(row);
                Participant p1 = pair.getParticipant_1();
                Participant p2 = pair.getParticipant_2();

                if (p1 != null && p2 != null) {
                    longitudes.add(p1.getKitchenLocation().getLongitude());
                    latitudes.add(p1.getKitchenLocation().getLatitude());
                    longitudes.add(p2.getKitchenLocation().getLongitude());
                    latitudes.add(p2.getKitchenLocation().getLatitude());
                }
            }

            ScatterPlotPanel scatterPlotPanel = new ScatterPlotPanel(longitudes, latitudes);
            JFrame chartFrame = new JFrame("Pairs Graph");
            chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            chartFrame.getContentPane().add(scatterPlotPanel);
            chartFrame.setSize(800, 600);
            chartFrame.setLocationRelativeTo(null);
            chartFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Please select at least one pair to generate the graph.", "No Pairs Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    public JPanel getListViewPanel() {
        return listViewPanel;
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
