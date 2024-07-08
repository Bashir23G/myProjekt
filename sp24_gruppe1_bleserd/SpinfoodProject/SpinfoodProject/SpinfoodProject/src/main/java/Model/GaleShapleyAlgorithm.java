package Model;

import java.io.Serializable;
import java.util.*;

public class GaleShapleyAlgorithm implements Serializable {
    private final int criteriaMultiplier = 10;

    /**
     *
     * @param pairGenerationCriteriaRanking, Absolutes Ranking welches gewählt wurde
     * @param pairList, bisherige Pärchenliste, welche wir mit Pärchen füllen
     * @param participantList, alle Teilnehmer, die bisher noch keinen Partner haben
     * Erstellt gemäß Anforderungen die Pärchen aus den Teilnehmern, Teilnehmer ohne gefundenen Partner sind in freeParticipants zu finden
     */
    public void generatePairsFromParticipantList(List<Criteria> pairGenerationCriteriaRanking, PairList pairList, ParticipantList participantList) {
        if (pairGenerationCriteriaRanking == null) {
            throw new IllegalArgumentException("Criteria ranking list cannot be null.");
        }
        // Nicht relevante Kriterien für Pärchenerstellung werden entfernt
        pairGenerationCriteriaRanking.remove(Criteria.minimizePathLength);
        int minimizeLeftoverParticipantsRank = (pairGenerationCriteriaRanking.indexOf(Criteria.minimizeLeftoverParticipants)) +1;
        pairGenerationCriteriaRanking.remove(minimizeLeftoverParticipantsRank -1) ;

        // Definieren der Multiplier zur Berücksichtigung des vom Verwender erstellten Rankings
        int ageDifferenceMultiplier = (4 - (pairGenerationCriteriaRanking.indexOf(Criteria.ageDifference) +1)) * criteriaMultiplier;
        int foodPreferenceMultiplier = (4 - (pairGenerationCriteriaRanking.indexOf(Criteria.foodPreference) +1)) * criteriaMultiplier;
        int genderDiversityMultiplier = (4 - (pairGenerationCriteriaRanking.indexOf(Criteria.genderDiversity) +1)) * criteriaMultiplier;

        // Setzen des CutOffPoints, wie gut ein Pair zusammenpassen muss, um es zu erlauben
        int minWeightNeededForPair = switch (minimizeLeftoverParticipantsRank) {
            case 1 -> 0;
            case 2 -> 150;
            case 3 -> 300;
            default -> 400;
        };


        generatePreferredPartnerListForParticipants(minimizeLeftoverParticipantsRank,ageDifferenceMultiplier, foodPreferenceMultiplier, genderDiversityMultiplier, participantList.getParticipantList(), pairList.getPairList());
        findBestPartnerParticipantForFreeParticipants(participantList.getParticipantList());
        generateValidPairs(minWeightNeededForPair, pairList, participantList);
        generatePreferredPartnerListPostAlgorithm(minimizeLeftoverParticipantsRank,ageDifferenceMultiplier, foodPreferenceMultiplier, genderDiversityMultiplier, participantList.getParticipantList(), pairList.getPairList());
    }


    /**
     *
     * @param minimizeLeftoverParticipants, Spezifikation definierter Parameter, der vom Verwender absolut geranked wird
     * @param pairList, Pärchenliste mit allen bisherigen Pärchen
     * @param participantList, Liste der Teilnehmer, die noch keinem Pärchen zugeordent sind
     * Methode generiert erlaubte Pärchen, Teilnehmer die in einem Pärchen sind werden aus der participantList entfernt
     */
    public void  generateValidPairs(int minimizeLeftoverParticipants, PairList pairList, ParticipantList participantList) {
        List<Participant>  unmatchedParticipants =  participantList.getParticipantList();
        List<Participant> participantsToRemove = new ArrayList<>();

        for (Participant participant : unmatchedParticipants) {
            Participant partnerParticipant = participant.getPartnerParticipant();

            // Sollte nur der Fall sein, wenn die übergebene Teilnehmerliste eine ungerade Anzahl an Teilnehmern hat
            if(partnerParticipant == null) {
                continue;
            }

            int weight = participant.getPreferedPartnerList().get(findIndexFromParticipantInPreferredPartnerList(partnerParticipant, participant)).getWeight();

            if (weight > minimizeLeftoverParticipants) {
                if (!participantsToRemove.contains(participant) || !participantsToRemove.contains(partnerParticipant)) {
                    Pair pair = new Pair(participant, partnerParticipant);
                    pairList.addPairToList(pair);

                    participantsToRemove.add(participant);
                    participantsToRemove.add(partnerParticipant);
                }
            }
        }
        unmatchedParticipants.removeAll(participantsToRemove);
        participantList.setParticipantList(unmatchedParticipants);
    }


