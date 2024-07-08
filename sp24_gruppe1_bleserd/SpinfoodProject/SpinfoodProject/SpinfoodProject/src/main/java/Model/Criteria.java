package Model;

import java.io.Serializable;

/**
 * Kriterien, die der Verwender beim erstellen der Listen einstellen kann
 */
public enum Criteria implements Serializable {
    minimizeLeftoverParticipants,
    foodPreference,
    genderDiversity,
    ageDifference,
    minimizePathLength;
}
