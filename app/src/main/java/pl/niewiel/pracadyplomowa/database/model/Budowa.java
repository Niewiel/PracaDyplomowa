package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.util.Date;

@Table(name = "build")
public class Budowa {

    @Column(name = "date_add")
    private Date dateAdd;
    @Column(name = "date_edit")
    private Date dateEdit;
    private String name;
    private String latitude;
    private String longitude;

    public Budowa() {
    }

    public Budowa(Date dateAdd, Date dateEdit, String name, String latitude, String longitude) {
        this.dateAdd = dateAdd;
        this.dateEdit = dateEdit;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Budowa{" +
                "dateAdd=" + dateAdd +
                ", dateEdit=" + dateEdit +
                ", name='" + name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
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
