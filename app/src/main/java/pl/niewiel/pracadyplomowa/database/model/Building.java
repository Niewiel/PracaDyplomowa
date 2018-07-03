package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Column;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.sql.Timestamp;

@Table(name = "building")
public class Building {

    @Column(name = "mid")
    @Unique
    private long mId;
    @Column(name = "bs_id")
    @Unique
    private long bsId;
    @Column(name = "date_add")
    private Timestamp dateAdd;
    @Column(name = "date_edit")
    private Timestamp dateEdit;
    @Column(name = "date_start")
    private String dateStart;
    @Column(name = "date_end")
    private String dateEnd;
    @Column(name = "name")
    private String name;
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "longitude")
    private String longitude;
    private boolean sync = false;


    public Building() {
    }

    public Building(long bsId, Build build, String name, boolean sync) {
        this.bsId = bsId;
        this.dateAdd = new Timestamp(System.currentTimeMillis());
        this.name = name;
        this.sync = sync;
    }

    public Building(Build budowa, Timestamp dateEdit, String dateStart, String dateEnd, String name, String latitude, String longitude) {
        this.dateAdd = new Timestamp(System.currentTimeMillis());
        this.dateEdit = dateEdit;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public void setDateAdd() {
        this.dateAdd = new Timestamp(System.currentTimeMillis());
    }

    public Timestamp getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(Timestamp dateEdit) {
        this.dateEdit = new Timestamp(System.currentTimeMillis());
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
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
