import Model.*;
import View.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static JFrame frame;
    private static JPanel mainPanel;
    private static CardLayout cardLayout;

    public static void main(String[] args) {
        // Initialize Data
        List<Criteria> rankingList = new ArrayList<>();
        rankingList.add(Criteria.foodPreference);
        rankingList.add(Criteria.minimizeLeftoverParticipants);
        rankingList.add(Criteria.genderDiversity);
        rankingList.add(Criteria.minimizePathLength);
        rankingList.add(Criteria.ageDifference);

        DataHandler.getInstance().setCriteriaRankingList(rankingList);
        DataHandler.getInstance().initialize("SpinfoodProject/SpinfoodProject/SpinfoodProject/Data/teilnehmerliste.csv", "SpinfoodProject/SpinfoodProject/SpinfoodProject/Data/partylocation.csv");
        DataHandler.getInstance().generatePairsFromParticipantList();
        DataHandler.getInstance().generateGroupsFromPairList(1000);

        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Event Organizer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 600);

            mainPanel = new JPanel(new CardLayout());
            cardLayout = (CardLayout) mainPanel.getLayout();

            MainMenu mainMenu = new MainMenu();
            ListView listView = new ListView();
            GroupListVisualization groupListVisualization = new GroupListVisualization();
            PairListComparisonView pairListComparison = new PairListComparisonView();
            GroupListComparisonView groupListComparison = new GroupListComparisonView(); // Added GroupListComparison

            mainPanel.add(mainMenu.getMainMenuPanel(), "MainMenu");
            mainPanel.add(listView.getListViewPanel(), "ListView");
            mainPanel.add(groupListVisualization.getGroupListPanel(), "GroupListVisualization");
            mainPanel.add(pairListComparison.getComparisonPanel(), "PairListComparison");
            mainPanel.add(groupListComparison.getComparisonPanel(), "GroupListComparison"); // Added GroupListComparison panel

            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }

    public static void showCard(String cardName) {
        cardLayout.show(mainPanel, cardName);
    }
}

class MainMenu {
    private JPanel mainMenuPanel;

    public MainMenu() {
        mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new GridLayout(5, 1)); // Updated to 5 rows

        JButton listViewButton = new JButton("View Pair List");
        listViewButton.addActionListener(e -> Main.showCard("ListView"));

        JButton groupVisualizationButton = new JButton("View Group List");
        groupVisualizationButton.addActionListener(e -> Main.showCard("GroupListVisualization"));

        JButton pairComparisonButton = new JButton("Compare Pair Lists");
        pairComparisonButton.addActionListener(e -> Main.showCard("PairListComparison"));

        JButton groupComparisonButton = new JButton("Compare Group Lists");
        groupComparisonButton.addActionListener(e -> Main.showCard("GroupListComparison")); // Added GroupListComparison button

        mainMenuPanel.add(listViewButton);
        mainMenuPanel.add(groupVisualizationButton);
        mainMenuPanel.add(pairComparisonButton); // Added pair comparison button
        mainMenuPanel.add(groupComparisonButton); // Added group comparison button
    }

    public JPanel getMainMenuPanel() {
        return mainMenuPanel;
    }
}
