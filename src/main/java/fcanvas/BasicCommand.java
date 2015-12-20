package fcanvas;

/**
 * Created by cfy on 15-12-1.
 */
public abstract class BasicCommand {
    protected abstract void Do(FeynmanCanvas fcanvas);
    protected abstract void Undo(FeynmanCanvas feynmanCanvas);
}
