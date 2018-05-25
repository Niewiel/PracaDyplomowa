package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.sql.Timestamp;

@Table(name = "component")
public class Component {

    @Column(name="bs_id")
    private long bsId;
    private Building budynek;
    @Column(name = "date_add")
    private Timestamp dateAdd;
    @Column(name = "date_edit")
    private Timestamp dateEdit;
    private int status;
    private String name;
    private boolean sync = false;

    public Component() {
    }

    public Component(Building budynek, Timestamp dateAdd, Timestamp dateEdit, int status, String name) {
        this.budynek = budynek;
        this.dateAdd = dateAdd;
        this.dateEdit = dateEdit;
        this.status = status;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Component{" +
                "bsId=" + bsId +
                ", budynek=" + budynek +
                ", dateAdd=" + dateAdd +
                ", dateEdit=" + dateEdit +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", sync=" + sync +
                '}';
    }

    public long getBsId() {
        return bsId;
    }

    public void setBsId(long bsId) {
        this.bsId = bsId;
    }

    public Building getBudynek() {
        return budynek;
    }

    public void setBudynek(Building budynek) {
        this.budynek = budynek;
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

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
