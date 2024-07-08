package Controller;

import Model.FoodPreference;
import Model.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.io.Serializable;

public class CSVWriter implements Serializable {

    public static void writePairsToCSV(List<Pair> pairs, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Pair pair : pairs) {
                String participant1Name = pair.getParticipant_1().getName();
                String participant2Name = pair.getParticipant_2().getName();
                int isRegisteredTogether = pair.getParticipant_1().isIs_registered_together() ? 1 : 0;
                double kitchenLongitude = pair.getKitchenLocation().getLongitude();
                double kitchenLatitude = pair.getKitchenLocation().getLatitude();
                String foodPreference = pair.getFoodPreference().toString();
                int pairId = pair.getPairId();
                int starterGroup = pair.getStarterGroupId();
                int mainGroup = pair.getMainGroupId();
                int dessertGroup = pair.getDessertGroupId();
                boolean participantThatProvidesKitchen = pair.isParticipantThatProvidesKitchen();
                int hostingDish = pair.getHostingDish().getIntValue();

                // Locale.US um Double werte mit . zu trennen, statt komma
                String csvLine = String.format(Locale.US, "%s;%s;%d;%s;%s;%s;%d;%d;%d;%d;%b;%d",
                        participant1Name, participant2Name, isRegisteredTogether, kitchenLongitude, kitchenLatitude,
                        foodPreference, pairId, starterGroup, mainGroup, dessertGroup, participantThatProvidesKitchen, hostingDish);

                writer.write(csvLine + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
