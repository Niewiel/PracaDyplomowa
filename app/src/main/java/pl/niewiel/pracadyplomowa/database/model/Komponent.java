package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.sql.Date;

@Table(name = "component")
public class Komponent {

    private Budynek budynek;
    @Column(name = "date_add")
    private Date dateAdd;
    @Column(name = "date_edit")
    private Date dateEdit;

    private int status;
    private String name;

    public Komponent() {
    }

    public Komponent(Budynek budynek, Date dateAdd, Date dateEdit, int status, String name) {
        this.budynek = budynek;
        this.dateAdd = dateAdd;
        this.dateEdit = dateEdit;
        this.status = status;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Komponent{" +
                "budynek=" + budynek +
                ", dateAdd=" + dateAdd +
                ", dateEdit=" + dateEdit +
                ", status=" + status +
                ", name='" + name + '\'' +
                '}';
    }

    public Budynek getBudynek() {
        return budynek;
    }

    public void setBudynek(Budynek budynek) {
        this.budynek = budynek;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
