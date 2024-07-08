package Model;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Stack;

public class PairListView {
    private JPanel pairListPanel;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton loadButton;
    private JButton moveButton;
    private JButton undoButton;
    private JButton redoButton;
    private JButton removeButton;

    private Stack<Pair> undoStack = new Stack<>();
    private Stack<Pair> redoStack = new Stack<>();

    // Assuming you have a reference to the successor list component
    private JTable successorListTable;

    public PairListView(JTable successorListTable) {
        this.successorListTable = successorListTable;

        pairListPanel = new JPanel(new BorderLayout());

        table = new JTable();
        scrollPane = new JScrollPane(table);
        loadButton = new JButton("Load Pair List");
        moveButton = new JButton("Move to Pair List");
        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");
        removeButton = new JButton("Remove");

        loadButton.addActionListener(e -> loadPairList());
        moveButton.addActionListener(e -> moveToPairList());
        undoButton.addActionListener(e -> undo());
        redoButton.addActionListener(e -> redo());
        removeButton.addActionListener(e -> removePair());

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        buttonPanel.add(moveButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);
        buttonPanel.add(removeButton);

        pairListPanel.add(scrollPane, BorderLayout.CENTER);
        pairListPanel.add(loadButton, BorderLayout.NORTH);
        pairListPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadPairList() {
        PairList pairList = DataHandler.getInstance().getPairList();
        List<Pair> pairs = pairList.getPairList();

        String[] columnNames = {"Pair ID", "Participant 1", "Participant 2", "Kitchen Location", "Distance to Party"};
        Object[][] data = new Object[pairs.size()][5];

        for (int i = 0; i < pairs.size(); i++) {
            Pair p = pairs.get(i);
            data[i][0] = p.getPairId();
            data[i][1] = p.getParticipant_1().getName();
            data[i][2] = p.getParticipant_2().getName();
            data[i][3] = p.getKitchenLocation().toString();
            data[i][4] = p.getDistanceToPartyLocation();
        }

        table.setModel(new CustomTableModel(data, columnNames));
    }

    private void moveToPairList() {
        Participant selectedSuccessor = getSelectedSuccessor();
        Participant anotherParticipant = getAnotherParticipant();

        if (selectedSuccessor != null && anotherParticipant != null) {
            Pair newPair = new Pair(generatePairId(), selectedSuccessor, anotherParticipant);
            DataHandler.getInstance().addPair(newPair);
            undoStack.push(newPair);
            loadPairList();
        }
    }

    private void undo() {
        if (!undoStack.isEmpty()) {
            Pair lastAction = undoStack.pop();
            redoStack.push(lastAction);
            DataHandler.getInstance().removePair(lastAction);
            loadPairList();
        }
    }

    private void redo() {
        if (!redoStack.isEmpty()) {
            Pair lastUndo = redoStack.pop();
            undoStack.push(lastUndo);
            DataHandler.getInstance().addPair(lastUndo);
            loadPairList();
        }
    }

    private void removePair() {
        Pair selectedPair = getSelectedPair();
        if (selectedPair != null) {
            DataHandler.getInstance().removePair(selectedPair);
            undoStack.push(selectedPair);
            loadPairList();
        }
    }

    public JPanel getPairListPanel() {
        return pairListPanel;
    }

    private Participant getSelectedSuccessor() {
        int selectedRow = successorListTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Assuming the first column in your table model holds Participant IDs as Strings
            String participantId = (String) successorListTable.getValueAt(selectedRow, 0);

            // Retrieve Participant object using the identifier from the table
            return findParticipantById(participantId);
        }
        return null;
    }

    private Participant findParticipantById(String participantId) {
        // Implement a method to find and return Participant object by ID
        List<Participant> participants = DataHandler.getInstance().getSuccessorParticipants().getParticipantList();
        for (Participant participant : participants) {
            if (participant.getId().equals(participantId)) {
                return participant;
            }
        }
        return null; // Return null if no Participant found with given ID
    }


    private Participant getAnotherParticipant() {
        int selectedRow = successorListTable.getSelectedRow();
        if (selectedRow >= 0) {
            return (Participant) successorListTable.getValueAt(selectedRow, 0); // Assuming the Participant object is in the first column
        }
        return null;
    }

    private Pair getSelectedPair() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int pairId = (int) table.getValueAt(selectedRow, 0);
            return DataHandler.getInstance().getPairList().getPairById(pairId); // Assuming a method to get a Pair by its ID
        }
        return null;
    }

    private int generatePairId() {
        // Implementation to generate a unique pair ID
        return DataHandler.getInstance().generateUniquePairId();
    }
}