    /**
     * Algorhtmus zum finden der besten Partnerkonstellationen
     * @param unmatchedParticipants, Teilnehmerliste, die bisher noch keinen Partner haben
     */
    public void findBestPartnerParticipantForFreeParticipants(List<Participant> unmatchedParticipants) {

        for(int i = 0; i < unmatchedParticipants.size(); i++)  {
            Participant participant = unmatchedParticipants.get(i);

            // Aktueller Teilnehmer, hat bereits einen Partner
            if(participant.isPartnered())  {
                Participant currentPartner = participant.getPartnerParticipant();
                int currentPartnerIndex = findIndexFromParticipantInPreferredPartnerList(currentPartner, participant);

                for(int preferredPartnerIndex = 0; preferredPartnerIndex < participant.getPreferedPartnerList().size(); preferredPartnerIndex++) {

                    //Versuchen besseren Partner zu finden
                    if(currentPartnerIndex > preferredPartnerIndex) {
                        Participant preferredPartner = participant.getPreferedPartnerList().get(preferredPartnerIndex).getParticipant();

                        if(preferredPartner.isPartnered()) {
                            //Suche den bevorzugten Partner in der Partnerliste seines aktuellen Partners
                            Participant preferredPartnerCurrentPartner =  preferredPartner.getPartnerParticipant();
                            int preferredPartnerCurrentPartnerIndex = findIndexFromParticipantInPreferredPartnerList(preferredPartnerCurrentPartner, preferredPartner);
                            int participantIndexInPreferredPartnerList =  findIndexFromParticipantInPreferredPartnerList(participant, preferredPartner);

                            //Aktueller Teilnehmer passt besser zur preferredPartner als bisheriger Partner
                            if(preferredPartnerCurrentPartnerIndex > participantIndexInPreferredPartnerList) {
                                unmatchedParticipants = moveParticipantToBeginningOfListAndRemovePartner(preferredPartnerCurrentPartner, unmatchedParticipants);
                                unmatchedParticipants = moveParticipantToBeginningOfListAndRemovePartner(currentPartner, unmatchedParticipants);
                                participant.setPartnerParticipant(preferredPartner);
                                preferredPartner.setPartnerParticipant(participant);
                                i = -1;
                                break;

                            }


                        } else {
                            // Präferierter Partner hat noch keinen Partner
                            unmatchedParticipants = moveParticipantToBeginningOfListAndRemovePartner(currentPartner, unmatchedParticipants);
                            participant.setPartnerParticipant(preferredPartner);
                            preferredPartner.setPartnerParticipant(participant);

                            i = -1;
                            break;
                        }


                    }  else {
                        // Stoppen, wenn aktueller Partner geringeren Index als den bevorzugten hat (Meist sind diese dann gleich, weil sie die selben sind)
                        break;
                    }
                }


            } else {

                // Gehe Prioritätenliste durch und suche passenden Partner
                for(int k = 0; k < participant.getPreferedPartnerList().size(); k++)  {
                    Participant preferredPartner = participant.getPreferedPartnerList().get(k).getParticipant();

                    // Bevorzugter Partner hat bereits einen anderen Partner
                    if(preferredPartner.isPartnered()) {

                        int participantIndexInPreferredPartnerList = findIndexFromParticipantInPreferredPartnerList(participant, preferredPartner); // Index von Teilnehmer in der Bevorzugtenpartnerliste von dem bevorzugten Teilnehmer
                        int currentPartnerIndexFromPreferredPartner = findIndexFromParticipantInPreferredPartnerList(preferredPartner.getPartnerParticipant(), preferredPartner);
                        // PreferredPartner bevorzugt lieber aktuellen Teilnehmer, als seinen bisherigen Partner, setze entfernten Partner an anfang von Liste starte loop neu.
                        if(participantIndexInPreferredPartnerList < currentPartnerIndexFromPreferredPartner) {
                            Participant participantToRemoveAsPartner = preferredPartner.getPartnerParticipant();

                            unmatchedParticipants = moveParticipantToBeginningOfListAndRemovePartner(participantToRemoveAsPartner ,unmatchedParticipants);
                            preferredPartner.setPartnerParticipant(participant);
                            participant.setPartnerParticipant(preferredPartner);

                            i  = -1;
                            break;

                        }

                    } else  {
                        // Beide haben noch keinen Partner, können also Partner werden
                        participant.setPartnerParticipant(preferredPartner);
                        preferredPartner.setPartnerParticipant(participant);

                        break;
                    }

                }

            }
        }
    }


