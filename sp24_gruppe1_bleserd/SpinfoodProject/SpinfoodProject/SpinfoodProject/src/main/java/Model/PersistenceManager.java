package Model;

import java.io.*;

public class PersistenceManager {
    private static final String DATA_HANDLER_FILE_PATH = "datahandler.ser";
    private static final String GROUP_LIST_FILE_PATH = "grouplist.dat";

    public void saveState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_HANDLER_FILE_PATH))) {
            oos.writeObject(DataHandler.getInstance());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_HANDLER_FILE_PATH))) {
            DataHandler loadedDataHandler = (DataHandler) ois.readObject();
            DataHandler.instance = loadedDataHandler;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveGroupList(GroupList groupList, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(groupList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GroupList loadGroupList(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (GroupList) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
