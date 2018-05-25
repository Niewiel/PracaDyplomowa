package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.sql.Timestamp;
import java.util.List;

@Table(name = "building")
public class Building {

    @Column(name = "mid")
    private long mId;
    @Column(name="bs_id")
    private long bsId;
    private Build budowa;
    private List<Component> componentList;
    @Column(name = "date_add")
    private Timestamp dateAdd;
    @Column(name = "date_edit")
    private Timestamp dateEdit;
    @Column(name = "date_start")
    private Timestamp dateStart;
    @Column(name = "date_end")
    private Timestamp dateEnd;
    @Column(name = "name")
    private String name;
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "longitude")
    private String longitude;


    public Building() {
    }

    public Building(Build budowa, Timestamp dateAdd, Timestamp dateEdit, Timestamp dateStart, Timestamp dateEnd, String name, String latitude, String longitude) {
        this.budowa = budowa;
        this.dateAdd = dateAdd;
        this.dateEdit = dateEdit;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Building{" +
                "bsId=" + bsId +
                ", budowa=" + budowa +
                ", dateAdd=" + dateAdd +
                ", dateEdit=" + dateEdit +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", name='" + name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }

    public long getBsId() {
        return bsId;
    }

    public void setBsId(long bsId) {
        this.bsId = bsId;
    }

    public Build getBudowa() {
        return budowa;
    }

    public void setBudowa(Build budowa) {
        this.budowa = budowa;
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

    public Timestamp getDateStart() {
        return dateStart;
    }

    public void setDateStart(Timestamp dateStart) {
        this.dateStart = dateStart;
    }

    public Timestamp getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Timestamp dateEnd) {
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
}
