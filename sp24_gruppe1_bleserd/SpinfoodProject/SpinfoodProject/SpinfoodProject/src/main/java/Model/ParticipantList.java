package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParticipantList implements Serializable{

    private List<Participant> participantList = new ArrayList<>();


    public List<Participant> getParticipants() {
        return participantList;
    }
    public void add(Participant participant) {
        participantList.add(participant);
    }


    public void printList() {
        participantList.forEach(Participant::printParticipant);
    }

    public List<Participant> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(List<Participant> participantList) {
        this.participantList = participantList;
    }
    public void addAll(List<Participant> participants) {
        participantList.addAll(participants);
    }

    public void clear() {
        participantList.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantList that = (ParticipantList) o;
        return Objects.equals(participantList, that.participantList);
    }
}
