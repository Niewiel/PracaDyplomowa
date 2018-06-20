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
    boolean sync = false;
    @Column(name = "date_add")
    private Timestamp dateAdd;
    private String path;

    public Photo() {
    }

    public Photo(String name, Uri path) {
        this.name = name;
        this.path = "/storage/emulated/0/Android/data/pl.niewiel.pracadyplomowa/files/Pictures/" + path.getLastPathSegment();
        this.dateAdd = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mId=" + mId +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    //geters and setters
    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public long getBsId() {
        return bsId;
    }

    public void setBsId(long bsId) {
        this.bsId = bsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = "/storage/emulated/0/Android/data/pl.niewiel.pracadyplomowa/files/Pictures/" + path;
    }

    public Timestamp getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Timestamp dateAdd) {
        this.dateAdd = dateAdd;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
