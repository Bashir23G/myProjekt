package Model;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SuccessorListView {
    private JPanel successorListPanel;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton loadButton;

    public SuccessorListView() {
        successorListPanel = new JPanel(new BorderLayout());

        table = new JTable();
        scrollPane = new JScrollPane(table);
        loadButton = new JButton("Load Successor List");

        loadButton.addActionListener(e -> loadSuccessorList());

        successorListPanel.add(scrollPane, BorderLayout.CENTER);
        successorListPanel.add(loadButton, BorderLayout.SOUTH);
    }

    private void loadSuccessorList() {
        ParticipantList successorList = DataHandler.getInstance().getSuccessorParticipants();
        List<Participant> participants = successorList.getParticipantList();

        String[] columnNames = {"ID", "Name", "Age", "Gender"};
        Object[][] data = new Object[participants.size()][4];

        for (int i = 0; i < participants.size(); i++) {
            Participant p = participants.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getName();
            data[i][2] = p.getAge();
            data[i][3] = p.getGender();
        }

        table.setModel(new CustomTableModel(data, columnNames));
    }

    public JPanel getSuccessorListPanel() {
        return successorListPanel;
    }

    public JTable getSuccessorListTable() {
        return table;
    }
}
