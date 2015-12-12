package File;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by cfy on 15-12-12.
 */
public abstract class Saveable implements Parcelable{
    protected String extentionName;

    public abstract void Save(File dest);
    public String getExtentionName(){
        return extentionName;
    }

}
