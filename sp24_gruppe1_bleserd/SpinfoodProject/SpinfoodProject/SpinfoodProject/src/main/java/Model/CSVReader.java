package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.io.Serializable;

public class CSVReader implements Serializable {
    private static final int ID_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int FOOD_PREFERENCE_INDEX = 3;
    private static final int AGE_INDEX = 4;
    private static final int GENDER_INDEX = 5;
    private static final int KITCHEN_INDEX = 6;
    private static final int KITCHEN_STORY_INDEX = 7;
    private static final int KITCHEN_LOCATION_LON_INDEX = 8;
    private static final int KITCHEN_LOCATION_LAT_INDEX = 9;
    private static final int SECOND_PARTICIPANT_ID_INDEX = 10;
    private static final int SECOND_PARTICIPANT_NAME_INDEX = 11;
    private static final int SECOND_PARTICIPANT_AGE_INDEX = 12;
    private static final int SECOND_PARTICIPANT_GENDER_INDEX = 13;

    /**
     *
     * @param filePath, Pfad zur Teilnehmerliste
     * @param participantList, Teilnehmerliste (Alle Teilnehhmer, die noch keinen Partner haben)
     * @param pairList Pärchenliste
     * extrahiert aus der gegebenen Teilnehmerliste, alle Pärchen und Einzelanmeldungen. Alle Pärchenanmeldungen
     * werden in PairList gespeichert und alle Einzelanmeldungen in ParticipantList
     */
    public void generateParticipantsAndPairsFromCSV(String filePath, ParticipantList participantList, PairList pairList) {
        File file = new File(filePath);
        int currentLine = 0;

        if (file.length() == 0) {
            System.out.println("The CSV file is empty.");
            return;
        }
        try (Scanner scanner = new Scanner(file)) {
            skipFirstLine(scanner);

            while (scanner.hasNextLine()) {
                currentLine++;
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if(parts.length < 7 || parts.length > 14) {
                    throw new IllegalArgumentException("Fehler in Formatierung, zu wenig oder zu viele Einträge in Zeile: " + currentLine);
                }

                Participant participant1 = createParticipant(parts);
                if (isCoupleRegistration(parts)) {
                    Participant participant2 = createSecondParticipant(parts, participant1);
                    participant1.setPartnerParticipant(participant2);
                    participant1.setIs_registered_together(true);

                    Pair pair = new Pair(participant1, participant2);
                    pairList.addPairToList(pair);
                } else {
                    participantList.add(participant1);
                }

            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Location generatePartyLocationFromCSV(String filePath) {
        File file = new File(filePath);
        Location partyLocation;

        try(Scanner scanner =  new Scanner(file)) {
            skipFirstLine(scanner);

            String[] parts = scanner.nextLine().split(",");
            partyLocation = new Location(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return partyLocation;
    }

    public Participant createParticipant(String[] parts) {
        String id = parts[ID_INDEX];
        String name = parts[NAME_INDEX];
        FoodPreference foodPreference;
        if(parts[FOOD_PREFERENCE_INDEX].equals("none")) {
             foodPreference = FoodPreference.ANY;
        } else {
             foodPreference = FoodPreference.valueOf(parts[FOOD_PREFERENCE_INDEX].toUpperCase());
        }
        int age = Integer.parseInt(parts[AGE_INDEX]);
        Gender gender = Gender.valueOf(parts[GENDER_INDEX].toUpperCase());
        Kitchen kitchen = Kitchen.valueOf(parts[KITCHEN_INDEX].toUpperCase());


        // Setzen Kichenlocation auf -1, -1 wenn keine Kitchen vorhanden
        if(kitchen == Kitchen.NO) {
            return new Participant(id, name, foodPreference, age, gender, kitchen, 0, new Location(-1, -1), null, false);
        }

        double kitchenStory = parseDouble(parts[KITCHEN_STORY_INDEX]);
        Location kitchenLocation = new Location(parseDouble(parts[KITCHEN_LOCATION_LON_INDEX]), parseDouble(parts[KITCHEN_LOCATION_LAT_INDEX]), parseDouble(parts[KITCHEN_STORY_INDEX]));
        return new Participant(id, name, foodPreference, age, gender, kitchen, kitchenStory, kitchenLocation, null, false);
    }

    /**
     *
     * @param parts, Informationen der Anmeldung
     * @param participant1, zuvor erstellter Teilnehmer
     * @return Partner von participant2
     * Wird aufgerufen wenn es eine Pärchenanmeldung ist, generiert neuen Teilnehmer
     */
    private Participant createSecondParticipant(String[] parts, Participant participant1) {
        String id = parts[SECOND_PARTICIPANT_ID_INDEX];
        String name = parts[SECOND_PARTICIPANT_NAME_INDEX];
        int age = (int) Double.parseDouble(parts[SECOND_PARTICIPANT_AGE_INDEX]);
        Gender gender = Gender.valueOf(parts[SECOND_PARTICIPANT_GENDER_INDEX].toUpperCase());

        return new Participant(id, name, participant1.getFoodPreference(), age, gender, participant1.getKitchen(),
                participant1.getKitchenStory(), participant1.getKitchenLocation(), participant1, true);
    }

    private void skipFirstLine(Scanner scanner) {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }


    /**
     *
     * @param parts, Details der Anmeldung
     * @return
     * Prüft, ob Einzelanmeldung oder Pärchenanmeldung, in dem es die Länge der partsArray analysiert
     */
    public boolean isCoupleRegistration(String[] parts) {
        return parts.length > SECOND_PARTICIPANT_ID_INDEX;
    }

    /**
     *
     * @param value, kitchen_story
     * @return
     * Da Kitchen_Story auch null sein kann, wird diese Methode verwendet, um null gegen 0.0 auszutauschen.
     */
    private double parseDouble(String value) {
        return value.isEmpty() ? 0.0 : Double.parseDouble(value);
    }



}
