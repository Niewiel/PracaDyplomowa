package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Column;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.sql.Timestamp;

@Table(name = "build")
public class Build {

    @Column(name = "mid")
    @Unique
    private long mId;
    @Unique
    @Column(name = "bs_id")
    private long bsId;
    @Column(name = "date_add")
    private Timestamp dateAdd;
    @Column(name = "date_edit")
    private Timestamp dateEdit;
    private String name;
    private String latitude;
    private String longitude;
    private boolean sync = false;

    public Build() {
        this.dateAdd = new Timestamp(System.currentTimeMillis());
    }


    @Override
    public String toString() {
        return name;
    }

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

    public Timestamp getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Timestamp dateAdd) {
        this.dateAdd = dateAdd;
    }

    public Timestamp getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(Timestamp dateEdit) {
        this.dateEdit = dateEdit;
    }

    public void setDateEdit() {
        this.dateEdit = new Timestamp(System.currentTimeMillis());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

}
