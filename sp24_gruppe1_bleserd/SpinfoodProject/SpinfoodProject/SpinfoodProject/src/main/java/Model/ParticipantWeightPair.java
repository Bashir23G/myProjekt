package Model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Wird verwendet, um für jeden Teilnehmer ein Weightwert zu berechnen, ohne dabei direkt das Participant Objekt anzupassen
 */

public class ParticipantWeightPair implements Serializable {
    private int weight;
    private Participant participant;

    public ParticipantWeightPair(int weight, Participant participant) {
        this.weight = weight;
        this.participant = participant;
    }

    public int getWeight() {
        return weight;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
