package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.sql.Date;

@Table(name = "building")
public class Budynek {


    private Budowa budowa;
    @Column(name = "date_add")
    private Date dateAdd;
    @Column(name = "date_edit")
    private Date dateEdit;
    @Column(name = "date_start")
    private Date dateStart;
    @Column(name = "date_end")
    private Date dateEnd;
    @Column(name = "name")
    private String name;
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "longitude")
    private String longitude;

    public Budynek() {
    }

    public Budynek(Budowa budowa, Date dateAdd, Date dateEdit, Date dateStart, Date dateEnd, String name, String latitude, String longitude) {
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
        return "Budynek{" +
                "budowa=" + budowa +
                ", dateAdd=" + dateAdd +
                ", dateEdit=" + dateEdit +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", name='" + name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }

    public Budowa getBudowa() {
        return budowa;
    }

    public void setBudowa(Budowa budowa) {
        this.budowa = budowa;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public Date getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(Date dateEdit) {
        this.dateEdit = dateEdit;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
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
