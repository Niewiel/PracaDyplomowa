package pl.niewiel.pracadyplomowa.database.model;

import android.net.Uri;

import com.orm.dsl.Column;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.sql.Timestamp;

@Table
public class Photo {

    @Column(name = "mid")
    @Unique
    private long mId;
    @Column(name = "bs_id")
    @Unique
    private long bsId;
    private String name;
    private Uri path;
    private Component component;
    @Column(name = "date_add")
    private Timestamp dateAdd;

    public Photo() {
    }

    public Photo(String name, Uri path, Component component) {
        this.name = name;
        this.path = path;
        this.component = component;
        this.dateAdd = new Timestamp(System.currentTimeMillis());
    }


    //geters and setters
    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getPath() {
        return path;
    }

    public void setPath(Uri path) {
        this.path = path;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Timestamp getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Timestamp dateAdd) {
        this.dateAdd = dateAdd;
    }
}
