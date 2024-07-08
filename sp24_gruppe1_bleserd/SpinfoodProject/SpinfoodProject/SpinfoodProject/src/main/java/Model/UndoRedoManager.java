package Model;

import java.util.Stack;

public class UndoRedoManager {
    private Stack<Runnable> undoStack;
    private Stack<Runnable> redoStack;

    public UndoRedoManager() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void saveState(Runnable undoAction, Runnable redoAction) {
        undoStack.push(undoAction);
        redoStack.push(redoAction);
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Runnable undoAction = undoStack.pop();
            undoAction.run();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Runnable redoAction = redoStack.pop();
            redoAction.run();
        }
    }
}
