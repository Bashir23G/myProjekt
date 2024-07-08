package View;

import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GroupListVisualization {
    private JPanel groupListPanel;
    private JTable groupTable;
    private JTable unassignedPairsTable;
    private JScrollPane groupScrollPane;
    private JScrollPane unassignedPairsScrollPane;
    private JButton loadButton;
    private JButton removeButton;
    private JButton assignButton;
    private JButton createGroupButton;
    private JButton undoButton;
    private JButton redoButton;
    private CustomTableModel groupTableModel;
    private CustomTableModel unassignedPairsTableModel;
    private UndoRedoManager undoRedoManager;
    private JComboBox<Criteria> criteriaComboBox;

    private PersistenceManager persistenceManager;
    private JButton closeButton;
    private JButton printQualityButton;

    public GroupListVisualization() {
        groupListPanel = new JPanel(new BorderLayout());

        // Initialize tables with empty data
        String[] groupColumnNames = {"Group ID", "Participants"};
        groupTableModel = new CustomTableModel(new Object[][]{}, groupColumnNames);
        groupTable = new JTable(groupTableModel);

        String[] pairColumnNames = {"Pair ID", "Participant 1", "Participant 2"};
        unassignedPairsTableModel = new CustomTableModel(new Object[][]{}, pairColumnNames);
        unassignedPairsTable = new JTable(unassignedPairsTableModel);

        groupScrollPane = new JScrollPane(groupTable);
        unassignedPairsScrollPane = new JScrollPane(unassignedPairsTable);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 8));
        loadButton = new JButton("Load Data");
        assignButton = new JButton("Assign");
        removeButton = new JButton("Remove");
        createGroupButton = new JButton("Create Group");
        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");
        closeButton = new JButton("Close");
        printQualityButton = new JButton("Print Quality");
        criteriaComboBox = new JComboBox<>(Criteria.values());

        buttonPanel.add(loadButton);
        buttonPanel.add(assignButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(createGroupButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);
        buttonPanel.add(printQualityButton);
        buttonPanel.add(criteriaComboBox);
        buttonPanel.add(closeButton);

        JLabel unassignedPairsHeading = new JLabel("Ungrouped Pairs");
        JLabel groupTableHeading = new JLabel("Group Table");

        JPanel tablePanel = new JPanel(new GridLayout(1, 2));
        JPanel unassignedPairsPanel = new JPanel(new BorderLayout());
        JPanel groupPanel = new JPanel(new BorderLayout());

        unassignedPairsPanel.add(unassignedPairsHeading, BorderLayout.NORTH);
        unassignedPairsPanel.add(unassignedPairsScrollPane, BorderLayout.CENTER);

        groupPanel.add(groupTableHeading, BorderLayout.NORTH);
        groupPanel.add(groupScrollPane, BorderLayout.CENTER);

        tablePanel.add(unassignedPairsPanel);
        tablePanel.add(groupPanel);

        groupListPanel.add(tablePanel, BorderLayout.CENTER);
        groupListPanel.add(buttonPanel, BorderLayout.SOUTH);

        undoRedoManager = new UndoRedoManager();
        persistenceManager = new PersistenceManager();

        persistenceManager.loadState();

        loadButton.addActionListener(e -> loadData());
        assignButton.addActionListener(e -> assignPairToGroup());
        removeButton.addActionListener(e -> removeFromGroupList());
        createGroupButton.addActionListener(e -> createNewGroup());
        undoButton.addActionListener(e -> undoAction());
        redoButton.addActionListener(e -> redoAction());
        closeButton.addActionListener(e -> {
            groupListPanel.setVisible(false);
        });
        criteriaComboBox.addActionListener(e -> sortGroupsByCriteria());

        printQualityButton.addActionListener(e -> printGroupListQuality());
    }

    private void loadData() {
        GroupList groupList = DataHandler.getInstance().getGroupList();
        List<Group> groups = groupList.getAllGroups();

        String[] groupColumnNames = {"Group ID", "Participants"};
        Object[][] groupData = new Object[groups.size()][2];
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            StringBuilder participants = new StringBuilder();
            for (Pair pair : group.getPairs()) {
                participants.append(pair.getParticipant_1().getName())
                        .append(" & ")
                        .append(pair.getParticipant_2().getName())
                        .append(", ");
            }
            if (participants.length() > 0) {
                participants.setLength(participants.length() - 2);
            }
            groupData[i][0] = group.getId();
            groupData[i][1] = participants.toString();
        }
        groupTableModel.setDataVector(groupData, groupColumnNames);

        List<Pair> unassignedPairs = DataHandler.getInstance().getPairList().getUnassignedPairs();
        String[] pairColumnNames = {"Pair ID", "Participant 1", "Participant 2"};
        Object[][] pairData = new Object[unassignedPairs.size()][3];
        for (int i = 0; i < unassignedPairs.size(); i++) {
            Pair pair = unassignedPairs.get(i);
            pairData[i][0] = pair.getPairId();
            pairData[i][1] = (pair.getParticipant_1() != null) ? pair.getParticipant_1().getName() : "None";
            pairData[i][2] = (pair.getParticipant_2() != null) ? pair.getParticipant_2().getName() : "None";
        }
        unassignedPairsTableModel.setDataVector(pairData, pairColumnNames);
    }

    private void sortGroupsByCriteria() {
        Criteria selectedCriteria = (Criteria) criteriaComboBox.getSelectedItem();
        if (selectedCriteria == null) {
            return;
        }

        GroupList groupList = DataHandler.getInstance().getGroupList();
        List<Group> groups = groupList.getAllGroups();

        switch (selectedCriteria) {
            case minimizeLeftoverParticipants:
                groups.sort(Comparator.comparingInt(Group::getNumberOfLeftoverParticipants));
                break;
            case foodPreference:
                groups.sort(Comparator.comparing(Group::getFoodPreferenceScore));
                break;
            case genderDiversity:
                groups.sort(Comparator.comparing(Group::getGenderDiversityScore));
                break;
            case ageDifference:
                groups.sort(Comparator.comparing(Group::getAgeDifferenceScore));
                break;

        }

        String[] groupColumnNames = {"Group ID", "Participants"};
        Object[][] groupData = new Object[groups.size()][2];
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            StringBuilder participants = new StringBuilder();
            for (Pair pair : group.getPairs()) {
                participants.append(pair.getParticipant_1().getName())
                        .append(" & ")
                        .append(pair.getParticipant_2().getName())
                        .append(", ");
            }
            if (participants.length() > 0) {
                participants.setLength(participants.length() - 2);
            }
            groupData[i][0] = group.getId();
            groupData[i][1] = participants.toString();
        }
        groupTableModel.setDataVector(groupData, groupColumnNames);
    }

    private void printGroupListQuality() {
        GroupList groupList = DataHandler.getInstance().getGroupList();
        String groupListQuality = String.valueOf(groupList.getGroupListQuality());
        JOptionPane.showMessageDialog(groupListPanel, groupListQuality, "Group List Quality", JOptionPane.INFORMATION_MESSAGE);
    }

    private void assignPairToGroup() {
        int selectedGroupRow = groupTable.getSelectedRow();
        int selectedPairRow = unassignedPairsTable.getSelectedRow();
        if (selectedGroupRow != -1 && selectedPairRow != -1) {
            GroupList groupList = DataHandler.getInstance().getGroupList();
            List<Group> groups = groupList.getAllGroups();
            Group group = groups.get(selectedGroupRow);

            List<Pair> unassignedPairs = new ArrayList<>();
            for (Pair pair : DataHandler.getInstance().getPairList().getPairList()) {
                if (!pair.isAssignedToGroup()) {
                    unassignedPairs.add(pair);
                }
            }

            Pair pair = unassignedPairs.get(selectedPairRow);
            if (pair != null) {
                group.addPair(pair);
                pair.setAssignedToGroup(true);

                Runnable undoAction = () -> {
                    group.removePair(pair);
                    pair.setAssignedToGroup(false);
                    loadData();
                };

                Runnable redoAction = () -> {
                    group.addPair(pair);
                    pair.setAssignedToGroup(true);
                    loadData();
                };

                undoRedoManager.saveState(undoAction, redoAction);

                loadData();
                persistenceManager.saveState();
            }
        }
    }

    private void removeFromGroupList() {
        int selectedGroupRow = groupTable.getSelectedRow();
        if (selectedGroupRow != -1) {
            GroupList groupList = DataHandler.getInstance().getGroupList();
            List<Group> groups = groupList.getAllGroups();
            Group group = groups.get(selectedGroupRow);

            List<Pair> removedPairs = new ArrayList<>(group.getPairs());

            Runnable undoAction = () -> {
                groupList.addGroup(group);
                for (Pair pair : removedPairs) {
                    pair.setAssignedToGroup(true);
                }
                loadData();
            };

            groupList.removeGroup(group);
            for (Pair pair : removedPairs) {
                pair.setAssignedToGroup(false);
            }

            Runnable redoAction = () -> {
                groupList.removeGroup(group);
                for (Pair pair : removedPairs) {
                    pair.setAssignedToGroup(false);
                }
                loadData();
            };

            undoRedoManager.saveState(undoAction, redoAction);

            loadData();
            persistenceManager.saveState();
        }
    }

    private void createNewGroup() {
        int[] selectedRows = unassignedPairsTable.getSelectedRows();
        if (selectedRows.length != 3) {
            JOptionPane.showMessageDialog(groupListPanel, "Please select exactly 3 pairs to create a group.");
            return;
        }

        List<Pair> selectedPairs = new ArrayList<>();
        List<Pair> unassignedPairs = DataHandler.getInstance().getPairList().getUnassignedPairs();

        for (int row : selectedRows) {
            Pair pair = unassignedPairs.get(row);
            selectedPairs.add(pair);
            pair.setAssignedToGroup(true);
        }

        Pair hostingPair = selectedPairs.get(0);

        Group newGroup = new Group(selectedPairs, Dish.STARTER, hostingPair);
        DataHandler.getInstance().getGroupList().addGroup(newGroup);

        for (Pair pair : selectedPairs) {
            unassignedPairs.remove(pair);
        }

        Runnable undoAction = () -> {
            DataHandler.getInstance().getGroupList().removeGroup(newGroup);
            for (Pair pair : selectedPairs) {
                unassignedPairs.add(pair);
                pair.setAssignedToGroup(false);
            }
            loadData();
        };

        Runnable redoAction = () -> {
            DataHandler.getInstance().getGroupList().addGroup(newGroup);
            for (Pair pair : selectedPairs) {
                unassignedPairs.remove(pair);
                pair.setAssignedToGroup(true);
            }
            loadData();
        };

        undoRedoManager.saveState(undoAction, redoAction);

        loadData();
        persistenceManager.saveState();
    }

    private void undoAction() {
        undoRedoManager.undo();
        loadData();
    }

    private void redoAction() {
        undoRedoManager.redo();
        loadData();
    }

    public JPanel getGroupListPanel() {
        return groupListPanel;
    }

    private class CustomTableModel extends DefaultTableModel {
        public CustomTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}
