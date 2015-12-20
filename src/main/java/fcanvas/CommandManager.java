package fcanvas;

import java.util.ArrayList;

/**
 * Created by cfy on 15-12-1.
 */
public class CommandManager {
    private ArrayList<BasicCommand> undolist;
    private ArrayList<BasicCommand> redolist;

    private FeynmanCanvas fcanvas;

    private int undocount = 15;

    public CommandManager(FeynmanCanvas feynmanCanvas){

        undolist = new ArrayList<>();
        redolist = new ArrayList<>();
        this.fcanvas = feynmanCanvas;
    }
    public void add(BasicCommand cmd){
        this.undolist.add(cmd);
        redolist.clear();
        this.fcanvas.callOnEdit(cmd);
        if(undolist.size() > undocount && undocount > 0){
            undolist.remove(0);
        }
    }
    public void setUndoCount(int count){
        this.undocount = count;
    }
    public void Do(BasicCommand cmd){
        cmd.Do(fcanvas);
        add(cmd);
    }
    public void Undo(){
        BasicCommand cmd = undolist.get(undolist.size() - 1);
        undolist.remove(cmd);
        cmd.Undo(fcanvas);
        redolist.add(cmd);
    }
    public void Redo(){
        BasicCommand cmd = redolist.get(redolist.size() - 1);
        redolist.remove(cmd);
        cmd.Do(fcanvas);
        undolist.add(cmd);
    }
    public boolean canUndo(){
        return !undolist.isEmpty();
    }
    public boolean canRedo(){
        return !redolist.isEmpty();
    }
}