    /**
     *
     * @param participant, Teilnehmer der an den Anfang der Liste verschoben werden soll
     * @param unmatchedParticipants, Teilnehmerliste
     * @return Teilnehmerliste, bei der participant  am Anfang der Liste steht
     */

    public List<Participant>  moveParticipantToBeginningOfListAndRemovePartner(Participant participant, List<Participant> unmatchedParticipants) {
            participant.setPartnerParticipant(null);
            unmatchedParticipants.remove(participant);
            unmatchedParticipants.add(0, participant);
            return unmatchedParticipants;
    }


    /**
     *
     * @param participant, Participant welcher gesucht werden soll
     * @param preferredPartner, Participant in dessen Preferenzenliste nach participant gesucht werden soll
     * @return index von  participant in  Preferenzliste von preferredPartner
     */
    public int findIndexFromParticipantInPreferredPartnerList(Participant participant,  Participant preferredPartner) {

        for(int i = 0; i < preferredPartner.getPreferedPartnerList().size(); i++) {

            if(preferredPartner.getPreferedPartnerList().get(i).getParticipant().getId().equals(participant.getId())) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Erstellt für jeden Teilnehmer, eine Liste mit präferierten Teilnehmern
     * @param ageDifferenceMultiplier, definierter Multiplier, zur Berücksichtigung des erhaltenen Rankings
     * @param foodPreferenceMultiplier, definierter Multiplier, zur Berücksichtigung des erhaltenen Rankings
     * @param genderDiversityMultiplier, definierter Multiplier, zur Berücksichtigung des erhaltenen Rankings
     * @param participants, alle Teilnehmer ohne Partner
     */
    public void generatePreferredPartnerListForParticipants(int minimizeLeftoverParticipantsRanking, int ageDifferenceMultiplier, int foodPreferenceMultiplier, int genderDiversityMultiplier, List<Participant> participants, List<Pair> pairList) {
        setupPreferredPartnerListForParticipant(minimizeLeftoverParticipantsRanking, ageDifferenceMultiplier, foodPreferenceMultiplier, genderDiversityMultiplier, participants);
    }

    private void setupPreferredPartnerListForParticipant(int minimizeLeftoverParticipantsRanking, int ageDifferenceMultiplier, int foodPreferenceMultiplier, int genderDiversityMultiplier, List<Participant> participants) {
        for(Participant current : participants) {
            List<ParticipantWeightPair> preferredPartnerList = new ArrayList<>();

            // Berechnen von weight von current zu jedem Teilnehmer (Hoher weightwert -> passen gut zusammen)
            for(Participant candidate : participants) {
                int weight = 0;

                if(current.getId().equals(candidate.getId())) {
                    continue;
                }

                weight += calculateFoodPreferenceWeight(current.getFoodPreference(), candidate.getFoodPreference(), foodPreferenceMultiplier);
                weight += calculateAgeDifferenceWeight(current.getAgeRange(), candidate.getAgeRange(), ageDifferenceMultiplier);
                weight += calculateGenderDiversityWeight(current.getGender(), candidate.getGender(), genderDiversityMultiplier);
                weight += calculateValidLocationWeight(current.getKitchenLocation(), candidate.getKitchenLocation());
                weight += calculateKitchenAvailabilityWeight(minimizeLeftoverParticipantsRanking, current.getKitchen(), candidate.getKitchen());   // evtl Pärchenzusammstellung ranking verwenden
                preferredPartnerList.add(new ParticipantWeightPair(weight, candidate));
            }

            preferredPartnerList.sort(Comparator.comparingInt(ParticipantWeightPair::getWeight).reversed());
            current.setPreferredPartnerList(preferredPartnerList);
        }
    }


    /**
     * Erstellt präferierte Partnerliste NACH dem Algorithm, wichtig, weil es feste Pärchen gibt, die nur aufgelöst werden sollen, wenn sich eine Person abmeldet
     * @param minimizeLeftoverParticipantsRanking
     * @param ageDifferenceMultiplier
     * @param foodPreferenceMultiplier
     * @param genderDiversityMultiplier
     * @param participants
     * @param pairList
     */
    public void generatePreferredPartnerListPostAlgorithm(int minimizeLeftoverParticipantsRanking, int ageDifferenceMultiplier, int foodPreferenceMultiplier, int genderDiversityMultiplier, List<Participant> participants, List<Pair> pairList) {

        List<Participant> allParticipants = new ArrayList<>(participants);

        for(Pair pair : pairList)  {
            allParticipants.add(pair.getParticipant_1());
            allParticipants.add(pair.getParticipant_2());
        }


        setupPreferredPartnerListForParticipant(minimizeLeftoverParticipantsRanking, ageDifferenceMultiplier, foodPreferenceMultiplier, genderDiversityMultiplier, allParticipants);
    }





    public int calculateFoodPreferenceWeight(FoodPreference  p1Preference, FoodPreference p2Preference, int foodPreferenceMultiplier) {

        // Prüfen auf verbotene Kombination VEGAN/VEGGIE mit MEAT
        if(((p1Preference == FoodPreference.VEGAN || p1Preference == FoodPreference.VEGGIE) &&   p2Preference == FoodPreference.MEAT) ||
            ((p2Preference == FoodPreference.VEGAN || p2Preference == FoodPreference.VEGGIE) && p1Preference == FoodPreference.MEAT)) {
        return 0; // ANFORDERUNGSÄNDERUNG: PÄRCHEN ERLAUBT, sollen dennoh selten sein!
        }

        // MEAT == MEAT (Optimal)
        if(p1Preference == FoodPreference.MEAT && p2Preference == FoodPreference.MEAT)  {
            return 10 * foodPreferenceMultiplier;
        }

        //NONE vorzugsweise mit MEAT
        if((p1Preference == FoodPreference.MEAT && p2Preference == FoodPreference.ANY) ||
                (p1Preference == FoodPreference.ANY && p2Preference == FoodPreference.MEAT)) {
            return 8 * foodPreferenceMultiplier;
        }


        // VEGAN mit VEGGIE (ok)
        if((p1Preference == FoodPreference.VEGAN && p2Preference == FoodPreference.VEGGIE) || (p2Preference == FoodPreference.VEGAN && p1Preference == FoodPreference.VEGGIE) ) {
            return 5 * foodPreferenceMultiplier;
        }


        // NONE sollte bevor es mit NONE gepartnert wird lieber mit VEGAN/VEGGIE (Etwas schlechtere Wertung, da NONE lieber mit MEAT zusammen)

        if((p1Preference == FoodPreference.VEGGIE && p2Preference == FoodPreference.ANY) || (p2Preference == FoodPreference.VEGGIE && p1Preference == FoodPreference.ANY))  {
            return 3 * foodPreferenceMultiplier;
        }


        if((p1Preference == FoodPreference.VEGAN && p2Preference == FoodPreference.ANY) ||
           (p2Preference == FoodPreference.VEGAN  && p1Preference == FoodPreference.ANY)) {
            return foodPreferenceMultiplier;
        }

        // Kein hoher weight Wert, da NONE extrem wichtig ist bei der Pärchenerstellung und vorzugsweise mit MEAT alternativ mit VEGAN/VEGGIE zusammen sein soll
        if(p1Preference == FoodPreference.ANY && p2Preference == FoodPreference.ANY) {
            return foodPreferenceMultiplier;
        }


        if ((p1Preference == FoodPreference.VEGAN && p2Preference == FoodPreference.VEGAN) || (p2Preference == FoodPreference.VEGGIE && p1Preference == FoodPreference.VEGGIE)) {
            return 10 * foodPreferenceMultiplier;
        }

            return  0;
        }

    public int calculateAgeDifferenceWeight(int p1AgeRange, int p2AgeRange, int ageDifferenceMultiplier) {
        int ageRangeDifference = Math.abs(p1AgeRange - p2AgeRange);

        // Persönlich definierte Multiplier, Range 4,5,6,7,8 zu hoher altersunterschied um weight zu geben. 3 gerade noch so okay.
        return switch (ageRangeDifference) {
            case 0 -> 10 * ageDifferenceMultiplier;
            case 1 -> 8 * ageDifferenceMultiplier;
            case 2 -> 5 * ageDifferenceMultiplier;
            case 3 -> 3 * ageDifferenceMultiplier;
            case 4, 5, 6, 7, 8 -> ageDifferenceMultiplier;
            default -> throw new IllegalArgumentException("Invalid age range difference: " + ageRangeDifference);
        };
    }

    public int calculateGenderDiversityWeight(Gender p1Gender, Gender p2Gender, int genderDiversityMultiplier) {

        if(p1Gender.name().equals( p2Gender.name())) {
            return 5 * genderDiversityMultiplier;
        } else  {
            return 10 * genderDiversityMultiplier;
        }
    }

    public int calculateValidLocationWeight(Location p1Location, Location p2Location) {
        // Selbe Location soll vermieden werden (WG)
        if((p1Location.getLongitude() == p2Location.getLongitude()) && p1Location.getLatitude() == p2Location.getLatitude()) {
            return -100000;
        } else {
            return 0;
        }

    }

    public int calculateKitchenAvailabilityWeight(int minimizeLeftoverParticipantsRank, Kitchen p1Kitchen, Kitchen p2Kitchen) {

        // Beide keine Küche, Pärchen darf nicht erstellt werden
        if(p1Kitchen.equals(Kitchen.NO) && p2Kitchen.equals(Kitchen.NO)) {
            return -100000;
        }

        // Sollte vermieden werden, aufgrund von Küchenknappheit
        if(p1Kitchen.equals(Kitchen.YES) && p2Kitchen.equals(Kitchen.YES)) {
            return 0;
        }

        // Notfallküchen verwenden, eher vermeiden aber trotzdem besser als YES YES
        if(p1Kitchen.equals(Kitchen.MAYBE) && p2Kitchen.equals(Kitchen.MAYBE)) {
            return 50;
        }


        // Best Case, weight wert ist ähnlich, wie als wäre es 1. Platz geranked wurden vom Benutzer bei der wichtigkeit
        if((p1Kitchen.equals(Kitchen.YES) && p2Kitchen.equals(Kitchen.NO)) || (p2Kitchen.equals(Kitchen.YES) && p1Kitchen.equals(Kitchen.NO))) {
            return 300;
        }

        // Notfallküche, immernoch gut, aufgrund von Küchenmangelheit
        // Hat viel Einfluss, wenn also gewählt wird, dass möglichst viele Pärchen gebildet werden soll, returnen wir hier einen höheren wert, um mehr pärchen zu bilden
        if((p1Kitchen.equals(Kitchen.MAYBE) && p2Kitchen.equals(Kitchen.NO)) || (p2Kitchen.equals(Kitchen.MAYBE) && p1Kitchen.equals(Kitchen.NO))) {

            // Wenn soviele Pärchen wie nötig gebildet werden sollen, werden mehr Notfallküchen verwendet
            int weightForFindingMaxPairsRank = switch (minimizeLeftoverParticipantsRank) {
                case 1 -> 100;
                case 2 -> 50;
                case 3 -> 10;
                default -> 0;
            };

            return 100 +  weightForFindingMaxPairsRank;
        }

        // 2. best case, schwer zu sagen ob wichtiger als MAYBE NO.
        if((p1Kitchen.equals(Kitchen.YES) && p2Kitchen.equals(Kitchen.MAYBE)) || (p2Kitchen.equals(Kitchen.YES) && p1Kitchen.equals(Kitchen.MAYBE))) {
            return 150;
        }

        return 0;
    }


}
